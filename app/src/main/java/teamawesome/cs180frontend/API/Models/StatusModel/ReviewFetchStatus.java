package teamawesome.cs180frontend.API.Models.StatusModel;

import android.content.Context;

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
