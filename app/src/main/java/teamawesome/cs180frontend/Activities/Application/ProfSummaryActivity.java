package teamawesome.cs180frontend.Activities.Application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.DataModel.ProfRespBundle;
import teamawesome.cs180frontend.API.Models.StatusModel.ProfSummaryStatus;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetProfSummaryCallback;
import teamawesome.cs180frontend.Adapters.SimpleListAdapter2;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class ProfSummaryActivity extends AppCompatActivity {

    @Bind(R.id.prof_summ_parent) CoordinatorLayout parent;
    @Bind(R.id.search_professor_class_list) ListView classList;
    @Bind(R.id.search_professor_university) TextView university;
    @Bind(R.id.avg_rating_tv) TextView avgRating;
    @Bind({R.id.prof_rate_1, R.id.prof_rate_2,
            R.id.prof_rate_3, R.id.prof_rate_4,
            R.id.prof_rate_5}) IconTextView[] ratings;

    private int profId;

    private ProgressDialog progressDialog;
    private SimpleListAdapter2 adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_summary);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        String profName = intent.getStringExtra(getString(R.string.PROFESSOR_NAME));
        profId = intent.getIntExtra(getString(R.string.PROFESSOR_ID), 0);

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

    @OnClick(R.id.search_professor_reviews_button)
    public void searchProfessor() {
        /*Intent intent = new Intent(this, ResultsListActivity.class);
        intent.putExtra(getString(R.string.PROFESSOR_NAME), profName);
        intent.putExtra(getString(R.string.PROFESSOR_ID), profId);
        startActivity(intent);*/
    }

    @Subscribe
    public void profSummaryResp(ProfRespBundle resp) {
        System.out.println("SCHOOL NAME" + resp.getSchoolName());
        progressDialog.dismiss();

        String school = resp.getSchoolName();
        university.setText(school);

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

        parent.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onFailure(ProfSummaryStatus status) {
        progressDialog.dismiss();
        switch (status.getStatus()) {
            case APIConstants.HTTP_STATUS_INVALID:
                Utils.showSnackbar(this, parent, R.color.colorAccent, getString(R.string.prof_dne));
                break;
            case APIConstants.HTTP_STATUS_ERROR:
                Utils.showSnackbar(this, parent, R.color.colorAccent, getString(R.string.server_error));
                break;
            default:
                Utils.showSnackbar(this, parent, R.color.colorAccent, getString(R.string.unable_to_request));
                break;
        }
    }
}
