package teamawesome.cs180frontend.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.android.gms.ads.AdRequest;

import java.util.List;

import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRespBundle;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.Misc.ViewHolders.MainFeedViewHolder;
import teamawesome.cs180frontend.R;

/**
 * Created by KongK on 11/14/2016.
 */

public class MainFeedAdapter extends BaseAdapter{

    private Context mContext;
    private AdRequest adRequest;
    private List<ReviewRespBundle> reviewList;

    public MainFeedAdapter(Context mContext, AdRequest adRequest, List<ReviewRespBundle> reviewList) {
        this.mContext = mContext;
        this.adRequest = adRequest;
        this.reviewList = reviewList;
    }

    public void append(List<ReviewRespBundle> reviewPage) {
        if (reviewList.size() != 0 && reviewList.get(reviewList.size() - 1) == null) {
            reviewList.remove(reviewList.size() - 1);
        }

        reviewList.addAll(reviewPage);
        if (reviewPage.size() >= 10) {
            reviewList.add(null);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        reviewList.clear();
        notifyDataSetChanged();
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

        if (reviewList.get(position) != null) {
            renderReview(holder, position);
        } else if ((reviewList.get(position) == null) &&
                (position == reviewList.size() - 1)){
            renderLoading(holder);
        } else {
            renderAdLayout(holder);
        }

        return convertView;
    }

    public void renderReview(MainFeedViewHolder holder, int position) {
        holder.cardView.setVisibility(View.VISIBLE);
        holder.loadingLayout.setVisibility(View.GONE);
        holder.adLayout.setVisibility(View.GONE);

        holder.professorTV.setText(reviewList.get(position).getProfName());
        holder.classNameTV.setText(reviewList.get(position).getClassName());
        holder.dateTV.setText(Utils.getLocalTimeString(reviewList.get(position).getReviewDate()));
        holder.reviewTV.setText(reviewList.get(position).getReviewMsg());
        for (int i = 0; i < 5; i++) {
            if (i < reviewList.get(position).getRating()) {
                holder.ratings[i].setTextColor(mContext.getResources().getColor(R.color.colorGreen));
            } else {
                holder.ratings[i].setTextColor(mContext.getResources().getColor(R.color.colorGrey));
            }
        }

//        if (position == 0) {
//            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
//        }
    }

    public void renderLoading(MainFeedViewHolder holder) {
        holder.cardView.setVisibility(View.GONE);
        holder.loadingLayout.setVisibility(View.VISIBLE);
        holder.adLayout.setVisibility(View.GONE);
    }

    public void renderAdLayout(MainFeedViewHolder holder) {
        holder.cardView.setVisibility(View.GONE);
        holder.loadingLayout.setVisibility(View.GONE);
        holder.adLayout.setVisibility(View.VISIBLE);

        holder.adView.loadAd(this.adRequest);
    }
}
