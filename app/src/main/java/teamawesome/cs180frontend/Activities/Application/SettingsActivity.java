package teamawesome.cs180frontend.Activities.Application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.StatusModel.FailedUpdate;
import teamawesome.cs180frontend.API.Models.UserModel.UpdatePasswordStatus;
import teamawesome.cs180frontend.API.Models.UserModel.UpdateSchoolStatus;
import teamawesome.cs180frontend.API.Models.UserModel.UpdateUserBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.ChangePasswordCallback;
import teamawesome.cs180frontend.API.Services.Callbacks.ChangeSchoolCallback;
import teamawesome.cs180frontend.Adapters.SimpleListAdapter;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.SPSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.activity_settings) CoordinatorLayout parent;
    @Bind(R.id.change_school) TextView changeSchoolTV;
    @Bind(R.id.change_password) TextView changePasswordTV;
    @Bind(R.id.settings_school_tv) TextView mUniversityTV;
    @Bind(R.id.settings_password_tv) TextView mPasswordTV;
    @Bind(R.id.settings_university_ac) AutoCompleteTextView schoolAC;
    @Bind(R.id.settings_school_bt) Button mSchoolButton;
    @Bind(R.id.old_password_et) EditText oldPasswordET;
    @Bind(R.id.new_password_et) EditText newPasswordET;
    @Bind(R.id.settings_password_bt) Button mPasswordButton;
    @Bind(R.id.settings_old_password_til) TextInputLayout oldPasswordTIL;
    @Bind(R.id.settings_new_password_til) TextInputLayout newPasswordTIL;

    private int mSchoolId;
    private int newSchoolId;

    private SimpleListAdapter schoolAdapter;
    private String newPasswordHolder; //HOLDS THE NEW PASSWORD AS IT'S BEING CHANGED
    private ProgressDialog progressDialog;

    SparseIntArray hiddenElemMap;
    int lastClicked = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mSchoolId = Utils.getSchoolId(this);

        fillAutoComplete();
        fillHiddenViewMap();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.hideKeyboard(parent, this);
    }

    public void fillAutoComplete() {
        schoolAdapter = new SimpleListAdapter(this,
                R.layout.simple_list_item,
                DataSingleton.getInstance().getSchoolCache());
        schoolAC.setAdapter(schoolAdapter);
    }

    public void fillHiddenViewMap() {
        hiddenElemMap = new SparseIntArray();
        hiddenElemMap.append(R.id.change_school, R.id.settings_dropdown1);
        hiddenElemMap.append(R.id.change_password, R.id.settings_dropdown2);
    }

    @OnClick(R.id.settings_school_bt)
    public void changeSchool() {
        Utils.hideKeyboard(parent, this);
        Integer schoolId = DataSingleton.getInstance().getSchoolId(schoolAC.getText().toString());
        if (schoolId != null) {
            if (schoolId != Utils.getSchoolId(this)) {
                newSchoolId = schoolId;

                UpdateUserBundle user = new UpdateUserBundle(Utils.getUserId(this),
                        Utils.getPassword(this),
                        Utils.getPassword(this),
                        schoolId);

                progressDialog.show();
                ChangeSchoolCallback callback = new ChangeSchoolCallback();
                RetrofitSingleton.getInstance()
                        .getUserService()
                        .updateAccount(user)
                        .enqueue(callback);
            } else {
                schoolAC.setError(getString(R.string.same_school_error));
            }
        } else {
            schoolAC.setError(getString(R.string.school_dne));
        }
    }

    @OnClick(R.id.settings_password_bt)
    public void changePassword() {
        Utils.hideKeyboard(parent, this);
        oldPasswordET.setError(null);
        newPasswordET.setError(null);
        View focusView = null;

        String oldPassword = Utils.getMD5Hash(oldPasswordET.getText().toString());
        String rawNewPassword = newPasswordET.getText().toString();
        String newPassword = Utils.getMD5Hash(rawNewPassword);

        if (rawNewPassword.length() < 8) {
            newPasswordTIL.setError(getString(R.string.error_invalid_password));
            focusView = newPasswordET;
        } else if (oldPassword.equals(Utils.getPassword(this))) {
            newPasswordHolder = newPassword;
            UpdateUserBundle user = new UpdateUserBundle(Utils.getUserId(this),
                    oldPassword,
                    newPassword,
                    mSchoolId);

            progressDialog.show();

            ChangePasswordCallback callback = new ChangePasswordCallback();
            RetrofitSingleton.getInstance()
                    .getUserService()
                    .updateAccount(user)
                    .enqueue(callback);
        } else {
            Utils.showSnackbar(this, parent, R.color.colorPrimary,
                    getString(R.string.old_password_error));
        }
        if (focusView != null) {
            focusView.requestFocus();
        }
    }

    @OnClick({R.id.change_school, R.id.change_password})
    public void onSettingsElemClick(View v) {
        Utils.hideKeyboard(parent, this);
        int viewToExpand = v.getId();

        if (lastClicked == viewToExpand) {
            View pressedOnView = findViewById(hiddenElemMap.get(lastClicked));

            if (pressedOnView.getVisibility() == View.VISIBLE) {
                pressedOnView.setVisibility(View.GONE);
            } else {
                pressedOnView.setVisibility(View.VISIBLE);
            }
            return;
        } else if (lastClicked != -1) {
            if (lastClicked == R.id.change_school) {
                schoolAC.setError(null);
            } else {
                oldPasswordET.setError(null);
                newPasswordET.setError(null);
            }
            findViewById(hiddenElemMap.get(lastClicked)).setVisibility(View.GONE);
        }

        lastClicked = viewToExpand;
        findViewById(hiddenElemMap.get(lastClicked)).setVisibility(View.VISIBLE);
        if (lastClicked == R.id.change_school) {
            schoolAC.requestFocus();
        } else {
            oldPasswordET.requestFocus();
        }
    }

    @Subscribe
    public void onUpdateSchoolResp(UpdateSchoolStatus resp) {
        progressDialog.dismiss();
        if (resp.getStatus() == APIConstants.HTTP_STATUS_OK) {
            SPSingleton.getInstance(this).getSp().edit().putInt(Constants.SCHOOL_ID, newSchoolId).commit();
            schoolAC.setText("");
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Utils.showSnackbar(this, parent, R.color.colorPrimary,
                    getString(R.string.invalid_user_data));
        }
    }

    @Subscribe
    public void onUpdatePasswordResp(UpdatePasswordStatus resp) {
        progressDialog.dismiss();
        if (resp.getStatus() == APIConstants.HTTP_STATUS_OK) {
            System.out.println(newPasswordHolder);
            Utils.savePassword(this, newPasswordHolder);
            System.out.println("NEW SCHOOL ID " + Utils.getSchoolId(this) + "\nNEW PASSWORD " + Utils.getPassword(this));
            oldPasswordET.setText("");
            newPasswordET.setText("");
            Utils.showSnackbar(this, parent, R.color.colorPrimary,
                    getString(R.string.account_update_success));
        } else {
            Utils.showSnackbar(this, parent, R.color.colorPrimary,
                    getString(R.string.invalid_user_data));
        }
    }

    @Subscribe
    public void onUpdateFailure(FailedUpdate failed) {
        progressDialog.dismiss();
        Utils.showSnackbar(this, parent, R.color.colorPrimary,
                getString(R.string.update_failed));
    }
}
