package teamawesome.cs180frontend.Activities.Application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewIDRespBundle;
import teamawesome.cs180frontend.API.Models.ReviewModel.UserReview;
import teamawesome.cs180frontend.API.Models.StatusModel.PostReviewStatus;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.PostReviewCallback;
import teamawesome.cs180frontend.Adapters.SimpleACAdapter;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class WriteReviewActivity extends AppCompatActivity {

    @Bind(R.id.activity_write_review) CoordinatorLayout parent;
    @Bind(R.id.write_professor_et) AutoCompleteTextView profAC;
    @Bind(R.id.write_class_et) AutoCompleteTextView classAC;
    @Bind(R.id.write_review_et) EditText reviewText;
    @Bind({R.id.write_rate_1, R.id.write_rate_2,
            R.id.write_rate_3, R.id.write_rate_4,
            R.id.write_rate_5}) Button[] stars;
    @Bind(R.id.write_submit_bt) Button submit;

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

        rating = 0;

        SimpleACAdapter profAdapter = new SimpleACAdapter(this, R.layout.simple_list_item,
                DataSingleton.getInstance().getProfessorCache());
        profAC.setAdapter(profAdapter);
        SimpleACAdapter classAdapter = new SimpleACAdapter(this, R.layout.simple_list_item,
                DataSingleton.getInstance().getClassCache());
        classAC.setAdapter(classAdapter);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.hideKeyboard(parent, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }

    @OnClick(R.id.write_submit_bt)
    public void submitReview() {

        String professorName = this.profAC.getText().toString();
        Integer profId = DataSingleton.getInstance().getProfessorId(professorName);

        String className = this.classAC.getText().toString();
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
                this.classAC.setError(getString(R.string.select_valid_class));
                focusView = this.classAC;
            }
        } else {
            this.profAC.setError(getString(R.string.prof_dne));
            focusView = this.profAC;
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
        profAC.setText("");
        classAC.setText("");
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
}
