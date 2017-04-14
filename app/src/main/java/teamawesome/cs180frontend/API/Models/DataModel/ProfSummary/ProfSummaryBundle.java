package teamawesome.cs180frontend.API.Models.DataModel.ProfSummary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import teamawesome.cs180frontend.API.Models.DataModel.ProfClassBundle;

public class ProfSummaryBundle implements Serializable {

    @SerializedName("prof_name")
    @Expose
    private String prof_name;

    @SerializedName("avg_rating")
    @Expose
    private double rating;

    @SerializedName("ratings_info")
    @Expose
    private List<RatingDataBundle> ratingDataBundles;

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

    public List<RatingDataBundle> getRatingDataBundles() { return ratingDataBundles; }

    public int getTotalRatings() { return totalRatings; }

    public String getSchoolName() {
        return schoolName;
    }

    public List<ProfClassBundle> getClasses() {
        return classes;
    }
}
