package teamawesome.cs180frontend.services;

import retrofit2.Call;
import retrofit2.http.POST;
import teamawesome.cs180frontend.utils.User;

/**
 * Created using tutorial found at:
 * https://futurestud.io/tutorials/retrofit-getting-started-and-android-client
 *
 * Created by nicholas on 10/9/16.
 */

public interface LoginService {
    @POST("/login")
    Call<User> basicLogin();
}
