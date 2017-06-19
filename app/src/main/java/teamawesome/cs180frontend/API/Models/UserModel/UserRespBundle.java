package teamawesome.cs180frontend.API.Models.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//Response to logging in/registering
public class UserRespBundle {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("verified")
    @Expose
    private boolean verified;

    @SerializedName("school_id")
    @Expose
    private int schoolId;

    @SerializedName("system_type")
    @Expose
    private String systemType;

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

    public void setSchoolId(int schoolId) { this.schoolId = schoolId; }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }
}
