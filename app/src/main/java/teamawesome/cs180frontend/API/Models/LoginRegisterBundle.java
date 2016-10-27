package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jman0_000 on 10/27/2016.
 */

public class LoginRegisterBundle implements Serializable{
    @SerializedName("phone_number")
    @Expose
    String phoneNumber;

    @SerializedName("password")
    @Expose
    String password;

    public LoginRegisterBundle(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
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

    public void setPassword(String password) {
        this.password = password;
    }
}
