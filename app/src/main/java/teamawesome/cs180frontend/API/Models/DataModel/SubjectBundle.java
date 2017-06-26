package teamawesome.cs180frontend.API.Models.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubjectBundle implements Serializable {
    @SerializedName("subject_id")
    @Expose
    private int subjectId;

    @SerializedName("school_id")
    @Expose
    private int schoolId;

    @SerializedName("subject_ident")
    @Expose
    private String subjectIdent;

    @SerializedName("subject_name")
    @Expose
    private String subjectName;

    public SubjectBundle(int subjectId, int schoolId, String subjectIdent, String subjectName) {
        this.subjectId = subjectId;
        this.schoolId = schoolId;
        this.subjectIdent = subjectIdent;
        this.subjectName = subjectName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getSubjectIdent() {
        return subjectIdent;
    }

    public void setSubjectIdent(String subjectIdent) {
        this.subjectIdent = subjectIdent;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
