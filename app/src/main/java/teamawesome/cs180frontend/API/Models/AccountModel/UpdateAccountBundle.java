package teamawesome.cs180frontend.API.Models.AccountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateAccountBundle implements Serializable{
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("new_password")
    @Expose
    private String newPassword;

    @SerializedName("school_id")
    @Expose
    private int schoolId;

    public UpdateAccountBundle(int id, String password, String newPassword, int schoolId) {
        this.id = id;
        this.password = password;
        this.newPassword = newPassword;
        this.schoolId = schoolId;
    }

    public void setPassword(String new_password) {
        this.newPassword = new_password;
    }
}
