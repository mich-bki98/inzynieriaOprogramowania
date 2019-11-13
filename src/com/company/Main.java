package com.company;

import com.company.fileAnalysis.FileAnalyzer;
import com.company.model.FileDetails;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Path> files;
        files = FileAnalyzer.getFilesList();

        List<FileDetails> filesList = new ArrayList<>();



    }
}
