package teamawesome.cs180frontend.Misc.ViewHolders;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import teamawesome.cs180frontend.R;

public class SimpleViewHolder2 {
    @Bind(R.id.name_textview) public TextView nameTextView;
    @Bind(R.id.count_textview) public TextView countTextView;

    public SimpleViewHolder2(View v) {ButterKnife.bind(this, v); }
}
