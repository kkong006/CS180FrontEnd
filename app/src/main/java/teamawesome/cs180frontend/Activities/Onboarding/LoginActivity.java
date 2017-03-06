package teamawesome.cs180frontend.Activities.Onboarding;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.API.Models.DataModel.CacheData.CacheDataBundle;
import teamawesome.cs180frontend.API.Models.StatusModel.CacheReqStatus;
import teamawesome.cs180frontend.API.Models.StatusModel.LoginRegisterStatus;
import teamawesome.cs180frontend.API.Models.UserModel.LoginRegisterBundle;
import teamawesome.cs180frontend.API.Models.DataModel.SchoolBundle;
import teamawesome.cs180frontend.API.Models.UserModel.UserRespBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetCacheDataCallback;
import teamawesome.cs180frontend.API.Services.Callbacks.LoginRegisterCallback;
import teamawesome.cs180frontend.Activities.Application.MainActivity;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class LoginActivity extends AppCompatActivity {

    private LoginRegisterBundle loginRegisterBundle;

    // UI references.
    @Bind(R.id.login_parent) CoordinatorLayout parent;
    @Bind(R.id.login_form) ScrollView loginFormView;
    @Bind(R.id.number) EditText phoneEditText;
    @Bind(R.id.password) EditText passwordEditText;
    @Bind(R.id.password_til) TextInputLayout passwordTIL;
    @Bind(R.id.phone_number_til) TextInputLayout phoneNumberTIL;
    @Bind(R.id.sign_in_button) Button signInButton;
    @Bind(R.id.register) TextView registerText;

    ProgressDialog progressDialog;
    boolean loginMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Utils.showSnackbar(this, parent, R.color.colorPrimary,
                    extras.getString(Constants.MESSAGE, null));
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private boolean isNumberValid(String number) {
        return number.length() == 10;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    @OnClick(R.id.register)
    public void startRegister() {
        EventBus.getDefault().unregister(this);
        Intent intent = new Intent(this, AccountInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.sign_in_button)
    public void attemptLogin() {

        // Reset errors.
        phoneNumberTIL.setError(null);
        passwordTIL.setError(null);

        // Store values at the time of the login attempt.
        String phoneNum = phoneEditText.getText().toString();
        String rawPassword = passwordEditText.getText().toString();
        String password = Utils.getMD5Hash(rawPassword);

        boolean cancel = false;
        Integer schoolId = null;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(rawPassword) && !isPasswordValid(rawPassword)) {
            passwordTIL.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        // Check for a phone number
        if (TextUtils.isEmpty(phoneNum)) {
            phoneNumberTIL.setError(getString(R.string.error_field_required));
            focusView = phoneEditText;
            cancel = true;
        } else if (!isNumberValid(phoneNum)) {
            phoneNumberTIL.setError(getString(R.string.error_invalid_number));
            focusView = phoneEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Utils.hideKeyboard(parent, this);
            loginRegisterBundle = new LoginRegisterBundle(phoneNum, password,
                    loginMode ? 1 : schoolId.intValue());
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            progressDialog.setMessage(loginMode ? getString(R.string.login_loading) :
                    getString(R.string.register_loading));
            progressDialog.show();

            //Login & register are nearly identical => use the same callback
            if (loginMode) {
                RetrofitSingleton.getInstance()
                        .getUserService()
                        .login(loginRegisterBundle)
                        .enqueue(new LoginRegisterCallback());
            }
        }
    }

    @Subscribe //Successful response
    public void onLogin(UserRespBundle resp) {
        progressDialog.dismiss();
        Utils.saveUserData(this, resp, loginRegisterBundle.getPassword(),
                loginRegisterBundle.getPhoneNumber());
        Intent intent = new Intent(this, MainActivity.class);
        setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();
    }

    @Subscribe
    public void onLoginResisterFailure(LoginRegisterStatus status) {
        progressDialog.dismiss();
        if (status.getStatus() == -1) {
            Utils.showSnackbar(this, parent, R.color.colorPrimary,
                    getString(R.string.unable_to_login));
        } else {
            Utils.showSnackbar(this, parent, R.color.colorPrimary,
                    getString(R.string.login_failed));
        }
    }
}
