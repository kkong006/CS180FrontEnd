package teamawesome.cs180frontend.Runnable;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import teamawesome.cs180frontend.API.Models.DataModel.ClassBundle;
import teamawesome.cs180frontend.API.Models.DataModel.SubjectBundle;
import teamawesome.cs180frontend.Listeners.AnimationListener.Search.FinishSearchAnim;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.R;

public class SearchRunnable implements Runnable{
    private Context context;
    private String type;
    private String queryParam;
    private View v;

    public SearchRunnable(Context context, String type, String queryParam, View v) {
        this.context = context;
        this.type = type;
        this.queryParam = queryParam;
        this.v = v;
    }

    @Override
    public void run() {
        List<String> results = new ArrayList<>();
        if (type.equals(context.getString(R.string.classes))) {
            for (ClassBundle c : DataSingleton.getInstance().getClassCache()) {
                if (c.getName().contains(queryParam.toUpperCase())) {
                    results.add(c.getName());
                }
            }
        } else if (type.equals(context.getString(R.string.subjects))) {
            for (SubjectBundle s : DataSingleton.getInstance().getSubjectCache()) {
                String subjectIdent = s.getSubjectIdent();
                String subjectName = s.getSubjectName();
                if (subjectIdent.equals(queryParam.toUpperCase()) ||
                        subjectName.contains(queryParam)) {
                    results.add(subjectIdent + " (" + subjectName + ")");
                }
            }
        } else {
            EventBus.getDefault().post(-1);
            return;
        }
        AlphaAnimation hideProgressBar = new AlphaAnimation(1.0f, 0.0f);
        hideProgressBar.setDuration(750);
        hideProgressBar.setAnimationListener(new FinishSearchAnim(v, results));
        v.startAnimation(hideProgressBar);
    }
}
