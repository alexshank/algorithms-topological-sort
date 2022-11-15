package org.example;


import org.example.model.Graph;
import org.example.model.RunRecord;
import org.example.model.RunType;
import org.example.sorting.DfsTopologicalSort;
import org.example.sorting.KhanTopologicalSort;
import org.example.sorting.LibraryTopologicalSort;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class App {

    private static final int RUN_REPEATS = 10;

    public static void main(String[] args) throws Exception {
        List<RunRecord> records = new ArrayList<>();
        Graph dag = new Graph();

        // TODO alex create V+E, E proportional to V logic
        for (long vertices = 10; vertices <= 600; vertices += 100) {
            for (long edges = 10; edges <= ((vertices - 1) * vertices) / 2; edges += 100) {
                System.out.println("vertices = " + vertices + ", edges = " + edges + " of " + ((vertices - 1) * vertices) / 2 + ", ");
                for(int i = 0; i < RUN_REPEATS; i++){
                    System.out.println("\tRun " + i + " of " + RUN_REPEATS);
                    GraphBuilders.createDirectedAcyclicGraph(dag, vertices, edges);
                    timeAlgorithm((new LibraryTopologicalSort())::sort, dag, records, RunType.LIBRARY);
                    timeAlgorithm((new DfsTopologicalSort())::sort, dag, records, RunType.DFS);
                    timeAlgorithm((new KhanTopologicalSort())::sort, dag, records, RunType.KHAN);
                }
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
            Function<Graph, Long[]> sorterFunction,
            Graph dag,
            List<RunRecord> records,
            RunType runType
    ) {
        long start = System.nanoTime();
        Long[] topologicalOrder = sorterFunction.apply(dag);
        long elapsedTime = System.nanoTime() - start;
        records.add(new RunRecord(runType, elapsedTime, dag));
    }

}
