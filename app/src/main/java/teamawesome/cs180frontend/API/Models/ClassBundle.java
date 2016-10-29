package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by KongK on 10/25/2016.
 */

public class ClassBundle implements Serializable{

    @SerializedName("subject_id")
    @Expose
    private int subjectId;

    @SerializedName("school_id")
    @Expose
    private int schoolId;

    @SerializedName("class_id")
    @Expose
    private int classId;

    @SerializedName("class_name")
    @Expose
    private String className;

    public ClassBundle(int subjectId, int classId, int schoolId, String className) {
        this.subjectId = subjectId;
        this.classId = classId;
        this.schoolId = schoolId;
        this.className = className;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getSubjectId() {
        return subjectId;
    }

//    public void setSubjectId(int subjectId) {
//        this.subjectId = subjectId;
//    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

//    public void setClassName(String className) {
//        this.className = className;
//    }
}
