package teamawesome.cs180frontend.Listeners.AnimationListener.Generic;

import android.view.View;
import android.view.animation.Animation;

public class GenericHideAnimationListener implements Animation.AnimationListener{
    private View v;

    public GenericHideAnimationListener(View v) { this.v = v; }

    @Override
    public void onAnimationStart(Animation animation) { }

    @Override
    public void onAnimationEnd(Animation animation) { v.setVisibility(View.GONE);}

    @Override
    public void onAnimationRepeat(Animation animation) {}
}
