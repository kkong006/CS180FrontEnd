package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by KongK on 11/7/2016.
 */

public class ReviewIDRespBundle implements Serializable {

    @SerializedName("review_id")
    @Expose
    private int review_id;


    public ReviewIDRespBundle(int review_id) {
        this.review_id = review_id;
    }

    public int getReviewId() {
        return review_id;
    }
}
