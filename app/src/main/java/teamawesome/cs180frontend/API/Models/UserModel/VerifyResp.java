package teamawesome.cs180frontend.API.Models.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VerifyResp implements Serializable {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("verified")
    @Expose
    public boolean verified;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public boolean isVerified() { return verified; }

    public void setVerified(boolean verified) { this.verified = verified; }
}
