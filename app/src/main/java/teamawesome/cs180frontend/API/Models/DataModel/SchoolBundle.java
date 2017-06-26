package teamawesome.cs180frontend.API.Models.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SchoolBundle implements Serializable, AbstractBundle {

    @SerializedName("school_id")
    @Expose
    private int schoolId;

    @SerializedName("school_name")
    @Expose
    private String name;

    @SerializedName("system_type")
    @Expose
    private String systemType;

    public SchoolBundle( int schoolId, String systemType) {
        this.systemType = systemType;
        this.schoolId = schoolId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public String getName() {
        return name;
    }

    public String getSystemType() {
        return systemType;
    }
}
