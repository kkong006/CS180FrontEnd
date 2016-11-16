package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by KongK on 10/24/2016.
 */

public class ReviewRespBundle implements Serializable {

    @SerializedName("subject_id")
    @Expose
    private int subject_id;

    @SerializedName("review_id")
    @Expose
    private int review_id;

    @SerializedName("review_date")
    @Expose
    private String review_date;

    @SerializedName("school_id")
    @Expose
    private int school_id;

//    @SerializedName("rating_id")
//    @Expose
//    private int rating_id;

    @SerializedName("class_id")
    @Expose
    private int class_id;

    @SerializedName("rating")
    @Expose
    private int rating;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("review_rating")
    @Expose
    private int review_rating;

    @SerializedName("prof_id")
    @Expose
    private int prof_id;

    public ReviewRespBundle(int subject_id, int review_id, String review_date, int school_id, /*int rating_id,*/ int class_id, int rating, String message, int prof_id, int review_rating) {
        this.subject_id = subject_id;
        this.review_id = review_id;
        this.review_date = review_date;
        this.school_id = school_id;
//        this.rating_id = rating_id;
        this.class_id = class_id;
        this.rating = rating;
        this.message = message;
        this.prof_id = prof_id;
        this.review_rating = review_rating;
    }

    public int getSubjectId() {
        return subject_id;
    }

    public int getReviewId() {
        return review_id;
    }

    public String getReviewDate() {
        return review_date;
    }

    public int getSchool_id() {
        return school_id;
    }

//    public int getRatingId() {
//        return rating_id;
//    }

    public int getClassId() {
        return class_id;
    }

    public int getRating() {
        return rating;
    }

    public String getMessage() {
        return message;
    }

    public int getProfId() {
        return prof_id;
    }

    public int getReviewRating() {
        return review_rating;
    }
}
