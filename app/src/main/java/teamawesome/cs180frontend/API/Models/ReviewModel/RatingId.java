package teamawesome.cs180frontend.API.Models.ReviewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by KongK on 10/25/2016.
 */

public class RatingId {

    @SerializedName("ratingId")
    @Expose
    private int ratingId;

    public int getRatingId() {
        return ratingId;
    }

    public RatingId(int ratingId) {
        this.ratingId = ratingId;
    }
}
