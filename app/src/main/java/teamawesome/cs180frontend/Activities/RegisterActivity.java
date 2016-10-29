package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.API.Models.LoginRegisterBundle;
import teamawesome.cs180frontend.API.Models.RegisterBundle;
import teamawesome.cs180frontend.API.Models.UserRespBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.LoginRegisterCallback;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;


//TODO: School id on register
/**
 * A register screen that offers register via phone number/password.
 */
public class RegisterActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private LoginRegisterBundle registerRegisterBundle;

    // UI references.
    @Bind(R.id.register_parent) CoordinatorLayout parent;
    @Bind(R.id.number) EditText phoneEditText;
    @Bind(R.id.password) EditText mPasswordView;
    @Bind(R.id.register_form) View mRegisterFormView;
    @Bind(R.id.sign_in_button) Button signInButton;
    @Bind(R.id.login_text) TextView loginTextView;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the register form.
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.login_text)
    public void startLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Attempts to sign in or register the account specified by the register form.
     * If there are form errors (invalid phone number, missing fields, etc.), the
     * errors are presented and no actual register attempt is made.
     */
    @OnClick(R.id.register_button)
    public void attemptRegister() {

        // Reset errors.
        phoneEditText.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the register attempt.
        String phoneNum = phoneEditText.getText().toString();
        String password = Utils.getMD5Hash(mPasswordView.getText().toString());

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid phone number.
        if (TextUtils.isEmpty(phoneNum)) {
            phoneEditText.setError(getString(R.string.error_field_required));
            focusView = phoneEditText;
            cancel = true;
        } else if (!isNumberValid(phoneNum)) {
            phoneEditText.setError(getString(R.string.error_invalid_number));
            focusView = phoneEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Utils.hideKeyboard(parent, this);
            RegisterBundle RegisterBundle = new RegisterBundle(phoneNum, password);
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.register_loading));
            progressDialog.setCancelable(false);
            progressDialog.show();

            //Login & register are nearly identical => use the same callback
            RetrofitSingleton.getInstance().
                    getUserService().
                    register(RegisterBundle)
                    .enqueue(new LoginRegisterCallback());
        }
    }

    private boolean isNumberValid(String number) {
        return number.length() == 10;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 8;
    }

    @Subscribe
    public void onLoginOrRegister(UserRespBundle resp) {
        progressDialog.dismiss();
        Utils.saveUserData(this, resp, registerRegisterBundle.getPassword(),
                registerRegisterBundle.getPhoneNumber());
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        System.out.println("FINISH");
        finish();
    }

    @Subscribe
    public void onRegisterResisterFailure(String msg) {
        progressDialog.dismiss();
        Utils.showSnackbar(this, parent, msg);
    }
}


