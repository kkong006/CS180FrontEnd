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
    private ArrayList<SchoolBundle> schoolCache;
    private ArrayList<SubjectBundle> subjectCache;
    private ArrayList<ClassBundle> classCache;
    private ArrayList<ProfessorBundle> professorCache;
    private HashMap<String, Integer> schoolMap; //FOR O(1) access
    private HashMap<Integer, String> schoolNameMap;
    private HashMap<String, Integer> subjectMap;
    private HashMap<String, Integer> classMap;
    private HashMap<Integer, String> classNameMap;
    private HashMap<String, Integer> professorMap;
    private HashMap<Integer, String> professorNameMap;

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
        schoolNameMap = new HashMap<>();
        subjectMap = new HashMap<>();
        classMap = new HashMap<>();
        classNameMap = new HashMap<>();
        professorMap = new HashMap<>();
        professorNameMap = new HashMap<>();
    }

    public void cacheDataBundle(CacheDataBundle data) {
        this.schoolCache.clear();
        this.subjectCache.clear();
        this.classCache.clear();
        this.professorCache.clear();
        this.schoolMap.clear();
        this.schoolNameMap.clear();
        this.subjectMap.clear();
        this.classMap.clear();
        this.classNameMap.clear();
        this.professorMap.clear();
        this.professorNameMap.clear();

        schoolCache.addAll(data.getSchools());

        for (SchoolBundle s : schoolCache) {
            schoolMap.put(s.getSchoolName(), s.getSchoolId());
            schoolNameMap.put(s.getSchoolId(), s.getSchoolName());
        }

        subjectCache.addAll(data.getSubjects());
        for (SubjectBundle s : subjectCache) {
            subjectMap.put(s.getSubjectIdent(), s.getSubjectId());
        }

        classCache.addAll(data.getClasses());
        for(ClassBundle c : classCache) {
            classMap.put(c.getClassName(), c.getClassId());
            classNameMap.put(c.getClassId(), c.getClassName());
        }

        professorCache.addAll(data.getProfs());
        for(ProfessorBundle p : professorCache) {
            professorMap.put(p.getProfessorName(), p.getProfessorId());
            professorNameMap.put(p.getProfessorId(), p.getProfessorName());
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

    public Integer getSchoolId(String schoolName) {
        //NOTE: ONLY OBJECTS CAN BE NULL NOT PRIMITIVES
        return schoolMap.get(schoolName); //RETURNS NULL IF NO MATCH FOUND
    }

    public String getSchoolName(Integer schoolId) { return schoolNameMap.get(schoolId); }

    public Integer getSubjectId(String subjectName) { return subjectMap.get(subjectName); }

    public Integer getClassId(String className) { return classMap.get(className); }

    public String getClassName(Integer classId) { return classNameMap.get(classId); }

    public Integer getProfessorId(String profName) {
        return professorMap.get(profName);
    }

    public String getProfessorName(Integer profId) { return professorNameMap.get(profId); }

    public ArrayList<SchoolBundle> getSchoolCache() { return schoolCache; }

    public ArrayList<SubjectBundle> getSubjectCache() { return subjectCache; }

    public ArrayList<ClassBundle> getClassCache() {
        return classCache;
    }

    public ArrayList<ProfessorBundle> getProfessorCache() {
        return professorCache;
    }
}