package com.company.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FileDetails {
    private String name;
    private double weight;
    private HashMap<String,Integer> dependencyMap;

    public FileDetails(String name, double weight, HashMap<String, Integer> dependencyMap) {
        this.name = name;
        this.weight = weight;
        this.dependencyMap = dependencyMap;
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
