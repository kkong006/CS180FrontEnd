package teamawesome.cs180frontend.Misc.ViewHolders;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import teamawesome.cs180frontend.R;

/**
 * Created by KongK on 10/14/2016.
 */

public class NavDrawerViewHolder {
    @Bind(R.id.icon) public TextView icon;
    @Bind(R.id.text) public TextView text;

    public NavDrawerViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
