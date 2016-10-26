package teamawesome.cs180frontend.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.Models.Professor;
import teamawesome.cs180frontend.Misc.Constants;

/**
 * Created by KongK on 10/25/2016.
 */

public class GetClassesCallback implements Callback<List<Class>> {

    @Override
    public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
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
    public void onFailure(Call<List<Class>> call, Throwable t) {
        EventBus.getDefault().post("ERROR");
    }
}
