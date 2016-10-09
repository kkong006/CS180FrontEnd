package teamawesome.cs180frontend.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * container for user data
 *
 * Created by nicholas on 10/9/16.
 */

public class User {
    @SerializedName("userId")
    @Expose
    public long userId;
    @Expose
    @SerializedName("username")
    public String username;
    @SerializedName("password")
    @Expose
    public String password;

    public User(long userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
