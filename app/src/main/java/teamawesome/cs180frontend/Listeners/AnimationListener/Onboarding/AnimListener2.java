package teamawesome.cs180frontend.Listeners.AnimationListener.Onboarding;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import org.greenrobot.eventbus.EventBus;

import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetCacheDataCallback;
import teamawesome.cs180frontend.Misc.DataSingleton;

public class AnimListener2 implements Animation.AnimationListener {
    private View v;
    private View nextView;
    private int visibility;
    private boolean fetchData;

    public AnimListener2(View v, View nextView, int visibility, boolean fetchData) {
        this.v = v;
        this.nextView = nextView;
        this.visibility = visibility;
        this.fetchData = fetchData;
    }

    @Override
    public void onAnimationStart(Animation animation) { }

    @Override
    public void onAnimationEnd(Animation animation) {
        v.setVisibility(visibility);
        if (nextView != null) {
            AlphaAnimation showView = new AlphaAnimation(0.0f, 1.0f);
            showView.setAnimationListener(new AnimListener2(nextView, null, View.VISIBLE, fetchData));
            showView.setDuration(750);
            nextView.startAnimation(showView);
        } else {
            if (fetchData) {
                if (DataSingleton.getInstance().getSchoolCache().isEmpty()) {
                    GetCacheDataCallback callback = new GetCacheDataCallback();
                    RetrofitSingleton.getInstance()
                            .getMatchingService()
                            .getData(null, null).enqueue(callback);
                } else {
                    EventBus.getDefault().post(true);
                }
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) { }

}
