package teamawesome.cs180frontend.API.Models.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AccountBundle implements Serializable {
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("school_id")
    @Expose
    private Integer schoolId;

    public AccountBundle(String phoneNumber, String password, Integer schoolId) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.schoolId = schoolId;
    }

    public void setLoginRegisterBundle(String phoneNumber, String password, int school_id) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.schoolId = school_id;
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

    public Integer getSchoolId() { return schoolId; }

    public void setSchoolId(Integer schoolId) { this.schoolId = schoolId; }
}
