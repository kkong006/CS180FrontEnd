package teamawesome.cs180frontend.API.Services.Callbacks;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewBundle;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewPageBundle;
import teamawesome.cs180frontend.API.Models.StatusModel.ReviewFetchStatus;

public class GetReviewsCallback implements Callback<List<ReviewBundle>> {

    private Context context;

    public GetReviewsCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onResponse(Call<List<ReviewBundle>> call, Response<List<ReviewBundle>> resp) {
        System.out.println("GET REVIEWS " + resp.code());
        switch (resp.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(new ReviewPageBundle(context, resp.body()));
                break;
            default:
                EventBus.getDefault().post(new ReviewFetchStatus(context, resp.code()));
                break;
        }
    }

    @Override
    public void onFailure(Call<List<ReviewBundle>> call, Throwable t) {
        EventBus.getDefault().post(new ReviewFetchStatus(context, -1));
    }
}
