package com.IOgroup.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MethodDetails implements Serializable {
    private String methodName;
    private int callCounter;
    private HashMap<String, Integer> methodDependencies;
    private String packageName;
    private String className;
    private String fileName;

    public int getCallCounter() {
        return callCounter;
    }

    public void setCallCounter(int callCounter) {
        this.callCounter = callCounter;
    }


    public MethodDetails(String methodName,String packageName,String className) {
        this.methodName = methodName;
        this.callCounter = 0;
        this.methodDependencies = new HashMap<>();
        this.packageName = packageName;
        this.className = className;
    }

    public MethodDetails(String methodName,String packageName,String className, String fileName) {
        this.methodName = methodName;
        this.callCounter = 0;
        this.methodDependencies = new HashMap<>();
        this.packageName = packageName;
        this.className = className;
        this.fileName= fileName;
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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {

        String temp="Hello! My name is: "+this.methodName+"\n";
        temp+="I've been called "+ this.callCounter+ " times\n";
        temp+="I'm located in "+this.fileName+" file\n";
        if(this.methodDependencies.isEmpty())
        {
            temp+="\n--------------------\n";
            return  temp;
        }
        temp += "I've called these functions myself:\n";
        for (Map.Entry<String, Integer> entry : this.methodDependencies.entrySet()) {
            String k = entry.getKey();
            Integer v = entry.getValue();
            temp += "Function: " + k + ",\t\t" + v + " times\n";
        }
        temp += "\n--------------------\n";
        return temp;
    }


    //Przy sprawdzaniu obiektow klasy MethodDetails porownujemy jedynie methodName i packageName
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodDetails that = (MethodDetails) o;
        return Objects.equals(methodName, that.methodName) &&
                Objects.equals(packageName, that.packageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, packageName);
    }
}
