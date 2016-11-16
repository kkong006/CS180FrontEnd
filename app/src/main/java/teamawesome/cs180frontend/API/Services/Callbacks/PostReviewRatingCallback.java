package teamawesome.cs180frontend.API.Services.Callbacks;

import android.app.usage.UsageEvents;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.RatingId;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.Utils;

/**
 * Created by KongK on 10/26/2016.
 */

public class PostReviewRatingCallback implements Callback<Void> {

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        System.out.println(response.code());
        switch (response.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(1);
                break;
            case APIConstants.HTTP_STATUS_INVALID:
                EventBus.getDefault().post(0);
                break;
            case APIConstants.HTTP_STATUS_ERROR:
                EventBus.getDefault().post(-1);
                break;
        }
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        Utils.getStackTrace(t);
        EventBus.getDefault().post("ERROR");
    }
}