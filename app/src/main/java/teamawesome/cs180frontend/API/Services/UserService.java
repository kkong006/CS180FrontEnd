package teamawesome.cs180frontend.API.Services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import teamawesome.cs180frontend.API.Models.AccountModel.AccountBundle;
import teamawesome.cs180frontend.API.Models.AccountModel.UpdateAccountBundle;
import teamawesome.cs180frontend.API.Models.AccountModel.AccountRespBundle;
import teamawesome.cs180frontend.API.Models.AccountModel.VerifyBundle;
import teamawesome.cs180frontend.API.Models.AccountModel.VerifyResp;

public interface UserService {
    @POST("/updateAccount")
    Call<Void> updateAccount(@Body UpdateAccountBundle updateUserBundle);

    @POST("/login")
    Call<AccountRespBundle> login(@Body AccountBundle accountBundle);

    @POST("/register")
    Call<AccountRespBundle> register(@Body AccountBundle accountBundle);

    @POST("/verify")
    Call<VerifyResp> verifyUser(@Body VerifyBundle verifyBundle);
}
