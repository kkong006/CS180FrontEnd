package teamawesome.cs180frontend.API.Models.ReviewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReviewRatingResp implements Serializable {
    @SerializedName("review_id")
    @Expose
    private int reviewId;

    @SerializedName("review_rating")
    @Expose
    private int reviewRatingVal;

    public int getReviewId() {
        return reviewId;
    }

    public int getReviewRatingVal() {
        return reviewRatingVal;
    }
}
