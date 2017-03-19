package teamawesome.cs180frontend.API.Services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import teamawesome.cs180frontend.API.Models.UserModel.AccountBundle;
import teamawesome.cs180frontend.API.Models.UserModel.UpdateUserBundle;
import teamawesome.cs180frontend.API.Models.UserModel.UserRespBundle;
import teamawesome.cs180frontend.API.Models.UserModel.VerifyBundle;
import teamawesome.cs180frontend.API.Models.UserModel.VerifyResp;

/**
 * Created by KongK on 10/21/2016.
 */

public interface UserService {
    @POST("/updateAccount")
    Call<Void> updateAccount(@Body UpdateUserBundle updateUserBundle);

    @POST("/login")
    Call<UserRespBundle> login(@Body AccountBundle accountBundle);

    @POST("/register")
    Call<UserRespBundle> register(@Body AccountBundle accountBundle);

    @POST("/verify")
    Call<VerifyResp> verifyUser(@Body VerifyBundle verifyBundle);
}
