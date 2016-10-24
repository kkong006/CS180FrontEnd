package teamawesome.cs180frontend.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import teamawesome.cs180frontend.R;

public class WriteReviewActivity extends AppCompatActivity {

    @Bind(R.id.write_professor_et) EditText mProfessorName;
    @Bind(R.id.write_class_et) EditText mClassName;
    @Bind(R.id.write_review_et) EditText mReviewText;
    @Bind(R.id.write_rate_1) Button mStar1;
    @Bind(R.id.write_rate_2) Button mStar2;
    @Bind(R.id.write_rate_3) Button mStar3;
    @Bind(R.id.write_rate_4) Button mStar4;
    @Bind(R.id.write_rate_5) Button mStar5;
    @Bind(R.id.write_submit_bt) Button mSubmit;

    private Button[] mStars;

    public static final String PROFESSOR_NAME = "PROFESSOR_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        ButterKnife.bind(this);

        mStars = new Button[] {mStar1, mStar2, mStar3, mStar4, mStar5};

        mStar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStarColor(1);
            }
        });

        mStar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStarColor(2);
            }
        });

        mStar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStarColor(3);
            }
        });

        mStar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStarColor(4);
            }
        });

        mStar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStarColor(5);
            }
        });


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: grab information and send to servlet
            }
        });

    }

    private void setStarColor(int count) {
        for(int i = 0; i < 5; i++){
            if(i < count) {
                mStars[i].setTextColor(getApplicationContext().getResources().getColor(R.color.colorGreen));
            } else {
                mStars[i].setTextColor(getApplicationContext().getResources().getColor(R.color.colorGrey));
            }
        }
    }
}
