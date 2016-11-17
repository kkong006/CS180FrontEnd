package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.widget.IconTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.API.Models.ClassBundle;
import teamawesome.cs180frontend.API.Models.ProfessorClassBundle;
import teamawesome.cs180frontend.API.Models.ProfessorRespBundle;
import teamawesome.cs180frontend.API.Models.SchoolBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetProfessorSummaryCallback;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class SearchProfessorResultsActivity extends AppCompatActivity {

    @Bind(R.id.activity_search_professor_results) LinearLayout mParent;
    @Bind(R.id.search_professor_school_list) ListView mSchools;
    @Bind(R.id.search_professor_class_list) ListView mClasses;
    @Bind(R.id.prof_rate_1) IconTextView mOne;
    @Bind(R.id.prof_rate_2) IconTextView mTwo;
    @Bind(R.id.prof_rate_3) IconTextView mThree;
    @Bind(R.id.prof_rate_4) IconTextView mFour;
    @Bind(R.id.prof_rate_5) IconTextView mFive;

    private ProgressDialog mProgressDialog;

    private String mProfessorName;
    private int mProfessorId;
    private int mRating;
    private String[] mClassNames;
    private String[] mSchoolNames;
    private IconTextView[] mRatings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_professor_results);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);

        mRatings = new IconTextView[5];
        mRatings[0] = mOne;
        mRatings[1] = mTwo;
        mRatings[2] = mThree;
        mRatings[3] = mFour;
        mRatings[4] = mFive;

        Intent intent = getIntent();
        mProfessorName = intent.getStringExtra(getString(R.string.PROFESSOR_NAME));
        mProfessorId = intent.getIntExtra(getString(R.string.PROFESSOR_ID), 0);

        System.out.println("PROFESOR RESULTS PROFESSOR ID " + mProfessorId);

        if(mProfessorId > 0) {
            getSupportActionBar().setTitle(mProfessorName);
            //TODO: Make API call for prof stats
            mProgressDialog.show();
            RetrofitSingleton.getInstance()
                    .getMatchingService()
                    .getProfessorSummary(mProfessorId)
                    .enqueue(new GetProfessorSummaryCallback());
        }
    }

    @OnClick(R.id.search_professor_reviews_button)
    public void searchProfessor() {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra(getString(R.string.PROFESSOR_NAME), mProfessorName);
        intent.putExtra(getString(R.string.PROFESSOR_ID), mProfessorId);
        startActivity(intent);
    }

    @Subscribe
    public void profSummaryResp(ProfessorRespBundle resp) {
        mProgressDialog.dismiss();
        mRating = resp.getAvgRating();
        List<SchoolBundle> schools = resp.getSchools();
        List<ProfessorClassBundle> classes = resp.getClasses();
        mSchoolNames = new String[schools.size()];
        mClassNames = new String[classes.size()];
        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mSchoolNames);
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mClassNames);
        mSchools.setAdapter(schoolAdapter);
        mClasses.setAdapter(classAdapter);
        for(int i = 0; i < mRating && i < 5; i++) {
            mRatings[i].setTextColor(getResources().getColor(R.color.colorGreen));
        }
        Utils.showSnackbar(this, mParent, getString(R.string.account_update_success));
    }

    @Subscribe
    public void intResp(Integer i) {
        mProgressDialog.dismiss();
        if(i.equals(0)) {
            Utils.showSnackbar(this, mParent, getString(R.string.error_retrieving_data));
        } else {
            Utils.showSnackbar(this, mParent, getString(R.string.error_retrieving_data));
        }
    }

    @Subscribe
    public void stringResp(String s) {
        mProgressDialog.dismiss();
        if(s.equals("ERROR")) {
            Utils.showSnackbar(this, mParent, getString(R.string.error_retrieving_data));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
