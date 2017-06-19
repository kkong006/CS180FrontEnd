package teamawesome.cs180frontend.API.Models.DataModel.CacheData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import teamawesome.cs180frontend.API.Models.DataModel.ClassBundle;
import teamawesome.cs180frontend.API.Models.DataModel.ProfBundle;
import teamawesome.cs180frontend.API.Models.DataModel.SchoolBundle;
import teamawesome.cs180frontend.API.Models.DataModel.SubjectBundle;

public class CacheDataBundle implements Serializable{
    @SerializedName("verified")
    @Expose
    private boolean verified;

    @SerializedName("system_type")
    @Expose
    private String systemType;

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

    public boolean isVerified() {
        return verified;
    }

    public String getSystemType() {
        return systemType;
    }

    public UserReviewRatingsBundle getReviewRatings() {
        return reviewRatings;
    }

    public List<SchoolBundle> getSchools() {
        return schools;
    }

    public List<SubjectBundle> getSubjects() {
        return subjects;
    }

    public List<ClassBundle> getClasses() {
        return classes;
    }

    public List<ProfBundle> getProfs() {
        return profs;
    }
}
