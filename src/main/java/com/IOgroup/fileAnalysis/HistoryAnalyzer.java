package com.IOgroup.fileAnalysis;

import com.IOgroup.model.FileDetails;
import com.IOgroup.model.MethodDetails;
import com.IOgroup.model.PackageDetails;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class HistoryAnalyzer {

    //Serialuj liste obiektow klasy MethodDetails, PackageDetails i FileDetails
    public static void serializeLogic(List<MethodDetails> methodDetailsList)
    {
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;
        try
        {
            fileOutputStream = new FileOutputStream("logicFile.txt");
            objectOutputStream= new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(methodDetailsList);
            fileOutputStream.close();
            objectOutputStream.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public static void serializePackage(List<PackageDetails> packageDetailsList)
    {
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;
        try
        {
            fileOutputStream = new FileOutputStream("packageFile.txt");
            objectOutputStream= new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(packageDetailsList);
            fileOutputStream.close();
            objectOutputStream.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public static void serializeFiles(List<FileDetails> fileDetailsList)
    {
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;
        try
        {
            fileOutputStream = new FileOutputStream("fileFile.txt");
            objectOutputStream= new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(fileDetailsList);
            fileOutputStream.close();
            objectOutputStream.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    //Deserializuj pliki zeby otrzymac liste obiektow MethodDetails, PackageDetails i FileDetails
    public static List<MethodDetails> deserializeLogic()
    {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        List<MethodDetails> methodDetailsList=new LinkedList<>();
        try
        {
            // Reading the object from a file
            fileInputStream = new FileInputStream("logicFile.txt");
            objectInputStream = new ObjectInputStream(fileInputStream);

            // Method for deserialization of object
            methodDetailsList=(List<MethodDetails>)objectInputStream.readObject();

            fileInputStream.close();
            objectInputStream.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return methodDetailsList;
    }
    public static List<PackageDetails> deserializePackage()
    {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        List<PackageDetails> packageDetailsList=new LinkedList<>();
        try
        {
            // Reading the object from a file
            fileInputStream = new FileInputStream("packageFile.txt");
            objectInputStream = new ObjectInputStream(fileInputStream);

            // Method for deserialization of object
            packageDetailsList=(List<PackageDetails>)objectInputStream.readObject();

            fileInputStream.close();
            objectInputStream.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return packageDetailsList;
    }
    public static List<FileDetails> deserializeFiles()
    {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        List<FileDetails> fileDetailsList=new LinkedList<>();
        try
        {
            // Reading the object from a file
            fileInputStream = new FileInputStream("fileFile.txt");
            objectInputStream = new ObjectInputStream(fileInputStream);

            // Method for deserialization of object
            fileDetailsList=(List<FileDetails>)objectInputStream.readObject();

            fileInputStream.close();
            objectInputStream.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return fileDetailsList;
    }


    //Wyznacz roznice miedzy tymi obiektami dzieki lambdom jako filtry oraz @override metod equals() i hashCode()
    public static List<MethodDetails> logicDifferences(List<MethodDetails> actualList, List<MethodDetails> historyList)
    {
        return actualList.stream().filter(i->!historyList.contains(i)).collect(toList());
    }
    public static List<PackageDetails> packageDifferences(List<PackageDetails> actualList, List<PackageDetails> historyList)
    {
        return actualList.stream().filter(i->!historyList.contains(i)).collect(toList());
    }
    public static List<FileDetails> fileDifferences(List<FileDetails> actualList, List<FileDetails> historyList)
    {
        return actualList.stream().filter(i->!historyList.contains(i)).collect(toList());
    }

    //Wypisz na ekran roznice
    public static void printDifferences(List<MethodDetails> md, List<FileDetails> fd, List<PackageDetails> pd)
    {
        if(md.size()!=0)
        {
            System.out.println("\nNew methods:");
            for (MethodDetails unit : md) {
                System.out.println(unit.getMethodName()+" from "+unit.getPackageName()+ " package");
            }
            System.out.println("=============");
        }

        if(fd.size()!=0)
        {
            System.out.println("\nNew files:");
            for (FileDetails unit : fd) {
                System.out.println(unit.getName()+" with "+unit.getWeight()+" weight");
            }
            System.out.println("=============");
        }

        if(pd.size()!=0)
        {
            System.out.println("\nNew packages:");
            for (PackageDetails unit : pd) {
                System.out.println(unit.getPackageName()+" on path: "+unit.getPathToPackage());
            }
            System.out.println("=============");
        }


    }

}
