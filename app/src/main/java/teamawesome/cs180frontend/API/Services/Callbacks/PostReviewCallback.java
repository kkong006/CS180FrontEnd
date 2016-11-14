package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.ReviewRespBundle;

/**
 * Created by KongK on 10/29/2016.
 */

public class PostReviewCallback implements Callback<ReviewRespBundle> {
    @Override
    public void onResponse(Call<ReviewRespBundle> call, Response<ReviewRespBundle> response) {
        switch (response.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(response.body());
                break;
            case APIConstants.HTTP_STATUS_INVALID:
                EventBus.getDefault().post(400);
                break;
            case APIConstants.HTTP_STATUS_DNE:
                EventBus.getDefault().post(401);
                break;
            case APIConstants.HTTP_STATUS_ERROR:
                EventBus.getDefault().post(500);
                break;
        }
    }

    @Override
    public void onFailure(Call<ReviewRespBundle> call, Throwable t) {
        EventBus.getDefault().post("ERROR");
    }
}
