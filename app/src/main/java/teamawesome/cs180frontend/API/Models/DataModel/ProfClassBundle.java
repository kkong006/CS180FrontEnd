package teamawesome.cs180frontend.API.Models.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProfClassBundle implements Serializable {

    @SerializedName("class_id")
    @Expose
    private int classId;

    @SerializedName("class_name")
    @Expose
    private String className;

    @SerializedName("review_cnt")
    @Expose
    private int reviewCnt;

    public ProfClassBundle(int classId, String className) {
        this.classId = classId;
        this.className = className;
    }

    public int getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public int getReviewCnt() { return reviewCnt; }
}
