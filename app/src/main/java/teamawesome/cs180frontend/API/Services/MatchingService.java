package teamawesome.cs180frontend.API.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;
import teamawesome.cs180frontend.API.Models.ClassBundle;
import teamawesome.cs180frontend.API.Models.Professor;
import teamawesome.cs180frontend.API.Models.ResponseReview;
import teamawesome.cs180frontend.API.Models.School;

/**
 * Created by jman0_000 on 10/27/2016.
 */

public interface MatchingService {
    /*@GET("/search")
    Call<Professor> search(@Query("professor_name") String name,
                           @Query("school_id") int school_id);*/

    @GET("/getSchools")
    Call<List<School>> getSchools();

    @GET("/getClasses")
    Call<List<ClassBundle>> getClasses(@Query("school_id") int school_id);

    @GET("/getProfessor")
    Call<List<Professor>> getProfs(@Query("school_id") int school_id);

    @GET("/reviews")
    Call<List<ResponseReview>> reviews(@Query("prof_id") int prof_id,
                                       @Query("user_id") int user_id);
}
