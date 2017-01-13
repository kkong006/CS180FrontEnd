package teamawesome.cs180frontend.API.Models.DataModel.CacheData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jman0_000 on 1/13/2017.
 */

public class ReviewId implements Serializable {
    @SerializedName("review_id")
    @Expose
    private int reviewId;

    public int getReviewId() {
        return reviewId;
    }
}
