package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewIDRespBundle;
import teamawesome.cs180frontend.API.Models.StatusModel.PostReviewStatus;

/**
 * Created by KongK on 10/29/2016.
 */

public class PostReviewCallback implements Callback<ReviewIDRespBundle> {
    @Override
    public void onResponse(Call<ReviewIDRespBundle> call, Response<ReviewIDRespBundle> resp) {
        System.out.println("POST REVIEW RESPONSE CODE " + resp.code());
        switch (resp.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(resp.body());
                break;
            default:
                EventBus.getDefault().post(new PostReviewStatus(resp.code()));
                break;
        }
    }

    @Override
    public void onFailure(Call<ReviewIDRespBundle> call, Throwable t) {
        EventBus.getDefault().post(new PostReviewStatus(-1));
    }
}
