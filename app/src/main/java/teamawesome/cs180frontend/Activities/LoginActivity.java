package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.API.Models.CacheDataBundle;
import teamawesome.cs180frontend.API.Models.LoginRegisterBundle;
import teamawesome.cs180frontend.API.Models.UserRespBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetCacheDataCallback;
import teamawesome.cs180frontend.API.Services.Callbacks.GetReviewsCallback;
import teamawesome.cs180frontend.API.Services.Callbacks.LoginRegisterCallback;
import teamawesome.cs180frontend.Adapters.SimpleListAdapter;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private LoginRegisterBundle loginRegisterBundle;

    // UI references.
    @Bind(R.id.login_parent)
    CoordinatorLayout parent;
    @Bind(R.id.number)
    EditText phoneEditText;
    @Bind(R.id.password)
    EditText mPasswordView;
    @Bind(R.id.login_form)
    View mLoginFormView;
    @Bind(R.id.sign_in_button)
    Button signInButton;
    @Bind(R.id.register_text)
    TextView registerTextView;
    @Bind(R.id.school_auto_complete)
    AutoCompleteTextView acTV;

    ProgressDialog progressDialog;
    SimpleListAdapter adapter;
    TranslateAnimation animation;
    boolean loginMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        if(Utils.getUserId(this) > 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        acTV.setVisibility(View.GONE);
        adapter = new SimpleListAdapter(this, R.layout.simple_list_item,
                DataSingleton.getInstance().getSchoolCache());
        acTV.setAdapter(adapter);
        acTV.setThreshold(1);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.register_text)
    public void startRegister() {
        if (registerTextView.getText().toString().equals(getString(R.string.register_now))) {
            loginMode = false;
            acTV.setVisibility(View.VISIBLE);
            signInButton.setText(getString(R.string.register));
            registerTextView.setText(getString(R.string.sign_in));
        } else {
            loginMode = true;
            acTV.setVisibility(View.GONE);
            signInButton.setText(getString(R.string.action_sign_in_short));
            registerTextView.setText(getString(R.string.register_now));
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    @OnClick(R.id.sign_in_button)
    public void attemptLogin() {

        // Reset errors.
        phoneEditText.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String phoneNum = phoneEditText.getText().toString();
        String password = Utils.getMD5Hash(mPasswordView.getText().toString());

        boolean cancel = false;
        Integer schoolId = null;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a phone number.
        if (TextUtils.isEmpty(phoneNum)) {
            phoneEditText.setError(getString(R.string.error_field_required));
            focusView = phoneEditText;
            cancel = true;
        } else if (!isNumberValid(phoneNum)) {
            phoneEditText.setError(getString(R.string.error_invalid_number));
            focusView = phoneEditText;
            cancel = true;
        } else if (!loginMode) {
            schoolId = DataSingleton.getInstance()
                    .getSchoolId(acTV.getText().toString());
            if (schoolId == null) {
                focusView = acTV;
                cancel = true;
            }
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
            } else {
                RetrofitSingleton.getInstance()
                        .getUserService()
                        .register(loginRegisterBundle)
                        .enqueue(new LoginRegisterCallback());
            }
        }
    }

    private boolean isNumberValid(String number) {
        return number.length() == 10;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 8;
    }

    private void getData() {
        progressDialog.show();
        RetrofitSingleton.getInstance()
                .getMatchingService()
                .getData(Utils.getSchoolId(this))
                .enqueue(new GetCacheDataCallback());
    }

    @Subscribe
    public void dataResp(CacheDataBundle data) {
        progressDialog.dismiss();
        DataSingleton.getInstance().cacheDataBundle(data);
        System.out.println("SCHOOL ID " + Utils.getSchoolId(this));
        System.out.println("SCHOOL SIZE " + DataSingleton.getInstance().getSchoolCache().size());
        System.out.println("PROFESSOR SIZE " + DataSingleton.getInstance().getProfessorCache().size());
        System.out.println("CLASS SIZE " + DataSingleton.getInstance().getClassCache().size());
        System.out.println("SUBJECT SIZE " + DataSingleton.getInstance().getSubjectCache().size());

        acTV.setVisibility(View.GONE);
        adapter = new SimpleListAdapter(this, R.layout.simple_list_item,
                DataSingleton.getInstance().getSchoolCache());
        acTV.setAdapter(adapter);
        acTV.setThreshold(1);
    }

    @Subscribe
    public void intResp(Integer i) {
        progressDialog.dismiss();
        if (i.equals(0)) {
            Utils.showSnackbar(this, parent, getString(R.string.data_doesnt_exist));
        } else if (i.equals(-1)) {
            Utils.showSnackbar(this, parent, getString(R.string.error_getting_data));
        }
    }

    @Subscribe
    public void onLoginOrRegister(UserRespBundle resp) {
        progressDialog.dismiss();
        Utils.saveUserData(this, resp, loginRegisterBundle.getPassword(),
                loginRegisterBundle.getPhoneNumber());
        Intent intent = new Intent(this, MainActivity.class);
        setResult(RESULT_OK, intent);
        System.out.println("FINISH");
        startActivity(intent);
        finish();
    }

    @Subscribe
    public void onLoginResisterFailure(String msg) {
        progressDialog.dismiss();
        Utils.showSnackbar(this, parent, msg);
    }
}
