package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Callback;
import teamawesome.cs180frontend.API.Models.ReviewRespBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetReviewsCallback;
import teamawesome.cs180frontend.Adapters.SearchResultsAdapter;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class SearchResultsActivity extends AppCompatActivity {

    @Bind(R.id.activity_search_results) CoordinatorLayout mParent;
    @Bind(R.id.results_list_view) ListView mResultsList;

    private String mProfessorName;
    private int mProfessorId;
//    private String mClassName;
    private SearchResultsAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private List<ReviewRespBundle> mReviews;
    private int[] mReviewIds;
    private int[] mClassIds;
    private int[] mProfIds;
    private int[] mRatings;
    private int[] mUserReviewRatings;
    private String[] mClassNames;
    private String[] mReviewDates;
    private String[] mMsgs;
    private int mPosition;

    public static final String REVIEW = "REVIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPosition = 0;

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);

        Intent i = getIntent();
        mProfessorName = i.getStringExtra(getString(R.string.PROFESSOR_NAME));
        mProfessorId = i.getIntExtra(getString(R.string.PROFESSOR_ID), 0);

        if(mProfessorId > 0) {
            getSupportActionBar().setTitle(mProfessorName);
            getReviews();
        }
    }

    public void getReviews() {
        mProgressDialog.show();
        Callback callback = new GetReviewsCallback();
        RetrofitSingleton.getInstance().getMatchingService()
                .reviewsProfessor(mProfessorId, Utils.getUserId(this))
                .enqueue(callback);
    }

    public void populateListView() {
        //TODO: pull class names and match
        if(mReviews != null) {
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
                mClassNames[i] = DataSingleton.getInstance().getClassName(mReviews.get(i).getClassId());
                mProfIds[i] = mReviews.get(i).getProfId();
                mReviewDates[i] = mReviews.get(i).getReviewDate();
                mRatings[i] = mReviews.get(i).getRating();

                mMsgs[i] = mReviews.get(i).getMessage();
                mUserReviewRatings[i] = mReviews.get(i).getReviewRating();
                System.out.println("USER RATING " + i + " " + mUserReviewRatings[i]);
            }

            mAdapter = new SearchResultsAdapter(this, mClassNames, mRatings, mReviewDates, mMsgs, mReviewIds);
            mResultsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mResultsList.setAdapter(mAdapter);
            mResultsList.setOnItemClickListener(new ReviewSelectClickListener());

            if(mPosition >= 0 && mPosition < mReviews.size()) {
                System.out.println("POSITION IN SETUP " + mPosition);
                mResultsList.setSelection(mPosition);
                mPosition = 0;
            }
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
                    System.out.println("POSITION BEFORE INTENT " + j);
                    mPosition = j;
                    Intent i = new Intent(getBaseContext(), ReadReviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(getString(R.string.REVIEW_ID), r[0]);
                    bundle.putInt(getString(R.string.REVIEW_RATING), r[1]);
                    bundle.putString(getString(R.string.REVIEW_CONTENT), mMsgs[j]);
                    bundle.putString(getString(R.string.REVIEW_CLASS_NAME), mClassNames[j]);
                    bundle.putString(getString(R.string.REVIEW_DATE), mReviewDates[j]);
                    bundle.putInt(getString(R.string.REVIEW_USER_RATING), mUserReviewRatings[j]);
                    bundle.putString(getString(R.string.PROFESSOR_NAME), mProfessorName);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            }
        }
    }

    @Subscribe
    public void reviewsResp(List<ReviewRespBundle> reviewList) {
        mProgressDialog.dismiss();

        mReviews = reviewList;

        if(reviewList.size() > 0) {
            populateListView();
        } else {
            Utils.showSnackbar(this, mParent, getString(R.string.reviews_dne));
        }
    }

    @Subscribe
    public void intResp(Integer i) {
        mProgressDialog.dismiss();
        if(i.equals(0)) {
            Utils.showSnackbar(this, mParent, getString(R.string.reviews_dne));
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
    protected void onRestart() {
        super.onRestart();
        EventBus.getDefault().register(this);
        getReviews();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

//    @Override
//    protected void onDestroy() {
//        EventBus.getDefault().unregister(this);
//        super.onDestroy();
//    }
}
