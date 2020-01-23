package com.IOgroup;

import com.IOgroup.fileAnalysis.FileAnalyzer;
import com.IOgroup.fileAnalysis.HistoryAnalyzer;
import com.IOgroup.fileAnalysis.LogicAnalyzer;
import com.IOgroup.fileAnalysis.PackageAnalyzer;
import com.IOgroup.graphs.Graphs;
import com.IOgroup.model.FileDetails;
import com.IOgroup.model.MethodDetails;
import com.IOgroup.model.PackageDetails;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static List<FileDetails> fileDetailsList;
    public static List<MethodDetails> methodDetailsList;

    private static void generateGraphs(List<FileDetails> filesList,
                                       List<MethodDetails> methodList) throws IOException {

        Graphs graph = new Graphs();
        graph.createClassRelationGraph(filesList);
        graph.createMethodRelationGraph(methodList);
    }

    public static void main(String[] args) throws IOException{

        //Deserialize
        List<MethodDetails> logicHistoryList= HistoryAnalyzer.deserializeLogic();
        List<FileDetails> fileHistoryList= HistoryAnalyzer.deserializeFiles();
        List<PackageDetails> packageHistoryList= HistoryAnalyzer.deserializePackage();


        //To trzeba zainicjowac bo inaczej nie bedzie dzialalo zwracanie roznic
        List<PackageDetails> packageDetailsList= new LinkedList<>();


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

        //List<MethodDetails> methodDetailsList = new ArrayList<>();
        //methodDetailsList = LogicAnalyzer.getMethodList(fileDetailsList);
        generateGraphs(fileDetailsList, methodDetails);
        PackageAnalyzer.findAllPackages();


        //Serialize actual state
        HistoryAnalyzer.serializeLogic(methodDetails);
        HistoryAnalyzer.serializeFiles(fileDetailsList);
        HistoryAnalyzer.serializePackage(packageDetailsList);

        //Sprawdzanie ktorym obiektem sie roznia
        List<MethodDetails> logicDifferences= HistoryAnalyzer.logicDifferences(methodDetails,logicHistoryList);
        List<FileDetails> fileDifferences= HistoryAnalyzer.fileDifferences(fileDetailsList,fileHistoryList);
        List<PackageDetails> packageDifferences= HistoryAnalyzer.packageDifferences(packageDetailsList,packageHistoryList);

        //Wyswietlanie roznic na ekran
        HistoryAnalyzer.printDifferences(logicDifferences,fileDifferences,packageDifferences);

        generateGraphs(fileDetailsList, methodDetails);
    }
}
