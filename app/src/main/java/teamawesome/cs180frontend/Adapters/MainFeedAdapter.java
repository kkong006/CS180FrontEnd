package teamawesome.cs180frontend.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import teamawesome.cs180frontend.Misc.ViewHolders.MainFeedViewHolder;
import teamawesome.cs180frontend.Misc.ViewHolders.SearchResultsViewHolder;
import teamawesome.cs180frontend.R;

/**
 * Created by KongK on 11/14/2016.
 */

public class MainFeedAdapter extends BaseAdapter{

    private String[] mProfessorNames;
    private int[] mRatings;
    private String[] mClassNames;
    private String[] mReviewDates;
    private String[] mReviews;
    private int[] mReviewIDs;

    private Context mContext;

    public MainFeedAdapter( Context mContext, String[] mProfessorNames, int[] mRatings, String[] mClassNames, String[] mReviewDates, String[] mReviews, int[] mReviewIDs) {
        this.mContext = mContext;
        this.mProfessorNames = mProfessorNames;
        this.mRatings = mRatings;
        this.mClassNames = mClassNames;
        this.mReviewDates = mReviewDates;
        this.mReviewIDs = mReviewIDs;
        this.mReviews = mReviews;
    }


    @Override
    public int getCount() {
        return mProfessorNames.length;
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
        MainFeedViewHolder holder;
        if(convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_item_main_feed, parent, false);
            holder = new MainFeedViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MainFeedViewHolder) convertView.getTag();
        }

        holder.professorTV.setText(mProfessorNames[position]);
        holder.classNameTV.setText(mClassNames[position]);
        holder.dateTV.setText(mReviewDates[position]);
        holder.reviewTV.setText(mReviews[position]);
        for(int i = 0; i < 5; i++) {
            if(i < mRatings[position]) {
                holder.ratings[i].setTextColor(mContext.getResources().getColor(R.color.colorGreen));
            } else {
                holder.ratings[i].setTextColor(mContext.getResources().getColor(R.color.colorGrey));
            }
        }

        if(position == 0) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }

        return convertView;
    }
}
