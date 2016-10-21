package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by KongK on 10/21/2016.
 */

public class School {

    @SerializedName("school_id")
    @Expose
    private int school_id;

    @SerializedName("school_name")
    @Expose
    private String school_name;

    public School(int school_id, String school_name) {
        this.school_id = school_id;
        this.school_name = school_name;
    }

    public int getSchoolId() {
        return school_id;
    }

    public String getSchoolName() {
        return school_name;
    }
}
