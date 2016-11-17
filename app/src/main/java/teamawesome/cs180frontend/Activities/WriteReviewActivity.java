package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import teamawesome.cs180frontend.API.Models.ClassBundle;
import teamawesome.cs180frontend.API.Models.ProfessorBundle;
import teamawesome.cs180frontend.API.Models.ReviewIDRespBundle;
import teamawesome.cs180frontend.API.Models.UserReview;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.PostReviewCallback;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class WriteReviewActivity extends FragmentActivity {

    @Bind(R.id.activity_write_review) CoordinatorLayout mParent;
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
    private int mRating;

    private List<ProfessorBundle> mProfessors;
    private List<ClassBundle> mClasses;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);

        mStars = new Button[] {mStar1, mStar2, mStar3, mStar4, mStar5};

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

//        final Context context = this;
        //Enter key to hide keyboard
//        mReviewText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if(keyCode == KeyEvent.KEYCODE_ENTER) {
//                    Utils.hideKeyboard(mReviewLayout, getApplicationContext());
//                }
//                return true;
//            }
//        });
    }

    @OnClick(R.id.write_submit_bt)
    public void submitReview() {
        String reviewText = mReviewText.getText().toString();

        if(mUserProfessorName == "" || mUserClassName == "") {
            String professorName = mProfessorName.getText().toString();
            String className = mClassName.getText().toString();
            Integer profId = DataSingleton.getInstance().getProfessorId(professorName);
            Integer classId = DataSingleton.getInstance().getClassId(className);
            if(!profId.equals(null) && !classId.equals(null)) {
                mUserProfessorName = professorName;
                mUserClassName = className;
            }
        }

        if(mUserProfessorName == "" || mUserClassName == "") {
            Utils.showSnackbar(this, mParent, getString(R.string.invalid_prof_class_name));
        } else {
            if(reviewText.length() >= 32) {
                Integer profId = DataSingleton.getInstance().getProfessorId(mUserProfessorName);
                Integer classId = DataSingleton.getInstance().getClassId(mUserClassName);

                int userId = Utils.getUserId(this);
                int schoolId = Utils.getSchoolId(this);
                String password = Utils.getPassword(this);
                System.out.println("PROF ID " + profId + "\nCLASS ID " + classId + "\nSCHOOL_ID " + schoolId + "\nUSER ID " + userId + "\nPASSWORD " + password + "\nRATING " + mRating + "\nREVIEW " + reviewText);
                if(profId != null && classId != null) {
                    UserReview r = new UserReview(userId, password, classId, profId, mRating, reviewText, schoolId);
                    submitReview(r);
                }
            } else {
                Utils.showSnackbar(this, mParent, getString(R.string.review_not_long_enough));
            }
        }
    }

    @OnClick(R.id.write_rate_1)
    public void writeRating1() { setStarColor(1); }

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
        mProgressDialog.show();
        Callback callback = new PostReviewCallback();
        RetrofitSingleton.getInstance().getUserService()
                .review(r)
                .enqueue(callback);
    }

    @Subscribe
    public void reviewResp(ReviewIDRespBundle r) {
        mProgressDialog.dismiss();
        mReviewText.setText("");
        mProfessorName.setText("");
        mClassName.setText("");
        mUserProfessorName = "";
        mUserClassName = "";
        Utils.showSnackbar(this, mParent, getString(R.string.account_update_success));

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Subscribe
    public void intResp(Integer i) {
        mProgressDialog.dismiss();
        if(i.equals(500)) {
            Utils.showSnackbar(this, mParent, getString(R.string.already_submitted_review));
        } else {
            Utils.showSnackbar(this, mParent, getString(R.string.failed_to_submit_review));
        }
    }

    @Subscribe
    public void stringResp(String s) {
        mProgressDialog.dismiss();
        if(s.equals("ERROR")) {
            Utils.showSnackbar(this, mParent, getString(R.string.failed_to_submit_review));
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
