package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by KongK on 11/15/2016.
 */

public class ProfessorClassBundle implements Serializable {

    @SerializedName("class_id")
    @Expose
    private int class_id;

    @SerializedName("class_name")
    @Expose
    private String class_name;

    public ProfessorClassBundle(int class_id, String class_name) {
        this.class_id = class_id;
        this.class_name = class_name;
    }

    public int getClassId() {
        return class_id;
    }

    public String getClassName() {
        return class_name;
    }
}
