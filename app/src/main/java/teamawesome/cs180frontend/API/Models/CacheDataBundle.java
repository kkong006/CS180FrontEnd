package teamawesome.cs180frontend.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jman0_000 on 10/31/2016.
 */

public class CacheDataBundle implements Serializable{
    @SerializedName("schools")
    @Expose
    private List<SchoolBundle> schools;

    @SerializedName("subjects")
    @Expose
    private List<SubjectBundle> subjects;

    @SerializedName("classes")
    @Expose
    private List<ClassBundle> classes;

    @SerializedName("professors")
    @Expose
    private List<ProfessorBundle> profs;
}
