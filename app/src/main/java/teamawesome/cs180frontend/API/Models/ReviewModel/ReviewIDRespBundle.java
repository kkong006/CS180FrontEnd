package teamawesome.cs180frontend.API.Models.ReviewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReviewIDRespBundle implements Serializable {

    @SerializedName("review_id")
    @Expose
    private int reviewId;


    public ReviewIDRespBundle(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getReviewId() {
        return reviewId;
    }
}
