package teamawesome.cs180frontend.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import teamawesome.cs180frontend.Misc.ViewHolders.SimpleViewHolder;
import teamawesome.cs180frontend.R;

public class SimpleListAdapter extends BaseAdapter {
    private Context context;
    private List<String> stringList;

    public SimpleListAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public String getItem(int pos) {
        return stringList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        SimpleViewHolder holder;
        if(convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.larger_text_list_item, parent, false);
            holder = new SimpleViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SimpleViewHolder) convertView.getTag();
        }

        holder.textView.setText(stringList.get(pos));

        return convertView;
    }

    public void loadNewList(List<String> stringList) {
        this.stringList.clear();
        this.stringList.addAll(stringList);
        notifyDataSetChanged();
    }
}
