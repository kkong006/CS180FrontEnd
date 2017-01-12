package teamawesome.cs180frontend.API.Models.ReviewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReviewRatingBundle implements Serializable{
    @SerializedName("rating_id")
    @Expose
    private int ratingId;

    @SerializedName("review_id")
    @Expose
    private int reviewId;

    @SerializedName("id")
    @Expose
    private int userId;

    @SerializedName("review_rating")
    @Expose
    private boolean reviewRating;

    public int getRatingId() {
        return ratingId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isReviewRating() {
        return reviewRating;
    }
}
