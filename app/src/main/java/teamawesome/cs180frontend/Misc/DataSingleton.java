package teamawesome.cs180frontend.Misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import teamawesome.cs180frontend.API.Models.CacheDataBundle;
import teamawesome.cs180frontend.API.Models.ClassBundle;
import teamawesome.cs180frontend.API.Models.ProfessorBundle;
import teamawesome.cs180frontend.API.Models.SchoolBundle;
import teamawesome.cs180frontend.API.Models.SubjectBundle;

/**
 * Created by jman0_000 on 10/27/2016.
 */

public class DataSingleton {
    private static DataSingleton instance = null;
    private List<SchoolBundle> schoolCache;
    private List<SubjectBundle> subjectCache;
    private List<ClassBundle> classCache;
    private List<ProfessorBundle> professorCache;
    private HashMap<String, Integer> schoolMap; //FOR O(1) access

    public static DataSingleton getInstance() {
        if (instance == null) {
            instance = new DataSingleton();
        }

        return instance;
    }

    private DataSingleton() {
        schoolCache = new ArrayList<>();
        subjectCache = new ArrayList<>();
        classCache = new ArrayList<>();
        professorCache = new ArrayList<>();
        schoolMap = new HashMap<>();
    }

    public void cacheDataBundle(CacheDataBundle data) {
        this.schoolCache.clear();
        this.subjectCache.clear();
        this.classCache.clear();
        this.professorCache.clear();

        this.schoolMap.clear();

        schoolCache.addAll(data.getSchools());

        for (SchoolBundle s : schoolCache) {
            schoolMap.put(s.getSchoolName(), s.getSchoolId());
        }

        subjectCache.addAll(data.getSubjects());
        classCache.addAll(data.getClasses());
        professorCache.addAll(data.getProfs());
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

    public Integer getSchoolId(String school) {
        //NOTE: ONLY OBJECTS CAN BE NULL NOT PRIMITIVES
        return schoolMap.get(school); //RETURNS NULL IF NO MATCH FOUND
    }

    public List<ClassBundle> getClassCache() {
        return classCache;
    }

    public List<ProfessorBundle> getProfessorCache() {
        return professorCache;
    }
}
