package teamawesome.cs180frontend.utils;

/**
 * container for user data
 *
 * Created by nicholas on 10/9/16.
 */

public class User {
    public long userId;
    public String username;
    public String password;

    public User(long userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
}
