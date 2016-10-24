package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by KongK on 10/24/2016.
 */

public class Professor {

    @SerializedName("professor_id")
    @Expose
    private int professor_id;

    @SerializedName("professor_name")
    @Expose
    private String professor_name;

    public Professor(int professor_id, String professor_name) {
        this.professor_id = professor_id;
        this.professor_name = professor_name;
    }

    public int getProfessorId() {
        return professor_id;
    }

    public String getProfessorName() {
        return professor_name;
    }
}
