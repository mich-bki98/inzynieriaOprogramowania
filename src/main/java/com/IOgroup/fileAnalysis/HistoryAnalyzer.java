package com.IOgroup.fileAnalysis;

import com.IOgroup.model.FileDetails;
import com.IOgroup.model.MethodDetails;
import com.IOgroup.model.PackageDetails;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class HistoryAnalyzer {

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
