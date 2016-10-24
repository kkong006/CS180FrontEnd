package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by KongK on 10/24/2016.
 */

public class ResponseReview {

    @SerializedName("review_id")
    @Expose
    private int review_id;

    @SerializedName("class_id")
    @Expose
    private int class_id;

    @SerializedName("prof_id")
    @Expose
    private int prof_id;

    @SerializedName("review_date")
    @Expose
    private String review_date;

    @SerializedName("rating")
    @Expose
    private int rating;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("user_review_rating")
    @Expose
    private int user_review_rating;

    public ResponseReview(int review_id, String msg, int user_review_rating, String review_date, int rating, int prof_id, int class_id) {
        this.review_id = review_id;
        this.msg = msg;
        this.user_review_rating = user_review_rating;
        this.review_date = review_date;
        this.rating = rating;
        this.prof_id = prof_id;
        this.class_id = class_id;
    }

    public int getReviewId() {
        return review_id;
    }

    public int getClassId() {
        return class_id;
    }

    public int getProfId() {
        return prof_id;
    }

    public String getReviewDate() {
        return review_date;
    }

    public int getRating() {
        return rating;
    }

    public String getMsg() {
        return msg;
    }

    public int getUserReviewRating() {
        return user_review_rating;
    }
}
