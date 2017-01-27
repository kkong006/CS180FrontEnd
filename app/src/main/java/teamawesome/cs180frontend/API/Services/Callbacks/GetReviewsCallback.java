package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.StatusModel.ReviewFetchStatus;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRespBundle;

public class GetReviewsCallback implements Callback<List<ReviewRespBundle>> {

    @Override
    public void onResponse(Call<List<ReviewRespBundle>> call, Response<List<ReviewRespBundle>> resp) {
        System.out.println("GET REVIEWS " + resp.code());
        switch (resp.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(resp.body());
                break;
            default:
                EventBus.getDefault().post(new ReviewFetchStatus(resp.code()));
                break;
        }
    }

    @Override
    public void onFailure(Call<List<ReviewRespBundle>> call, Throwable t) {
        EventBus.getDefault().post(new ReviewFetchStatus(-1));
    }
}
