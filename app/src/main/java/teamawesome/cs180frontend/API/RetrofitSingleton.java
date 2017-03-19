package teamawesome.cs180frontend.API;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import teamawesome.cs180frontend.API.Services.MatchingService;
import teamawesome.cs180frontend.API.Services.UserService;

/**
 * Created by KongK on 10/21/2016.
 */

public class RetrofitSingleton {
    private static RetrofitSingleton instance;
    private UserService userService;
    private MatchingService matchingService;

    public static RetrofitSingleton getInstance() {
        if (instance == null) {
            instance = new RetrofitSingleton();
        }

        return instance;
    }

    private RetrofitSingleton() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIConstants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userService = retrofit.create(UserService.class);
        matchingService = retrofit.create(MatchingService.class);
    }

    public UserService getUserService() {
        return userService;
    }

    public MatchingService getMatchingService() {
        return matchingService;
    }
}
