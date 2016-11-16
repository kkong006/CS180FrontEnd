package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KongK on 11/15/2016.
 */

public class ProfessorRespBundle implements Serializable {

    @SerializedName("prof_name")
    @Expose
    private String prof_name;

    @SerializedName("avg_rating")
    @Expose
    private int avg_rating;

    @SerializedName("schools")
    @Expose
    List<SchoolBundle> schools;

    @SerializedName("classes")
    @Expose
    List<ProfessorClassBundle> classes;

    public String getProfName() {
        return prof_name;
    }

    public int getAvgRating() {
        return avg_rating;
    }

    public List<SchoolBundle> getSchools() {
        return schools;
    }

    public List<ProfessorClassBundle> getClasses() {
        return classes;
    }
}
