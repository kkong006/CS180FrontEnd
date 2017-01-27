package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import retrofit2.Callback;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRespBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetReviewsCallback;
import teamawesome.cs180frontend.Adapters.SearchResultsAdapter;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class SearchResultsActivity extends AppCompatActivity {

    @Bind(R.id.activity_search_results)
    CoordinatorLayout mParent;
    @Bind(R.id.results_list_view)
    ListView mResultsList;

    private String mProfessorName;
    private int mProfessorId;
    //    private String className;
    private SearchResultsAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private List<ReviewRespBundle> reviews;
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

        if (mProfessorId > 0) {
            getSupportActionBar().setTitle(mProfessorName);
            getReviews();
        }
    }

    public void getReviews() {
        mProgressDialog.show();
        Callback callback = new GetReviewsCallback();
        RetrofitSingleton.getInstance().getMatchingService()
                .reviews(mProfessorId, null, Utils.getUserId(this), 0)
                .enqueue(callback);
    }

    public void populateListView() {
        //TODO: pull class names and match
        if (reviews != null) {

            mAdapter = new SearchResultsAdapter(this, reviews);
            mResultsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mResultsList.setAdapter(mAdapter);

            if (mPosition >= 0 && mPosition < reviews.size()) {
                System.out.println("POSITION IN SETUP " + mPosition);
                mResultsList.setSelection(mPosition);
                mPosition = 0;
            }
        }
    }

    @OnItemClick(R.id.results_list_view)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getBaseContext(), ReadReviewActivity.class);
        intent.putExtra("review", (Parcelable) mAdapter.getItem(position));
        startActivity(intent);
    }

    @Subscribe
    public void reviewsResp(List<ReviewRespBundle> reviewList) {
        mProgressDialog.dismiss();

        reviews = reviewList;

        if (reviewList.size() > 0) {
            populateListView();
        } else {
            Utils.showSnackbar(this, mParent, getString(R.string.reviews_dne));
        }
    }

    @Subscribe
    public void intResp(Integer i) {
        mProgressDialog.dismiss();
        if (i.equals(0)) {
            Utils.showSnackbar(this, mParent, getString(R.string.reviews_dne));
        } else {
            Utils.showSnackbar(this, mParent, getString(R.string.error_retrieving_data));
        }
    }

    @Subscribe
    public void stringResp(String s) {
        mProgressDialog.dismiss();
        if (s.equals("ERROR")) {
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
