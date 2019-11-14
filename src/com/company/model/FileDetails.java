package com.company.model;

import java.util.HashMap;

public class FileDetails {
    private String name;
    private String content;
    private double weight;
    private HashMap<String,Integer> dependencyMap;

    public void setDependencyMap(HashMap<String, Integer> dependencyMap) {
        this.dependencyMap = dependencyMap;
    }

    public String getContent() {
        return content;
    }

    public FileDetails(String name, double weight, String content) {
        this.name = name;
        this.weight = weight;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public HashMap<String, Integer> getDependencyMap() {
        return dependencyMap;
    }

}
