package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.security.auth.callback.Callback;

/**
 * Created by KongK on 10/25/2016.
 */

public class RatingId {

    @SerializedName("rating_id")
    @Expose
    private int rating_id;

    public int getRating_id() {
        return rating_id;
    }

    public RatingId(int rating_id) {
        this.rating_id = rating_id;
    }
}
