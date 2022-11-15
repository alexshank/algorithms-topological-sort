package org.example;

import org.example.model.Graph;
import org.jgrapht.graph.DefaultEdge;

// TODO alex could use the actual builder pattern
public class GraphBuilders {
    // will not produce cycles; all edges must go from a higher vertex to a lower vertex
    public static void createDirectedAcyclicGraph(
            Graph dag,
            long vertices,
            long edges
    ) throws Exception {
        // throw exception if we have more edges than a DAG could hold (i.e. cycle producing)
        long maxEdges = ((vertices - 1) * vertices) / 2;
        if (edges > maxEdges) {
            throw new Exception("Too many edges for vertex count.");
        }

        long startVertex = dag.vertexSet().size();
        long startEdge = dag.edgeSet().size();
        long additionalVertices = vertices - dag.vertexSet().size();
        long additionalEdges = edges - dag.edgeSet().size();

        // TODO alex break this into separate vertices and edges checks
        if(additionalEdges < 0 || additionalVertices < 0){
            // create brand new graph
            // TODO alex could just remove all edges (empty the set in a single operation?)
            // TODO alex or just calculate number of edges to add / remove
            dag = new Graph();
            startVertex = 0;
            startEdge = 0;
        }

        // TODO alex could just calculate the vertices to add / remove instead of starting from empty
        for (long i = startVertex; i < vertices; i++) {
            dag.addVertex(i);
        }

        long count = startEdge;
        while (count < edges) {
            long start = (long) (Math.random() * vertices);
            long end = (long) (Math.random() * start);
            if (start == end || dag.containsEdge(start, end)) {
                continue;
            } else {
                dag.addEdge(start, end);
                count++;
            }
        }
    }

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
