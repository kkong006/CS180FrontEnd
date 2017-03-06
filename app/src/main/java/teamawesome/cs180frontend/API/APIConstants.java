package teamawesome.cs180frontend.API;

/**
 * Created by jman0_000 on 10/27/2016.
 */

public class APIConstants {
    public static final String BASE_URL = "https://cs180team7.herokuapp.com";

    public static final int HTTP_STATUS_OK = 200;
    public static final int HTTP_STATUS_INVALID = 400;
    public static final int HTTP_STATUS_UNAUTHORIZED = 401;
    public static final int TOO_MANY_REQ = 429;
    public static final int HTTP_STATUS_ERROR = 500;

    public static final String LOGIN_FAILURE = "Unable to login. Please check your phone number/password.";
    public static final String LOGIN_ERROR = "Something went wrong. Please try again later.";
    public static final String DATA_FAILURE = "Unable to fetch data. Please try again later.";
    public static final String BAD_API_CALL = "Bad call made to fetch data. Please contact the developer.";

}
