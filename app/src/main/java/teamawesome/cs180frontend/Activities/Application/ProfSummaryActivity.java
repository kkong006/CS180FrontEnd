package teamawesome.cs180frontend.Activities.Application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.DataModel.ProfSummary.ProfSummaryBundle;
import teamawesome.cs180frontend.API.Models.DataModel.ProfSummary.RatingDataBundle;
import teamawesome.cs180frontend.API.Models.StatusModel.ProfSummaryStatus;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetProfSummaryCallback;
import teamawesome.cs180frontend.Adapters.SimpleListAdapter2;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.Misc.ViewHolders.RatingBarLayoutHolder;
import teamawesome.cs180frontend.R;

public class ProfSummaryActivity extends AppCompatActivity {

    @Bind(R.id.prof_summ_parent) CoordinatorLayout parent;
    @Bind(R.id.bar_layout) LinearLayout barLayout;
    @Bind(R.id.rating_freq_layout) LinearLayout ratingFreqLayout;
    @Bind(R.id.search_prof_class_list) ListView classList;
    @Bind(R.id.avg_rating_tv) TextView avgRating;
    @Bind({R.id.prof_rate_1, R.id.prof_rate_2,
            R.id.prof_rate_3, R.id.prof_rate_4,
            R.id.prof_rate_5}) IconTextView[] ratings;
    @Bind(R.id.search_professor_university) TextView university;
    @Bind(R.id.total_tv) TextView totalTV;

    private ProgressDialog progressDialog;
    //Too many views, using viewholder to reduce amount of code for this activity
    private RatingBarLayoutHolder ratingBarLayoutHolder;
    private SimpleListAdapter2 adapter = null;

    private String profName;
    private int profId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_summary);
        ButterKnife.bind(this);
        setStartMargin();
        ratingBarLayoutHolder = new RatingBarLayoutHolder(barLayout);
        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        profName = intent.getStringExtra(Constants.PROF_NAME);
        profId = intent.getIntExtra(Constants.PROF_ID, 0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(profName);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);

        if(profId > 0) {
            getSupportActionBar().setTitle(profName);
            progressDialog.show();
            RetrofitSingleton.getInstance()
                    .getMatchingService()
                    .getProfessorSummary(profId)
                    .enqueue(new GetProfSummaryCallback());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnItemClick(R.id.search_prof_class_list)
    public void onClassClick(AdapterView<?> parent, View view, int pos, long id) {
        String className = adapter.getItem(pos).getClassName();
        int classId = DataSingleton.getInstance()
                .getClassId(className);
        Intent intent = new Intent(this, ReviewsActivity.class);
        intent.putExtra(Constants.ID_TYPE, Constants.PROF_ID)
                .putExtra(Constants.PROF_ID, profId)
                .putExtra(Constants.ID_TYPE_2, Constants.CLASS_ID)
                .putExtra(Constants.CLASS_ID, classId)
                .putExtra(Constants.NAME, profName.split(" ")[1] + " for " + className);
        startActivity(intent);
    }

    @Subscribe
    public void profSummaryResp(ProfSummaryBundle resp) {
        progressDialog.dismiss();

        String school = resp.getSchoolName();
        university.setText(school);
        totalTV.setText(String.format(getString(R.string.review_total), resp.getTotalRatings()));

        adapter = new SimpleListAdapter2(this, resp.getClasses());
        classList.setAdapter(adapter);

        double rating = resp.getAvgRating();
        int roundedRating = (int) Math.floor(rating);
        int i = 0;

        for (; i < roundedRating; ++i) {
            ratings[i].setTextColor(getResources().getColor(R.color.colorGreen));
        }

        if (rating - i >= 0.3) {
            ratings[i].setText(getString(R.string.fa_star_half));
            ratings[i].setTextColor(getResources().getColor(R.color.colorGreen));
        }

        avgRating.setText(Double.toString(rating));

        ratingBarLayoutHolder.setUpRatingDistribution(resp,
                ((float) barLayout.getWidth()) * 0.8f);

        parent.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onFailure(ProfSummaryStatus status) {
        progressDialog.dismiss();
        switch (status.getStatus()) {
            case APIConstants.HTTP_STATUS_INVALID:
                Utils.showSnackBar(this, parent, R.color.colorAccent, getString(R.string.prof_dne));
                break;
            case APIConstants.HTTP_STATUS_ERROR:
                Utils.showSnackBar(this, parent, R.color.colorAccent, getString(R.string.server_error));
                break;
            default:
                Utils.showSnackBar(this, parent, R.color.colorAccent, getString(R.string.unable_to_request));
                break;
        }
    }

    //for rating_freq_layout
    public void setStartMargin() {
        int marginStart = Math.round(((float) getResources().getDisplayMetrics().widthPixels) * 0.1f);
        ((ViewGroup.MarginLayoutParams) ratingFreqLayout.getLayoutParams())
                .setMarginStart(marginStart);
        ratingFreqLayout.requestLayout();
    }
}
