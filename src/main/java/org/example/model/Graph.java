package org.example.model;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class Graph extends DefaultDirectedGraph<Long, DefaultEdge> {
    public Graph() {
        super(DefaultEdge.class);
    }
}
