package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.StatusModel.FailedUpdate;
import teamawesome.cs180frontend.API.Models.AccountModel.UpdatePasswordStatus;
import teamawesome.cs180frontend.Misc.Utils;

public class ChangePasswordCallback implements Callback<Void> {

    @Override
    public void onResponse(Call<Void> call, Response<Void> resp) {
        System.out.println(resp.code());
        switch (resp.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(new UpdatePasswordStatus(resp.code()));
                break;
            default:
                EventBus.getDefault().post(new UpdatePasswordStatus(resp.code()));
                break;
        }
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        EventBus.getDefault().post(new FailedUpdate(Utils.getStackTrace(t)));
    }
}
