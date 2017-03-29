package teamawesome.cs180frontend.Activities.Onboarding;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.DataModel.CacheData.CacheDataBundle;
import teamawesome.cs180frontend.API.Models.StatusModel.CacheReqStatus;
import teamawesome.cs180frontend.API.Models.StatusModel.LoginRegisterStatus;
import teamawesome.cs180frontend.API.Models.UserModel.AccountBundle;
import teamawesome.cs180frontend.API.Models.UserModel.UserRespBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.LoginRegisterCallback;
import teamawesome.cs180frontend.Adapters.SimpleACAdapter;
import teamawesome.cs180frontend.Listeners.AnimationListener.Onboarding.AnimationListener1;
import teamawesome.cs180frontend.Listeners.AnimationListener.Onboarding.AnimationListener2;
import teamawesome.cs180frontend.Listeners.AnimationListener.Onboarding.HideTextAnim1;
import teamawesome.cs180frontend.Listeners.AnimationListener.Onboarding.UpdateTextAnimation1;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Events.FinishEvent;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;
import teamawesome.cs180frontend.Runnable.InitOnboardRunnable;

public class AccountInfoActivity extends AppCompatActivity {
    private final Context context = this;
    @Bind(R.id.parent_account_info) CoordinatorLayout parent;
    @Bind(R.id.school_layout) LinearLayout schoolLayout;
    @Bind(R.id.info_layout_1) LinearLayout infoLayout1;
    @Bind(R.id.onboard_phone_number_TIL) TextInputLayout phoneNumberTIL;
    @Bind(R.id.onboarding_password_til) TextInputLayout passwordTIL;
    @Bind(R.id.onboarding_school_TIL) TextInputLayout schoolTIL;
    @Bind(R.id.onboarding_school_ac) AutoCompleteTextView schoolAC;
    @Bind(R.id.onboard_text_1) TextView onboardText1;
    @Bind(R.id.loading_schools) TextView loadingTextView;
    @Bind(R.id.onboarding_number) EditText phoneNumber;
    @Bind(R.id.onboarding_password) EditText password;
    @Bind(R.id.next) Button next;
    @Bind(R.id.done) Button done;

    private Handler handler;
    private AccountBundle registerBundle;
    private LoginRegisterCallback callback;
    private ProgressDialog progressDialog;

    private boolean nextClicked = false;
    private boolean textHidden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        callback = new LoginRegisterCallback();

