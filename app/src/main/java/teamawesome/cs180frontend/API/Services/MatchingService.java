package teamawesome.cs180frontend.API.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import teamawesome.cs180frontend.API.Models.CacheDataBundle;
import teamawesome.cs180frontend.API.Models.ReviewRespBundle;

/**
 * Created by jman0_000 on 10/27/2016.
 */

public interface MatchingService {
    /*@GET("/search")
    Call<Professor> search(@Query("professor_name") String name,
                           @Query("school_id") int school_id);*/

    @GET("/getData")
    Call<CacheDataBundle> getData(@Query("school_id") int school_id);

    @GET("/reviews")
    Call<List<ReviewRespBundle>> reviewsProfessor(@Query("prof_id") int prof_id,
                                         @Query("user_id") int user_id);

    @GET("/reviews")
    Call<List<ReviewRespBundle>> reviewsSchool(@Query("school_id") int school_id,
                                         @Query("user_id") int user_id);
}
