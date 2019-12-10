package com.IOgroup.fileAnalysis;

import com.IOgroup.model.FileDetails;
import com.IOgroup.model.PackageDetails;
import com.sun.tools.javac.main.CommandLine;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PackageAnalyzer {
    List<PackageDetails> packageDetailsList = new ArrayList<>();
    private static File file;


    public static List<PackageDetails> findAllPackages() throws IOException {

        File[] directories = new File(System.getProperty("user.dir")).listFiles(file -> file.isDirectory());

        for (File file2 : directories){
            if(file2.toString().contains("src")){
                file = file2;
            }
        }

        Files.walk(file.toPath())
                .filter(Files::isDirectory)
                .forEach((f)->{
                    String file2 = f.toString();
                    if( file2.endsWith("IOgroup"))
                        file=f.toFile();
                });

        directories = file.listFiles(pathname -> Files.isDirectory(pathname.toPath()));

        String[] packageNames=new String[directories.length];


        for(int i= 0;i<directories.length;i++){
            packageNames[i]=directories[i].toString().substring(directories[i].toString().lastIndexOf("\\")+1);
        }

        return null;
    }
}
