package teamawesome.cs180frontend.API.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import teamawesome.cs180frontend.API.Models.CacheDataBundle;
import teamawesome.cs180frontend.API.Models.ProfessorRespBundle;
import teamawesome.cs180frontend.API.Models.RateReview;
import teamawesome.cs180frontend.API.Models.ReviewIDRespBundle;
import teamawesome.cs180frontend.API.Models.ReviewRespBundle;
import teamawesome.cs180frontend.API.Models.UserReview;

/**
 * Created by jman0_000 on 10/27/2016.
 */

public interface MatchingService {

    @GET("/getData")
    Call<CacheDataBundle> getData(@Query("school_id") int school_id);

    @GET("/professor")
    Call<ProfessorRespBundle> getProfessorSummary(@Query("prof_id") int prof_id);

    @POST("/rateReview")
    Call<Void> rateReview(@Body RateReview rateReview);

    @POST("/review")
    Call<ReviewIDRespBundle> review(@Body UserReview userReview);

    @GET("/reviews")
    Call<List<ReviewRespBundle>> reviews(@Query("prof_id") Integer prof_id,
                                         @Query("school_id") Integer school_id,
                                         @Query("user_id") Integer user_id,
                                         @Query("offset") Integer offset);

}
