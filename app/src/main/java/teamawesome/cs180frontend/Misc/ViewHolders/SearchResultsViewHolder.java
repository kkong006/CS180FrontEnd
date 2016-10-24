package teamawesome.cs180frontend.Misc.ViewHolders;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import teamawesome.cs180frontend.R;

/**
 * Created by KongK on 10/16/2016.
 */

public class SearchResultsViewHolder {

    @Bind(R.id.result_class_tv) public TextView className;
    @Bind(R.id.result_rate_1) public TextView rateOne;
    @Bind(R.id.result_rate_2) public TextView rateTwo;
    @Bind(R.id.result_rate_3) public TextView rateThree;
    @Bind(R.id.result_rate_4) public TextView rateFour;
    @Bind(R.id.result_rate_5) public TextView rateFive;
    @Bind(R.id.result_date_tv) public TextView reviewDate;
    @Bind(R.id.result_review_tv) public TextView reviewContents;

    public TextView[] rating;

    public SearchResultsViewHolder(View view) {
        ButterKnife.bind(this, view);
        rating = new TextView[] { rateOne, rateTwo, rateThree, rateFour, rateFive };
    }
}
