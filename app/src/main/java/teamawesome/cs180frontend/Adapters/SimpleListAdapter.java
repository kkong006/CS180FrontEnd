package teamawesome.cs180frontend.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import teamawesome.cs180frontend.API.Models.DataModel.SchoolBundle;
import teamawesome.cs180frontend.Misc.ViewHolders.SimpleViewHolder;
import teamawesome.cs180frontend.R;

/**
 * Created by jman0_000 on 11/4/2016.
 */

public class SimpleListAdapter extends ArrayAdapter<SchoolBundle> {
    private Context mContext;
    private ArrayList<SchoolBundle> schools;
    private ArrayList<SchoolBundle> suggestions;

    @SuppressWarnings("unchecked")
    public SimpleListAdapter(Context context, int viewResourceId, ArrayList<SchoolBundle> schools) {
        super(context, viewResourceId, new ArrayList<SchoolBundle>());
        this.mContext = context;
        this.schools = (ArrayList<SchoolBundle>) schools.clone();
        this.suggestions = new ArrayList<>();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.simple_list_item, parent, false);
            holder = new SimpleViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SimpleViewHolder) convertView.getTag();
        }

        //USE BUILT IN GETITEM() METHOD
        holder.textView.setText(getItem(position).getSchoolName());

        return convertView;
    }

    @Override
    public android.widget.Filter getFilter() {
        return schoolFilter;
    }

    //COPY PASTED STRAIGHT FROM ALEX. NO SHAME.
    Filter schoolFilter = new Filter() {
        public String convertResultToString(Object resultValue) {
            return ((SchoolBundle) resultValue).getSchoolName();
        }

        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint){
            if (constraint != null) {
                suggestions.clear();
                for (int i = 0, j = 0; i < schools.size() && j <= 5; i++) {
                    if (schools.get(i).getSchoolName().toLowerCase()
                            .contains(constraint.toString().toLowerCase())) {
                        suggestions.add(schools.get(i));
                        j++;
                    }
                }

                FilterResults fr = new FilterResults();
                fr.values = suggestions;
                fr.count = suggestions.size();
                return fr;
            } else {
                return new FilterResults();
            }
        }

        @Override
        @SuppressWarnings("unchecked") //this warning was annoying me
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            List<SchoolBundle> filteredList = (ArrayList<SchoolBundle>) results.values;
            if (results.count > 0) {
                for (SchoolBundle s : filteredList) {
                    add(s);
                }
                notifyDataSetChanged();
            }
        }

    };
}
