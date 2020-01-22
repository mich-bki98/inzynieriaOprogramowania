package com.IOgroup.fileAnalysis;

import com.IOgroup.model.FileDetails;
import com.IOgroup.model.MethodDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.IOgroup.fileAnalysis.FileAnalyzer.filesList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;
import sun.tools.jar.CommandLine;

public class LogicAnalyzer {

    public static List<MethodDetails> findAllMethods(String projectDirectoryPath) {

        //Ustawienie sourceRoot na katalog glowny
        SourceRoot sourceRoot = new SourceRoot(Paths.get(projectDirectoryPath));

        //Model list
        List<MethodDetails> methodDetailsList = new LinkedList<>();

        //JavaParser
        List<MethodDeclaration> methodDeclarationList = new LinkedList<>();
        List<ParseResult<CompilationUnit>> compilationUnitResultList;
        List<Optional<CompilationUnit>> compilationUnitList;

        //Ile razy wywolala sie metoda
        int count;


        //Parsing the package name, temporary var for package name.


        String packageName;
        String[] temporary;
        String className;


        try {
            compilationUnitResultList = sourceRoot.tryToParse("");
            compilationUnitList = compilationUnitResultList.stream().map(ParseResult::getResult).collect(Collectors.toList());
        } catch (IOException ex) {
            //Blad przy otwieraniu plikow i parsowaniu ich
            ex.printStackTrace();
            return null;
        }

        //Na początku stworzenie listy z deklaracjami metod jako CompilationUnit oraz listy MethodDetails z wpisanymi jedynie nazwami metod.


        //Caly compilationUnit (cos na wzor klasy z całym opisem itd)
        for (Optional<CompilationUnit> compilationUnit : compilationUnitList) {
            //Znajdz w tych klasach wszystkie deklaracje funkcji (rowniez z calym opisem i zmiennymi)
            for (TypeDeclaration typeDeclaration : compilationUnit.get().getTypes()) {
                //Sprawdzaj dla kazdej czy...
                for (int i = 0; i < typeDeclaration.getMembers().size(); i++) {
                    //... jest deklaracja metody
                    if (typeDeclaration.getMember(i).isMethodDeclaration()) {


                         //if its method get which package is it in.

                        packageName = compilationUnit.get().getPackageDeclaration().toString();
                        temporary = packageName.split("\\.");
                        packageName = temporary[temporary.length - 1];
                        packageName = packageName.split(";")[0];
                        className = compilationUnit.get().getStorage().get().getFileName().split("\\.")[0];



                        //Zapisz do listy deklaracji i do listy MethodDetails
                        methodDeclarationList.add((MethodDeclaration) typeDeclaration.getMember(i));
                        methodDetailsList.add(new MethodDetails(((MethodDeclaration) typeDeclaration.getMember(i)).getNameAsString(), packageName,className));
                    }
                }
            }
        }

        //Iteracja po wszystkich elementach obydwu list- deklaracji i MethodDetails. Zliczanie ile zadana funkcja wywolala inna funkcje.
        for (MethodDetails methodDetails : methodDetailsList) {
            for (MethodDeclaration methodDeclaration : methodDeclarationList) {
                //Znajdz wszystkie wywolania metody przekazanej przez lambde
                count = methodDeclaration.findAll(MethodCallExpr.class, function -> function.getName().asString().equals(methodDetails.getMethodName())).size();

                if (count != 0) {
                    MethodDetails md = findMethodDetailsByMethodName(methodDetailsList, methodDeclaration.getNameAsString());
                    md.getMethodDependencies().put(methodDetails.getMethodName(), count);
                    md = findMethodDetailsByMethodName(methodDetailsList, methodDetails.getMethodName());
                    md.setCallCounter(md.getCallCounter() + count);
                }

            }

        }

        return methodDetailsList;
    }

    private static MethodDetails findMethodDetailsByMethodName(List<MethodDetails> methodDetailsList, String methodName) {
        for (MethodDetails value : methodDetailsList) {
            if (value.getMethodName().equals(methodName)) {
                return value;
            }
        }
        System.err.println("Nie znaleziono metody z taka nazwa");
        return null;
    }

}
