package teamawesome.cs180frontend.API.Models.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jman0_000 on 10/31/2016.
 */

public class CacheDataBundle implements Serializable{
    @SerializedName("liked")
    @Expose
    private List<Integer> liked;

    @SerializedName("disliked")
    @Expose
    private List<Integer> disliked;

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

    public List<Integer> getLiked() {
        return liked;
    }

    public void setLiked(List<Integer> liked) {
        this.liked = liked;
    }

    public List<Integer> getDisliked() {
        return disliked;
    }

    public void setDisliked(List<Integer> disliked) {
        this.disliked = disliked;
    }

    public List<SchoolBundle> getSchools() {
        return schools;
    }

    public void setSchools(List<SchoolBundle> schools) {
        this.schools = schools;
    }

    public List<SubjectBundle> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectBundle> subjects) {
        this.subjects = subjects;
    }

    public List<ClassBundle> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassBundle> classes) {
        this.classes = classes;
    }

    public List<ProfessorBundle> getProfs() {
        return profs;
    }

    public void setProfs(List<ProfessorBundle> profs) {
        this.profs = profs;
    }
}
