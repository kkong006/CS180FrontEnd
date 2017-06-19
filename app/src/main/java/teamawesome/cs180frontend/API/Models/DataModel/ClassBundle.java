package teamawesome.cs180frontend.API.Models.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by KongK on 10/25/2016.
 */

//Renamed to class bundle to avoid confusion with Class object
public class ClassBundle implements Serializable, AbstractBundle {

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
    private String name;

    public ClassBundle(int subjectId, int classId, int schoolId, String className) {
        this.subjectId = subjectId;
        this.classId = classId;
        this.schoolId = schoolId;
        this.name = className;
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

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }
}
