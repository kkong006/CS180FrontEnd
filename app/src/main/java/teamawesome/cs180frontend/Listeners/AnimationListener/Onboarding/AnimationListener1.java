package teamawesome.cs180frontend.Listeners.AnimationListener.Onboarding;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import teamawesome.cs180frontend.R;

public class AnimationListener1 implements Animation.AnimationListener {
    private Context context;
    private View v;
    private View nextView;
    private View finalView;
    private int visibility;

    public AnimationListener1(Context context, View v, View nextView, View finalView, int visibility) {
        this.context = context;
        this.v = v;
        this.nextView = nextView;
        this.finalView = finalView;
        this.visibility = visibility;
    }

    @Override
    public void onAnimationStart(Animation animation) { }

    @Override
    public void onAnimationEnd(Animation animation) {
        v.setVisibility(visibility);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_right_in);
        anim.setAnimationListener(new AnimationListener2(nextView, finalView, View.VISIBLE, false));
        nextView.startAnimation(anim);
    }

    @Override
    public void onAnimationRepeat(Animation animation) { }
}
