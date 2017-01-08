package teamawesome.cs180frontend.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRespBundle;
import teamawesome.cs180frontend.Misc.ViewHolders.SearchResultsViewHolder;
import teamawesome.cs180frontend.R;

/**
 * Created by KongK on 10/16/2016.
 */

public class SearchResultsAdapter extends BaseAdapter{

    private Context context;
    private List<ReviewRespBundle> reviews;

    public SearchResultsAdapter(Context context, List<ReviewRespBundle> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public ReviewRespBundle getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchResultsViewHolder holder;
        if(convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_item_search_results, parent, false);
            holder = new SearchResultsViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (SearchResultsViewHolder) convertView.getTag();
        }

        holder.className.setText(reviews.get(position).getClassName());
        holder.reviewDate.setText(reviews.get(position).getReviewDate());
        holder.reviewContents.setText(reviews.get(position).getReviewMsg());
        for(int i = 0; i < 5; i++) {
            if(i < reviews.get(position).getRating()) {
                holder.rating[i].setTextColor(context.getResources().getColor(R.color.colorGreen));
            } else {
                holder.rating[i].setTextColor(context.getResources().getColor(R.color.colorGrey));
            }
        }

        if(position == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

        return convertView;
    }
}
