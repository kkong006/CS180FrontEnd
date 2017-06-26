package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.StatusModel.LoginRegisterStatus;
import teamawesome.cs180frontend.API.Models.AccountModel.AccountRespBundle;

public class LoginRegisterCallback implements Callback<AccountRespBundle> {
    @Override
    public void onResponse(Call<AccountRespBundle> call, Response<AccountRespBundle> resp) {
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
    public void onFailure(Call<AccountRespBundle> call, Throwable t) {
        EventBus.getDefault().post(new LoginRegisterStatus(-1));
    }
}
