package teamawesome.cs180frontend.Activities.Application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.DataModel.ClassBundle;
import teamawesome.cs180frontend.API.Models.DataModel.ProfBundle;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewIDRespBundle;
import teamawesome.cs180frontend.API.Models.ReviewModel.UserReview;
import teamawesome.cs180frontend.API.Models.StatusModel.PostReviewStatus;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.PostReviewCallback;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class WriteReviewActivity extends AppCompatActivity {

    @Bind(R.id.activity_write_review) CoordinatorLayout parent;
    @Bind(R.id.write_professor_et) AutoCompleteTextView professorName;
    @Bind(R.id.write_class_et) AutoCompleteTextView className;
    @Bind(R.id.write_review_et) EditText reviewText;
    @Bind(R.id.write_rate_1) Button star1;
    @Bind(R.id.write_rate_2) Button star2;
    @Bind(R.id.write_rate_3) Button star3;
    @Bind(R.id.write_rate_4) Button star4;
    @Bind(R.id.write_rate_5) Button star5;
    @Bind(R.id.write_submit_bt) Button submit;

    private Button[] stars;
    private String[] professorNameArr;
    private String[] classNameArr;
    private int rating;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);

        stars = new Button[]{star1, star2, star3, star4, star5};

        rating = 0;

        //Fill autocomplete textviews for professor and classs choice
        List<ProfBundle> professors = DataSingleton.getInstance().getProfessorCache();
        professorNameArr = new String[professors.size()];
        for(int i = 0; i < professors.size(); i++) {
            professorNameArr[i] = professors.get(i).getProfessorName();
        }
        ArrayAdapter<String> profAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, professorNameArr);
        professorName.setAdapter(profAdapter);

        List<ClassBundle> classes = DataSingleton.getInstance().getClassCache();
        classNameArr = new String[classes.size()];
        for(int i = 0; i < classes.size(); i++) {
            classNameArr[i] = classes.get(i).getClassName();
        }
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, classNameArr);
        className.setAdapter(classAdapter);
    }

    @OnClick(R.id.write_submit_bt)
    public void submitReview() {

        String professorName = this.professorName.getText().toString();
        Integer profId = DataSingleton.getInstance().getProfessorId(professorName);

        String className = this.className.getText().toString();
        Integer classId = DataSingleton.getInstance().getClassId(className);

        String reviewText = this.reviewText.getText().toString();

        View focusView = null;

        if(profId != null) {
            if(classId != null) {
               if(reviewText.length() >= 32) {
                   if(rating > 0) {
                       int userId = Utils.getUserId(this);
                       int schoolId = Utils.getSchoolId(this);
                       String password = Utils.getPassword(this);
                       System.out.println("PROF ID " + profId + "\nCLASS ID " + classId + "\nSCHOOL_ID " + schoolId + "\nUSER ID " + userId + "\nPASSWORD " + password + "\nRATING " + rating + "\nREVIEW " + reviewText);
                       UserReview r = new UserReview(userId, password, classId, profId, rating, reviewText, schoolId);
                       submitReview(r);
                   } else {
                       Utils.showSnackbar(this, parent, R.color.colorPrimary,
                               getString(R.string.invalid_rating));
                   }
               } else {
                   Utils.showSnackbar(this, parent, R.color.colorPrimary,
                           getString(R.string.review_not_long_enough));
               }
            } else {
                this.className.setError(getString(R.string.select_valid_class));
                focusView = this.className;
            }
        } else {
            this.professorName.setError(getString(R.string.prof_dne));
            focusView = this.professorName;
        }
        if(focusView != null) {
            focusView.requestFocus();
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
        rating = count;
        for(int i = 0; i < 5; i++){
            if(i < count) {
                stars[i].setTextColor(getApplicationContext().getResources().getColor(R.color.colorGreen));
            } else {
                stars[i].setTextColor(getApplicationContext().getResources().getColor(R.color.colorGrey));
            }
        }
    }

    public void submitReview(UserReview r) {
        progressDialog.show();
        PostReviewCallback callback = new PostReviewCallback();
        RetrofitSingleton.getInstance().getMatchingService()
                .review(r)
                .enqueue(callback);
    }

    @Subscribe
    public void reviewResp(ReviewIDRespBundle r) {
        progressDialog.dismiss();
        reviewText.setText("");
        professorName.setText("");
        className.setText("");
        Utils.showSnackbar(this, parent, R.color.colorPrimary,
                getString(R.string.account_update_success));

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Subscribe
    public void onFailedReviewPost(PostReviewStatus status) {
        progressDialog.dismiss();
        if(status.getStatus() == APIConstants.HTTP_STATUS_ERROR) {
            Utils.showSnackbar(this, parent, R.color.colorPrimary,
                    getString(R.string.already_submitted_review));
        } else if (status.getStatus() == APIConstants.HTTP_STATUS_UNAUTHORIZED) {
            Utils.showSnackbar(this, parent, R.color.colorPrimary,
                    getString(R.string.prof_or_class_DNE));
        } else {
            Utils.showSnackbar(this, parent, R.color.colorPrimary,
                    getString(R.string.failed_to_submit_review));
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
