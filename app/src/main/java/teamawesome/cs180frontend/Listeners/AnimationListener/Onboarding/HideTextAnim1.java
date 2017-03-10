package teamawesome.cs180frontend.Listeners.AnimationListener.Onboarding;

import android.app.ProgressDialog;
import android.view.animation.Animation;
import android.widget.TextView;

import teamawesome.cs180frontend.API.Models.UserModel.AccountBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.LoginRegisterCallback;

/**
 * Created by jonathan on 3/8/17.
 */

public class HideTextAnim1 implements Animation.AnimationListener {
    private int visibility;
    private AccountBundle registerBundle;
    private LoginRegisterCallback callback;
    private TextView textView;
    private ProgressDialog progressDialog;

    public HideTextAnim1(int visibility, AccountBundle registerBundle,
                         LoginRegisterCallback callback, TextView textView,
                         ProgressDialog progressDialog) {
        this.visibility = visibility;
        this.registerBundle = registerBundle;
        this.callback = callback;
        this.textView = textView;
        this.progressDialog = progressDialog;
    }

    @Override
    public void onAnimationStart(Animation animation) { }

    @Override
    public void onAnimationEnd(Animation animation) {
        textView.setVisibility(visibility);
        progressDialog.show();
        RetrofitSingleton.getInstance()
                .getUserService()
                .register(registerBundle)
                .enqueue(callback);
    }

    @Override
    public void onAnimationRepeat(Animation animation) { }
}
