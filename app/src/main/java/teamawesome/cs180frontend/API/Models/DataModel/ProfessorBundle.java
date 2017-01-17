package teamawesome.cs180frontend.API.Models.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by KongK on 10/24/2016.
 */

public class ProfessorBundle implements Serializable{

    @SerializedName("prof_id")
    @Expose
    private int prof_id;

    @SerializedName("prof_name")
    @Expose
    private String prof_name;

    public ProfessorBundle(int prof_id, String prof_name) {
        this.prof_id = prof_id;
        this.prof_name = prof_name;
    }

    public int getProfessorId() {
        return prof_id;
    }

    public String getProfessorName() {
        return prof_name;
    }
}
