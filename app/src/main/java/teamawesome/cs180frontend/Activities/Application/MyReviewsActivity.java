package teamawesome.cs180frontend.Activities.Application;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewBundle;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewPageBundle;
import teamawesome.cs180frontend.API.Models.StatusModel.ReviewFetchStatus;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetReviewsCallback;
import teamawesome.cs180frontend.Adapters.MainFeedAdapter;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class MyReviewsActivity extends AppCompatActivity {
    @Bind(R.id.myreviews) ListView myReviews;
    @Bind(R.id.myreviews_progressbar) ProgressBar progressBar;
    @Bind(R.id.myreviews_parent) CoordinatorLayout parent;

    int offset = 0; //feed offset
    int lastSelected = 0;

    MainFeedAdapter mainFeedAdapter = null;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reviews);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        RetrofitSingleton.getInstance()
                .getMatchingService()
                .reviews(null, null, null, null, Utils.getUserId(this), offset)
                .enqueue(new GetReviewsCallback(this));
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
                                        null, null, null,
                                        Utils.getUserId(context), offset)
                                .enqueue(new GetReviewsCallback(context));
                    }
                }
            }
        });
    }

    @Subscribe
    public void reviewsResp(ReviewPageBundle page) {

        List<ReviewBundle> reviews = page.getReviews();

        if ((offset == 0) && (reviews.size() == 0)) {
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (reviews.size() > 2) {
            reviews.add(2, null);
        } else if ((offset == 0) && (reviews.size() > 0)) {
            reviews.add(null);
        }

        offset += reviews.size();

        if (offset > 10) {
            setOnScrollListener();
        }

        if (mainFeedAdapter == null) {
            mainFeedAdapter = new MainFeedAdapter(this, reviews);
            myReviews.setAdapter(mainFeedAdapter);
        } else {
            mainFeedAdapter.append(reviews);
        }

        myReviews.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        progressBar.setVisibility(View.GONE);
        myReviews.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void failedReviewFetch(ReviewFetchStatus failedFetch) {
        if (failedFetch.getContext().equals(this)) {
            progressBar.setVisibility(View.GONE);
            if (failedFetch.getStatus() != -1) {
                Utils.showSnackbar(this, parent, R.color.colorPrimary,
                        getString(R.string.invalid_review_request));
            } else {
                Utils.showSnackbar(this, parent, R.color.colorPrimary,
                        getString(R.string.failed_review_request));
            }

            myReviews.setVisibility(View.GONE);
        }
    }
}
