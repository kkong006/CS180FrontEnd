package teamawesome.cs180frontend.API.Models.StatusModel;

import android.content.Context;

/**
 * Created by jonathan on 1/26/17.
 */

public class ReviewFetchStatus extends AbstractStatus {
    Context context;

    public ReviewFetchStatus(Context context, int status) {
        super(status);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
