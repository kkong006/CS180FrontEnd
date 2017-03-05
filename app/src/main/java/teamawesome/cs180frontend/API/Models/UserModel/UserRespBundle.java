package teamawesome.cs180frontend.API.Models.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * container for user data
 *
 * Created by nicholas on 10/9/16.
 */

//Response to logging in/registering
public class UserRespBundle {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("verified")
    @Expose
    public boolean verified;

    @SerializedName("school_id")
    @Expose
    public int schoolId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVerified() { return verified;}

    public void setVerified(boolean verified) { this.verified = verified; }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
}
