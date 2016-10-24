package teamawesome.cs180frontend.API.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import teamawesome.cs180frontend.API.Models.Professor;
import teamawesome.cs180frontend.API.Models.RateReview;
import teamawesome.cs180frontend.API.Models.ResponseReview;
import teamawesome.cs180frontend.API.Models.School;
import teamawesome.cs180frontend.API.Models.User;
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

    @GET("/getSchools")
    Call<List<School>> getSchools();

    @POST("/rateReview")
    Call<Void> rateReview(@Body RateReview rateReview);

    @POST("/login")
    Call<User> basicLogin(@Body User user);

    @POST("/register")
    Call<User> basicRegister(@Body User user);

    @POST("/verify")
    Call<User> verifyUser(@Body User user);

    @GET("/search")
    Call<List<Professor>> search(@Query("name") String name,
                                 @Query("school_id") int school_id);

    @GET("/reviews")
    Call<List<ResponseReview>> reviews(@Query("prof_id") int prof_id,
                                       @Query("user_id") int user_id);
}
