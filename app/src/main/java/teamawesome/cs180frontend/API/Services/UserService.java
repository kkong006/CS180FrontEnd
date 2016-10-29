package teamawesome.cs180frontend.API.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import teamawesome.cs180frontend.API.Models.LoginRegisterBundle;
import teamawesome.cs180frontend.API.Models.Professor;
import teamawesome.cs180frontend.API.Models.RateReview;
import teamawesome.cs180frontend.API.Models.RatingId;
import teamawesome.cs180frontend.API.Models.ResponseReview;
import teamawesome.cs180frontend.API.Models.School;
import teamawesome.cs180frontend.API.Models.UserRespBundle;
import teamawesome.cs180frontend.API.Models.UpdateUserBundle;
import teamawesome.cs180frontend.API.Models.UserReview;

/**
 * Created by KongK on 10/21/2016.
 */

public interface UserService {
    @POST("/updateAccount")
    Call<Void> updateAccount(@Body UpdateUserBundle updateUserBundle);

    @POST("/review")
    Call<Void> review(@Body UserReview userReview);

    @POST("/rateReview")
    Call<Void> rateReview(@Body RateReview rateReview);

    @POST("/login")
    Call<UserRespBundle> login(@Body LoginRegisterBundle loginRegisterBundle);

    @POST("/register")
    Call<UserRespBundle> basicRegister(@Body LoginRegisterBundle userRespBundle);

    @POST("/verify")
    Call<UserRespBundle> verifyUser(@Body UserRespBundle userRespBundle);
}
