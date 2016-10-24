package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by KongK on 10/21/2016.
 */

public class UserReview {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("school_id")
    @Expose
    private int school_id;

    @SerializedName("class_id")
    @Expose
    private int class_id;

    @SerializedName("prof_id")
    @Expose
    private int prof_id;

    @SerializedName("rating")
    @Expose
    private int rating;

    @SerializedName("review")
    @Expose
    private String review;

    public UserReview(int class_id, int prof_id, int rating, String review) {
//        TODO: grab values from shared preferences for id, school id
        this.class_id = class_id;
        this.prof_id = prof_id;
        this.rating = rating;
        this.review = review;
    }

    public int getClassId() { return class_id; }

    public void setClassId(int class_id) { this.class_id = class_id; }

    public int getProfId() { return prof_id; }

    public void setProfId(int prof_id) { this.prof_id = prof_id; }

    public int getRating() { return rating; }

    public void setRating(int rating) { this.rating = rating; }

    public String getReview() { return review; }

    public void setReview(String review) { this.review = review; }
}
