package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import retrofit2.Callback;
import teamawesome.cs180frontend.API.Models.UserReview;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetReviewsCallback;
import teamawesome.cs180frontend.Adapters.SearchResultsAdapter;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.Review;
import teamawesome.cs180frontend.R;

public class SearchResultsActivity extends AppCompatActivity {

    @Bind(R.id.results_list_view) ListView mResultsList;

    private String mProfessorName;
    private int mProfessorId;
//    private String mClassName;
    private SearchResultsAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private List<UserReview> mReviews;
    private int[] review_ids;
    private int[] class_ids;
    private int[] prof_ids;
    private String[] review_dates;
    private int[] ratings;
    private String[] msgs;
    private int[] user_review_rating;

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

        //NOTE: stubbed information




        mAdapter = new SearchResultsAdapter(getBaseContext(),
                getApplicationContext().getResources().getStringArray(R.array.class_names),
                getApplicationContext().getResources().getStringArray(R.array.ratings),
                getApplicationContext().getResources().getStringArray(R.array.review_dates),
                getApplicationContext().getResources().getStringArray(R.array.reviews),
                getApplicationContext().getResources().getStringArray(R.array.reviewIDs));

//        mResultsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mResultsList.setAdapter(mAdapter);
        mResultsList.setOnItemClickListener(new ReviewSelectClickListener());


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getReviews();

    }

    public void populateListView() {
        //TODO: pull class names and match

    }

    public void getReviews() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        Callback callback = new GetReviewsCallback();
        RetrofitSingleton.getInstance().getUserService()
                .reviews(mProfessorId, getSharedPreferences(Constants.USER_ID, Context.MODE_PRIVATE).getInt(Constants.USER_ID, -1))
                .enqueue(callback);
    }

    @Subscribe
    public void reviewsResp(List<UserReview> reviewList) {
        mProgressDialog.dismiss();
        mReviews = reviewList;

        if(reviewList.size() > 0) {
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
            Toast.makeText(this, getResources().getString(R.string.error_retrieving_data), Toast.LENGTH_SHORT).show();
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
            Intent i = new Intent(getBaseContext(), ReadReviewActivity.class);
            Review r = mAdapter.getItem(position);
            i.putExtra(REVIEW, (Serializable)r);
            startActivity(i);
        }
    }
}
