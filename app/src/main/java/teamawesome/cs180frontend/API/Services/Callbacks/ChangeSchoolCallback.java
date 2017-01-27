package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.StatusModel.FailedUpdate;
import teamawesome.cs180frontend.API.Models.UserModel.UpdateSchoolStatus;
import teamawesome.cs180frontend.Misc.Utils;

/**
 * Created by jonathan on 1/26/17.
 */

public class ChangeSchoolCallback implements Callback<Void> {
    @Override
    public void onResponse(Call<Void> call, Response<Void> resp) {
        switch (resp.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(new UpdateSchoolStatus(resp.code()));
                break;
            default:
                EventBus.getDefault().post(new UpdateSchoolStatus(resp.code()));
                break;
        }
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        EventBus.getDefault().post(new FailedUpdate(Utils.getStackTrace(t)));
    }
}
