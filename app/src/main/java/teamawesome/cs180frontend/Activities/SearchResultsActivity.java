package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Callback;
import teamawesome.cs180frontend.API.Models.ClassBundle;
import teamawesome.cs180frontend.API.Models.ResponseReview;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetClassesCallback;
import teamawesome.cs180frontend.API.Services.Callbacks.GetReviewsCallback;
import teamawesome.cs180frontend.Adapters.SearchResultsAdapter;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.R;

public class SearchResultsActivity extends AppCompatActivity {

    @Bind(R.id.results_list_view) ListView mResultsList;

    private String mProfessorName;
    private int mProfessorId;
//    private String mClassName;
    private SearchResultsAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private List<ResponseReview> mReviews;
    private List<ClassBundle> mClasses;
    private int[] mReviewIds;
    private int[] mClassIds;
    private String[] mClassNames;
    private int[] mProfIds;
    private String[] mReviewDates;
    private int[] mRatings;
    private String[] mMsgs;
    private int[] mUserReviewRatings;

    public static final String REVIEW = "REVIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);


        Bundle bundle = getIntent().getExtras();
        mProfessorName = bundle.getString(getResources().getString(R.string.professor_name));
        mProfessorId = bundle.getInt(getResources().getString(R.string.professor_id));

        getSupportActionBar().setTitle(mProfessorName);
//        mClassName = bundle.getString(SearchActivity.CLASS_NAME);

        getReviews();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getReviews();

    }

    public void populateListView() {
        //TODO: pull class names and match
        mReviewIds = new int[mReviews.size()];
        mClassIds = new int[mReviews.size()];
        mProfIds = new int[mReviews.size()];
        mReviewDates = new String[mReviews.size()];
        mRatings = new int[mReviews.size()];
        mMsgs = new String[mReviews.size()];
        mUserReviewRatings = new int[mReviews.size()];
        mClassNames = new String[mReviews.size()];

        for(int i = 0; i < mReviews.size(); i++) {
            mReviewIds[i] = mReviews.get(i).getReviewId();
            mClassIds[i] = mReviews.get(i).getClassId();
            mClassNames[i] = "";
            for(ClassBundle c : mClasses) {
                if(c.getClassId() == mClassIds[i]) {
                    mClassNames[i] = c.getClassName();
                }
            }
            mProfIds[i] = mReviews.get(i).getProfId();
            mReviewDates[i] = mReviews.get(i).getReviewDate();
            mRatings[i] = mReviews.get(i).getRating();
            mMsgs[i] = mReviews.get(i).getMsg();
            mUserReviewRatings[i] = mReviews.get(i).getUserReviewRating();
        }

        mAdapter = new SearchResultsAdapter(this, mClassNames, mRatings, mReviewDates, mMsgs, mReviewIds);
        mResultsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mResultsList.setAdapter(mAdapter);
        mResultsList.setOnItemClickListener(new ReviewSelectClickListener());
    }

    public void getReviews() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        Callback callback = new GetReviewsCallback();
        RetrofitSingleton.getInstance().getMatchingService()
                .reviews(mProfessorId, getSharedPreferences(Constants.USER_ID, Context.MODE_PRIVATE).getInt(Constants.USER_ID, -1))
                .enqueue(callback);
    }

    public void getClassNames() {
//        mProgressDialog = new ProgressDialog(this);
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.setMessage(getResources().getString(R.string.loading));
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        Callback callback = new GetClassesCallback();
        RetrofitSingleton.getInstance().getMatchingService()
                .getClasses(getSharedPreferences(Constants.SCHOOL_ID, Context.MODE_PRIVATE).getInt(Constants.SCHOOL_ID, -1))
                .enqueue(callback);
    }

    @Subscribe
    public void reviewsResp(List<ResponseReview> reviewList) {
        mProgressDialog.dismiss();
        mReviews = reviewList;

        if(reviewList.size() > 0) {
            getClassNames();
            populateListView();
        } else {
            Toast.makeText(this, getResources().getString(R.string.reviews_dne), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void classesResp(List<ClassBundle> classList) {
        mProgressDialog.dismiss();
        mClasses = classList;

        if(classList.size() > 0) {
            populateListView();
        } else {
            Toast.makeText(this, getResources().getString(R.string.reviews_dne), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void reviewsInt(Integer i) {
        mProgressDialog.dismiss();
        if(i == 0) {
            Toast.makeText(this, getResources().getString(R.string.reviews_dne), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_retrieving_data) + "query", Toast.LENGTH_SHORT).show();
        }

    }

    @Subscribe
    public void schoolsString(String s) {
        mProgressDialog.dismiss();
        if(s == "ERROR") {
            Toast.makeText(this, getResources().getString(R.string.error_retrieving_data), Toast.LENGTH_SHORT).show();
        }
    }

    public class ReviewSelectClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int[] r = mAdapter.getItem(position);
            if(r.length == 2) {
                int j = 0;
                for(; j < mReviewIds.length; j++) {
                    if(mReviewIds[j] == r[0]) {
                        break;
                    }
                }
                if(j < mReviewIds.length) {
                    Intent i = new Intent(getBaseContext(), ReadReviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(getResources().getString(R.string.REVIEW_ID), r[0]);
                    bundle.putInt(getResources().getString(R.string.REVIEW_RATING), r[1]);
                    bundle.putString(getResources().getString(R.string.REVIEW_CONTENT), mMsgs[j]);
                    bundle.putString(getResources().getString(R.string.REVIEW_CLASS_NAME), mClassNames[j]);
                    bundle.putString(getResources().getString(R.string.REVIEW_DATE), mReviewDates[j]);
                    bundle.putInt(getResources().getString(R.string.REVIEW_USER_RATING), mUserReviewRatings[j]);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
