package com.IOgroup.graphs;
import com.IOgroup.model.FileDetails;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static guru.nidi.graphviz.engine.Graphviz.fromGraph;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

public class ClassRelationGraph {  //https://github.com/nidi3/graphviz-java#complex-example

    public void createClassRelationGraph(List<FileDetails> filesList) throws IOException {

        List mainNodes = new ArrayList();
        for(FileDetails file : filesList){ //dla każdego pliku z listy plików

            String nodeName = file.getName();
            double weight = file.getWeight();
            Node mainNode = node(nodeName).with(Label.html("<b>" + nodeName + "</b><br/>" + weight + "B")); //stwórz głównego noda

            List toNodes = new ArrayList();

            for(Map.Entry<String, Integer> oneFile : file.getDependencyMap().entrySet() ){
                //Map.Entry oraz .entrySet() traktuje K i V jako jedną klasę dzięki czemu iterowanie jest prostsze
                //Z każdego pliku file wyciągamy jego dependency mapę i z niej wyciągamy każdą zależność ( jedna dependency mapa ma kilka zależności)
                String newNodeName = oneFile.getKey() + ".java";
                toNodes.add(to(node(newNodeName)).with(Label.of(oneFile.getValue().toString())));  //do listy dodajemy węzły połączone z naszym źródłem
            }
            mainNodes.add(mainNode.link(toNodes));
        }

        Graph g = graph("example1").directed().with(mainNodes);
        File resultFile = new File("graph.png");
        resultFile.createNewFile();
      try {
         fromGraph(g).render(Format.PNG).toFile(resultFile);
       } catch (IOException e) {
            e.printStackTrace();
       }

    }



}
