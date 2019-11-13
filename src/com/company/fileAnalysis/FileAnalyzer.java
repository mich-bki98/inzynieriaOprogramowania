package com.company.fileAnalysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class FileAnalyzer {
    public static List<Path> getFilesList() {
        try {
            Stream<Path> path = Files.walk(Paths.get(System.getProperty("user.dir")));
            List<Path> list = path
                    .filter(files -> files.toString().contains(".java"))
                    .collect(Collectors.toList());
            return list;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

public