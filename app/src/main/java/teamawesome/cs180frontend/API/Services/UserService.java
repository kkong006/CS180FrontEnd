package teamawesome.cs180frontend.API.Services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import teamawesome.cs180frontend.API.Models.LoginRegisterBundle;
import teamawesome.cs180frontend.API.Models.RateReview;
import teamawesome.cs180frontend.API.Models.ReviewIDRespBundle;
import teamawesome.cs180frontend.API.Models.UserRespBundle;
import teamawesome.cs180frontend.API.Models.UpdateUserBundle;
import teamawesome.cs180frontend.API.Models.UserReview;

/**
 * Created by KongK on 10/21/2016.
 */

public interface UserService {
    @POST("/updateAccount")
    Call<Void> updateAccount(@Body UpdateUserBundle updateUserBundle);

    @POST("/login")
    Call<UserRespBundle> login(@Body LoginRegisterBundle loginRegisterBundle);

    @POST("/register")
    Call<UserRespBundle> register(@Body LoginRegisterBundle loginRegisterBundle);

    @POST("/verify")
    Call<UserRespBundle> verifyUser(@Body UserRespBundle userRespBundle);
}
