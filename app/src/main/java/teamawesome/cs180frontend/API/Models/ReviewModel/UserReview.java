package teamawesome.cs180frontend.API.Models.ReviewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserReview implements Serializable{

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("school_id")
    @Expose
    private int schoolId;

    @SerializedName("interval_name")
    @Expose
    private String intervalName;

    @SerializedName("year")
    @Expose
    private int year;

    @SerializedName("subject_id")
    @Expose
    private int subjectId;

    @SerializedName("class_id")
    @Expose
    private int classId;

    @SerializedName("prof_id")
    @Expose
    private int profId;

    @SerializedName("rating")
    @Expose
    private int rating;

    @SerializedName("review")
    @Expose
    private String review;

    public UserReview(int id, String password, int schoolId, String intervalName, int year,
                      int subjectId, int classId, int profId, int rating, String review) {
        this.id = id;
        this.password = password;
        this.schoolId = schoolId;
        this.intervalName = intervalName;
        this.year = year;
        this.subjectId = subjectId;
        this.classId = classId;
        this.profId = profId;
        this.rating = rating;
        this.review = review;
    }
}
