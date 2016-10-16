package teamawesome.cs180frontend.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import teamawesome.cs180frontend.Misc.ViewHolders.NavDrawerViewHolder;
import teamawesome.cs180frontend.R;

/**
 * Created by KongK on 10/14/2016.
 */

public class NavDrawerAdapter extends BaseAdapter {
    private String[] icons;
    private String[] text;
    Context context;

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

        if(position == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

        return convertView;
    }
}
