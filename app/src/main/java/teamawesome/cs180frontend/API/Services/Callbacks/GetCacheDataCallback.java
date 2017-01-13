package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.DataModel.CacheData.CacheDataBundle;
import teamawesome.cs180frontend.Misc.Utils;

/**
 * Created by jman0_000 on 10/31/2016.
 */

public class GetCacheDataCallback implements Callback<CacheDataBundle> {
    @Override
    public void onResponse(Call<CacheDataBundle> call, Response<CacheDataBundle> resp) {
        System.out.println(resp.code());
        switch (resp.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(resp.body());
                break;
            default:
                EventBus.getDefault().post(0);
                break;
        }
    }

    @Override
    public void onFailure(Call<CacheDataBundle> call, Throwable t) {
        System.out.println(Utils.getStackTrace(t));
        EventBus.getDefault().post(-1);
    }
}
