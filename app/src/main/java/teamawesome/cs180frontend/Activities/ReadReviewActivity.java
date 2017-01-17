package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRatingResp;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRespBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.PostReviewRatingCallback;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.SPSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class ReadReviewActivity extends AppCompatActivity {

    @Bind(R.id.activity_read_review) CoordinatorLayout mParent;
    @Bind(R.id.read_class_tv) TextView mClassName;
    @Bind(R.id.read_date_tv) TextView mReviewDate;
    @Bind(R.id.read_rate_1) TextView mRate1;
    @Bind(R.id.read_rate_2) TextView mRate2;
    @Bind(R.id.read_rate_3) TextView mRate3;
    @Bind(R.id.read_rate_4) TextView mRate4;
    @Bind(R.id.read_rate_5) TextView mRate5;
    @Bind(R.id.read_review_tv) TextView mReviewText;
    @Bind(R.id.read_like_bt) Button mThumbsUp;
    @Bind(R.id.read_dislike_bt) Button mThumbsDown;
    @Bind(R.id.read_like_count_tv) TextView mLikeCount;
    @Bind(R.id.read_dislike_count_tv) TextView mDislikeCount;

    private TextView[] mRatings;
    ReviewRespBundle review;
    private int userRating = 0;
    private int mNewUserRating;
    boolean ratingProcessing = false;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_review);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Utils.getUserId(this) == 0) {
            mThumbsUp.setVisibility(View.GONE);
            mThumbsDown.setVisibility(View.GONE);
            mLikeCount.setVisibility(View.GONE);
            mDislikeCount.setVisibility(View.GONE);
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);

        mRatings = new TextView[] {mRate1, mRate2, mRate3, mRate4, mRate5};

        Bundle bundle = getIntent().getExtras();
        review = bundle.getParcelable("review");
        userRating = bundle.getInt("yourRating");

        getSupportActionBar().setTitle(review.getProfName());
        loadReview();
    }

    public void loadReview() {
        mClassName.setText(review.getClassName());
        mReviewDate.setText(Utils.getLocalTimeString(review.getReviewDate()));
        mReviewText.setText(review.getReviewMsg());

        for(int i = 0; i < review.getRating() && i < 5; i++) {
            mRatings[i].setTextColor(getResources().getColor(R.color.colorGreen));
        }

        setUserRating();
    }

    private void setUserRating() {

        if(userRating == 0) {
            mThumbsUp.setTextColor(getResources().getColor(R.color.colorGrey));
            mThumbsDown.setTextColor(getResources().getColor(R.color.colorGrey));
        } else if(userRating == 1) {
            mThumbsUp.setTextColor(getResources().getColor(R.color.colorGreen));
            mThumbsDown.setTextColor(getResources().getColor(R.color.colorGrey));
        } else if(userRating == 2) {
            mThumbsUp.setTextColor(getResources().getColor(R.color.colorGrey));
            mThumbsDown.setTextColor(getResources().getColor(R.color.colorRed));
        }
    }

    public void updateResponse() {
        if(userRating != mNewUserRating && !ratingProcessing) {
            ratingProcessing = true;
            int userId = Utils.getUserId(this);
            String password = Utils.getPassword(this);
            //System.out.println("USER ID " + userId + "\nPASSWORD " + password + "\nREVIEW ID " + mReviewId + "\nUSER RATING " + userRating + "\nNEW USER RATING " + mNewUserRating);
            RateReview r = new RateReview(userId, password, review.getReviewId(), mNewUserRating);
            Callback callback = new PostReviewRatingCallback();
            RetrofitSingleton.getInstance().getMatchingService()
                    .rateReview(r)
                    .enqueue(callback);
        }
    }

    @OnClick(R.id.read_like_bt)
    public void thumbsUp() {
        if (ratingProcessing) {
            return;
        }
        //Utils.showSnackbar(this, mParent, getString(R.string.liked));

        if(userRating == 1) {
            mNewUserRating = 0;
        } else {
            mNewUserRating = 1;
        }
        updateResponse();
    }

    @OnClick(R.id.read_dislike_bt)
    public void thumbsDown() {
        //Utils.showSnackbar(this, mParent, getString(R.string.disliked));
        if (ratingProcessing) {
            return;
        }

        if(userRating == 2) {
            mNewUserRating = 0;
        } else {
            mNewUserRating = 2;
        }
        updateResponse();
    }

    @Subscribe
    public void intLikeDislikeResp(ReviewRatingResp resp) {
        ratingProcessing = false;
        Utils.showSnackbar(this, mParent, getString(R.string.account_update_success));
        userRating = mNewUserRating;
        setUserRating();
    }

    @Subscribe
    public void onFailedResp(String s) {
        ratingProcessing = false;
        if(s.equals("ERROR")) {
            Utils.showSnackbar(this, mParent, getString(R.string.error_retrieving_data));
        }
    }

    @Override
    protected void onDestroy() {
        System.out.println("onDestroy Read");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
