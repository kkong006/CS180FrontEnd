package teamawesome.cs180frontend.Activities.Application;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import teamawesome.cs180frontend.API.Models.ReviewModel.RateReview;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewBundle;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRatingResp;
import teamawesome.cs180frontend.API.Models.StatusModel.ReviewRatingStatus;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.PostReviewRatingCallback;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class ReadReviewActivity extends AppCompatActivity {

    @Bind(R.id.activity_read_review) CoordinatorLayout parent;
    @Bind(R.id.read_class_tv) TextView className;
    @Bind(R.id.read_date_tv) TextView reviewDate;
    @Bind({R.id.read_rate_1, R.id.read_rate_2, R.id.read_rate_3,
            R.id.read_rate_4, R.id.read_rate_5}) TextView[] ratings;
    @Bind(R.id.read_review_tv) TextView reviewText;
    @Bind(R.id.read_like_bt) Button thumbsUp;
    @Bind(R.id.read_dislike_bt) Button thumbsDown;
    @Bind(R.id.read_like_count_tv) TextView likeCount;
    @Bind(R.id.read_dislike_count_tv) TextView dislikeCount;

    ReviewBundle review;
    AlertDialog alertDialog;
    final Context context = this;

    private int userRating = 0;
    private int newUserRating = -1;
    boolean isRatingProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_review);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Utils.getUserId(this) == 0) {
            thumbsUp.setVisibility(View.GONE);
            thumbsDown.setVisibility(View.GONE);
            likeCount.setVisibility(View.GONE);
            dislikeCount.setVisibility(View.GONE);
        }

        Bundle bundle = getIntent().getExtras();
        review = bundle.getParcelable(Constants.REVIEW);
        userRating = bundle.getInt(Constants.USER_RATING);

        getSupportActionBar().setTitle(review.getProfName());
        loadReview();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.readreview, menu);
        return true;
    }

    @OnClick(R.id.read_like_bt)
    public void thumbsUp() {
        if (isRatingProcessing) {
            return;
        }

        if(userRating == 1) {
            newUserRating = 0;
        } else {
            newUserRating = 1;
        }

        setUserRating(newUserRating);
        updateResponse();
    }

    @OnClick(R.id.read_dislike_bt)
    public void thumbsDown() {
        if (isRatingProcessing) {
            return;
        }

        if(userRating == 2) {
            newUserRating = 0;
        } else {
            newUserRating = 2;
        }

        setUserRating(newUserRating);
        updateResponse();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_report:
                showReportDialog();
                return true;
            default:
                return true;
        }
    }

    @Subscribe
    public void likeDislikeResp(ReviewRatingResp resp) {
        isRatingProcessing = false;
        userRating = newUserRating;
    }

    @Subscribe
    public void onFailedResp(ReviewRatingStatus status) {
        isRatingProcessing = false;
    }

    public void loadReview() {
        className.setText(review.getClassName());
        reviewDate.setText(Utils.getLocalTimeString(review.getReviewDate()));
        reviewText.setText(review.getReviewMsg());

        for(int i = 0; i < review.getRating() && i < 5; i++) {
            ratings[i].setTextColor(getResources().getColor(R.color.colorGreen));
        }

        setUserRating(userRating);
    }

    private void setUserRating(int newRating) {

        if(newRating == 0) {
            thumbsUp.setTextColor(getResources().getColor(R.color.colorGrey));
            thumbsDown.setTextColor(getResources().getColor(R.color.colorGrey));
        } else if(newRating == 1) {
            thumbsUp.setTextColor(getResources().getColor(R.color.colorGreen));
            thumbsDown.setTextColor(getResources().getColor(R.color.colorGrey));
        } else if(newRating == 2) {
            thumbsUp.setTextColor(getResources().getColor(R.color.colorGrey));
            thumbsDown.setTextColor(getResources().getColor(R.color.colorRed));
        }
    }

    public void updateResponse() {
        if(userRating != newUserRating && !isRatingProcessing) {
            isRatingProcessing = true;
            int userId = Utils.getUserId(this);
            String password = Utils.getPassword(this);
            //System.out.println("USER ID " + userId + "\nPASSWORD " + password + "\nREVIEW ID " + mReviewId + "\nUSER RATING " + userRating + "\nNEW USER RATING " + newUserRating);
            RateReview r = new RateReview(userId, password, review.getReviewId(), newUserRating);
            PostReviewRatingCallback callback = new PostReviewRatingCallback();
            RetrofitSingleton.getInstance().getMatchingService()
                    .rateReview(r)
                    .enqueue(callback);
        }
    }

    public void showReportDialog() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.report)
                    .setItems(R.array.report_option_array, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, R.string.review_reported, Toast.LENGTH_SHORT).show();
                        }
                    }).create();
        }

        alertDialog.show();
    }

}
