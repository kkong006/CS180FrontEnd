package teamawesome.cs180frontend.Listeners.AnimationListener.Search;

import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class FinishSearchAnim implements Animation.AnimationListener {
    private View v;
    private List<String> results;

    public FinishSearchAnim(View v, List<String> results) {
        this.v = v;
        this.results = results;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        v.setVisibility(View.INVISIBLE);
        EventBus.getDefault().post(results);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
