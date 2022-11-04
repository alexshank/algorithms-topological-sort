package org.example;


import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graphs;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting application...");

        // write example graph to output
        writeGraphToFile(createExampleGraph(), "example.png");

        // write random graph to output
        DefaultDirectedGraph<String, DefaultEdge> randomGraph = createRandomGraph(15, 15);
        writeGraphToFile(randomGraph, "temp.png");

        // print libraries topological order
        System.out.println("Library:");
        TopologicalOrderIterator<String, DefaultEdge> iter = new TopologicalOrderIterator<String, DefaultEdge>(randomGraph);
        while (iter.hasNext()) {
            String v = iter.next();
            System.out.print(v + " ");
        }

        // print my simple topological order
        System.out.println();
        System.out.println("Simple:");
        mySimpleTopologicalSort(randomGraph);

        // print my DFS based topological sort
        System.out.println();
        System.out.println("DFS:");
        myDFSTopologicalSort(randomGraph);
    }

    private static void myDFSTopologicalSort(DefaultDirectedGraph<String, DefaultEdge> graph) {
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        graph.vertexSet().forEach(v -> {
            if (!visited.contains(v)) {
                topologicalSortVisit(graph, stack, visited, v);
            }
        });

        while (stack.size() > 0)
            System.out.print(stack.pop() + " ");
    }

    private static void topologicalSortVisit(
            DefaultDirectedGraph<String, DefaultEdge> graph,
            Stack<String> stack,
            Set<String> visited,
            String v
    ) {
        System.out.print(v + ".");
        visited.add(v);

        // get neighbors of vertex
        java.util.List<String> neighbors = Graphs.successorListOf(graph, v);
        neighbors.forEach(neighbor -> {
            if (!visited.contains(neighbor)) {
                topologicalSortVisit(graph, stack, visited, neighbor);
            }
        });

        stack.push(v);
    }


    private static void mySimpleTopologicalSort(DefaultDirectedGraph<String, DefaultEdge> graph) {
        DefaultDirectedGraph<String, DefaultEdge> workingGraph = (DefaultDirectedGraph<String, DefaultEdge>) graph.clone();
        while (!workingGraph.vertexSet().isEmpty()) {
            String nextVertex = removeInDegreeZeroVertex(workingGraph); // has side effects
            System.out.print(nextVertex + " ");
        }
    }

    private static String removeInDegreeZeroVertex(DefaultDirectedGraph<String, DefaultEdge> workingGraph) {
        String vertex = workingGraph.vertexSet().stream()
                .filter(v -> workingGraph.incomingEdgesOf(v).size() == 0)
                .findFirst()
                .get();
        Set<DefaultEdge> incomingEdges = workingGraph.incomingEdgesOf(vertex);
        incomingEdges.forEach(edge -> workingGraph.removeEdge(edge));
        workingGraph.removeVertex(vertex);
        return vertex;
    }

    // will not contain cycles
    private static DefaultDirectedGraph<String, DefaultEdge> createRandomGraph(int vertices, int edges) {
        // return null if we have more than a fully connected graph
        if (edges > (((vertices - 1) * vertices) / 2)) {
            return null;
        }

        DefaultDirectedGraph<String, DefaultEdge> g =
                new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

        for (int i = 0; i < vertices; i++) {
            g.addVertex(createVertexLabel(i));
        }


        for (int i = 0; i < edges; i++) {

            String start = createVertexLabel((int) (Math.random() * vertices));
            String end = createVertexLabel((int) (Math.random() * vertices));
            if (start.equals(end)) {
                continue;
            }

            // TODO alex may error if same edge added twice?
            g.addEdge(start, end);

            CycleDetector cd = new CycleDetector(g);
            if (cd.detectCyclesContainingVertex(start)) {
                g.removeEdge(start, end);
            }
        }

        System.out.println("\nRandom graph has vertex count: " + g.vertexSet().size());
        System.out.println("Random graph has edge count  : " + g.edgeSet().size());
        return g;
    }

    private static String createVertexLabel(int i) {
        if (i < 10) {
            return "V0" + i;
        } else {
            return "V" + i;
        }
    }

    // just testing the JGraphT library functionality
    private static DefaultDirectedGraph<String, DefaultEdge> createExampleGraph() {
        DefaultDirectedGraph<String, DefaultEdge> g =
                new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

        String x1 = "x1";
        String x2 = "x2";
        String x3 = "x3";

        g.addVertex(x1);
        g.addVertex(x2);
        g.addVertex(x3);

        g.addEdge(x1, x2);
        g.addEdge(x2, x3);
        g.addEdge(x3, x1);

        return g;
    }

    // write a PNG of the given graph to the given file in the "output" directory
    private static void writeGraphToFile(
            DefaultDirectedGraph<String, DefaultEdge> graph,
            String fileName
    ) throws IOException {
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(graph);

        // we don't need edge labels on our graph
        graphAdapter.getEdgeToCellMap().forEach((edge, cell) -> cell.setValue(null));

        mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(
                graphAdapter, null, 2, Color.BLACK, true, null
        );

        String outputDirPath = Paths.get("").toAbsolutePath() + "/output/";
        new File(outputDirPath).mkdirs();
        File imgFile = new File(outputDirPath + fileName);
        imgFile.createNewFile();
        ImageIO.write(image, "PNG", imgFile);
    }
}
