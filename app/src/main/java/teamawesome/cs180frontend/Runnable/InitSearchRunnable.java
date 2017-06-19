package teamawesome.cs180frontend.Runnable;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import teamawesome.cs180frontend.Listeners.AnimationListener.Search.BeginSearchAnim;

public class InitSearchRunnable implements Runnable {
    private Context context;
    private View[] disable;
    private TextView textView;
    private ListView listView;
    private ProgressBar progressBar;

    public InitSearchRunnable(Context context, View[] disable, TextView textView, ListView listView, ProgressBar progressBar) {
        this.context = context;
        this.disable = disable;
        this.textView = textView;
        this.listView = listView;
        this.progressBar = progressBar;
    }

    @Override
    public void run() {
        disable[0].clearFocus();

        for (View v : disable) {
            v.setEnabled(false);
        }
        textView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

        AlphaAnimation showProgressBar = new AlphaAnimation(0.0f, 1.0f);
        showProgressBar.setDuration(900);
        showProgressBar.setAnimationListener(new BeginSearchAnim(context,
                ((Spinner) disable[0]).getSelectedItem().toString(),
                ((EditText) disable[1]).getText().toString(), progressBar));

        progressBar.startAnimation(showProgressBar);
    }
}
