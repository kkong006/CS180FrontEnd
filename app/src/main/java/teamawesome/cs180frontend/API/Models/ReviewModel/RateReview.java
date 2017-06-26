package teamawesome.cs180frontend.API.Models.ReviewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RateReview {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("password")
    @Expose
    private String password;


    @SerializedName("review_id")
    @Expose
    private int reviewId;

    @SerializedName("review_rating")
    @Expose
    private int reviewRating;


    public RateReview(int id, String password, int reviewId, int reviewRating) {
        this.id = id;
        this.password = password;
        this.reviewId = reviewId;
        this.reviewRating = reviewRating;
    }
}
