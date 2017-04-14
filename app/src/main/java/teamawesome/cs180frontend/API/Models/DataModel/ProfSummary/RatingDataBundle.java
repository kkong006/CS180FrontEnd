package teamawesome.cs180frontend.API.Models.DataModel.ProfSummary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RatingDataBundle implements Serializable {
    @SerializedName("rating")
    @Expose
    private int rating;

    @SerializedName("total_ratings")
    @Expose
    private int totalRatings;

    public int getRating() {
        return rating;
    }

    public int getTotalRatings() {
        return totalRatings;
    }
}
