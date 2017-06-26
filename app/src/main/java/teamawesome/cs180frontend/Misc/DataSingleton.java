package teamawesome.cs180frontend.Misc;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import teamawesome.cs180frontend.API.Models.DataModel.CacheData.CacheDataBundle;
import teamawesome.cs180frontend.API.Models.DataModel.CacheData.ReviewId;
import teamawesome.cs180frontend.API.Models.DataModel.ClassBundle;
import teamawesome.cs180frontend.API.Models.DataModel.ProfBundle;
import teamawesome.cs180frontend.API.Models.DataModel.SchoolBundle;
import teamawesome.cs180frontend.API.Models.DataModel.SubjectBundle;

//THIS HOLDS ALL LOCAL DATA THAT I DON'T WANT TO PERSIST
public class DataSingleton {
    private static DataSingleton instance = null;

    private ArrayList<SchoolBundle> schoolCache;
    private ArrayList<SubjectBundle> subjectCache;
    private ArrayList<ClassBundle> classCache;
    private ArrayList<ProfBundle> professorCache;

    private HashMap<String, SchoolBundle> schoolMap; //FOR O(1) access
    private HashMap<String, Integer> subjectMap;
    private HashMap<String, Integer> classMap;
    private HashMap<String, Integer> professorMap;

    private HashSet<Integer> likedSet;
    private HashSet<Integer> dislikedSet;

    public static DataSingleton getInstance() {
        if (instance == null) {
            instance = new DataSingleton();
        }
        return instance;
    }

    private DataSingleton() {
        this.schoolCache = new ArrayList<>();
        this.subjectCache = new ArrayList<>();
        this.classCache = new ArrayList<>();
        this.professorCache = new ArrayList<>();

        this.schoolMap = new HashMap<>();
        this.subjectMap = new HashMap<>();
        this.classMap = new HashMap<>();
        this.professorMap = new HashMap<>();

        this.likedSet = new HashSet<>();
        this.dislikedSet = new HashSet<>();
    }

    public void cacheDataBundle(Context context, CacheDataBundle data) {
        this.schoolCache.clear();
        this.subjectCache.clear();
        this.classCache.clear();
        this.professorCache.clear();
        this.schoolMap.clear();
        this.subjectMap.clear();
        this.classMap.clear();
        this.professorMap.clear();
        this.likedSet.clear();
        this.dislikedSet.clear();

        schoolCache.addAll(data.getSchools());
        for (SchoolBundle s : schoolCache) {
            schoolMap.put(s.getName(), s);
        }

        subjectCache.addAll(data.getSubjects());
        for (SubjectBundle s : subjectCache) {
            subjectMap.put(s.getSubjectIdent(), s.getSubjectId());
        }

        classCache.addAll(data.getClasses());
        for (ClassBundle c : classCache) {
            classMap.put(c.getName(), c.getClassId());
        }

        professorCache.addAll(data.getProfs());
        for (ProfBundle p : professorCache) {
            professorMap.put(p.getName(), p.getProfessorId());
        }

        if (Utils.changedSchool(context)) {
            Utils.saveSchoolData(context,
                    new SchoolBundle(data.getSchoolId(), data.getSystemType()));
            Utils.setSystemType(context, data.getSystemType());
            Utils.setChangeSchool(context, false);
        }
        
        cacheReviewRatings(context, data.getReviewRatings().getLiked(),
                data.getReviewRatings().getDisliked());
    }

    public void cacheReviewRatings(Context context, List<ReviewId> liked, List<ReviewId> disliked) {
        Set<String> cachedLiked = SPSingleton.getInstance(context)
                .getSp()
                .getStringSet(Constants.LIKED, null);
        Set<String> cachedDisliked = SPSingleton.getInstance(context)
                .getSp()
                .getStringSet(Constants.DISLIKED, null);

        //SHORT CIRCUITING
        //IF NO LIKES ARE CACHED OR THERE'S A DIFFERENCE IN SIZE APPLY LIKED LIST PULLED FROM SERVER
        if (cachedLiked == null || liked.size() != cachedLiked.size()) {
            for (ReviewId r : liked) {
                this.likedSet.add(r.getReviewId());
            }
        } else { //ELSE LOAD THE CACHED VALUES
            for (String s : cachedLiked) {
                try {
                    this.likedSet.add(Integer.valueOf(s));
                } catch (NumberFormatException e) {
                    System.out.println("CACHED LIKE BROKE");
                }
            }
        }

        //SHORT CIRCUITING
        if (cachedDisliked == null || disliked.size() != cachedLiked.size()) {
            for (ReviewId r : disliked) {
                this.dislikedSet.add(r.getReviewId());
            }
        } else {
            for (String s : cachedDisliked) {
                try {
                    this.dislikedSet.add(Integer.valueOf(s));
                } catch (NumberFormatException e) {
                    System.out.println("CACHED DISLIKE BROKE");
                }
            }
        }
    }

    public void saveReviewRatings(Context context) {
        Set<String> likedStrSet = new HashSet<>();
        Set<String> dislikedStrSet = new HashSet<>();

        for (Integer i : this.likedSet) {
            likedStrSet.add(i.toString());
        }

        for (Integer i : this.dislikedSet) {
            dislikedStrSet.add(i.toString());
        }

        SPSingleton.getInstance(context).getSp().edit()
                .putStringSet(Constants.LIKED, likedStrSet).commit();

        SPSingleton.getInstance(context).getSp().edit()
                .putStringSet(Constants.DISLIKED, dislikedStrSet).commit();
    }

    public Integer getSchoolId(String schoolName) {
        //NOTE: ONLY OBJECTS CAN BE NULL NOT PRIMITIVES
        SchoolBundle school = schoolMap.get(schoolName);
        return school != null ? school.getSchoolId() : null; //RETURNS NULL IF NO MATCH FOUND
    }

    public SchoolBundle getSchoolBundleByName(String schoolName) {
        return schoolMap.get(schoolName);
    }

    public Integer getSubjectId(String subjectName) {
        return subjectMap.get(subjectName);
    }

    public Integer getClassId(String className) {
        return classMap.get(className);
    }

    public Integer getProfessorId(String profName) {
        return professorMap.get(profName);
    }

    public ArrayList<SchoolBundle> getSchoolCache() {
        return schoolCache;
    }

    public ArrayList<SubjectBundle> getSubjectCache() {
        return subjectCache;
    }

    public ArrayList<ClassBundle> getClassCache() {
        return classCache;
    }

    public ArrayList<ProfBundle> getProfessorCache() {
        return professorCache;
    }

    public HashSet<Integer> getLikedSet() {
        return likedSet;
    }

    public HashSet<Integer> getDislikedSet() {
        return dislikedSet;
    }

}
