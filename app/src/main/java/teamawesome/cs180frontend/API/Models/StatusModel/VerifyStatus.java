package teamawesome.cs180frontend.API.Models.StatusModel;

import android.content.Context;


public class VerifyStatus extends AbstractStatus {
    private Context context;

    public VerifyStatus(int status, Context context) {
        super(status);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
