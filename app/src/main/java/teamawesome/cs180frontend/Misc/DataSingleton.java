package teamawesome.cs180frontend.Misc;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import teamawesome.cs180frontend.API.Models.DataModel.CacheDataBundle;
import teamawesome.cs180frontend.API.Models.DataModel.ClassBundle;
import teamawesome.cs180frontend.API.Models.DataModel.ProfessorBundle;
import teamawesome.cs180frontend.API.Models.DataModel.SchoolBundle;
import teamawesome.cs180frontend.API.Models.DataModel.SubjectBundle;

public class DataSingleton {
    private static DataSingleton instance = null;

    private ArrayList<SchoolBundle> schoolCache;
    private ArrayList<SubjectBundle> subjectCache;
    private ArrayList<ClassBundle> classCache;
    private ArrayList<ProfessorBundle> professorCache;

    private HashMap<String, Integer> schoolMap; //FOR O(1) access
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
            schoolMap.put(s.getSchoolName(), s.getSchoolId());
        }

        subjectCache.addAll(data.getSubjects());
        for (SubjectBundle s : subjectCache) {
            subjectMap.put(s.getSubjectIdent(), s.getSubjectId());
        }

        classCache.addAll(data.getClasses());
        for (ClassBundle c : classCache) {
            classMap.put(c.getClassName(), c.getClassId());
        }

        professorCache.addAll(data.getProfs());
        for (ProfessorBundle p : professorCache) {
            professorMap.put(p.getProfessorName(), p.getProfessorId());
        }
    }

    public List<SchoolBundle> cacheSchools(List<SchoolBundle> schools) {
        this.schoolCache.clear();
        this.schoolCache.addAll(schools);
        return this.schoolCache;
    }

    public List<SubjectBundle> cacheSubjects(List<SubjectBundle> subjects) {
        this.subjectCache.clear();
        this.subjectCache.addAll(subjects);
        return this.subjectCache;
    }

    public List<ClassBundle> cacheClasses(List<ClassBundle> classes) {
        this.classCache.clear();
        this.classCache.addAll(classes);
        return this.classCache;
    }

    public List<ProfessorBundle> cacheProfessors(List<ProfessorBundle> profs) {
        this.professorCache.clear();
        this.professorCache.addAll(profs);
        return this.professorCache;
    }

    public void cacheReviewRatings(Context context, List<Integer> liked, Set<Integer> disliked) {
        Set<String> cachedLiked = SPSingleton.getInstance(context)
                .getSp()
                .getStringSet(Constants.LIKED, null);
        Set<String> cachedDisliked = SPSingleton.getInstance(context)
                .getSp()
                .getStringSet(Constants.DISLIKED, null);

        //SHORT CIRCUITING
        //IF NO LIKES ARE CACHED OR THERE'S A DIFFERENCE IN SIZE APPLY LIKED LIST PULLED FROM SERVER
        if (cachedLiked == null || liked.size() != cachedLiked.size()) {
            for (Integer i : liked) {
                this.likedSet.add(i);
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
            for (Integer i : disliked) {
                this.dislikedSet.add(i);
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

    public Integer getSchoolId(String schoolName) {
        //NOTE: ONLY OBJECTS CAN BE NULL NOT PRIMITIVES
        return schoolMap.get(schoolName); //RETURNS NULL IF NO MATCH FOUND
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

    public ArrayList<ProfessorBundle> getProfessorCache() {
        return professorCache;
    }
}
