package teamawesome.cs180frontend.API.Models.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KongK on 11/15/2016.
 */

public class ProfRespBundle implements Serializable {

    @SerializedName("prof_name")
    @Expose
    private String prof_name;

    @SerializedName("avg_rating")
    @Expose
    private double rating;

    @SerializedName("total_ratings")
    @Expose
    private int totalRatings;

    @SerializedName("school_name")
    @Expose
    private String schoolName;

    @SerializedName("classes")
    @Expose
    List<ProfClassBundle> classes;

    public String getProfName() {
        return prof_name;
    }

    public double getAvgRating() {
        return rating;
    }

    public int getTotalRatings() { return totalRatings; }

    public String getSchoolName() {
        return schoolName;
    }

    public List<ProfClassBundle> getClasses() {
        return classes;
    }
}
