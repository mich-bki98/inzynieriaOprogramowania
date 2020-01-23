package com.IOgroup.model;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PackageDetails implements Serializable {

    public PackageDetails() {
    }

    private String packageName;
    private int callCounter=0;
    private HashMap<String, Integer> packageDependencies=new HashMap<>();
    private HashMap<String, HashMap<String,Integer>> methodDependencies= new HashMap<>();
    private List<FileDetails> classes=new ArrayList<>();
    private Path pathToPackage;

    public HashMap<String, HashMap<String,Integer>> getMethodDepedencies() {
        return methodDependencies;
    }


    public void setMethodDependencies(HashMap<String,HashMap<String,Integer>> methodDepedencies) {
        this.methodDependencies.putAll(methodDepedencies);
    }

    public PackageDetails(Path pathToPackage, String packageName) {
        this.pathToPackage = pathToPackage;
        this.packageName = packageName;
    }

    public void addClass(FileDetails fileDetails){
        this.classes.add(fileDetails);
    }

    public String getPackageName() {
        return packageName;
    }

    public HashMap<String, Integer> getPackageDependencies() {
        return packageDependencies;
    }

    public void setPackageDependencies(HashMap<String, Integer> packageDependencies) {
        this.packageDependencies = packageDependencies;
    }

    public Path getPathToPackage() {
        return pathToPackage;
    }

    public void updateCallCounter(int packageCallCounter) {
        this.callCounter+=packageCallCounter;
    }

    //Przy sprawdzaniu obiektow klasy PackageDetails porownujemy jedynie packageName i pathToPackage
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageDetails that = (PackageDetails) o;
        return Objects.equals(packageName, that.packageName) &&
                Objects.equals(pathToPackage, that.pathToPackage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageName, pathToPackage);
    }
}