package teamawesome.cs180frontend.Activities.Application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewBundle;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewPageBundle;
import teamawesome.cs180frontend.API.Models.StatusModel.ReviewFetchStatus;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetReviewsCallback;
import teamawesome.cs180frontend.Adapters.MainFeedAdapter;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class ReviewsActivity extends AppCompatActivity {
    @Bind(R.id.reviews_parent) CoordinatorLayout parent;
    @Bind(R.id.reviews) ListView myReviews;
    @Bind(R.id.reviews_progressbar) ProgressBar progressBar;
    @Bind(R.id.error_tv) TextView errorTV;

    Integer classId;

    String idType; //what type of ID is being passed in
    String idType2; //using two for when I'm searching for reviews for class X taught by prof Y

    int offset = 0; //feed offset
    int lastSelected = 0;
    int id;
    int id2;

    MainFeedAdapter mainFeedAdapter = null;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idType = bundle.getString(Constants.ID_TYPE, "");
            id = bundle.getInt(idType, -1);

            idType2 = bundle.getString(Constants.ID_TYPE_2, "");
            id2 = bundle.getInt(idType2, -1);

            if (idType.equals(Constants.USER_ID) && id == Utils.getUserId(this)) {
                getSupportActionBar().setTitle(R.string.my_reviews);
            } else {
                getSupportActionBar()
                        .setTitle(bundle.getString(Constants.NAME, "") + " " + Constants.REVIEWS);
            }

            mainFeedAdapter = new MainFeedAdapter(this, new ArrayList<ReviewBundle>());
            myReviews.setAdapter(mainFeedAdapter);
        } else {
            finish();
        }

        /*only having classId check idType2 since right now it's the only scenario where I have to check
        it to make sure if I have to put 2 values into the API call  */
        if (idType.equals(Constants.CLASS_ID)) {
            classId = id;
        } else if (idType2.equals(Constants.CLASS_ID)) {
            classId = id2;
        }

        getReviews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void setOnScrollListener() {
        myReviews.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
                if ((lastVisibleIndex == (totalItemCount - 1))
                        && totalItemCount != 0
                        && mainFeedAdapter.getItem(lastVisibleIndex) == null) {
                    if (lastSelected != lastVisibleIndex) {
                        lastSelected = lastVisibleIndex;

                        RetrofitSingleton.getInstance()
                                .getMatchingService()
                                .reviews(null,
                                        idType.equals(Constants.SUBJECT_ID) ? id : null,
                                        classId,
                                        idType.equals(Constants.PROF_ID) ? id : null,
                                        idType.equals(Constants.USER_ID) ? id : null, offset)
                                .enqueue(new GetReviewsCallback(context));
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }

    @OnClick(R.id.error_tv)
    public void tryLoadingReviewsAgain() {
        errorTV.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        myReviews.setVisibility(View.VISIBLE);

        getReviews();
    }

    @OnItemClick(R.id.reviews)
    public void onReviewClick(AdapterView<?> parent, View view, int position, long id) {
        ReviewBundle review = mainFeedAdapter.getItem(position);
        if (review != null) {
            Intent intent = new Intent(this, ReadReviewActivity.class);
            intent.putExtra(Constants.REVIEW, (Parcelable) review);
            intent.putExtra(Constants.USER_RATING, Utils.getReviewRating(review.getReviewId()));
            startActivity(intent);
        }
    }

    @Subscribe
    public void reviewsResp(ReviewPageBundle page) {
        List<ReviewBundle> reviews = page.getReviews();

        if ((offset == 0) && (reviews.size() == 0)) {
            progressBar.setVisibility(View.GONE);
            return;
        }

        offset += reviews.size();

        if (reviews.size() > 2) {
            reviews.add(2, null);
        } else if ((mainFeedAdapter.getCount() == 0) && (reviews.size() > 0)) {
            reviews.add(null);
        }

        if (offset >= 10) {
            setOnScrollListener();
        }

        mainFeedAdapter.append(reviews);

        myReviews.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        progressBar.setVisibility(View.GONE);
        myReviews.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void failedReviewFetch(ReviewFetchStatus failedFetch) {
        if (failedFetch.getContext().equals(this)) {
            progressBar.setVisibility(View.GONE);
            myReviews.setVisibility(View.GONE);
            errorTV.setVisibility(View.VISIBLE);

            if (failedFetch.getStatus() != -1) {
                Utils.showSnackBar(this, parent, R.color.colorPrimary,
                        getString(R.string.invalid_review_request));
            } else {
                Utils.showSnackBar(this, parent, R.color.colorPrimary,
                        getString(R.string.failed_review_request));
            }
        }
    }

    public void getReviews() {
        RetrofitSingleton.getInstance()
                .getMatchingService()
                .reviews(null, //The main feed is already a list of school specific reviews
                        idType.equals(Constants.SUBJECT_ID) ? id : null,
                        classId,
                        idType.equals(Constants.PROF_ID) ? id : null,
                        idType.equals(Constants.USER_ID) ? id : null, offset)
                .enqueue(new GetReviewsCallback(this));
    }
}
