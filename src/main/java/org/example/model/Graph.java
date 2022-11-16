package org.example.model;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashSet;
import java.util.Set;

public class Graph extends DefaultDirectedGraph<Long, DefaultEdge> {
    public Graph() {
        super(DefaultEdge.class);
    }

    // will not produce cycles; all edges must go from a higher vertex to a lower vertex
    public void createDirectedAcyclicGraph(int vertices, int edges){
        // check if we have more edges than a DAG could hold (i.e. cycle producing)
        assert(edges <= ((vertices - 1) * vertices) / 2);

        // remove or add vertices as necessary
        int vertexDifference = vertices - this.vertexSet().size();
        if(vertexDifference > 0){
            for (long i = this.vertexSet().size(); i < vertices; i++) {
                this.addVertex(i);
            }
        }else if(vertexDifference < 0){
            // TODO alex could start value be an issue while iterating?
            for (int i = this.vertexSet().size() - 1; i > vertices - 1; i--){
                this.removeVertex((long) i);
            }
        }

        // always add new random edges so tests are averaged
        // TODO this is wonky
        Set<DefaultEdge> copiedEdges = new HashSet<>(this.edgeSet());
        this.removeAllEdges(copiedEdges);
        long count = 0;
        while (count < edges) {
            long start = (long) (Math.random() * vertices);
            long end = (long) (Math.random() * start);
            if (start == end || this.containsEdge(start, end)) {
                continue;
            } else {
                this.addEdge(start, end);
                count++;
            }
        }
    }

    // TODO alex will use this for blog post??
    // can produce cycles
    private static Graph createRandomGraph(long vertices, long edges) throws Exception {
        // throw exception if we have more edges than a fully connected graph
        if (edges > ((((vertices - 1) * vertices)))) {
            throw new Exception("Too many edges for vertex count.");
        }

        Graph g = new Graph();

        for (long i = 0; i < vertices; i++) {
            g.addVertex(i);
        }

        long count = 0;
        while (count < edges) {
            long start = (long) (Math.random() * vertices);
            long end = (long) (Math.random() * vertices);

            if (g.containsEdge(start, end)) {
                continue;
            } else {
                g.addEdge(start, end);
                count++;
            }
        }

        return g;
    }
}
