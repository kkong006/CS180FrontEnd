package teamawesome.cs180frontend.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.ResourceBundle;

import teamawesome.cs180frontend.API.Models.ReviewRespBundle;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.ViewHolders.MainFeedViewHolder;
import teamawesome.cs180frontend.Misc.ViewHolders.SearchResultsViewHolder;
import teamawesome.cs180frontend.R;

/**
 * Created by KongK on 11/14/2016.
 */

public class MainFeedAdapter extends BaseAdapter{

    private List<ReviewRespBundle> reviewList;
    private Context mContext;
    private DataSingleton data;

    public MainFeedAdapter( Context mContext, List<ReviewRespBundle> reviewList) {
        this.mContext = mContext;
        this.reviewList = reviewList;
        this.data = DataSingleton.getInstance();
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public ReviewRespBundle getItem(int position) {
        return reviewList.get(position);
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

        holder.professorTV.setText(data.getProfessorName(reviewList.get(position).getProfId()));
        holder.classNameTV.setText(data.getClassName(reviewList.get(position).getClassId()));
        holder.dateTV.setText(reviewList.get(position).getReviewDate());
        holder.reviewTV.setText(reviewList.get(position).getMessage());
        for(int i = 0; i < 5; i++) {
            if(i < reviewList.get(position).getRating()) {
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
