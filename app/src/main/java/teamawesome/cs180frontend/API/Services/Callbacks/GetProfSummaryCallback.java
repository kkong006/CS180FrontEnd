package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.DataModel.ProfSummary.ProfSummaryBundle;
import teamawesome.cs180frontend.API.Models.StatusModel.ProfSummaryStatus;
import teamawesome.cs180frontend.Misc.Utils;

public class GetProfSummaryCallback implements Callback<ProfSummaryBundle>{
    @Override
    public void onResponse(Call<ProfSummaryBundle> call, Response<ProfSummaryBundle> resp) {
        System.out.println("PROFESSOR SUMMARY RESPONSE " + resp.code());
        switch (resp.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(resp.body());
                break;
            default:
                EventBus.getDefault().post(new ProfSummaryStatus(resp.code()));
                break;
        }
    }

    @Override
    public void onFailure(Call<ProfSummaryBundle> call, Throwable t) {
        System.out.println(Utils.getStackTrace(t));
        EventBus.getDefault().post(new ProfSummaryStatus(-1));
    }
}
