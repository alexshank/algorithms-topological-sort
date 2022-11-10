package org.example;


import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graphs;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

// TODO alex space complexity of each of these?

// TODO alex add readme
// TODO alex add finalized report to repo at end
public class App {

    private static class RunRecord {
        private String type;
        private long timeCount; // milliseconds
        private long vertexCount;
        private long edgeCount;
        private long actualVertexCount;
        private long actualEdgeCount;

        // TODO alex update so that vertex and edge count are actual values not attempted values
        public RunRecord(String type, long timeCount, long vertexCount, long edgeCount, long actualVertexCount, long actualEdgeCount) {
            this.type = type;
            this.timeCount = timeCount / 1_000_000;
            this.vertexCount = vertexCount;
            this.edgeCount = edgeCount;
            this.actualVertexCount = actualVertexCount;
            this.actualEdgeCount = actualEdgeCount;
        }

        // TODO alex add method that takes file pointer and prints record to it

        @Override
        public String toString() {
            return this.type + "," + this.timeCount + "," + this.vertexCount + "," + this.actualVertexCount + "," + this.edgeCount + "," + this.actualEdgeCount;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Starting application...");
        List<RunRecord> records = new ArrayList<>();

        // TODO alex testing limits one time
//        DefaultDirectedGraph<Long, DefaultEdge> testGraph = createDirectedAcyclicGraph(1_000_000, (1_000_000 * (1_000_000 - 1))/2);
//
//        long start = System.nanoTime();
//        TopologicalOrderIterator<Long, DefaultEdge> iter =
//                new TopologicalOrderIterator<Long, DefaultEdge>(testGraph);
//        List<Long> topologicalOrder = new ArrayList<>();
//        while (iter.hasNext()) {
//            long v = iter.next();
//            topologicalOrder.add(v);
//        }
//        long elapsedTime = System.nanoTime() - start;
//        System.out.println("\tLibrary " + elapsedTime / 1_000_000);

        // TODO alex testing cycles aren't created, printing small graph to visually confirm
//        TopologicalOrderIterator<Long, DefaultEdge> iter =
//                        new TopologicalOrderIterator<Long, DefaultEdge>(testGraph);
////         write random graph to output
//        writeGraphToFile(testGraph, "temp.png");

        // TODO alex add trivial cases of zero edges to maybe 100

        // TODO alex create one case where we do all the edges up to 10,000 vertices

        for (long i = 10; i <= 10_000; i = Double.valueOf(i * 1.1).longValue()) {
            System.out.println("i = " + i);
            // go to max number of possible edges in graph
//            for (int j = 10; j <= ((i - 1) * i) / 2; j = j * 10){
            // TODO alex this is taking too long right now
            System.out.println("Vertices: " + i + " out of " + 10_000);
            System.out.println("Going up to " + ((i - 1) * i) / 2 + " edges...");
            System.out.print("\tj = ");
            for (long j = 10; j<= ((i - 1) * i) / 2; j = Double.valueOf(j * 1.1).longValue()) {

                System.out.print(j + ", ");

                DefaultDirectedGraph<Long, DefaultEdge> randomGraph = createDirectedAcyclicGraph(i, j);
                // write random graph to output
                //        writeGraphToFile(randomGraph, "temp.png");


                // print libraries topological order
                long start = System.nanoTime();
                TopologicalOrderIterator<Long, DefaultEdge> iter =
                        new TopologicalOrderIterator<Long, DefaultEdge>(randomGraph);
                List<Long> topologicalOrder = new ArrayList<>();
                while (iter.hasNext()) {
                    long v = iter.next();
                    topologicalOrder.add(v);
                }
                long elapsedTime = System.nanoTime() - start;
//                System.out.println("\tLibrary " + elapsedTime / 1_000_000);
                records.add(new RunRecord("Library", elapsedTime, i, j, randomGraph.vertexSet().size(), randomGraph.edgeSet().size()));

                // print my DFS based topological sort
                start = System.nanoTime();
                topologicalOrder = myDFSTopologicalSort(randomGraph);
                elapsedTime = System.nanoTime() - start;
//                System.out.println("\tDFS " + elapsedTime / 1_000_000);
                records.add(new RunRecord("DFS", elapsedTime, i, j, randomGraph.vertexSet().size(), randomGraph.edgeSet().size()));

                // TODO alex fix once other analysis is good.
                // TODO alex we can try a very similar, but modified, algorithm
                // print my simple topological order
                start = System.nanoTime();
                topologicalOrder = mySecondSimpleTopologicalSort(randomGraph);
                elapsedTime = System.nanoTime() - start;
//                System.out.println("\tSimple " + elapsedTime / 1_000_000);
                records.add(new RunRecord("Simple", elapsedTime, i, j, randomGraph.vertexSet().size(), randomGraph.edgeSet().size()));
            }
            System.out.println();
        }

        // print all test results
        System.out.println();
        System.out.println("type, timeCount, vertexCount, actualVertexCount, edgeCount, actualEdgeCount");
        for (RunRecord record : records) {
            System.out.println(record.toString());
        }

        writeCSVToFile(records, "many_edges_data.csv");
    }

    private static List<Long> myDFSTopologicalSort(DefaultDirectedGraph<Long, DefaultEdge> graph) {
        Set<Long> visited = new HashSet<>();
        Stack<Long> stack = new Stack<>();

        graph.vertexSet().forEach(v -> {
            if (!visited.contains(v)) {
                topologicalSortVisit(graph, stack, visited, v);
            }
        });

        List<Long> result = new ArrayList<>();
        while (stack.size() > 0) {
            result.add(stack.pop());
        }
        return result;
    }

