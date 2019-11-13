package com.company.fileAnalysis;

import com.company.model.FileDetails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileAnalyzer {
    public static List<String> filesList = new ArrayList<>();

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

    public static void getFileNames(List<Path> list) {
        for (int i = 0; i < list.size(); i++) {
            String name;
            name = list.get(i).getFileName().toString();
            filesList.add(name);
        }
    }

    public static HashMap<String,Integer> getConnection(String name, List<Path> pathList) throws IOException {
        for (int i = 0; i < pathList.size(); i++) {
            String content = Files.readString(pathList.get(i));

        }
    }

    public static List<FileDetails> analyzeList(List<Path> pathList) throws IOException {
        for (int i = 0; i < pathList.size(); i++) {
            String name = pathList.get(i).getFileName().toString();
            Long size = Files.size(pathList.get(i));
            Files.readString(pathList.get(i));
            String content = Files.readString(pathList.get(i));


            FileDetails fileDetails = new FileDetails(name, size, );
        }

    }

}
