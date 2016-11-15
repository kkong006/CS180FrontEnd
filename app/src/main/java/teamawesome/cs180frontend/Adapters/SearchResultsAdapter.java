package teamawesome.cs180frontend.Adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import teamawesome.cs180frontend.Misc.Review;
import teamawesome.cs180frontend.Misc.ViewHolders.SearchResultsViewHolder;
import teamawesome.cs180frontend.R;

/**
 * Created by KongK on 10/16/2016.
 */

public class SearchResultsAdapter extends BaseAdapter{

    private String[] mClassNames;
    private int[] mRatings;
    private String[] mReviewDates;
    private String[] mReviewContents;
    private int[] mReviewIDs;
    private Context mContext;

    public SearchResultsAdapter(Context context, String[] classes,
            int[] ratings, String[] reviewDates, String[] reviews, int[] reviewIDs) {
        this.mContext = context;
        this.mClassNames = classes;
        this.mRatings = ratings;
        this.mReviewDates = reviewDates;
        this.mReviewContents = reviews;
        this.mReviewIDs = reviewIDs;
    }

    @Override
    public int getCount() {
        return mClassNames.length;
    }

    @Override
    public int[] getItem(int position) {
        return new int[] {mReviewIDs[position], mRatings[position]};
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchResultsViewHolder holder;
        if(convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_item_search_results, parent, false);
            holder = new SearchResultsViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (SearchResultsViewHolder) convertView.getTag();
        }

        holder.className.setText(mClassNames[position]);
        holder.reviewDate.setText(mReviewDates[position]);
        holder.reviewContents.setText(mReviewContents[position]);
        for(int i = 0; i < mRatings[position] && i < 5; i++) {
            holder.rating[i].setTextColor(mContext.getResources().getColor(R.color.colorGreen));
        }

        if(position == 0) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }

        return convertView;
    }
}
