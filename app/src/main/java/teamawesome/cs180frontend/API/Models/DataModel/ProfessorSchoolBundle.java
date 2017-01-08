package teamawesome.cs180frontend.API.Models.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by KongK on 11/17/2016.
 */

public class ProfessorSchoolBundle implements Serializable {
    @SerializedName("school_name")
    @Expose
    private String school_name;


    public ProfessorSchoolBundle(String school_name) {
        this.school_name = school_name;
    }

    public String getSchoolName() {
        return school_name;
    }
}
