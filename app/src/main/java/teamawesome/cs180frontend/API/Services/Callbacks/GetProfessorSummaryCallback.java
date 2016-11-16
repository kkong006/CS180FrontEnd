package teamawesome.cs180frontend.API.Services.Callbacks;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teamawesome.cs180frontend.API.Models.ProfessorRespBundle;

/**
 * Created by KongK on 11/15/2016.
 */

public class GetProfessorSummaryCallback implements Callback<ProfessorRespBundle>{
    @Override
    public void onResponse(Call<ProfessorRespBundle> call, Response<ProfessorRespBundle> response) {
        System.out.println("PROFESSOR SUMMARY RESPONSE " + response.code());

    }

    @Override
    public void onFailure(Call<ProfessorRespBundle> call, Throwable t) {

    }
}
