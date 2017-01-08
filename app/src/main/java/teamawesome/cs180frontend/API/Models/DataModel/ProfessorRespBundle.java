package teamawesome.cs180frontend.API.Models.DataModel;

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

    @SerializedName("rating")
    @Expose
    private int rating;

//    @SerializedName("schools")
//    @Expose
//    List<ProfessorSchoolBundle> schools;

    @SerializedName("school_name")
    @Expose
    private String schoolName;

    @SerializedName("classes")
    @Expose
    List<ProfessorClassBundle> classes;

    public ProfessorRespBundle(String prof_name, int rating, String schoolName, List<ProfessorClassBundle> classes) {
        this.prof_name = prof_name;
        this.rating = rating;
        this.schoolName = schoolName;
        this.classes = classes;
    }

    public String getProfName() {
        return prof_name;
    }

    public int getAvgRating() {
        return rating;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public List<ProfessorClassBundle> getClasses() {
        return classes;
    }
}
