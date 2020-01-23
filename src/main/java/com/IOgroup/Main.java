package com.IOgroup;

import com.IOgroup.fileAnalysis.FileAnalyzer;
import com.IOgroup.fileAnalysis.LogicAnalyzer;
import com.IOgroup.fileAnalysis.PackageAnalyzer;
import com.IOgroup.graphs.Graphs;
import com.IOgroup.graphs.Story5Controller;
import com.IOgroup.model.FileDetails;
import com.IOgroup.model.MethodDetails;
import com.IOgroup.model.PackageDetails;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static List<FileDetails> fileDetailsList;
    public static List<MethodDetails> methodDetailsList;

    private static void generateGraphs(List<FileDetails> filesList,
                                       List<MethodDetails> methodList,
                                       List<PackageDetails> packageDetails) throws IOException {

        Graphs graph = new Graphs();
        graph.createClassRelationGraph(filesList);
        graph.createMethodRelationGraph(methodList);
        graph.createPackageRelationGraph(packageDetails);
        graph.createFunctionToFileRelationGraph(methodList);
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

        PackageAnalyzer.findAllPackages();
        List<PackageDetails>  packageDetails = PackageAnalyzer.getPackageDetailsList();

        generateGraphs(fileDetailsList, methodDetails, packageDetails);

        System.out.println("Czy chcesz wyświetlić story 5? T/N");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if(answer.equals("T"))
        {
            Story5Controller story5 = new Story5Controller(fileDetailsList, methodDetails, packageDetails);
            story5.start();
        }
    }
}
