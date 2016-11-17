package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.ProfessorRespBundle;
import teamawesome.cs180frontend.Misc.Utils;

/**
 * Created by KongK on 11/15/2016.
 */

public class GetProfessorSummaryCallback implements Callback<ProfessorRespBundle>{
    @Override
    public void onResponse(Call<ProfessorRespBundle> call, Response<ProfessorRespBundle> response) {
        System.out.println("PROFESSOR SUMMARY RESPONSE " + response.code());
        System.out.println(response.toString());
        System.out.println(response.body().toString());
        switch (response.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(response.body());
                break;
            case APIConstants.HTTP_STATUS_INVALID:
                EventBus.getDefault().post(0);
                break;
            case APIConstants.HTTP_STATUS_ERROR:
                EventBus.getDefault().post(-1);
                break;
            default:
                EventBus.getDefault().post(-1);
                break;
        }

    }

    @Override
    public void onFailure(Call<ProfessorRespBundle> call, Throwable t) {
        System.out.println(Utils.getStackTrace(t));
        EventBus.getDefault().post("ERROR");
    }
}
