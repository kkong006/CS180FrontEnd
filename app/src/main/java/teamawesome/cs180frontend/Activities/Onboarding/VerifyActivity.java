package teamawesome.cs180frontend.Activities.Onboarding;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import teamawesome.cs180frontend.API.Models.StatusModel.VerifyStatus;
import teamawesome.cs180frontend.API.Models.UserModel.VerifyBundle;
import teamawesome.cs180frontend.API.Models.UserModel.VerifyResp;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.VerifyCallback;
import teamawesome.cs180frontend.Activities.Application.MainActivity;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;
import teamawesome.cs180frontend.Runnable.InitOnboardRunnable;

public class VerifyActivity extends AppCompatActivity {

    @Bind(R.id.activity_verify) CoordinatorLayout parent;
    @Bind(R.id.verify_button_layout) LinearLayout buttonLayout;
    @Bind(R.id.onboard_verify_text) TextView verifyText;
    @Bind(R.id.onboard_verify_TIL) TextInputLayout verifyTIL;
    @Bind(R.id.onboarding_pin) EditText pinEditText;
    @Bind(R.id.onboard_verify) Button verify;

    Handler handler;
    ProgressDialog progressDialog;
    VerifyBundle bundle;
    VerifyCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        callback = new VerifyCallback(this);
        createProgressDialog();
        initAnimation();
    }

    private void createProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.verifying));
    }

    private void initAnimation() {
        handler = new Handler();
        handler.postDelayed(new InitOnboardRunnable(this, verifyText,
                verifyTIL, buttonLayout,
                View.VISIBLE), 100);
    }

    @OnClick(R.id.onboard_verify)
    public void onVerify() {
        verifyTIL.setError(null);

        String pin = pinEditText.getText().toString();
        if (pin.length() < 4) {
            verifyTIL.setError(getString(R.string.pin_too_short));
        }

        Utils.hideKeyboard(parent, this);
        if (bundle == null) {
            bundle = new VerifyBundle(Utils.getUserId(this),
                    Utils.getPassword(this), pin);
        } else {
            bundle.changeValues(Utils.getUserId(this),
                    Utils.getPassword(this), pin);
        }

        progressDialog.show();

        RetrofitSingleton.getInstance()
                .getUserService()
                .verifyUser(bundle)
                .enqueue(callback);

    }

    @OnClick(R.id.onboard_later)
    public void dismissVerify() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Subscribe
    public void onVerifyResp(VerifyResp resp) {
        Utils.setVerified(this, true);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.MESSAGE, getString(R.string.verify_success));
        progressDialog.dismiss();
        startActivity(intent);
    }

    @Subscribe
    public void onVerifyFailure(VerifyStatus status) {
        Utils.failedVerifySnackBar(null, parent, R.color.colorAccent, status.getStatus(), this);
    }
}
