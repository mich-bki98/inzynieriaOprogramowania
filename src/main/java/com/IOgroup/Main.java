package com.IOgroup;

import com.IOgroup.fileAnalysis.FileAnalyzer;
import com.IOgroup.fileAnalysis.LogicAnalyzer;
import com.IOgroup.fileAnalysis.PackageAnalyzer;
import com.IOgroup.graphs.Graphs;
import com.IOgroup.model.FileDetails;
import com.IOgroup.model.MethodDetails;
import com.IOgroup.model.PackageDetails;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static List<FileDetails> fileDetailsList;
    public static List<MethodDetails> methodDetailsList;

    private static void generateGraphs(List<FileDetails> filesList,
                                       List<MethodDetails> methodList) throws IOException {
        //Tutaj będą wywołania funkcji rysowania grafów, na razie jest historyjka 1 więc tylko jeden graf
        // juz kurwa nie

        Graphs graph = new Graphs();
        graph.createClassRelationGraph(filesList);
        graph.createMethodRelationGraph(methodList);
    }

    public static void main(String[] args) throws IOException{


        List<Path> files;
        files = FileAnalyzer.getFilesList();

        if(files == null){
            throw new IOException();
        }

        FileAnalyzer.getFileNames(files);

        fileDetailsList = new ArrayList<>();

        try {
            fileDetailsList = FileAnalyzer.analyzeList(files);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileAnalyzer.analyzeDependencies(fileDetailsList);

        List<MethodDetails> methodDetails= LogicAnalyzer.findAllMethods(System.getProperty("user.dir"));
        methodDetailsList=methodDetails;



       // for(MethodDetails unit: methodDetails)
       // {
        //    System.out.println(unit);
       // }

        //List<MethodDetails> methodDetailsList = new ArrayList<>();
        //methodDetailsList = LogicAnalyzer.getMethodList(fileDetailsList);
        generateGraphs(fileDetailsList, methodDetails);
        PackageAnalyzer.findAllPackages();








        generateGraphs(fileDetailsList, methodDetails);
    }
}
