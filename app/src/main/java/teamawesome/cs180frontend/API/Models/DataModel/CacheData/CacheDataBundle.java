package teamawesome.cs180frontend.API.Models.DataModel.CacheData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import teamawesome.cs180frontend.API.Models.DataModel.ClassBundle;
import teamawesome.cs180frontend.API.Models.DataModel.ProfBundle;
import teamawesome.cs180frontend.API.Models.DataModel.SchoolBundle;
import teamawesome.cs180frontend.API.Models.DataModel.SubjectBundle;

/**
 * Created by jman0_000 on 10/31/2016.
 */

public class CacheDataBundle implements Serializable{
    @SerializedName("votes")
    @Expose
    private UserReviewRatingsBundle reviewRatings;

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
    private List<ProfBundle> profs;

    public UserReviewRatingsBundle getReviewRatings() {
        return reviewRatings;
    }

    public void setReviewRatings(UserReviewRatingsBundle reviewRatings) {
        this.reviewRatings = reviewRatings;
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

    public List<ProfBundle> getProfs() {
        return profs;
    }

    public void setProfs(List<ProfBundle> profs) {
        this.profs = profs;
    }
}
