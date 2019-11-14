package com.company.fileAnalysis;

import com.company.model.FileDetails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
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
            String[] str = name.split("\\.", 2);
            filesList.add(str[0]);
        }
    }

    public static List<FileDetails> analyzeList(List<Path> pathList) throws IOException {
        List<FileDetails> analyzedList = new ArrayList<>();
        for (int i = 0; i < pathList.size(); i++) {
            String name = pathList.get(i).getFileName().toString();
            String[] str = name.split("\\.", 2);
            Long size = Files.size(pathList.get(i));
            Files.readString(pathList.get(i));
            String content = Files.readString(pathList.get(i));
            FileDetails fileDetails = new FileDetails(str[0], size, content);
            analyzedList.add(fileDetails);
        }
        return analyzedList;
    }

    public static void analyzeDependencies(List<FileDetails> fileDetailsList) {

        for (int i = 0; i < fileDetailsList.size(); i++) {
            HashMap<String, Integer> dependenciesMap = new HashMap<>();

            for (int j = 0; j < filesList.size(); j++) {
                String lookedFor = filesList.get(j);
                if (lookedFor.equals(fileDetailsList.get(i).getName())) continue;

                int k = 0;
                String tmp;
                StringTokenizer stringTokenizer = new StringTokenizer(fileDetailsList.get(i).getContent(), ". ;\r\n<>()");
                tmp = stringTokenizer.nextToken();

                while (stringTokenizer.hasMoreElements() && tmp != fileDetailsList.get(i).getName()) {
                    if (tmp.equals(lookedFor)) {
                        k++;
                    }
                    tmp = stringTokenizer.nextToken();
                }
                if (k != 0) {
                    dependenciesMap.put(lookedFor, k);
                }
            }
            fileDetailsList.get(i).setDependencyMap(dependenciesMap);
        }
    }
}

