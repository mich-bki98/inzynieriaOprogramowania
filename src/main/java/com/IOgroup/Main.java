package com.IOgroup;


import com.IOgroup.fileAnalysis.FileAnalyzer;
import com.IOgroup.graphs.ClassRelationGraph;
import com.IOgroup.model.FileDetails;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static void generateGraphs(List<FileDetails> filesList) throws IOException { //Tutaj będą wywołania funkcji rysowania grafów, na razie jest historyjka 1 więc tylko jeden graf

        ClassRelationGraph crelGraph = new ClassRelationGraph();
        crelGraph.createClassRelationGraph(filesList);
    }

    public static void main(String[] args) throws IOException {
        List<Path> files;
        files = FileAnalyzer.getFilesList();
        FileAnalyzer.getFileNames(files);

        List<FileDetails> filesList = new ArrayList<>();
        try {
            filesList = FileAnalyzer.analyzeList(files);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileAnalyzer.analyzeDependencies(filesList);
        generateGraphs(filesList);

    }
}
