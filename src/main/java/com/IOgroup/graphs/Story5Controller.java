package com.IOgroup.graphs;

import com.IOgroup.model.FileDetails;
import com.IOgroup.model.MethodDetails;
import com.IOgroup.model.PackageDetails;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.attribute.Rank.RankDir.TOP_TO_BOTTOM;
import static guru.nidi.graphviz.engine.Graphviz.fromGraph;
import static guru.nidi.graphviz.model.Factory.graph;

public class Story5Controller {

    Boolean files;
    Boolean methods;
    Boolean packages;

    List<FileDetails> filesList;
    List<MethodDetails> methodList;
    List<PackageDetails> packageList;

    public Story5Controller(List<FileDetails> filesList,  List<MethodDetails> methodList, List<PackageDetails> packageList){
        this.filesList = filesList;
        this.methodList = methodList;
        this.packageList = packageList;
        //tu beda package
        files = true;
        methods = true;
        packages = true;
    }

    public void start() throws IOException {

        while(true){

            consolidation(files, methods, packages);
            System.out.println("Zaladowano graf, odswiez plik aby zobaczyc zmiane | false - warstwa wylaczona | true - warstwa wlaczana");
            System.out.println("Warstwa plikow: " + files);
            System.out.println("Warstwa metod: " + methods);
            System.out.println("Warstwa pakietow: " + packages);
            System.out.println("Wpisujac odpowiednia cyfre mozesz wlaczac/wylaczac warstwy:");
            System.out.println("1. Warstwa relacji plikow");
            System.out.println("2. Warstwa relacji metod");
            System.out.println("3. Warstwa relacji pakietow");
            System.out.println("4. Wyjscie ze story 5");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine();




            if(answer.equals("4")) break;
            switch(answer){
                case "1":
                    files = !files;
                    break;
                case "2":
                    methods = !methods;
                    break;
                case "3":
                    packages = !packages;
                    break;
            }
            System.out.println("Ladowanie grafu");
        }
    }

    private void consolidation(Boolean files, Boolean methods, Boolean packages) throws IOException {

        List nodes = new ArrayList(); //tworzymy liste
        if(files == true) nodes = Graphs.addClassRelationGraph(filesList, nodes); //do listy dodajemy lub nie odpowiednie grafy
        if(methods == true) nodes = Graphs.addMethodRelationGraph(methodList,nodes);
        if(packages == true) nodes = Graphs.addPackageRelationGraph(packageList,nodes);

        Graphs.addVersionControlNode(nodes); //do listy dodajemy prostokąd z numerem wersji kodu

        Graph g = graph("consolidation") //tworzenie grafu
                .directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .with(nodes);

        File resultFile = new File("Story5Graph.png");
        resultFile.createNewFile();
        try {
            fromGraph(g).render(Format.PNG).toFile(resultFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
