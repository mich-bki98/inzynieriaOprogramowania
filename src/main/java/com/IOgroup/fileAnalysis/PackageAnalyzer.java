package com.IOgroup.fileAnalysis;

import com.IOgroup.model.FileDetails;
import com.IOgroup.model.MethodDetails;
import com.IOgroup.model.PackageDetails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.IOgroup.Main.fileDetailsList;
import static com.IOgroup.Main.methodDetailsList;

public class PackageAnalyzer {

    private static List<PackageDetails> packageDetailsList = new ArrayList<>();
    private static File file;

    public static List<PackageDetails> getPackageDetailsList() {
        return packageDetailsList;
    }

    public static void findAllPackages() throws IOException {

        File[] directories = new File(System.getProperty("user.dir")).listFiles(file -> file.isDirectory());

        //skanowanie katalogu, wchodzenie do folderu IOgroup
        for (File file2 : directories) {
            if (file2.toString().contains("src")) {
                file = file2;
            }
        }

        Files.walk(file.toPath())
                .filter(Files::isDirectory)
                .forEach((f) -> {
                    String file2 = f.toString();
                    if (file2.endsWith("IOgroup"))
                        file = f.toFile();
                });

        directories = file.listFiles(pathname -> Files.isDirectory(pathname.toPath()));


        String[] packageNames = new String[directories.length];
        Path[] paths = new Path[directories.length];


        for (int i = 0; i < directories.length; i++) {
            packageNames[i] = directories[i].toString().substring(directories[i].toString().lastIndexOf("\\") + 1);
            paths[i] = directories[i].toPath();


            PackageDetails packageDetails = new PackageDetails(paths[i], packageNames[i]);
            packageDetailsList.add(packageDetails);

        }
        bindClassesToPackages();
        calculatePackagesMethods();
   }

    private static void bindClassesToPackages() throws IOException {

        for (int i = 0; i < packageDetailsList.size(); i++) {
            Path tmp = packageDetailsList.get(i).getPathToPackage();

            Stream<Path> path = Files.walk(tmp);
            List<Path> list = path
                    .filter(files -> files.toString().contains(".java"))
                    .collect(Collectors.toList());

            String name;
            for (Path paths : list) {
                name = paths.getFileName().toString();
                String[] str = name.split("\\.", 2);
                for (FileDetails fileDetails : fileDetailsList) {
                    if (fileDetails.getName().equals(str[0]))
                        packageDetailsList.get(i).addClass(fileDetails);
                }

            }
        }
        for (int i = 0; i < packageDetailsList.size(); i++) {
            for (int j = 0; j < packageDetailsList.size(); j++) {
                if (!packageDetailsList.get(i).getPackageName().toLowerCase().equals(packageDetailsList.get(j).getPackageName().toLowerCase()))
                    packageDetailsList.get(i).getPackageDependencies().put(packageDetailsList.get(j).getPackageName(), 0);
            }
        }
        calculateDependencies();
        bindPackageToPackage();
    }

    private static String findPackageByFunctionName(String name) {
        for (MethodDetails methodDetails : methodDetailsList)
            if (name.equals(methodDetails.getMethodName())) return methodDetails.getPackageName();
        return null;
    }

    private static void updatePackageCallCounter(int count, String packageName) {
        for (PackageDetails packageDetails : packageDetailsList) {
            if (packageName.equals(packageDetails.getPackageName())) packageDetails.updateCallCounter(count);
        }
    }

    private static void calculateDependencies() {
        String packageName;

        // iterating for each : each method details

        for (MethodDetails methodDetails : methodDetailsList) {

            //Setting temporary var to specific package

            int packageCallCounter = 0;
            HashMap<String, HashMap<String, Integer>> methodDependencies = new HashMap<>();
            packageName = methodDetails.getPackageName();
            PackageDetails packageDetailsTemp = new PackageDetails();


            for (PackageDetails packageDetails : packageDetailsList) {
                if (packageDetails.getPackageName().equals(packageName)) packageDetailsTemp = packageDetails;
            }


            Iterator it;
            it = methodDetails.getMethodDependencies().entrySet().iterator();

            HashMap<String, Integer> tmpHashMap = new HashMap<>();
            while (it.hasNext()) {

                //Calculating and updating method dependencies
                //Dependencies set
                Map.Entry pair = (Map.Entry) it.next();
                if (!findPackageByFunctionName(pair.getKey().toString()).equals(packageDetailsTemp.getPackageName())) {
                    tmpHashMap.put(findPackageByFunctionName(pair.getKey().toString()), Integer.parseInt(pair.getValue().toString()));
                    methodDependencies.put(methodDetails.getMethodName(), tmpHashMap);
                }
            }
            //Setting variables to data structure
            packageDetailsTemp.setMethodDependencies(methodDependencies);
            packageDetailsTemp.updateCallCounter(packageCallCounter);
        }
    }

    private static void incrementHashMapValueByKey(String key, int val, HashMap<String, Integer> hashMap) {
        if (!hashMap.containsKey(key)) hashMap.put(key, val);
        else {
            hashMap.put(key, hashMap.get(key) + val);
        }
    }

    private static void calculatePackagesMethods(){
        HashMap<String,Integer> tmpHash = new HashMap<>();
        PackageDetails packageDetailsTmp = new PackageDetails();
        for(MethodDetails methodDetails : methodDetailsList){
            for(PackageDetails packageDetails : packageDetailsList){
                if(packageDetails.getPackageName().equals(methodDetails.getPackageName())) packageDetailsTmp=packageDetails;
            }
            incrementHashMapValueByKey(methodDetails.getMethodName(),
                    methodDetails.getCallCounter(),
                    packageDetailsTmp.getThisMethods());
                    packageDetailsTmp.updateCallCounter(methodDetails.getCallCounter());
        }
    }

    private static void bindPackageToPackage() {
        for (PackageDetails packageDetails : packageDetailsList) {
            HashMap<String, Integer> tmpHash = new HashMap<>();
            HashMap<String, Integer> tmpHash2 = new HashMap<>();
            Iterator it;
            it = packageDetails.getMethodDepedencies().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                Iterator interIt;
                HashMap<String, Integer> internalMap = (HashMap<String, Integer>) pair.getValue();
                interIt = internalMap.entrySet().iterator();
                while (interIt.hasNext()) {
                    Map.Entry pairInt = (Map.Entry) interIt.next();
                    if (pairInt.getKey().toString().equals(packageDetails.getPackageName())) continue;
                    incrementHashMapValueByKey(pairInt.getKey().toString(), Integer.parseInt(pairInt.getValue().toString()), tmpHash);
                    incrementHashMapValueByKey(pair.getKey().toString(),Integer.parseInt(pairInt.getValue().toString()),tmpHash2);
                }
            }
            packageDetails.setPackageDependencies(tmpHash);
            packageDetails.setMethodToThisPackage(tmpHash2);
        }
    }

}


