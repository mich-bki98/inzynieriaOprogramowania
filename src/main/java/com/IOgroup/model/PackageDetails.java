package com.IOgroup.model;

import java.util.HashMap;

public class PackageDetails {
    private String packageName;
    private int callCounter;
    private HashMap<String, Integer> packageDependencies;

    public PackageDetails() {
    }

    public PackageDetails(String packageName, int callCounter, HashMap<String, Integer> packageDependencies) {
        this.packageName = packageName;
        this.callCounter = callCounter;
        this.packageDependencies = packageDependencies;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getCallCounter() {
        return callCounter;
    }

    public void setCallCounter(int callCounter) {
        this.callCounter = callCounter;
    }

    public HashMap<String, Integer> getPackageDependencies() {
        return packageDependencies;
    }

    public void setPackageDependencies(HashMap<String, Integer> packageDependencies) {
        this.packageDependencies = packageDependencies;
    }
}
