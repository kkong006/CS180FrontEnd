package teamawesome.cs180frontend.Misc;

import java.util.ArrayList;
import java.util.List;

import teamawesome.cs180frontend.API.Models.ClassBundle;
import teamawesome.cs180frontend.API.Models.Professor;
import teamawesome.cs180frontend.API.Models.School;

/**
 * Created by jman0_000 on 10/27/2016.
 */

public class DataSingleton {
    private static DataSingleton instance = null;
    private List<ClassBundle> classCache;
    private List<Professor> professorCache;
    private boolean hasLoaded; //temporary measure

    public static DataSingleton getInstance() {
        if (instance == null) {
            instance = new DataSingleton();
        }

        return instance;
    }

    private DataSingleton() {
        classCache = new ArrayList<>();
        professorCache = new ArrayList<>();
    }

    public List<ClassBundle> cacheClasses(List<ClassBundle> classes) {
        this.classCache.clear();
        this.classCache.addAll(classes);
        hasLoaded = false;
        return this.classCache;
    }

    public List<Professor> cacheProfessors(List<Professor> profs) {
        this.professorCache.clear();
        this.professorCache.addAll(profs);
        return this.professorCache;
    }

    public List<ClassBundle> getClassCache() {
        return classCache;
    }

    public List<Professor> getProfessorCache() {
        return professorCache;
    }
}
