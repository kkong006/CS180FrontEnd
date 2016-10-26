package teamawesome.cs180frontend.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import teamawesome.cs180frontend.Misc.Review;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_review);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

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
            mRatings[i].setTextColor(getApplicationContext().getResources().getColor(R.color.colorGreen));
        }

        //TODO: if we get like/dislike count, set it

        mThumbsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Like", Toast.LENGTH_SHORT).show();
                //TODO: make call to update likes/dislikes
            }
        });

        mThumbsDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Dislike", Toast.LENGTH_SHORT).show();
                //TODO: make call to update likes/dislikes
            }
        });
    }
    


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
