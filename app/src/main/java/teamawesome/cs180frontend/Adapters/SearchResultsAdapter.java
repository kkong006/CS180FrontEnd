package teamawesome.cs180frontend.Adapters;

import android.content.Context;
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
    private String[] mRatings;
    private String[] mReviewDates;
    private String[] mReviewContents;
    private String[] mReviewIDs;
    private Context mContext;

    public SearchResultsAdapter(Context context, String[] classes,
            String[] ratings, String[] reviewDates, String[] reviews, String[] reviewIDs) {
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
    public Review getItem(int position) {
        return new Review(mClassNames[position], mRatings[position], mReviewDates[position], mReviewContents[position], mReviewIDs[position]);
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
        try {
            for(int i = 0; i < Integer.parseInt(mRatings[position]); i++) {
                holder.rating[i].setTextColor(mContext.getResources().getColor(R.color.colorGreen));
            }
        } catch(Exception e) {
            Toast.makeText(mContext, "Error reading rating", Toast.LENGTH_SHORT).show();
        }
        holder.reviewDate.setText(mReviewDates[position]);
        holder.reviewContents.setText(mReviewContents[position]);

        if(position == 0) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }

        return convertView;
    }
}
