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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class App {

    private static class RunRecord {
        private String type;
        private double timeCount; // seconds
        private int vertexCount;
        private int edgeCount;
        private int actualVertexCount;
        private int actualEdgeCount;

        // TODO alex update so that vertex and edge count are actual values not attempted values
        public RunRecord(String type, long timeCount, int vertexCount, int edgeCount, int actualVertexCount, int actualEdgeCount) {
            this.type = type;
            this.timeCount = (double) timeCount / 1_000_000_000;
            this.vertexCount = vertexCount;
            this.edgeCount = edgeCount;
            this.actualVertexCount = actualVertexCount;
            this.actualEdgeCount = actualEdgeCount;
        }

        // TODO alex add method that takes file pointer and prints record to it
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Starting application...");
        List<RunRecord> records = new ArrayList<>();

//        // write example graph to output
//        writeGraphToFile(createExampleGraph(), "example.png");

        for (int i = 1000; i <= 1_000_000; i = i * 10) {
            System.out.println("( i = " + i + " )");
            // go to max number of possible edges in graph
//            for (int j = 10; j <= ((i - 1) * i) / 2; j = j * 10){
            // TODO alex this is taking too long right now

            for (int j = 10; j <= i; j = j * 10) {

                System.out.println("\t( j = " + j + " )");
                DefaultDirectedGraph<Integer, DefaultEdge> randomGraph = createRandomGraph(i, j);
                // write random graph to output
                //        writeGraphToFile(randomGraph, "temp.png");

                System.out.println("\tLibrary");
                // print libraries topological order
                long start = System.nanoTime();
                TopologicalOrderIterator<Integer, DefaultEdge> iter =
                        new TopologicalOrderIterator<Integer, DefaultEdge>(randomGraph);
                List<Integer> topologicalOrder = new ArrayList<>();
                while (iter.hasNext()) {
                    int v = iter.next();
                    topologicalOrder.add(v);
                }
                long elapsedTime = System.nanoTime() - start;
                System.out.println("\t" + (double) elapsedTime / 1_000_000_000);
                records.add(new RunRecord("Library", elapsedTime, i, j, randomGraph.vertexSet().size(), randomGraph.edgeSet().size()));

                System.out.println("\tDFS");
                // print my DFS based topological sort
                start = System.nanoTime();
                topologicalOrder = myDFSTopologicalSort(randomGraph);
                elapsedTime = System.nanoTime() - start;
                System.out.println("\t" + (double) elapsedTime / 1_000_000_000);
                records.add(new RunRecord("DFS   ", elapsedTime, i, j, randomGraph.vertexSet().size(), randomGraph.edgeSet().size()));

                // TODO alex fix once other analysis is good.
                // TODO alex we can try a very similar, but modified, algorithm
//                System.out.println("\tSimple");
//                // print my simple topological order
//                start = System.nanoTime();
//                topologicalOrder = mySimpleTopologicalSort(randomGraph);
//                elapsedTime = System.nanoTime() - start;
//                System.out.println("\t" + (double) elapsedTime / 1_000_000_000);
//                records.add(new RunRecord("Simple", elapsedTime, i, j, randomGraph.vertexSet().size(), randomGraph.edgeSet().size()));
            }
        }

        // print all test results
        System.out.println();
        for (RunRecord record : records) {
            System.out.println("type, timeCount, vertexCount, actualVertexCount, edgeCount, actualEdgeCount");
            System.out.println(record.type + ", " + record.timeCount + ", " + record.vertexCount + ", " + record.actualVertexCount + ", " + record.edgeCount + ", " + record.actualEdgeCount);
        }
    }

    private static List<Integer> myDFSTopologicalSort(DefaultDirectedGraph<Integer, DefaultEdge> graph) {
        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();

        graph.vertexSet().forEach(v -> {
            if (!visited.contains(v)) {
                topologicalSortVisit(graph, stack, visited, v);
            }
        });

        List<Integer> result = new ArrayList<>();
        while (stack.size() > 0) {
            result.add(stack.pop());
        }
        return result;
    }

    private static void topologicalSortVisit(
            DefaultDirectedGraph<Integer, DefaultEdge> graph,
            Stack<Integer> stack,
            Set<Integer> visited,
            int v
    ) {
        visited.add(v);
        Graphs.successorListOf(graph, v).forEach(child -> {
            if (!visited.contains(child)) {
                topologicalSortVisit(graph, stack, visited, child);
            }
        });
        stack.push(v);
    }

    // TODO alex rewrite this with alternative approach described in bookmarks
    private static List<Integer> mySimpleTopologicalSort(DefaultDirectedGraph<Integer, DefaultEdge> graph) {
        // TODO alex uhhh don't copy graph but this isn't great
//        DefaultDirectedGraph<Integer, DefaultEdge> workingGraph =
//                (DefaultDirectedGraph<Integer, DefaultEdge>) graph.clone();


        System.out.print("\t\tremoving zero degree vertex... v count = ");
        List<Integer> result = new ArrayList<>();
        while (!graph.vertexSet().isEmpty()) {
            if (graph.vertexSet().size() % 100_000 == 0) {
                System.out.print(graph.vertexSet().size() + ", ");
            }

            for (int j : graph.vertexSet()) {
                if (graph.outDegreeOf(j) == 0) {
                    graph.removeVertex(j);
                    result.add(j);
                    break;
                }
            }
        }
        System.out.println();
        return result;
    }

    // TODO alex this doesn't seem to be a bottleneck, but we know we want to end up with a
    // TODO alex DAG. We could get a DAG in a more direct/efficient way
    // TODO alex
    // TODO alex We should maybe still use this way and note when we create 1 or more cycles
    // TODO alex Ultimately, our algorithms should be able to detect a cycle without issue
    // will not contain cycles
    private static DefaultDirectedGraph<Integer, DefaultEdge> createRandomGraph(int vertices, int edges) {
        // return null if we have more than a fully connected graph
        if ((long) edges > (((((long) vertices - 1) * (long) vertices) / 2))) {
            return null;
        }

        DefaultDirectedGraph<Integer, DefaultEdge> g =
                new DefaultDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);

        for (int i = 0; i < vertices; i++) {
            g.addVertex(i);
        }

        for (int i = 0; i < edges; i++) {
            int start = (int) (Math.random() * vertices);
            int end = (int) (Math.random() * vertices);
            if (start == end) {
                continue;  // TODO maybe improve? once we start making cycles, exit
            }

            g.addEdge(start, end);

            CycleDetector cd = new CycleDetector(g);
            if (cd.detectCyclesContainingVertex(start)) {
                g.removeEdge(start, end);
            }
        }

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
