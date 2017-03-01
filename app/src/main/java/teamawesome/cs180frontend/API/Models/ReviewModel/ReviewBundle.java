package teamawesome.cs180frontend.API.Models.ReviewModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by KongK on 10/24/2016.
 */

public class ReviewBundle implements Serializable, Parcelable {

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
    private int accountId;

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

    public int getAccountId() {
        return accountId;
    }

    public String getReviewMsg() {
        return reviewMsg;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.reviewId);
        dest.writeString(this.reviewDate);
        dest.writeString(this.profName);
        dest.writeInt(this.rating);
        dest.writeString(this.schoolName);
        dest.writeInt(this.accountId);
        dest.writeString(this.reviewMsg);
        dest.writeString(this.className);
    }

    protected ReviewBundle(Parcel p) {
        this.reviewId = p.readInt();
        this.reviewDate = p.readString();
        this.profName = p.readString();
        this.rating = p.readInt();
        this.schoolName = p.readString();
        this.accountId = p.readInt();
        this.reviewMsg = p.readString();
        this.className = p.readString();
    }

    public static final Parcelable.Creator<ReviewBundle> CREATOR = new Parcelable.Creator<ReviewBundle>() {
        public ReviewBundle createFromParcel(Parcel source) {
            return new ReviewBundle(source);
        }

        public ReviewBundle[] newArray(int size) {
            return new ReviewBundle[size];
        }
    };


}
