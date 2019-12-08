package com.IOgroup;

import com.IOgroup.exceptions.NoFilesException;
import com.IOgroup.fileAnalysis.FileAnalyzer;
import com.IOgroup.fileAnalysis.LogicAnalyzer;
import com.IOgroup.graphs.Graphs;
import com.IOgroup.model.FileDetails;
import com.IOgroup.model.MethodDetails;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static void generateGraphs(List<FileDetails> filesList,
                                       List<MethodDetails> methodList) throws IOException { //Tutaj będą wywołania funkcji rysowania grafów, na razie jest historyjka 1 więc tylko jeden graf

        Graphs graph = new Graphs();
        graph.createClassRelationGraph(filesList);
        graph.createMethodRelationGraph(methodList);
    }

    public static void main(String[] args) throws IOException, NoFilesException {


        List<Path> files;
        files = FileAnalyzer.getFilesList();

        if(files == null){
            throw new NoFilesException("No valid files found in working direction!" );
        }

        FileAnalyzer.getFileNames(files);

        List<FileDetails> fileDetailsList = new ArrayList<>();

        try {
            fileDetailsList = FileAnalyzer.analyzeList(files);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileAnalyzer.analyzeDependencies(fileDetailsList);



        List<MethodDetails> methodDetails= LogicAnalyzer.findAllMethods(System.getProperty("user.dir"));

       // for(MethodDetails unit: methodDetails)
       // {
        //    System.out.println(unit);
       // }

        //List<MethodDetails> methodDetailsList = new ArrayList<>();
        //methodDetailsList = LogicAnalyzer.getMethodList(fileDetailsList);




        generateGraphs(fileDetailsList, methodDetails);
    }
}
