package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit2.Callback;
import teamawesome.cs180frontend.API.Models.ClassBundle;
import teamawesome.cs180frontend.API.Models.ProfessorBundle;
import teamawesome.cs180frontend.API.Models.UserReview;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.PostReviewCallback;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

import static teamawesome.cs180frontend.Misc.Utils.getUserId;

public class WriteReviewActivity extends AppCompatActivity {

    @Bind(R.id.activity_write_review) LinearLayout mReviewLayout;
    @Bind(R.id.write_professor_et) AutoCompleteTextView mProfessorName;
    @Bind(R.id.write_class_et) AutoCompleteTextView mClassName;
    @Bind(R.id.write_review_et) EditText mReviewText;
    @Bind(R.id.write_rate_1) Button mStar1;
    @Bind(R.id.write_rate_2) Button mStar2;
    @Bind(R.id.write_rate_3) Button mStar3;
    @Bind(R.id.write_rate_4) Button mStar4;
    @Bind(R.id.write_rate_5) Button mStar5;
    @Bind(R.id.write_submit_bt) Button mSubmit;

    private Button[] mStars;
    private String[] mProfessorNames;
    private String[] mClassNames;
    private String mUserProfessorName;
    private String mUserClassName;
    private int mClassId;
    private int mProfId;
    private int mRating;
    private String mReview;

    private List<ProfessorBundle> mProfessors;
    private List<ClassBundle> mClasses;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mStars = new Button[] {mStar1, mStar2, mStar3, mStar4, mStar5};

        if (Utils.getSchoolId(this) != 0) {
        } else {
            //finish();
        }

        mRating = 0;

        //Fill autocomplete textviews for professor and classs choice
        mProfessors = DataSingleton.getInstance().getProfessorCache();
        mProfessorNames = new String[mProfessors.size()];
        for(int i = 0; i < mProfessors.size(); i++) {
            mProfessorNames[i] = mProfessors.get(i).getProfessorName();
        }
        ArrayAdapter<String> profAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mProfessorNames);
        mProfessorName.setAdapter(profAdapter);
        mProfessorName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mUserProfessorName = parent.getItemAtPosition(position).toString();
            }
        });

        mClasses = DataSingleton.getInstance().getClassCache();
        mClassNames = new String[mClasses.size()];
        for(int i = 0; i < mClasses.size(); i++) {
            mClassNames[i] = mClasses.get(i).getClassName();
        }
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mClassNames);
        mClassName.setAdapter(classAdapter);
        mClassName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mUserClassName = parent.getItemAtPosition(position).toString();
            }
        });

        final Context context = this;
        //Enter key to hide keyboard
        mReviewText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    Utils.hideKeyboard(mReviewLayout, context);
                }
               return false;
            }
        });
    }

    @OnClick(R.id.write_submit_bt)
    public void submitReview() {
//        String professorName = mProfessorName.getText().toString();
//        String className = mClassName.getText().toString();
        String reviewText = mReviewText.getText().toString();

        if(reviewText.length() >= 32) {
            Integer profId = DataSingleton.getInstance().getProfessorId(mUserProfessorName);
            Integer classId = DataSingleton.getInstance().getClassId(mUserClassName);
            int userId = Utils.getUserId(this);
            int schoolId = Utils.getSchoolId(this);
            if(profId != null && classId != null) {
                UserReview r = new UserReview(userId,schoolId,classId,profId,mRating,reviewText);
                submitReview(r);
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.review_not_long_enough), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.write_rate_2)
    public void writeRating2() {
        setStarColor(2);
    }

    @OnClick(R.id.write_rate_3)
    public void writeRating3() {
        setStarColor(3);
    }

    @OnClick(R.id.write_rate_4)
    public void writeRating4() {
        setStarColor(4);
    }

    @OnClick(R.id.write_rate_5)
    public void writeRating5() {
        setStarColor(5);
    }

    private void setStarColor(int count) {
        mRating = count;
        for(int i = 0; i < 5; i++){
            if(i < count) {
                mStars[i].setTextColor(getApplicationContext().getResources().getColor(R.color.colorGreen));
            } else {
                mStars[i].setTextColor(getApplicationContext().getResources().getColor(R.color.colorGrey));
            }
        }
    }

    public void submitReview(UserReview r) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        Callback callback = new PostReviewCallback();
        RetrofitSingleton.getInstance().getUserService()
                .review(r)
                .enqueue(callback);
    }

    @Subscribe
    public void intResp(Integer i) {
        mProgressDialog.dismiss();
        if(i == 0) {
            Toast.makeText(this, getResources().getString(R.string.failed_to_submit_review) + " 0", Toast.LENGTH_SHORT).show();
        } else if(i == -1) {
            Toast.makeText(this, getResources().getString(R.string.failed_to_submit_review) + " -1", Toast.LENGTH_SHORT).show();
        } else if(i == 1) {
            mProfessorName.setText("");
            mClassName.setText("");
            mReviewText.setText("");
            setStarColor(0);
            Toast.makeText(this, getResources().getString(R.string.account_update_success) + " 1", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void stringResp(String s) {
        mProgressDialog.dismiss();
        if(s == "ERROR") {
            Toast.makeText(this, getResources().getString(R.string.failed_to_submit_review), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
