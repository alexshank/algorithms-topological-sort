package org.example;


import org.example.model.RunRecord;
import org.example.model.RunType;
import org.example.sorting.DfsTopologicalSort;
import org.example.sorting.KhanTopologicalSort;
import org.example.sorting.LibraryTopologicalSort;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class App {

    public static void main(String[] args) throws Exception {
        List<RunRecord> records = new ArrayList<>();
        DefaultDirectedGraph<Long, DefaultEdge> dag = new DefaultDirectedGraph<>(DefaultEdge.class);

        for (long vertices = 10; vertices <= 600; vertices += 100) {
            for (long edges = 10; edges <= ((vertices - 1) * vertices) / 2; edges += 100) {
                System.out.println("vertices = " + vertices + ", edges = " + edges + " of " + ((vertices - 1) * vertices) / 2 + ", ");

                GraphBuilders.createDirectedAcyclicGraph(dag, vertices, edges);
                timeAlgorithm((new LibraryTopologicalSort())::sort, dag, records, RunType.LIBRARY);
                timeAlgorithm((new DfsTopologicalSort())::sort, dag, records, RunType.DFS);
                timeAlgorithm((new KhanTopologicalSort())::sort, dag, records, RunType.KHAN);
            }
        }

        // print all test results
        System.out.println();
        System.out.println(RunRecord.getHeaderRow());
        for (RunRecord record : records) {
            System.out.println(record.toString());
        }
        WriterHelp.writeRecordsToCsv(records, "many_edges_data_01.csv");
    }

    // time the execution of the given topological sort function
    private static void timeAlgorithm(
            // TODO alex can update now that we have interface?
            Function<DefaultDirectedGraph<Long, DefaultEdge>, Long[]> sorterFunction,
            DefaultDirectedGraph<Long, DefaultEdge> dag,
            List<RunRecord> records,
            RunType runType
    ) {
        long start = System.nanoTime();
        Long[] topologicalOrder = sorterFunction.apply(dag);
        long elapsedTime = System.nanoTime() - start;
        records.add(new RunRecord(runType, elapsedTime, dag));
    }

}
