package teamawesome.cs180frontend.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.Misc.ViewHolders.NavDrawerViewHolder;
import teamawesome.cs180frontend.R;

public class NavDrawerAdapter extends BaseAdapter {
    private String[] icons;
    private String[] text;
    private Context context;

    public NavDrawerAdapter(String[] icons, String[] text, Context context) {
        this.icons = icons;
        this.text = text;
        this.context = context;
    }

    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int position) {
        return text[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NavDrawerViewHolder holder;

        if(convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_item_nav_drawer, parent, false);
            holder = new NavDrawerViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (NavDrawerViewHolder) convertView.getTag();
        }

        holder.icon.setText(icons[position]);
        holder.text.setText(text[position]);

        return convertView;
    }
}
