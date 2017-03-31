package teamawesome.cs180frontend.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import teamawesome.cs180frontend.API.Models.DataModel.ProfClassBundle;
import teamawesome.cs180frontend.Misc.ViewHolders.SimpleViewHolder2;
import teamawesome.cs180frontend.R;

//for displaying the classes & respective review count for each class
public class SimpleListAdapter2 extends BaseAdapter {
    private Context context;
    private List<ProfClassBundle> profClassBundleList;

    public SimpleListAdapter2(Context context, List<ProfClassBundle> profClassBundleList) {
        this.context = context;
        this.profClassBundleList = profClassBundleList;
    }

    @Override
    public int getCount() { return profClassBundleList.size(); }

    @Override
    public ProfClassBundle getItem(int position) { return profClassBundleList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleViewHolder2 holder;
        if(convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.simple_list_item_2, parent, false);
            holder = new SimpleViewHolder2(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SimpleViewHolder2) convertView.getTag();
        }

        holder.nameTextView.setText(getItem(position).getClassName());
        holder.countTextView.setText(String.valueOf(getItem(position).getReviewCnt()));

        return convertView;
    }

}
