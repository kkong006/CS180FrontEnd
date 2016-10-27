package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.UserRespBundle;

/**
 * Created by jman0_000 on 10/27/2016.
 */

public class LoginRegisterCallback implements Callback<UserRespBundle> {
    @Override
    public void onResponse(Call<UserRespBundle> call, Response<UserRespBundle> resp) {
        System.out.println(resp.code());
        switch (resp.code()) {
            case (APIConstants.HTTP_STATUS_OK):
                EventBus.getDefault().post(resp.body());
                break;
            default:
                EventBus.getDefault().post(APIConstants.LOGIN_FAILURE);
        }
    }

    @Override
    public void onFailure(Call<UserRespBundle> call, Throwable t) {
        EventBus.getDefault().post(APIConstants.LOGIN_ERROR);
    }
}
