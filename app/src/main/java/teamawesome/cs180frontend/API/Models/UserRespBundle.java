package teamawesome.cs180frontend.API.Models;

import android.os.Parcel;
import android.os.Parcelable;

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

    @SerializedName("active")
    @Expose
    public boolean active;

    @SerializedName("school_id")
    @Expose
    public int schoolId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
}