        initAnimation();
        createProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_in_250, R.anim.slide_right_out_250);
    }

    private void createProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.creating_account));
    }

    private void initAnimation() {
        handler = new Handler();
        handler.postDelayed(new InitOnboardRunnable(this, onboardText1,
                infoLayout1, next,
                View.VISIBLE), 100);
    }

    public void setUpSchoolAC() {
        SimpleACAdapter adapter = new SimpleACAdapter(this, R.layout.simple_list_item,
                DataSingleton.getInstance().getSchoolCache());
        schoolAC.setAdapter(adapter);

        AlphaAnimation hideLoadingText = new AlphaAnimation(1.0f, 0.0f);
        hideLoadingText.setAnimationListener(new AnimationListener1(this, loadingTextView,
                schoolLayout, done, View.GONE));
        hideLoadingText.setDuration(650);

        AlphaAnimation hideOnboardText = new AlphaAnimation(1.0f, 0.0f);
        hideOnboardText.setAnimationListener(new UpdateTextAnimation1(onboardText1,
                loadingTextView, getString(R.string.enter_school), hideLoadingText, View.INVISIBLE));
        hideOnboardText.setDuration(650);
        onboardText1.startAnimation(hideOnboardText);
    }

    public View checkNumberAndPass() {
        phoneNumberTIL.setError(null);
        passwordTIL.setError(null);
        phoneNumberTIL.clearFocus();
        passwordTIL.clearFocus();

        if (phoneNumber.getText().toString().length() < 10) {
            phoneNumberTIL.setError(getString(R.string.error_invalid_number));
            return phoneNumberTIL;
        }

        if (password.getText().toString().length() < 8) {
            passwordTIL.setError(getString(R.string.error_invalid_password));
            return passwordTIL;
        }

        return null;
    }

    @OnClick(R.id.next)
    public void onNext() {
        if (!nextClicked) {
            nextClicked = true;
            Utils.hideKeyboard(parent, this);

            View v = checkNumberAndPass();
            if (v != null) {
                v.requestFocus();
                nextClicked = false;
                return;
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    AlphaAnimation hideNext = new AlphaAnimation(1.0f, 0.0f);
                    hideNext.setAnimationListener(new AnimationListener2(next,
                            loadingTextView,
                            View.GONE,
                            true));
                    hideNext.setDuration(650);
                    next.startAnimation(hideNext);
                }
            });
        }
    }

    @OnClick(R.id.done)
    public void createNewAccount() {
        schoolTIL.setError(null);

        View faultyPrevView = checkNumberAndPass();
        if (faultyPrevView != null) {
            faultyPrevView.requestFocus();
            return;
        }

        Utils.hideKeyboard(parent, this);
        String schoolName = schoolAC.getText().toString();

        if (schoolName.length() == 0) {
            schoolTIL.setError(getString(R.string.school_empty));
            return;
        }

        Integer schoolId = DataSingleton.getInstance().getSchoolId(schoolName);
        if (schoolId == null) {
            schoolTIL.setError(getString(R.string.school_dne));
            return;
        }

        //done.setClickable(false);

        if (registerBundle == null) {
            registerBundle = new AccountBundle(phoneNumber.getText().toString(),
                    password.getText().toString(), schoolId);
        } else {
            registerBundle.setLoginRegisterBundle(phoneNumber.getText().toString(),
                    password.getText().toString(), schoolId);
        }

        if (!textHidden) {
            textHidden = true;
            AlphaAnimation hideText = new AlphaAnimation(1.0f, 0.0f);
            hideText.setAnimationListener(new HideTextAnim1(View.INVISIBLE, registerBundle,
                    callback, onboardText1, progressDialog));
            hideText.setDuration(800);
            onboardText1.startAnimation(hideText);
        } else {
            progressDialog.show();
            RetrofitSingleton.getInstance()
                    .getUserService()
                    .register(registerBundle)
                    .enqueue(callback);
        }
    }

    @Subscribe //already have cached schools
    public void onDataAlreadyFetched(Boolean b) {
        setUpSchoolAC();
    }

    @Subscribe
    public void onDataFetched(CacheDataBundle data) {
        DataSingleton.getInstance().cacheDataBundle(this, data);
        setUpSchoolAC();
    }

    @Subscribe
    public void failedDataResp(CacheReqStatus resp) {
        if (resp.getStatus() != -1) {
            Utils.showSnackbar(this, parent,
                    R.color.colorPrimary, getString(R.string.data_doesnt_exist));
        } else {
            Utils.showSnackbar(this, parent,
                    R.color.colorPrimary, getString(R.string.error_getting_data));
        }
    }

    @Subscribe //listening to FinishEvent to ensure that finishing activity chaining will happen
    public void onRegister(FinishEvent<UserRespBundle> bundle) {
        Utils.saveUserData(this, bundle.getObject(), registerBundle.getPassword(),
                registerBundle.getPhoneNumber());
        Intent intent = new Intent(this, VerifyActivity.class);
        progressDialog.dismiss();
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_250, R.anim.slide_left_out_250);
        finish();
    }

    @Subscribe
    public void onResisterFailure(LoginRegisterStatus status) {
        progressDialog.dismiss();

        int respCode = status.getStatus();
        if (respCode == APIConstants.HTTP_STATUS_UNAUTHORIZED) {
            Utils.showSnackbar(this, parent, R.color.colorAccent, getString(R.string.account_exists));
        } else if (respCode == APIConstants.HTTP_STATUS_ERROR || respCode == -1) {
            Utils.showSnackbar(this, parent, R.color.colorAccent, getString(R.string.server_issue));
        } else if (respCode == APIConstants.TOO_MANY_REQ) {
            Utils.showSnackbar(this, parent, R.color.colorAccent, getString(R.string.too_many_req));
        }

        done.setClickable(true);
    }

}
