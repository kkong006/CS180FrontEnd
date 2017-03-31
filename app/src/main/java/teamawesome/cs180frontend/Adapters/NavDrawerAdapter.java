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
    private String[] mIcons;
    private String[] mText;
    private Context mContext;

    public NavDrawerAdapter(String[] icons, String[] text, Context context) {
        this.mIcons = icons;
        this.mText = text;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mIcons.length;
    }

    @Override
    public Object getItem(int position) {
        return mText[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void changeLoginElem() {

        if (mText[3].equals(mContext.getString(R.string.login))) {
            mText[3] = mContext.getString(R.string.logout);
        } else {
            mText[3] = mContext.getString(R.string.login);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NavDrawerViewHolder holder;

        if(convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_item_nav_drawer, parent, false);
            holder = new NavDrawerViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (NavDrawerViewHolder) convertView.getTag();
        }
        holder.icon.setText(mIcons[position]);

        if(position == 0) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }

        if (position == 4) {
            if (Utils.getUserId(mContext) == 0) {
                mText[4] = mContext.getString(R.string.login);
                holder.text.setText(mContext.getString(R.string.login));
            } else {
                mText[4] = mContext.getString(R.string.logout);
                holder.text.setText(mContext.getString(R.string.logout));
            }
        } else {
            holder.text.setText(mText[position]);
        }

        return convertView;
    }
}
