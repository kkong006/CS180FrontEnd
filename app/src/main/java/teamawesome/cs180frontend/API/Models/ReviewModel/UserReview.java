package teamawesome.cs180frontend.API.Models.ReviewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by KongK on 10/21/2016.
 */

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

    public UserReview(int id, String password, int schoolId, int subjectId, int classId, int profId, int rating, String review) {
        this.id = id;
        this.password = password;
        this.schoolId = schoolId;
        this.subjectId = subjectId;
        this.classId = classId;
        this.profId = profId;
        this.rating = rating;
        this.review = review;
    }

    public int getClassId() { return classId; }

    public void setClassId(int class_id) { this.classId = class_id; }

    public int getProfId() { return profId; }

    public void setProfId(int prof_id) { this.profId = prof_id; }

    public int getRating() { return rating; }

    public void setRating(int rating) { this.rating = rating; }

    public String getReview() { return review; }

    public void setReview(String review) { this.review = review; }
}
