package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;

/**
 * Created by KongK on 10/29/2016.
 */

public class PostReviewCallback implements Callback<Void> {
    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        switch (response.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(1);
                break;
            case APIConstants.HTTP_STATUS_DNE:
                EventBus.getDefault().post(0);
                break;
            case APIConstants.HTTP_STATUS_ERROR:
                EventBus.getDefault().post(-1);
                break;
        }
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {

    }
}
