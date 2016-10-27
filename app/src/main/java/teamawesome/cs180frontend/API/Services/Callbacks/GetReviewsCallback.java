package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.UserReview;
import teamawesome.cs180frontend.Misc.Constants;

/**
 * Created by KongK on 10/25/2016.
 */

public class GetReviewsCallback implements Callback<List<UserReview>> {

    @Override
    public void onResponse(Call<List<UserReview>> call, Response<List<UserReview>> response) {
        switch (response.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(response.body());
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
    public void onFailure(Call<List<UserReview>> call, Throwable t) {
        EventBus.getDefault().post("ERROR");
    }
}
