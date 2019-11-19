package com.IOgroup.fileAnalysis;

import com.IOgroup.model.FileDetails;
import com.IOgroup.model.MethodDetails;

import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import static com.IOgroup.fileAnalysis.FileAnalyzer.filesList;

public class LogicAnalyzer {

    public static List<MethodDetails> getMethodList(List<FileDetails> fileDetailsList) {
        /**
         * TODO:
         * funkcja dostaje fileDetailsList
         * przeszukje wszystkie metody i tworzy ich obiekt o polach jak w MethodDetails
         * uzupelniajac pola name oraz counter wywołań
         */
        return null;
    }

    public static void analyzeLogicDependencies(List<FileDetails> fileDetailsList, List<MethodDetails> methodDetailsList) {
        for (MethodDetails methodDetails : methodDetailsList) {
            for (FileDetails fileDetails : fileDetailsList) {
                scanFile(fileDetails,methodDetails);
            }
        }
    }

    private static void scanFile(FileDetails fileDetails, MethodDetails methodDetailsList) {
        /**
         * TODO:
         * ta metoda dostaje fileDetails jako obiekt typu fileDetails
         * fileDetails[x].getContent(); = zwraca zawartosc pliku jako string
         * należy przeskanowac plik tak aby ustawił w HashMapie danego methodDetails zaleznosci między metodami A->B
         * wywoływany przez analyzeLogicDependencies
         */

    }

}
