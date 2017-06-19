package teamawesome.cs180frontend.Misc.ViewHolders;


import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import teamawesome.cs180frontend.API.Models.DataModel.ProfSummary.ProfSummaryBundle;
import teamawesome.cs180frontend.API.Models.DataModel.ProfSummary.RatingDataBundle;
import teamawesome.cs180frontend.R;

public class RatingBarLayoutHolder {
    @Bind({R.id.five_stars_bar, R.id.four_stars_bar,
            R.id.three_stars_bar, R.id.two_stars_bar,
            R.id.one_star_bar}) View[] ratingBars;
    @Bind({R.id.five_stars_count, R.id.four_stars_count,
            R.id.three_stars_count, R.id.two_stars_count,
            R.id.one_star_count}) TextView[] ratingCountTV;

    public RatingBarLayoutHolder(View v) {
        ButterKnife.bind(this, v);
    }

    public void setUpRatingDistribution(ProfSummaryBundle resp, float maxBarWidth) {
        //float width = (float) barLayout.getWidth();
        int overallTotal = resp.getTotalRatings();

        for (RatingDataBundle b : resp.getRatingDataBundles()) {
            int index = 5 - b.getRating();
            ratingBars[index]
                    .getLayoutParams().width = Math.round(maxBarWidth * b.getTotalRatings() / overallTotal);
            ratingBars[index].requestLayout();
            ratingCountTV[index].setText(String.valueOf(b.getTotalRatings()));
        }
    }
}
