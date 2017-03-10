package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.StatusModel.VerifyStatus;
import teamawesome.cs180frontend.API.Models.UserModel.VerifyBundle;
import teamawesome.cs180frontend.API.Models.UserModel.VerifyResp;

/**
 * Created by jonathan on 3/10/17.
 */

public class VerifyCallback implements Callback<VerifyResp> {
    @Override
    public void onResponse(Call<VerifyResp> call, Response<VerifyResp> resp) {
        switch(resp.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(resp.body());
                break;
            default:
                EventBus.getDefault().post(new VerifyStatus(resp.code()));
        }
    }

    @Override
    public void onFailure(Call<VerifyResp> call, Throwable t) {
        EventBus.getDefault().post(new VerifyStatus(-1));
    }
}
