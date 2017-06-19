package teamawesome.cs180frontend.API.Models.DataModel.CacheData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

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
