package teamawesome.cs180frontend.services;

import retrofit2.Call;
import retrofit2.http.POST;
import teamawesome.cs180frontend.utils.User;

/**
 * Created by nicholas on 10/9/16.
 */

public interface LoginService {
    @POST("/login")
    Call<User> basicLogin();
}
