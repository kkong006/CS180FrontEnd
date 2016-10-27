package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.ClassBundle;
import teamawesome.cs180frontend.API.Models.Professor;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.Utils;

/**
 * Created by KongK on 10/25/2016.
 */

public class GetClassesCallback implements Callback<List<ClassBundle>> {

    @Override
    public void onResponse(Call<List<ClassBundle>> call, Response<List<ClassBundle>> response) {
        System.out.println(response.code());
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
    public void onFailure(Call<List<ClassBundle>> call, Throwable t) {
        System.out.println(Utils.getStackTrace(t));
        EventBus.getDefault().post("ERROR");
    }
}
