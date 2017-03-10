package teamawesome.cs180frontend.Listeners.AnimationListener.Generic;

import android.view.View;
import android.view.animation.Animation;

public class GenericAnimationListener implements Animation.AnimationListener{
    private int visibility;
    private View v;

    public GenericAnimationListener(int visibility, View v) {
        this.visibility = visibility;
        this.v = v;
    }

    @Override
    public void onAnimationStart(Animation animation) { }

    @Override
    public void onAnimationEnd(Animation animation) { v.setVisibility(visibility);}

    @Override
    public void onAnimationRepeat(Animation animation) {}
}
