package teamawesome.cs180frontend.Activities.Settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.DataModel.SchoolBundle;
import teamawesome.cs180frontend.API.Models.StatusModel.FailedUpdate;
import teamawesome.cs180frontend.API.Models.StatusModel.VerifyStatus;
import teamawesome.cs180frontend.API.Models.AccountModel.UpdatePasswordStatus;
import teamawesome.cs180frontend.API.Models.AccountModel.UpdateSchoolStatus;
import teamawesome.cs180frontend.API.Models.AccountModel.UpdateAccountBundle;
import teamawesome.cs180frontend.API.Models.AccountModel.VerifyBundle;
import teamawesome.cs180frontend.API.Models.AccountModel.VerifyResp;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.ChangePasswordCallback;
import teamawesome.cs180frontend.API.Services.Callbacks.ChangeSchoolCallback;
import teamawesome.cs180frontend.API.Services.Callbacks.VerifyCallback;
import teamawesome.cs180frontend.Adapters.SimpleACAdapter;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class SettingElemActivity extends AppCompatActivity {

    @Bind(R.id.activity_setting_elem) CoordinatorLayout parent;
    @Bind({R.id.setting_elem1,
            R.id.setting_elem2,
            R.id.setting_elem3}) LinearLayout[] settingElems;
    @Bind(R.id.settings_university_ac) AutoCompleteTextView schoolAC;
    @Bind(R.id.settings_school_bt) Button schoolButton;
    @Bind(R.id.old_password_et) EditText oldPasswordET;
    @Bind(R.id.new_password_et) EditText newPasswordET;
    @Bind(R.id.verify_et) EditText verifyET;
    @Bind(R.id.change_school_TIL) TextInputLayout changeSchoolTIL;
    @Bind(R.id.settings_old_password_til) TextInputLayout oldPasswordTIL;
    @Bind(R.id.settings_new_password_til) TextInputLayout newPasswordTIL;
    @Bind(R.id.verify_til) TextInputLayout verifyTIL;

    private ProgressDialog progressDialog;
    private VerifyBundle bundle;
    private VerifyCallback verifyCallback;
    private SchoolBundle newSchool;

    private int schoolId;
    private String newPasswordHolder; //HOLDS THE NEW PASSWORD AS IT'S BEING CHANGED\
    private boolean makingAPICall = false;
    int lastClicked = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_elem);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            finish();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(extras.getString(Constants.TAG, ""));
        settingElems[extras.getInt(Constants.INDEX, -1)].setVisibility(View.VISIBLE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        verifyCallback = new VerifyCallback(this);

        schoolId = Utils.getSchoolId(this);

        //fill school AC adapter
        SimpleACAdapter schoolAdapter = new SimpleACAdapter(this, R.layout.simple_list_item,
                DataSingleton.getInstance().getSchoolCache());
        schoolAC.setAdapter(schoolAdapter);
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

    @OnClick(R.id.settings_school_bt)
    public void changeSchool() {
        changeSchoolTIL.setError(null);
        Utils.hideKeyboard(parent, this);
        newSchool = DataSingleton.getInstance()
                .getSchoolBundleByName(schoolAC.getText().toString());
        if (newSchool != null) {
            if (newSchool.getSchoolId() != Utils.getSchoolId(this)) {

                UpdateAccountBundle user = new UpdateAccountBundle(Utils.getUserId(this),
                        Utils.getPassword(this),
                        Utils.getPassword(this),
                        newSchool.getSchoolId());

                progressDialog.setMessage(getString(R.string.updating_account));
                progressDialog.show();

                Utils.setChangeSchool(this, true);
                ChangeSchoolCallback callback = new ChangeSchoolCallback();
                RetrofitSingleton.getInstance()
                        .getUserService()
                        .updateAccount(user)
                        .enqueue(callback);
            } else {
                changeSchoolTIL.setError(getString(R.string.same_school_error));
            }
        } else {
            changeSchoolTIL.setError(getString(R.string.school_dne));
        }
    }

    @OnClick(R.id.settings_password_bt)
    public void changePassword() {
        Utils.hideKeyboard(parent, this);
        oldPasswordTIL.setError(null);
        newPasswordTIL.setError(null);
        View focusView = null;

        String oldPassword = Utils.getMD5Hash(oldPasswordET.getText().toString());
        String rawNewPassword = newPasswordET.getText().toString();
        String newPassword = Utils.getMD5Hash(rawNewPassword);

        if (rawNewPassword.length() < 8) {
            newPasswordTIL.setError(getString(R.string.error_invalid_password));
            focusView = newPasswordET;
        } else if (oldPassword.equals(Utils.getPassword(this))) {
            newPasswordHolder = newPassword;
            UpdateAccountBundle user = new UpdateAccountBundle(Utils.getUserId(this),
                    oldPassword,
                    newPassword,
                    schoolId);

            progressDialog.setMessage(getString(R.string.updating_account));
            progressDialog.show();

            ChangePasswordCallback callback = new ChangePasswordCallback();
            RetrofitSingleton.getInstance()
                    .getUserService()
                    .updateAccount(user)
                    .enqueue(callback);
        } else {
            oldPasswordTIL.setError(getString(R.string.old_password_error));
            focusView = oldPasswordET;
        }

        if (focusView != null) {
            focusView.requestFocus();
        }
    }

    @OnClick(R.id.verify_bt)
    public void verifyAcc() {
        if (!makingAPICall) {
            makingAPICall = true;
            String pin = verifyET.getText().toString();

            if (pin.length() < 4) {
                Utils.showSnackBar(this, parent, R.color.colorPrimary, getString(R.string.pin_too_short));
                makingAPICall = false;
                return;
            }

            Utils.hideKeyboard(parent, this);
            progressDialog.setMessage(getString(R.string.verifying));
            progressDialog.show();

            if (bundle == null) {
                bundle = new VerifyBundle(Utils.getUserId(this),
                        Utils.getPassword(this), pin);
            } else {
                bundle.changeValues(Utils.getUserId(this),
                        Utils.getPassword(this), pin);
            }

            RetrofitSingleton.getInstance()
                    .getUserService()
                    .verifyUser(bundle)
                    .enqueue(verifyCallback);
        }
    }

    @Subscribe
    public void onUpdateSchoolResp(UpdateSchoolStatus resp) {
        progressDialog.dismiss();
        if (resp.getStatus() == APIConstants.HTTP_STATUS_OK) {
            Utils.setChangeSchool(this, false);
            Utils.saveSchoolData(this, newSchool);
            schoolAC.setText("");
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Utils.showSnackBar(this, parent, R.color.colorPrimary,
                    getString(R.string.invalid_user_data));
        }
    }

    @Subscribe
    public void onUpdatePasswordResp(UpdatePasswordStatus resp) {
        progressDialog.dismiss();
        if (resp.getStatus() == APIConstants.HTTP_STATUS_OK) {
            Utils.savePassword(this, newPasswordHolder);
            oldPasswordET.setText("");
            newPasswordET.setText("");
            Utils.showSnackBar(this, parent, R.color.colorPrimary,
                    getString(R.string.account_update_success));
        } else {
            Utils.showSnackBar(this, parent, R.color.colorPrimary,
                    getString(R.string.invalid_user_data));
        }
    }

    @Subscribe
    public void onUpdateFailure(FailedUpdate failed) {
        progressDialog.dismiss();
        Utils.showSnackBar(this, parent, R.color.colorPrimary,
                getString(R.string.update_failed));
    }

    @Subscribe
    public void onVerifyResp(VerifyResp resp) {
        progressDialog.hide();
        //I'm lazy I'll let MainActivity handle everything
        finish();
    }

    @Subscribe
    public void onVerifyFailed(VerifyStatus status) {
        if (status.getContext().equals(this)) {
            makingAPICall = false;
            Utils.failedVerifySnackBar(progressDialog, parent, R.color.colorPrimary, status.getStatus(), this);
        }
    }
}
