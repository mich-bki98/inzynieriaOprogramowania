package com.company;

import com.company.fileAnalysis.FileAnalyzer;

import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Path> files;
        files = FileAnalyzer.getFilesList();
    }
}
