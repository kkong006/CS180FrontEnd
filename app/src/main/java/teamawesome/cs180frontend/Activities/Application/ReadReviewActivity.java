package teamawesome.cs180frontend.Activities.Application;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class ReadReviewActivity extends AppCompatActivity {

    @Bind(R.id.activity_read_review) CoordinatorLayout parent;
    @Bind(R.id.read_class_tv) TextView className;
    @Bind(R.id.read_date_tv) TextView reviewDate;
    @Bind(R.id.read_rate_1) TextView rate1;
    @Bind(R.id.read_rate_2) TextView rate2;
    @Bind(R.id.read_rate_3) TextView rate3;
    @Bind(R.id.read_rate_4) TextView rate4;
    @Bind(R.id.read_rate_5) TextView rate5;
    @Bind(R.id.read_review_tv) TextView reviewText;
    @Bind(R.id.read_like_bt) Button thumbsUp;
    @Bind(R.id.read_dislike_bt) Button thumbsDown;
    @Bind(R.id.read_like_count_tv) TextView likeCount;
    @Bind(R.id.read_dislike_count_tv) TextView dislikeCount;

    private TextView[] ratings;
    ReviewBundle review;
    private int userRating = 0;
    private int newUserRating = -1;
    boolean isRatingProcessing = false;

    private ProgressDialog progressDialog;

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

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);

        ratings = new TextView[] {rate1, rate2, rate3, rate4, rate5};

        Bundle bundle = getIntent().getExtras();
        review = bundle.getParcelable("review");
        userRating = bundle.getInt("yourRating");

        getSupportActionBar().setTitle(review.getProfName());
        loadReview();
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
            Callback callback = new PostReviewRatingCallback();
            RetrofitSingleton.getInstance().getMatchingService()
                    .rateReview(r)
                    .enqueue(callback);
        }
    }

    @OnClick(R.id.read_like_bt)
    public void thumbsUp() {
        if (isRatingProcessing) {
            return;
        }
        //Utils.showSnackbar(this, parent, getString(R.string.liked));

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
        //Utils.showSnackbar(this, parent, getString(R.string.disliked));
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

    @Subscribe
    public void likeDislikeResp(ReviewRatingResp resp) {
        isRatingProcessing = false;
        Utils.showSnackbar(this, parent, R.color.colorPrimary,
                getString(R.string.account_update_success));
        userRating = newUserRating;
    }

    @Subscribe
    public void onFailedResp(ReviewRatingStatus status) {
        isRatingProcessing = false;
    }

    @Override
    protected void onDestroy() {
        System.out.println("onDestroy Read");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
