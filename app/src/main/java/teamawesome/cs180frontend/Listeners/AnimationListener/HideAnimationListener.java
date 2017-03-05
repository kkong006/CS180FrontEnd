package teamawesome.cs180frontend.Listeners.AnimationListener;

import android.view.View;
import android.view.animation.Animation;

public class HideAnimationListener implements Animation.AnimationListener{
    private View v;

    public HideAnimationListener(View v) {
        this.v = v;
    }

    @Override
    public void onAnimationStart(Animation animation) { }

    @Override
    public void onAnimationEnd(Animation animation) { v.setVisibility(View.GONE);}

    @Override
    public void onAnimationRepeat(Animation animation) {}
}
