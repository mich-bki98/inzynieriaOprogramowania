package com.company;

import com.company.fileAnalysis.FileAnalyzer;
import com.company.model.FileDetails;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
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

        System.out.println("xxxx");
    }
}
