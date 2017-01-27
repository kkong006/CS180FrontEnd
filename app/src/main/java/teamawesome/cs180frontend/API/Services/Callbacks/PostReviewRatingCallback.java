package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRatingResp;
import teamawesome.cs180frontend.API.Models.StatusModel.ReviewRatingStatus;
import teamawesome.cs180frontend.Misc.Utils;

/**
 * Created by KongK on 10/26/2016.
 */

public class PostReviewRatingCallback implements Callback<ReviewRatingResp> {

    @Override
    public void onResponse(Call<ReviewRatingResp> call, Response<ReviewRatingResp> resp) {
        System.out.println(resp.code());
        switch (resp.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(resp.body());
                break;
            default:
                EventBus.getDefault().post(new ReviewRatingStatus(resp.code()));
                break;
        }
    }

    @Override
    public void onFailure(Call<ReviewRatingResp> call, Throwable t) {
        Utils.getStackTrace(t);
        EventBus.getDefault().post(new ReviewRatingStatus(-1));
    }
}
