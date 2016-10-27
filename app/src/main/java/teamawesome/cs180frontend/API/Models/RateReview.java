package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by KongK on 10/21/2016.
 */

public class RateReview {

    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("password")
    @Expose
    private String password;


    @SerializedName("review_id")
    @Expose
    private int review_id;

    @SerializedName("rating")
    @Expose
    private boolean rating;

    public RateReview(int user_id, String password, int review_id, boolean rating) {
        this.user_id = user_id;
        this.password = password;
        this.review_id = review_id;
        this.rating = rating;
    }

    public void setRating(boolean rating) {
        this.rating = rating;
    }

    public void setReviewId(int review_id) {
        this.review_id = review_id;
    }

}
