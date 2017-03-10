package teamawesome.cs180frontend.Runnable;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;

import teamawesome.cs180frontend.Listeners.AnimationListener.Onboarding.AnimationListener1;

/**
 * Created by jonathan on 3/9/17.
 */

public class InitOnboardRunnable implements Runnable {
    private Context context;
    private View v1;
    private View v2;
    private View v3;
    int visibility;

    public InitOnboardRunnable(Context context, View v1, View v2, View v3, int visibility) {
        this.context = context;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.visibility = visibility;
    }

    @Override
    public void run() {
        AlphaAnimation showText = new AlphaAnimation(0.0f, 1.0f);
        showText.setAnimationListener(new AnimationListener1(context,
                v1, v2, v3, visibility));
        showText.setDuration(650);
        v1.startAnimation(showText);
    }
}
