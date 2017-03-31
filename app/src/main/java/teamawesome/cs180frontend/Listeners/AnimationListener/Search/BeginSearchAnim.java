package teamawesome.cs180frontend.Listeners.AnimationListener.Search;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;

import teamawesome.cs180frontend.Runnable.SearchRunnable;

public class BeginSearchAnim implements Animation.AnimationListener {
    private Context context;
    private String type;
    private String queryParam;
    private View v;

    public BeginSearchAnim(Context context, String type, String queryParam, View v) {
        this.context = context;
        this.type = type;
        this.queryParam = queryParam;
        this.v = v;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        new Handler().postDelayed(new SearchRunnable(context, type, queryParam, v), 100);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
