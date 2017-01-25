package teamawesome.cs180frontend.API.Models.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by KongK on 10/21/2016.
 */

public class UpdateUserBundle implements Serializable{
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

    public UpdateUserBundle(int id, String password, String new_password, int school_id) {
        this.id = id;
        this.password = password;
        this.new_password = new_password;
        this.school_id = school_id;
    }

    public void setPassword(String new_password) {
        this.new_password = new_password;
    }
}
