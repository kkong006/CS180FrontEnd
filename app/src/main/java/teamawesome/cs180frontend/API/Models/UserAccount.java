package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by KongK on 10/21/2016.
 */

public class UserAccount {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("new_password")
    @Expose
    private String new_password;

    @SerializedName("school_id")
    @Expose
    private int school_id;

    public UserAccount(int id, String password, String new_password, int school_id) {
        this.id = id;
        this.password = password;
        this.new_password = new_password;
        this.school_id = school_id;
    }

    public void setPassword(String new_password) {
        this.new_password = new_password;
    }

    public void setSchoolId(int school_id) {
        this.school_id = school_id;
    }
}
