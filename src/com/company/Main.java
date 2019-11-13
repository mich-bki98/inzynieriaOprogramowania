package com.company;

import com.company.FileAnalysis.FileAnalyzer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Path> files;
        files = FileAnalyzer.getFilesList();
    }
}
