package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import teamawesome.cs180frontend.API.Models.RateReview;
import teamawesome.cs180frontend.API.Models.RatingId;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetReviewsCallback;
import teamawesome.cs180frontend.API.Services.Callbacks.PostReviewRatingCallback;
import teamawesome.cs180frontend.API.Services.Callbacks.PostUpdateAccountCallback;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.Review;
import teamawesome.cs180frontend.Misc.SPSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class ReadReviewActivity extends AppCompatActivity {

    @Bind(R.id.read_class_tv) TextView mClassName;
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
    private int mReviewId;
    private int mReviewRating;
    private String mReviewContent;
    private String mReviewClassName;
    private String mReviewDate;
    private int mUserRating;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_review);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        if(Utils.getUserId(this) == 0) {
            mThumbsUp.setVisibility(View.GONE);
            mThumbsDown.setVisibility(View.GONE);
        }

        mRatings = new TextView[] {mRate1, mRate2, mRate3, mRate4, mRate5};

        Bundle bundle = getIntent().getExtras();

        mReviewId = bundle.getInt(getResources().getString(R.string.REVIEW_ID));
        mReviewRating = bundle.getInt(getResources().getString(R.string.REVIEW_RATING));
        mReviewContent = bundle.getString(getResources().getString(R.string.REVIEW_CONTENT));
        mReviewClassName = bundle.getString(getResources().getString(R.string.REVIEW_CLASS_NAME));
        mReviewDate = bundle.getString(getResources().getString(R.string.REVIEW_DATE));
        mUserRating = bundle.getInt(getResources().getString(R.string.REVIEW_USER_RATING));

        mClassName.setText(mReviewClassName);
        mReviewText.setText(mReviewContent);

        for(int i = 0; i < mReviewRating && i < 5; i++) {
            mRatings[i].setTextColor(getResources().getColor(R.color.colorGreen));
        }

        mUserRating = 0;

        setUserRating();
    }

    public void setUserRating() {

        if(mUserRating == 0) {
            mThumbsUp.setTextColor(getResources().getColor(R.color.colorGrey));
            mThumbsDown.setTextColor(getResources().getColor(R.color.colorGrey));
        } else if(mUserRating == 1) {
            mThumbsUp.setTextColor(getResources().getColor(R.color.colorGreen));
            mThumbsDown.setTextColor(getResources().getColor(R.color.colorGrey));
        } else if(mUserRating == 2) {
            mThumbsUp.setTextColor(getResources().getColor(R.color.colorGrey));
            mThumbsDown.setTextColor(getResources().getColor(R.color.colorRed));
        }
    }

    @OnClick(R.id.read_like_bt)
    public void thumbsUp() {
        Toast.makeText(getBaseContext(), "Like", Toast.LENGTH_SHORT).show();

        if(mUserRating == 1) {
            mUserRating = 0;
        } else {
            mUserRating = 1;
        }

        updateResponse();
    }

    @OnClick(R.id.read_dislike_bt)
    public void thumbsDown() {
        Toast.makeText(getBaseContext(), "Dislike", Toast.LENGTH_SHORT).show();

        if(mUserRating == 2) {
            mUserRating = 0;
        } else {
            mUserRating = 2;
        }

        updateResponse();
    }

    public void updateResponse() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        setUserRating();
//        int user_id = SPSingleton.getInstance(this).getSp().getInt(Constants.USER_ID, -1);
        int user_id = 1;
        String password = SPSingleton.getInstance(this).getSp().getString(Constants.PASSWORD, "");
        Toast.makeText(this, password, Toast.LENGTH_SHORT).show();

        RateReview r = new RateReview(user_id, password, mReviewId, mUserRating);
        Callback callback = new PostReviewRatingCallback();
        RetrofitSingleton.getInstance().getUserService()
                .rateReview(r)
                .enqueue(callback);
    }

//    @Subscribe
//    public void likeDislikeResp(RatingId r) {
//        mProgressDialog.dismiss();
//        setUserRating();
//        Toast.makeText(this, getResources().getString(R.string.account_update_success), Toast.LENGTH_SHORT).show();
//    }

    @Subscribe
    public void likeDislikeInt(Integer i) {
        mProgressDialog.dismiss();
        if(i.equals(0)) {
            Toast.makeText(this, getResources().getString(R.string.reviews_dne), Toast.LENGTH_SHORT).show();
        } else if(i.equals(-1)) {
            Toast.makeText(this, getResources().getString(R.string.error_retrieving_data), Toast.LENGTH_SHORT).show();
        } else if(i.equals(1)) {
            Toast.makeText(this, getResources().getString(R.string.account_update_success), Toast.LENGTH_SHORT).show();
        } else if(i.equals(2)) {
            Toast.makeText(this, getResources().getString(R.string.error_retrieving_data), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void likeDislikeError(String s) {
        mProgressDialog.dismiss();
        if(s.equals("ERROR")) {
            Toast.makeText(this, getResources().getString(R.string.error_retrieving_data), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
