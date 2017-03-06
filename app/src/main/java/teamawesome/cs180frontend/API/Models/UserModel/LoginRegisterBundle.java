package teamawesome.cs180frontend.API.Models.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginRegisterBundle implements Serializable {
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("school_id")
    @Expose
    private int school_id;

    public LoginRegisterBundle(String phoneNumber, String password, int school_id) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.school_id = school_id;
    }

    public void setLoginRegisterBundle(String phoneNumber, String password, int school_id) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.school_id = school_id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

}
