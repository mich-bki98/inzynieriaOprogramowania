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

public class LogicAnalyzer {

    public static List<MethodDetails> findAllMethods(String projectDirectoryPath)
    {
        //Ustawienie sourceRoot na katalog glowny
        SourceRoot sourceRoot= new SourceRoot(Paths.get(projectDirectoryPath));

        //Model list
        List <MethodDetails> methodDetailsList= new LinkedList<>();

        //JavaParser
        List <MethodDeclaration> methodDeclarationList= new LinkedList<>();
        List <BodyDeclaration> bodyDeclarationList= new LinkedList<>();
        List<ParseResult<CompilationUnit>> compilationUnitResultList= new LinkedList<>();
        List<Optional<CompilationUnit>> compilationUnitList= new LinkedList<>();

        //Ile razy wywolala sie metoda
        int count;

        try{
            compilationUnitResultList= sourceRoot.tryToParse("");
            compilationUnitList= compilationUnitResultList.stream().map(ParseResult::getResult).collect(Collectors.toList());
        }
        catch (IOException ex)
        {
            //Blad przy otwieraniu plikow i parsowaniu ich
            ex.printStackTrace();
            return null;
        }


        //Na początku stworzenie listy z deklaracjami metod jako CompilationUnit oraz listy MethodDetails z wpisanymi jedynie nazwami metod.

        //Caly compilationUnit (cos na wzor klasy z całym opisem itd)
        for(Optional<CompilationUnit> compilationUnit: compilationUnitList)
        {
            if(compilationUnit.isPresent()==false)
            {
                //Pozbywanie się pustych obiektow, zeby nie otrzymac NPE
                continue;
            }
            //Znajdz w tych klasach wszystkie deklaracje funkcji (rowniez z calym opisem i zmiennymi)
            for (TypeDeclaration typeDeclaration: compilationUnit.get().getTypes())
            {
                //Sprawdzaj dla kazdej czy...
                for(int i=0;i<typeDeclaration.getMembers().size();i++)
                {
                    //... jest deklaracja metody
                    if(typeDeclaration.getMember(i).isMethodDeclaration())
                    {
                        //Zapisz do listy deklaracji i do listy MethodDetails
                        //Konstruktor zapisuje rowniez w jakim pliku aktualnie jestesmy
                        methodDeclarationList.add((MethodDeclaration)typeDeclaration.getMember(i));
                        methodDetailsList.add(new MethodDetails(((MethodDeclaration) typeDeclaration.getMember(i)).getNameAsString(),typeDeclaration.getNameAsString()));
                    }
                }
            }
        }

        //Iteracja po wszystkich elementach obydwu list- deklaracji i MethodDetails. Zliczanie ile zadana funkcja wywolala inna funkcje.
        for(MethodDetails methodDetails: methodDetailsList)
        {
            for(MethodDeclaration methodDeclaration: methodDeclarationList)
            {
                //Znajdz wszystkie wywolania metody przekazanej przez lambde jako filtr
                count=methodDeclaration.findAll(MethodCallExpr.class, function-> function.getName().asString().equals(methodDetails.getMethodName())).size();

                //Jezeli rozne od 0 czyli ta metoda wywolala chociaz jedna inna metode to...
                if(count!=0)
                {
                    //Znajdz na liscie te metode, zeby ustawic jej parametry
                    MethodDetails md=findMethodDetailsByMethodName(methodDetailsList,methodDeclaration.getNameAsString());
                    //Wpisz do hashmapy nazwe metody wywolywanej przez nia oraz ilosc wywolan
                    md.getMethodDependencies().put(methodDetails.getMethodName(),count);
                    //Znajdz te metode, ktora zostala wywolana i zwieksz jej ilosc wywolan o tyle ile razy zostala wywolana w innej metodzie
                    md=findMethodDetailsByMethodName(methodDetailsList,methodDetails.getMethodName());
                    md.setCallCounter(md.getCallCounter()+count);
                }

            }

        }

        return methodDetailsList;
    }

    private static MethodDetails findMethodDetailsByMethodName(List<MethodDetails> methodDetailsList, String methodName)
    {
        for(MethodDetails value: methodDetailsList)
        {
            if(value.getMethodName().equals(methodName))
            {
                return value;
            }
        }
        System.err.println("Nie znaleziono metody z taka nazwa");
        return null;
    }

}
