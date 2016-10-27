package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.Models.RatingId;
import teamawesome.cs180frontend.Misc.Constants;

/**
 * Created by KongK on 10/26/2016.
 */

public class PostReviewRatingCallback implements Callback<RatingId> {

    @Override
    public void onResponse(Call<RatingId> call, Response<RatingId> response) {
        switch (response.code()) {
            case Constants.HTTP_STATUS_OK:
                EventBus.getDefault().post(response.body());
                break;
            case Constants.HTTP_STATUS_DNE:
                EventBus.getDefault().post(0);
                break;
            case Constants.HTTP_STATUS_ERROR:
                EventBus.getDefault().post(-1);
                break;
        }
    }

    @Override
    public void onFailure(Call<RatingId> call, Throwable t) {
        EventBus.getDefault().post("ERROR");
    }
}
