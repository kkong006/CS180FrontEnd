package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import teamawesome.cs180frontend.API.Models.School;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetSchoolsCallback;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.R;

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.settings_university_et) EditText mSchoolNameET;
    @Bind(R.id.settings_current_university_tv) TextView mCurrentUniversityName;

    private ProgressDialog mProgressDialog;
    private String mSchoolName;
    private List<School> mSchools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.settings_logout_bt)
    public void logout() {
        getSharedPreferences(Constants.USER_ID, Context.MODE_PRIVATE)
                .edit().clear().apply();
        getSharedPreferences(Constants.PASSWORD, Context.MODE_PRIVATE)
                .edit().clear().apply();
        getSharedPreferences(Constants.SCHOOL_ID, Context.MODE_PRIVATE)
                .edit().clear().apply();
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.settings_school_tv)
    public void selectSchool() {
        //TODO: send request to change school
        mSchoolName = mSchoolNameET.getText().toString();
        if(mSchoolName.length() > 0) {
            mSchoolNameET.setText("");
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
            Callback callback = new GetSchoolsCallback();
            RetrofitSingleton.getInstance().getUserService()
                    .getSchools()
                    .enqueue(callback);
        } else {
                Toast.makeText(getApplicationContext(), "Enter school name", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void schoolsResp(List<School> schoolList) {
        mProgressDialog.dismiss();
        mSchools = schoolList;
        for(School school : schoolList) {
            if(school.getSchoolName() == mSchoolName) {
                getSharedPreferences(Constants.SCHOOL_ID, Context.MODE_PRIVATE)
                        .edit().putInt(Constants.SCHOOL_ID, school.getSchoolId()).apply();
                mCurrentUniversityName.setText(mSchoolName);
                Toast.makeText(getApplicationContext(), "School name changed!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(getApplicationContext(), "University does not exist", Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void schoolsInt(Integer i) {
        mProgressDialog.dismiss();
        if(i == 0) {
            Toast.makeText(getApplicationContext(), "Universities do not exist", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error retrieving data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
