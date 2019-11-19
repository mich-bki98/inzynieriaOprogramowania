package com.IOgroup.model;

import java.util.HashMap;

public class MethodDetails {
    private String methodName;
    private int callCounter;
    private HashMap<String, Integer> methodDependencies;

    public int getCallCounter() {
        return callCounter;
    }

    public void setCallCounter(int callCounter) {
        this.callCounter = callCounter;
    }


    public MethodDetails(String methodName) {
        this.methodName = methodName;
        this.callCounter = 0;
        this.methodDependencies = new HashMap<>();
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public HashMap<String, Integer> getMethodDependencies() {
        return methodDependencies;
    }

    public void setMethodDependencies(HashMap<String, Integer> methodDependencies) {
        this.methodDependencies = methodDependencies;
    }

}
