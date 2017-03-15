package teamawesome.cs180frontend.API.Services.Callbacks;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.StatusModel.LoginRegisterStatus;
import teamawesome.cs180frontend.API.Models.UserModel.UserRespBundle;

public class LoginRegisterCallback implements Callback<UserRespBundle> {
    @Override
    public void onResponse(Call<UserRespBundle> call, Response<UserRespBundle> resp) {
        System.out.println(resp.code());
        switch (resp.code()) {
            case (APIConstants.HTTP_STATUS_OK):
                EventBus.getDefault().post(resp.body());
                break;
            default:
                EventBus.getDefault().post(new LoginRegisterStatus(resp.code()));
        }
    }

    @Override
    public void onFailure(Call<UserRespBundle> call, Throwable t) {
        EventBus.getDefault().post(new LoginRegisterStatus(-1));
    }
}
