package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by KongK on 10/25/2016.
 */

public class Class {

    @SerializedName("class_id")
    @Expose
    private int class_id;

    @SerializedName("class_name")
    @Expose
    private String class_name;

    public Class(int class_id, String class_name) {
        this.class_id = class_id;
        this.class_name = class_name;
    }

    public int getClassId() { return class_id; }

    public String getClassName() { return class_name; }

}
