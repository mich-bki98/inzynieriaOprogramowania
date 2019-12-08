package com.IOgroup.model;

import java.util.HashMap;
import java.util.Map;

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

    @Override
    public String toString() {

        String temp="Hello! My name is: "+this.methodName+"\n";
        temp+="I've been called "+ this.callCounter+ " times\n";

        if(this.methodDependencies.isEmpty())
        {
            temp+="\n--------------------\n";
            return  temp;
        }
        temp+="I've called these functions myself:\n";
        for (Map.Entry<String, Integer> entry : this.methodDependencies.entrySet()) {
            String k = entry.getKey();
            Integer v = entry.getValue();
            temp+="Function: " + k +",\t\t" + v +" times\n";
        }
        temp+="\n--------------------\n";
        return temp;
    }

}
