package org.example;


import org.example.model.Graph;
import org.example.model.RunRecordManager;
import org.example.model.SortType;
import org.example.sorting.DfsTopologicalSort;
import org.example.sorting.KhanTopologicalSort;
import org.example.sorting.LibraryTopologicalSort;
import org.example.sorting.TopologicalSort;

import java.util.Arrays;


public class App {

    private static final int RUN_REPEATS = 10;
    private static final LibraryTopologicalSort libraryTopologicalSort = new LibraryTopologicalSort();
    private static final DfsTopologicalSort dfsTopologicalSort = new DfsTopologicalSort();
    private static final KhanTopologicalSort khanTopologicalSort = new KhanTopologicalSort();
    private static final RunRecordManager runRecordManager = new RunRecordManager();

    public static void main(String[] args) {
        // graph we'll re-use for each test run
        Graph dag = new Graph();

        // the size of the tests we'll run (N = V + E)
        Arrays.asList(10_000, 100_000, 500_000, 1_000_000, 5_000_000).forEach(N -> {
            // compute the number of vertices and edges needed
            Arrays.asList(
                10 * N / 11,    // E = V / 10
                4 * N / 7,      // E = 3 * V / 4
                N / 2,          // E = V
                // E = (1/4) * (V * (V - 1)) / 2 (a fourth of max possible edges)
                (int) ((-7.0 + Math.sqrt(49.0 + 32.0 * N)) / 2.0),
                // E = (1/2) * (V * (V - 1)) / 2 (a half of max possible edges)
                (int) ((-3.0 + Math.sqrt(9.0 + 16.0 * N)) / 2.0)
            ).forEach(vertices -> {
                System.out.println("Edges = " + (N - vertices) + " and vertices = " + vertices);
                for(int i = 0; i < RUN_REPEATS; i++){
                    System.out.println("\tRun " + i + " of " + RUN_REPEATS);
                    dag.createDirectedAcyclicGraph(vertices, N - vertices); // new graph (edge set) each iteration
                    timeSorting(libraryTopologicalSort, dag, SortType.LIBRARY);
                    timeSorting(dfsTopologicalSort, dag, SortType.DFS);
                    timeSorting(khanTopologicalSort, dag, SortType.KHAN);
                }
            });
        });

        // print all test results
        runRecordManager.printRunRecords();
        runRecordManager.writeRecordsToCsv();
    }

    // time the execution of the given topological sort function
    private static void timeSorting(TopologicalSort sorter, Graph dag, SortType sortType) {
        long start = System.nanoTime();
        Long[] topologicalOrder = sorter.sort(dag);
        long elapsedTime = System.nanoTime() - start;
        runRecordManager.addRecord(sortType, elapsedTime, dag);
    }
}