    private static void topologicalSortVisit(
            DefaultDirectedGraph<Long, DefaultEdge> graph,
            Stack<Long> stack,
            Set<Long> visited,
            long v
    ) {
        visited.add(v);
        Graphs.successorListOf(graph, v).forEach(child -> {
            if (!visited.contains(child)) {
                topologicalSortVisit(graph, stack, visited, child);
            }
        });
        stack.push(v);
    }

    // TODO alex figure out why this is so slow (probably traversing edges way too many times)
    private static List<Long> mySimpleTopologicalSort(DefaultDirectedGraph<Long, DefaultEdge> graph) {
        // TODO alex should not ever have to copy the graph
//        DefaultDirectedGraph<Long, DefaultEdge> workingGraph =
//                (DefaultDirectedGraph<Long, DefaultEdge>) graph.clone();

        System.out.print("\t\tremoving zero degree vertex... v count = ");
        List<Long> result = new ArrayList<>();
        while (!graph.vertexSet().isEmpty()) {
            if (graph.vertexSet().size() % 100_000 == 0) {
                System.out.print(graph.vertexSet().size() + ", ");
            }

            for (long j : graph.vertexSet()) {
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

    private static List<Long> mySecondSimpleTopologicalSort(DefaultDirectedGraph<Long, DefaultEdge> graph) {

        Queue<Long> queue = new PriorityQueue<>();
        long visitedCount = 0;

        long[] inDegrees = new long[graph.vertexSet().size()];
        graph.vertexSet().forEach(v -> {
            int inDegree = graph.inDegreeOf(v);
            inDegrees[v.intValue()] = inDegree;
            if (inDegree == 0) {
                queue.add(v);
            }
        });

        List<Long> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            long vertex = queue.remove();
            result.add(vertex);
            visitedCount++;
            graph.outgoingEdgesOf(vertex);
            Graphs.successorListOf(graph, vertex).forEach(v -> {
                inDegrees[v.intValue()]--;
                if (inDegrees[v.intValue()] == 0) {
                    queue.add(v);
                }
            });
        }

        // TODO alex otherwise we have a cycle
        assert (visitedCount == graph.vertexSet().size());
        return result;
    }

    // can produce cycles
    private static DefaultDirectedGraph<Long, DefaultEdge> createRandomGraph(long vertices, long edges) throws Exception {
        // throw exception if we have more edges than a fully connected graph
        if ((long) edges > (((((long) vertices - 1) * (long) vertices)))) {
            throw new Exception("Too many edges for vertex count.");
        }

        DefaultDirectedGraph<Long, DefaultEdge> g =
                new DefaultDirectedGraph<Long, DefaultEdge>(DefaultEdge.class);

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

    // TODO alex we could re-use the test graphs we created and just add / remove the difference of needed edges
    // will not contain cycles
    // all edges must go from a higher vertex to a lower vertex
    private static DefaultDirectedGraph<Long, DefaultEdge> createDirectedAcyclicGraph(long vertices, long edges) throws Exception {
        // throw exception if we have more edges than a DAG could hold (i.e. cycle producing)
        long maxEdges = ((vertices - 1) * vertices) / 2;
        if (edges > maxEdges) {
            throw new Exception("Too many edges for vertex count.");
        }

        DefaultDirectedGraph<Long, DefaultEdge> dag = new DefaultDirectedGraph<Long, DefaultEdge>(DefaultEdge.class);
        for (long i = 0; i < vertices; i++) {
            dag.addVertex(i);
        }

        // if we want a nearly "full" graph, randomly remove edges rather than randomly add them
        if ((long) edges > 3 * maxEdges / 4) {
            // add every possible edge
            for (long i = vertices - 1; i > 0; i--) {
                for (long j = i - 1; j >= 0; j--) {
                    dag.addEdge(i, j);
                }
            }

            long count = maxEdges;
            while (count > edges) {
                long start = (long) (Math.random() * vertices);
                long end = (long) (Math.random() * (start - 1));
                if (dag.removeEdge(start, end) != null) {
                    count--;
                }
            }
        } else {
            long count = 0;
            while (count < edges) {
                long start = (long) (Math.random() * vertices);
                long end = (long) (Math.random() * (start - 1));
                if (start == end || dag.containsEdge(start, end)) {
                    continue;
                } else {
                    dag.addEdge(start, end);
                    count++;
                }
            }
        }

        return dag;
    }

    // TODO alex add appendix with some examples of small graphs that are properly topologically sorted
    // write a PNG of the given graph to the given file in the "output" directory
    private static void writeGraphToFile(
            DefaultDirectedGraph<Long, DefaultEdge> graph,
            String fileName
    ) throws IOException {
        JGraphXAdapter<Long, DefaultEdge> graphAdapter = new JGraphXAdapter<Long, DefaultEdge>(graph);

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

    // write a CSV of runtimes to the given file in the "output" directory
    private static void writeCSVToFile(
            List<RunRecord> runRecords,
            String fileName
    ) throws IOException {

        String outputDirPath = Paths.get("").toAbsolutePath() + "/output/";
        new File(outputDirPath).mkdirs();
//        File csvFile = new File(outputDirPath + fileName); // TODO alex may not be needed?

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirPath + fileName));
        writer.write("type,timeCount,vertexCount,actualVertexCount,edgeCount,actualEdgeCount\n");
        for (RunRecord r : runRecords) {
            writer.write(r.toString() + "\n");
        }

        writer.close();
    }
}
