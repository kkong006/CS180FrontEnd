package teamawesome.cs180frontend.API.Models.ReviewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewRatingBundle {
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
