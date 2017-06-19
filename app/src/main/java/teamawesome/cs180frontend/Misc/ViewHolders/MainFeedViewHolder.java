package teamawesome.cs180frontend.Misc.ViewHolders;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import butterknife.Bind;
import butterknife.ButterKnife;
import teamawesome.cs180frontend.R;

public class MainFeedViewHolder {
    @Bind(R.id.main_feed_professor_tv) public TextView professorTV;
    @Bind(R.id.main_feed_class_tv) public TextView classNameTV;
    @Bind(R.id.main_time_tv) public TextView timeTV;
    @Bind({R.id.main_feed_rate_1, R.id.main_feed_rate_2,
            R.id.main_feed_rate_3, R.id.main_feed_rate_4,
            R.id.main_feed_rate_5}) public TextView[] ratings;
    @Bind(R.id.main_feed_review_tv) public TextView reviewTV;
    @Bind(R.id.adView) public AdView adView;
    @Bind(R.id.card_view) public CardView cardView;
    @Bind(R.id.loadingLayout) public LinearLayout loadingLayout;
    @Bind(R.id.adLayout) public LinearLayout adLayout;

    public MainFeedViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
