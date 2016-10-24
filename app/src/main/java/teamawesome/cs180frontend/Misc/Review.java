package teamawesome.cs180frontend.Misc;

import java.io.Serializable;

/**
 * Created by KongK on 10/16/2016.
 */

public class Review implements Serializable{
    public String mClassName;
    public String mRating;
    public String mReviewDate;
    public String mReviewContent;
    public String mReviewID;

    public Review(String className, String rating, String reviewDate, String review, String reviewID) {
        this.mClassName = className;
        this.mRating = rating;
        this.mReviewDate = reviewDate;
        this.mReviewContent = review;
        this.mReviewID = reviewID;
    }
}
