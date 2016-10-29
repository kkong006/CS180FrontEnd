package teamawesome.cs180frontend.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import teamawesome.cs180frontend.API.Models.School;
import teamawesome.cs180frontend.API.Models.UpdateUserBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetSchoolsCallback;
import teamawesome.cs180frontend.API.Services.Callbacks.PostUpdateAccountCallback;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.SPSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class SettingsActivity extends AppCompatActivity {

//    @Bind(R.id.settings_university_et) EditText mSchoolNameET;
//    @Bind(R.id.settings_current_university_tv) TextView mCurrentUniversityName;
    @Bind(R.id.settings_university_sp) Spinner mSpinner;
    @Bind(R.id.new_password_et) EditText mNewPasswordET;

    private ProgressDialog mProgressDialog;
    private String mSchoolName;
    private int mSchoolId;
    private String mNewSchoolName;
    private int mNewSchoolId;
    private List<School> mSchools;
    private String[] mSchoolNames;
    private ArrayAdapter<String> mAdapter;
    private boolean mSetSchool;
    private String mNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mNewPasswordET.setText("");

        mSetSchool = false;

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        Callback callback = new GetSchoolsCallback();
        RetrofitSingleton.getInstance().getMatchingService()
                .getSchools()
                .enqueue(callback);
    }

    @Subscribe
    public void schoolsResp(List<School> schoolList) {
        mProgressDialog.dismiss();
        mSchools = schoolList;

        mSchoolNames = new String[schoolList.size()];
        for(int i = 0; i < schoolList.size(); i++) {
            mSchoolNames[i] = schoolList.get(i).getSchoolName();
            if(schoolList.get(i).getSchoolId() == getSharedPreferences(Constants.SCHOOL_ID, Context.MODE_PRIVATE).getInt(Constants.SCHOOL_ID, -1)) {
                mSchoolName = schoolList.get(i).getSchoolName();
                mSchoolId = schoolList.get(i).getSchoolId();
//                mCurrentUniversityName.setText(mSchoolName);
            }

//            if(school.getSchoolName() == mSchoolName) {

//                mCurrentUniversityName.setText(mSchoolName);
//                Toast.makeText(this, getResources().getString(R.string.school_name_changed), Toast.LENGTH_SHORT).show();
//                return;
//            }
        }

        if(schoolList.size() == 0) {
            Toast.makeText(this, getResources().getString(R.string.university_dne), Toast.LENGTH_SHORT).show();
            return;
        }

        fillSpinner();
    }

    public void fillSpinner() {
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mSchoolNames);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        int i = 0;
        for(; i < mSchoolNames.length; i++) {
            if(mSpinner.getItemAtPosition(i) == mSchoolName) {
                mSpinner.setSelection(i);
                break;
            }
        }

        mSpinner.setOnItemSelectedListener(new SpinnerActivity());
    }

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String result = parent.getItemAtPosition(position).toString();
            if(result != mSchoolName) {
                for(School school : mSchools) {
                    if (school.getSchoolName() == result ) {
                        mNewSchoolName = result;
                        mNewSchoolId = school.getSchoolId();
                        mSetSchool = true;
//                        Toast.makeText(getApplicationContext(), "set school", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    }

    @OnClick(R.id.settings_school_bt)
    public void selectSchool() {
        int school_id = SPSingleton.getInstance(this).getSp().getInt(Constants.SCHOOL_ID, -1);

        if(school_id != -1) {
            int id = getSharedPreferences(Constants.USER_ID, Context.MODE_PRIVATE).getInt(Constants.USER_ID, -1);
            String password = getSharedPreferences(Constants.PASSWORD, Context.MODE_PRIVATE).getString(Constants.PASSWORD, "");

            if(mSetSchool) {

                mSetSchool = false;
                mProgressDialog.show();
                Callback callback = new PostUpdateAccountCallback();
                RetrofitSingleton.getInstance().getUserService()
                        .updateAccount(new UpdateUserBundle(id, password, password, mSchoolId))
                        .enqueue(callback);
            } else {
                Toast.makeText(this, getResources().getString(R.string.enter_school_name), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.please_sign_in), Toast.LENGTH_SHORT).show();
        }


    }

    @OnClick(R.id.settings_password_bt)
    public void changePassword() {
        String old_password = SPSingleton.getInstance(this).getSp().getString(Constants.PASSWORD, "");
        String mNewPassword = Utils.getMD5Hash(mNewPasswordET.getText().toString());

        int id = Utils.getUserId(this);
        int school_id = Utils.getSchoolId(this);

        if(id == 0) {
            Toast.makeText(this, getResources().getString(R.string.not_logged_in), Toast.LENGTH_SHORT).show();
        } else if(mNewPassword != old_password){
            UpdateUserBundle u = new UpdateUserBundle(id, old_password, mNewPassword, 1);
            mProgressDialog.show();
            Callback callback = new PostUpdateAccountCallback();
            RetrofitSingleton.getInstance().getUserService()
                    .updateAccount(u)
                    .enqueue(callback);
        }
    }

    @Subscribe
    public void respInt(Integer i) {
        mProgressDialog.dismiss();
        if(i == 0) {
            Toast.makeText(this, getResources().getString(R.string.universities_dne), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_retrieving_data)+" query", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void respString(String s) {
        mProgressDialog.dismiss();
        if(s == "ERROR") {
            Toast.makeText(this, getResources().getString(R.string.error_retrieving_data), Toast.LENGTH_SHORT).show();
        } else if(s == "SUCCESS") {
            mSchoolName = mNewSchoolName;
            mSchoolId = mNewSchoolId;
            getSharedPreferences(Constants.SCHOOL_ID, Context.MODE_PRIVATE)
                    .edit().putInt(Constants.SCHOOL_ID, mSchoolId).apply();
            getSharedPreferences(Constants.PASSWORD, Context.MODE_PRIVATE)
                    .edit().putString(Constants.PASSWORD, mNewPassword).apply();
            mNewPasswordET.setText("");
            fillSpinner();
            Toast.makeText(this, getResources().getString(R.string.account_update_success), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

//    @OnClick(R.id.settings_logout_bt)
//    public void logout() {
//        getSharedPreferences(Constants.USER_ID, Context.MODE_PRIVATE)
//                .edit().clear().apply();
//        getSharedPreferences(Constants.PASSWORD, Context.MODE_PRIVATE)
//                .edit().clear().apply();
//        getSharedPreferences(Constants.SCHOOL_ID, Context.MODE_PRIVATE)
//                .edit().clear().apply();
//        Intent i = new Intent(this, LoginActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(i);
//        finish();
//    }
}
