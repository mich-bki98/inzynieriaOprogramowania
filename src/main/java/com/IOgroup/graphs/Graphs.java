package com.IOgroup.graphs;
import com.IOgroup.model.FileDetails;
import com.IOgroup.model.MethodDetails;
import com.IOgroup.gitVersion.GitVer;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.engine.Graphviz.fromGraph;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

public class Graphs {  //https://github.com/nidi3/graphviz-java#complex-example

    public void createClassRelationGraph(List<FileDetails> filesList) throws IOException {

        List mainNodes = new ArrayList();
        for(FileDetails file : filesList){ //dla każdego pliku z listy plików

            String nodeName = file.getName();
            double weight = file.getWeight();
            Node mainNode = node(nodeName).with(Label.html("<b>" + nodeName + "</b><br/>" + weight + "B")); //stwórz głównego noda

            List toNodes = new ArrayList();

            for(Map.Entry<String, Integer> oneFile : file.getClassDependencyMap().entrySet() ){
                //Map.Entry oraz .entrySet() traktuje K i V jako jedną klasę dzięki czemu iterowanie jest prostsze
                //Z każdego pliku file wyciągamy jego dependency mapę i z niej wyciągamy każdą zależność ( jedna dependency mapa ma kilka zależności)
                String newNodeName = oneFile.getKey() ;//+ ".java"; <-------- NA HUJ TO JA SIE PYTAM XDDDD
                toNodes.add(to(node(newNodeName)).with(Label.of(oneFile.getValue().toString())));  //do listy dodajemy węzły połączone z naszym źródłem
            }
            mainNodes.add(mainNode.link(toNodes));
        }

        mainNodes.add(createVersionControlNode());  //dodaje maly kwadracik na grafie z numerem wersji kodu

        Graph g = graph("example1")
                .directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .with(mainNodes);

        File resultFile = new File("ClassRelationGraph.png");
        resultFile.createNewFile();
      try {
         fromGraph(g).render(Format.PNG).toFile(resultFile);
       } catch (IOException e) {
            e.printStackTrace();
       }

    }

    public void createMethodRelationGraph(List<MethodDetails> methodList) throws IOException {

        List mainNodes = new ArrayList();
        for(MethodDetails method : methodList) {

            String nodeName = method.getMethodName();
            int weight = method.getCallCounter();
            Node mainNode = node(nodeName).with(Label.html("<b>" + nodeName + "</b><br/>" + weight));

            List toNodes = new ArrayList();
            for(Map.Entry<String, Integer> oneFile : method.getMethodDependencies().entrySet() ){
                String newNodeName = oneFile.getKey() ;
                toNodes.add(to(node(newNodeName)).with(Label.of(oneFile.getValue().toString())));
            }
            mainNodes.add(mainNode.link(toNodes));
        }

        mainNodes.add(createVersionControlNode());  //dodaje maly kwadracik na grafie z numerem wersji kodu

        Graph g = graph("example2")
                .directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .with(mainNodes);

        File resultFile = new File("MethodRelationGraph.png");
        resultFile.createNewFile();
        try {
            fromGraph(g).render(Format.PNG).toFile(resultFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Node createVersionControlNode(){
        String version = GitVer.getGitVersion();
        Node vercontrol = node("Version_ID:").with(
                Shape.RECTANGLE,
                Style.FILLED,
                Color.hsv(.7, .3, 1.0),
                Label.html("<b>" + "Version_ID: " + "</b><br/>" + version)
        );
        return vercontrol;
    }



}
