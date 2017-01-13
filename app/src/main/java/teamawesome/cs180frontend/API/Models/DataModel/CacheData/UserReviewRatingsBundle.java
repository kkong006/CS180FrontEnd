package teamawesome.cs180frontend.API.Models.DataModel.CacheData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRatingBundle;

/**
 * Created by jman0_000 on 1/13/2017.
 */

public class UserReviewRatingsBundle implements Serializable {
    @SerializedName("liked")
    @Expose
    private List<ReviewId> liked;

    @SerializedName("disliked")
    @Expose
    private List<ReviewId> disliked;

    public List<ReviewId> getLiked() {
        return liked;
    }

    public List<ReviewId> getDisliked() {
        return disliked;
    }
}
