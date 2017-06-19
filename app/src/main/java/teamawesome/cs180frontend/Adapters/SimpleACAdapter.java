package teamawesome.cs180frontend.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import teamawesome.cs180frontend.API.Models.DataModel.AbstractBundle;
import teamawesome.cs180frontend.Misc.ViewHolders.SimpleViewHolder;
import teamawesome.cs180frontend.R;

//AC as in Auto Complete
public class SimpleACAdapter extends ArrayAdapter<AbstractBundle> {
    private Context context;
    private ArrayList<AbstractBundle> items;
    private ArrayList<AbstractBundle> suggestions;

    @SuppressWarnings("unchecked")
    public SimpleACAdapter(Context context, int viewResourceId, ArrayList<? extends AbstractBundle> items) {
        super(context, viewResourceId, new ArrayList<AbstractBundle>());
        this.context = context;
        this.items = (ArrayList<AbstractBundle>) items.clone();
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
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.simple_list_item, parent, false);
            holder = new SimpleViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SimpleViewHolder) convertView.getTag();
        }

        //USE BUILT IN GETITEM() METHOD
        holder.textView.setText(getItem(position).getName());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    //COPY PASTED STRAIGHT FROM ALEX. NO SHAME.
    private Filter filter = new Filter() {
        public String convertResultToString(Object resultValue) {
            return ((AbstractBundle) resultValue).getName();
        }

        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint){
            if (constraint != null) {
                suggestions.clear();
                for (int i = 0, j = 0; i < items.size() && j < 5; i++) {
                    if (items.get(i).getName().toLowerCase()
                            .contains(constraint.toString().toLowerCase())) {
                        suggestions.add(items.get(i));
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
            List<AbstractBundle> filteredList = (ArrayList<AbstractBundle>) results.values;
            if (results.count > 0) {
                for (AbstractBundle b : filteredList) {
                    add(b);
                }
                notifyDataSetChanged();
            }
        }

    };
}
