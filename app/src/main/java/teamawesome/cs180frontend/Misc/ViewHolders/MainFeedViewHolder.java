package teamawesome.cs180frontend.Misc.ViewHolders;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import teamawesome.cs180frontend.R;

/**
 * Created by KongK on 11/14/2016.
 */

public class MainFeedViewHolder {
    @Bind(R.id.main_feed_professor_tv) public TextView professorTV;
    @Bind(R.id.main_feed_class_tv) public TextView classNameTV;
    @Bind(R.id.main_feed_date_tv) public TextView dateTV;
    @Bind(R.id.main_feed_rate_1) public TextView oneTV;
    @Bind(R.id.main_feed_rate_2) public TextView twoTV;
    @Bind(R.id.main_feed_rate_3) public TextView threeTV;
    @Bind(R.id.main_feed_rate_4) public TextView fourTV;
    @Bind(R.id.main_feed_rate_5) public TextView fiveTV;
    @Bind(R.id.main_feed_review_tv) public TextView reviewTV;

    public TextView[] ratings;


    public MainFeedViewHolder(View view) {
        ButterKnife.bind(this, view);
        ratings = new TextView[] { oneTV, twoTV, threeTV, fourTV, fiveTV };
    }
}
