package teamawesome.cs180frontend.Listeners.AnimationListener.Onboarding;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class UpdateTextAnim1 implements Animation.AnimationListener{
    private TextView v;
    private View nextView;
    private String newText;
    private Animation anim;
    private int visibility;

    public UpdateTextAnim1(TextView v, View nextView, String newText, Animation anim, int visibility) {
        this.v = v;
        this.nextView = nextView;
        this.newText = newText;
        this.anim = anim;
        this.visibility = visibility;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        v.setVisibility(visibility);

        if (visibility == View.INVISIBLE) {
            v.setText(newText);
            AlphaAnimation show = new AlphaAnimation(0.0f, 1.0f);
            show.setAnimationListener(new UpdateTextAnim1(v, nextView, newText,
                    anim, View.VISIBLE));
            show.setDuration(750);
            v.startAnimation(show);
        } else {
            if (anim != null) {
                nextView.startAnimation(anim);
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
