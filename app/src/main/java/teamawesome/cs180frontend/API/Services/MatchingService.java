package teamawesome.cs180frontend.API.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import teamawesome.cs180frontend.API.Models.DataModel.CacheData.CacheDataBundle;
import teamawesome.cs180frontend.API.Models.DataModel.ProfSummary.ProfSummaryBundle;
import teamawesome.cs180frontend.API.Models.ReviewModel.RateReview;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewBundle;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewIDRespBundle;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRatingResp;
import teamawesome.cs180frontend.API.Models.ReviewModel.UserReview;

public interface MatchingService {

    @GET("/data")
    Call<CacheDataBundle> getData(@Query("id") Integer userId,
                                  @Query("changedschool") Boolean changeSchool);

    @GET("/professor")
    Call<ProfSummaryBundle> getProfessorSummary(@Query("id") int profId);

    @POST("/rateReview")
    Call<ReviewRatingResp> rateReview(@Body RateReview rateReview);

    @POST("/review")
    Call<ReviewIDRespBundle> review(@Body UserReview userReview);

    @GET("/reviewRatings")
    Call<Void> reviewRatings(@Query("id") Integer userId);

    @GET("/reviews")
    Call<List<ReviewBundle>> reviews(@Query("school_id") Integer schoolId,
                                     @Query("subject_id") Integer subjectId,
                                     @Query("class_id") Integer classId,
                                     @Query("prof_id") Integer profId,
                                     @Query("id") Integer userId,
                                     @Query("offset") Integer offset);

}
