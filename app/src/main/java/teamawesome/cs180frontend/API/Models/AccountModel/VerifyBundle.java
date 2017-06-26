package teamawesome.cs180frontend.API.Models.AccountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VerifyBundle implements Serializable {
    @SerializedName("id")
    @Expose
    private int userId;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("code")
    @Expose
    private String code;

    public VerifyBundle(int userId, String password, String code) {
        this.userId = userId;
        this.password = password;
        this.code = code;
    }

    public VerifyBundle changeValues(int userId, String password, String code) {
        this.userId = userId;
        this.password = password;
        this.code = code;
        return this;
    }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }
}
