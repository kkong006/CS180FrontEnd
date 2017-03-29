package teamawesome.cs180frontend.API.Models.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by KongK on 10/21/2016.
 */

public class SchoolBundle implements Serializable, AbstractBundle {

    @SerializedName("school_id")
    @Expose
    private int school_id;

    @SerializedName("school_name")
    @Expose
    private String name;

    public SchoolBundle(int school_id, String name) {
        this.school_id = school_id;
        this.name = name;
    }

    public int getSchoolId() {
        return school_id;
    }

    public String getName() {
        return name;
    }
}
