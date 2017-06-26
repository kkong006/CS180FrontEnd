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
    private int review_id;

    @SerializedName("review_rating")
    @Expose
    private int review_rating;


    public RateReview(int id, String password, int review_id, int review_rating) {
        this.id = id;
        this.password = password;
        this.review_id = review_id;
        this.review_rating = review_rating;
    }
}
