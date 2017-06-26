package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRatingBundle;

public class ReviewRatingsCallBack implements Callback<List<ReviewRatingBundle>> {
    @Override
    public void onResponse(Call<List<ReviewRatingBundle>> call, Response<List<ReviewRatingBundle>> resp) {
        switch (resp.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(resp.body());
                break;
            default:
                EventBus.getDefault().post(APIConstants.BAD_API_CALL);
                break;
        }
    }

    @Override
    public void onFailure(Call<List<ReviewRatingBundle>> call, Throwable t) {
        EventBus.getDefault().post(APIConstants.DATA_FAILURE);
    }
}
