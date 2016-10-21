package teamawesome.cs180frontend.API.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import teamawesome.cs180frontend.API.Models.RateReview;
import teamawesome.cs180frontend.API.Models.School;
import teamawesome.cs180frontend.API.Models.UserAccount;
import teamawesome.cs180frontend.API.Models.UserReview;

/**
 * Created by KongK on 10/21/2016.
 */

public interface UserService {
    @POST("/updateAccount")
    Call<Void> createAccount(@Body UserAccount userAccount);

    @POST("/review")
    Call<Void> review(@Body UserReview userReview);

    @POST("/getSchools")
    Call<List<School>> getSchools();

    @POST("/rateReview")
    Call<Void> rateReview(@Body RateReview rateReview);

}
