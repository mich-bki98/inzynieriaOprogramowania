package com.IOgroup.graphs;
import com.IOgroup.model.FileDetails;
import com.IOgroup.model.MethodDetails;
import com.IOgroup.gitVersion.GitVer;
import com.IOgroup.model.PackageDetails;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.engine.Graphviz.fromGraph;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

public class Graphs {  //https://github.com/nidi3/graphviz-java#complex-example

    public void createClassRelationGraph(List<FileDetails> filesList) throws IOException {
        List mainNodes = new ArrayList(); //tutaj tworzy sie cala lista na podstawie ktorej zbudowany zostanie graf
        addClassRelationGraph(filesList,mainNodes); //do listy dodawane sa wezly i polaczenia
        addVersionControlNode(mainNodes);  //do listy dodaje maly kwadracik na grafie z numerem wersji kodu
        Graph g = graph("ClassRelationGraph").directed().graphAttr().with(Rank.dir(LEFT_TO_RIGHT)).with(mainNodes); //tworzenie grafu
        File resultFile = new File("ClassRelationGraph.png");
        resultFile.createNewFile();
        try { fromGraph(g).render(Format.PNG).toFile(resultFile); } catch (IOException e) { e.printStackTrace(); }
    }

    public void createMethodRelationGraph(List<MethodDetails> methodList) throws IOException {
        //Analogicznie do funkcji wyzej
        List mainNodes = new ArrayList();
        addMethodRelationGraph(methodList,mainNodes);
        addVersionControlNode(mainNodes);
        Graph g = graph("MethodRelationGraph").directed().graphAttr().with(Rank.dir(LEFT_TO_RIGHT)).with(mainNodes);
        File resultFile = new File("MethodRelationGraph.png");
        resultFile.createNewFile();
        try { fromGraph(g).render(Format.PNG).toFile(resultFile); } catch (IOException e) { e.printStackTrace(); }
    }

    public void createPackageRelationGraph(List<PackageDetails> packageList) throws IOException {
        //Analogicznie do funkcji wyzej
        List mainNodes = new ArrayList();
        addPackageRelationGraph(packageList,mainNodes);
        addVersionControlNode(mainNodes);
        Graph g = graph("PackageRelationGraph").directed().graphAttr().with(Rank.dir(LEFT_TO_RIGHT)).with(mainNodes);
        File resultFile = new File("PackageRelationGraph.png");
        resultFile.createNewFile();
        try { fromGraph(g).render(Format.PNG).toFile(resultFile); } catch (IOException e) { e.printStackTrace(); }
    }

    public static List addClassRelationGraph(List<FileDetails> filesList, List mainNodes) throws IOException {
        for(FileDetails file : filesList){ //dla każdej klasy z listy klas
            String nodeName = file.getName();
            double weight = file.getWeight();
            Node mainNode = node(nodeName).with(Label.html("<b>" + nodeName + "</b><br/>" + weight + "B"), Color.GREEN); //stwórz noda klasy
            List toNodes = new ArrayList();
            for(Map.Entry<String, Integer> oneFile : file.getClassDependencyMap().entrySet() ){ //dla kazdej klasy z ktorymi jest polaczona nasza glowna klasa
                //Map.Entry oraz .entrySet() traktuje K i V jako jedną klasę dzięki czemu iterowanie jest prostsze
                //Z każdego pliku file wyciągamy jego dependency mapę i z niej wyciągamy każdą zależność ( jedna dependency mapa ma kilka zależności)
                String newNodeName = oneFile.getKey() ;
                toNodes.add(to(node(newNodeName)).with(Label.of(oneFile.getValue().toString()),Color.GREEN));  //do listy dodajemy węzły połączone z naszym źródłem
            }
            mainNodes.add(mainNode.link(toNodes));
        }
        return mainNodes;
    }

    public static List addMethodRelationGraph(List<MethodDetails> methodList, List mainNodes) throws IOException {
        for(MethodDetails method : methodList) {
            String nodeName = method.getMethodName();
            int weight = method.getCallCounter();
            Node mainNode = node(nodeName).with(Label.html("<b>" + nodeName + "</b><br/>" + weight), Color.RED);
            List toNodes = new ArrayList();
            for(Map.Entry<String, Integer> oneFile : method.getMethodDependencies().entrySet() ){
                String newNodeName = oneFile.getKey() ;
                toNodes.add(to(node(newNodeName)).with(Label.of(oneFile.getValue().toString()),Color.RED));
            }
            mainNodes.add(mainNode.link(toNodes));
        }
        return mainNodes;
    }

    public static List addPackageRelationGraph(List<PackageDetails> packageList, List mainNodes) throws IOException {
        for (PackageDetails onePackage : packageList) { //dla każdego pliku z listy plików
            String nodeName = onePackage.getPackageName();
            int weight = onePackage.getCallCounter();
            Node mainNode = node(nodeName).with(Label.html("<b>" + nodeName + "</b><br/>" + weight)); //stwórz głównego noda
            List toNodes = new ArrayList();
            //tworzenie polaczen package-package
            for (Map.Entry<String, Integer> packag : onePackage.getPackageDependencies().entrySet()) {
                String newNodeName = packag.getKey();
                toNodes.add(to(node(newNodeName)).with(Label.of(packag.getValue().toString())));
            }
            //tworzenie polaczen klasa-package (klasa pochodzi z package)
            mainNodes.add(mainNode.link(toNodes));
            for (Map.Entry<String, HashMap<String, Integer>> method : onePackage.getMethodDepedencies().entrySet()) {
                String methodName = method.getKey();
                Node methodNode = node(methodName).with(Label.of(methodName));
                mainNodes.add(methodNode.link(to(mainNode).with(Label.of("x"))));
                List toNodes2 = new ArrayList();
                //wg. struktury ktora dostalem do uzytku: tworzenie polaczen klasa-package (klasa uzywa package)
                for (Map.Entry<String, Integer> packag : method.getValue().entrySet()) {
                    String packageName = packag.getKey();
                    toNodes2.add(to(node(packageName)).with(Label.of(packag.getValue().toString())));
                }
                mainNodes.add(methodNode.link(toNodes2));
            }
        }
        return mainNodes;
    }

    public static List addVersionControlNode(List mainNodes){ //funkcja dodaje Node z numerem wersji do listy
        String version = GitVer.getGitVersion();
        Node vercontrol = node("Version_ID:").with(
                Shape.RECTANGLE,
                Style.FILLED,
                Color.hsv(.7, .3, 1.0),
                Label.html("<b>" + "Version_ID: " + "</b><br/>" + version)
        );
        mainNodes.add(vercontrol);
        return mainNodes;
    }
}
