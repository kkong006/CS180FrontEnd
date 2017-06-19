package teamawesome.cs180frontend.Activities.Onboarding;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.API.Models.StatusModel.LoginRegisterStatus;
import teamawesome.cs180frontend.API.Models.UserModel.AccountBundle;
import teamawesome.cs180frontend.API.Models.UserModel.UserRespBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.LoginRegisterCallback;
import teamawesome.cs180frontend.Activities.Application.MainActivity;
import teamawesome.cs180frontend.Misc.Events.FinishEvent;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class LoginActivity extends AppCompatActivity {

    private AccountBundle accountBundle;

    // UI references.
    @Bind(R.id.login_parent) CoordinatorLayout parent;
    @Bind(R.id.number) EditText phoneEditText;
    @Bind(R.id.password) EditText passwordEditText;
    @Bind(R.id.password_til) TextInputLayout passwordTIL;
    @Bind(R.id.phone_number_til) TextInputLayout phoneNumberTIL;
    @Bind(R.id.sign_in_button) Button signInButton;
    @Bind(R.id.register) TextView registerText;
    @Bind(R.id.error) TextView errorTV;

    ProgressDialog progressDialog;
    boolean isRegistering = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        isRegistering = false;
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
        isRegistering = true;
        Intent intent = new Intent(this, AccountInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_250, R.anim.slide_left_out_250);
    }

    @OnClick(R.id.sign_in_button)
    public void attemptLogin() {
        // Reset errors.
        phoneNumberTIL.setError(null);
        passwordTIL.setError(null);
        phoneEditText.clearFocus();
        passwordEditText.clearFocus();
        errorTV.setText("");

        // Store values at the time of the login attempt.
        String phoneNum = phoneEditText.getText().toString();
        String rawPassword = passwordEditText.getText().toString();
        String password = Utils.getMD5Hash(rawPassword);

        boolean cancel = false;
        View focusView = null;

        if (phoneNum.isEmpty() || rawPassword.isEmpty()) { // Check for a phone number
            (phoneNum.isEmpty() ? phoneNumberTIL : passwordTIL).setError(" ");
            errorTV.setText(R.string.error_field_required);
            focusView = (phoneNum.isEmpty() ? phoneEditText : passwordEditText);
            cancel = true;
        } else if (!isNumberValid(phoneNum)) {
            phoneNumberTIL.setError(" ");
            errorTV.setText(R.string.error_invalid_number);
            focusView = phoneEditText;
            cancel = true;
        } else if (!isPasswordValid(rawPassword)) {
            // Check for a valid password, if the user entered one.
            passwordTIL.setError(" ");
            errorTV.setText(R.string.error_invalid_password);
            focusView = passwordEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first form field with an error.
            focusView.requestFocus();
        } else {
            Utils.hideKeyboard(parent, this);
            accountBundle = new AccountBundle(phoneNum, password, null);
            // Show a progress spinner, and kick off a background task to perform the user login attempt.
            progressDialog.setMessage(getString(R.string.login_loading));
            progressDialog.show();

            //Login & register are nearly identical => use the same callback
            RetrofitSingleton.getInstance()
                    .getUserService()
                    .login(accountBundle)
                    .enqueue(new LoginRegisterCallback());
        }
    }

    @Subscribe //Successful response
    public void onLogin(UserRespBundle resp) {
        if (!isRegistering) {
            progressDialog.dismiss();
            Utils.saveUserData(this, resp, accountBundle.getPassword(),
                    accountBundle.getPhoneNumber());
            Intent intent = new Intent(this, MainActivity.class);
            setResult(RESULT_OK, intent);
            startActivity(intent);
            finish();
        } else {
            EventBus.getDefault().post(new FinishEvent<>(1, resp));
            finish();
        }
    }

    @Subscribe
    public void onLoginFailure(LoginRegisterStatus status) {
        if (!isRegistering) {
            progressDialog.dismiss();
            if (status.getStatus() == -1) {
                Utils.showSnackBar(this, parent, R.color.colorPrimary,
                        getString(R.string.unable_to_login));
            } else {
                Utils.showSnackBar(this, parent, R.color.colorPrimary,
                        getString(R.string.login_failed));
            }
        }
    }
}
