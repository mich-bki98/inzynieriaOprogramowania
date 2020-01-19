package com.IOgroup.fileAnalysis;

import com.IOgroup.model.FileDetails;

import java.io.File;
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

    /**
     * przechodzenie katalogu roboczego w poszukiwaniu plików klas
     **/

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

    /**
     * dodawanie sformatowanej nazwy pliku do listy nazw
     */

    public static void getFileNames(List<Path> list) {
        String name;
        for (Path path : list) {
            name = path.getFileName().toString();
            String[] str = name.split(File.separator + File.separator+".", 2);
            filesList.add(str[0]);
        }
    }

    /**
     * tworzenie listy plików fileDetails i uzupełnianie jej przetworzonymi informacjami
     */

    public static List<FileDetails> analyzeList(List<Path> pathList) throws IOException {

        List<FileDetails> analyzedList = new ArrayList<>();
        String[] str;
        String name, content;
        Long size;

        for (Path path : pathList) {
            name = path.getFileName().toString();
            str = name.split(File.separator + File.separator+".", 2);
            size = Files.size(path);
            content = Files.readString(path);
            FileDetails fileDetails = new FileDetails(str[0], size, content);
            analyzedList.add(fileDetails);
        }
        return analyzedList;
    }

    /**
     * sprawdzanie pola content z każdego obiektu w celu znalezienia odniesien do pozostałych plików
     */

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
            fileDetailsList.get(i).setClassDependencyMap(dependenciesMap);
        }
    }
}

