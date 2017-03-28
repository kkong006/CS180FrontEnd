package teamawesome.cs180frontend.Activities.Application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.DataModel.ProfClassBundle;
import teamawesome.cs180frontend.API.Models.DataModel.ProfRespBundle;
import teamawesome.cs180frontend.API.Models.StatusModel.ProfSummaryStatus;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetProfSummaryCallback;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class ProfSummaryActivity extends AppCompatActivity {

    @Bind(R.id.prof_summ_parent) CoordinatorLayout parent;
    @Bind(R.id.search_professor_class_list) ListView classes;
    @Bind(R.id.search_professor_university) TextView university;
    @Bind(R.id.avg_rating_tv) TextView avgRating;
    @Bind(R.id.prof_rate_1) IconTextView one;
    @Bind(R.id.prof_rate_2) IconTextView two;
    @Bind(R.id.prof_rate_3) IconTextView three;
    @Bind(R.id.prof_rate_4) IconTextView four;
    @Bind(R.id.prof_rate_5) IconTextView five;

    private ProgressDialog mProgressDialog;

    private String profName;
    private int profId;
    private String[] classNames;
    private IconTextView[] ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        profName = intent.getStringExtra(getString(R.string.PROFESSOR_NAME));
        setContentView(R.layout.activity_prof_summary);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(profName);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);

        ratings = new IconTextView[]{one, two, three, four, five};

        profId = intent.getIntExtra(getString(R.string.PROFESSOR_ID), 0);

        if(profId > 0) {
            getSupportActionBar().setTitle(profName);
            mProgressDialog.show();
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
        /*Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra(getString(R.string.PROFESSOR_NAME), profName);
        intent.putExtra(getString(R.string.PROFESSOR_ID), profId);
        startActivity(intent);*/
    }

    @Subscribe
    public void profSummaryResp(ProfRespBundle resp) {
        System.out.println("SCHOOL NAME" + resp.getSchoolName());
        mProgressDialog.dismiss();

        String school = resp.getSchoolName();
        List<ProfClassBundle> classes = resp.getClasses();
        classNames = new String[classes.size()];
        for(int i = 0; i < classes.size(); i++) {
            classNames[i] = classes.get(i).getClassName();
            System.out.println("\tCLASS NAME " + classes.get(i).getClassName());
        }
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, classNames);
        university.setText(school);
        this.classes.setAdapter(classAdapter);

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
        mProgressDialog.dismiss();
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