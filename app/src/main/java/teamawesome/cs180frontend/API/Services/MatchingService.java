package teamawesome.cs180frontend.API.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import teamawesome.cs180frontend.API.Models.DataModel.CacheData.CacheDataBundle;
import teamawesome.cs180frontend.API.Models.DataModel.ProfessorRespBundle;
import teamawesome.cs180frontend.API.Models.ReviewModel.RateReview;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewIDRespBundle;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRatingResp;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRespBundle;
import teamawesome.cs180frontend.API.Models.ReviewModel.UserReview;

/**
 * Created by jman0_000 on 10/27/2016.
 */

public interface MatchingService {

    @GET("/getData")
    Call<CacheDataBundle> getData(@Query("school_id") int schoolId, @Query("id") int userId);

    @GET("/professor")
    Call<ProfessorRespBundle> getProfessorSummary(@Query("prof_id") int profId);

    @POST("/rateReview")
    Call<ReviewRatingResp> rateReview(@Body RateReview rateReview);

    @POST("/review")
    Call<ReviewIDRespBundle> review(@Body UserReview userReview);

    @GET("/reviewRatings")
    Call<Void> reviewRatings(@Query("id") Integer userId);

    @GET("/reviews")
    Call<List<ReviewRespBundle>> reviews(@Query("school_id") Integer schoolId,
                                         @Query("subject_id") Integer subjectId,
                                         @Query("class_id") Integer classId,
                                         @Query("prof_id") Integer profId,
                                         @Query("user_id") Integer userId,
                                         @Query("offset") Integer offset);

}
