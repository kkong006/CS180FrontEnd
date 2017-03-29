package teamawesome.cs180frontend.API.Models.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProfBundle implements Serializable, AbstractBundle {

    @SerializedName("prof_id")
    @Expose
    private int prof_id;

    @SerializedName("prof_name")
    @Expose
    private String name;

    public ProfBundle(int prof_id, String prof_name) {
        this.prof_id = prof_id;
        this.name = prof_name;
    }

    public int getProfessorId() {
        return prof_id;
    }

    public String getName() {
        return name;
    }
}
