package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by KongK on 10/24/2016.
 */

public class ReviewRespBundle implements Serializable {

    @SerializedName("review_id")
    @Expose
    private int reviewId;

    @SerializedName("review_date")
    @Expose
    private String reviewDate;

    @SerializedName("prof_name")
    @Expose
    private String profName;

    @SerializedName("rating")
    @Expose
    private int rating;

    @SerializedName("school_name")
    @Expose
    private String schoolName;

    @SerializedName("id")
    @Expose
    private String accountId;

    @SerializedName("message")
    @Expose
    private String reviewMsg;

    @SerializedName("class_name")
    @Expose
    private String className;

    public int getReviewId() {
        return reviewId;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public String getProfName() {
        return profName;
    }

    public int getRating() {
        return rating;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getReviewMsg() {
        return reviewMsg;
    }

    public String getClassName() {
        return className;
    }
}
