package org.example.sorting;

import org.example.model.Graph;
import org.jgrapht.Graphs;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class DfsTopologicalSort implements TopologicalSort {

    public Long[] sort(Graph graph) {
        Set<Long> visited = new HashSet<>();
        Stack<Long> stack = new Stack<>();

        graph.vertexSet().forEach(v -> {
            if (!visited.contains(v)) {
                topologicalSortVisit(graph, stack, visited, v);
            }
        });

        // TODO alex could optimize by using fixed size array
        Long[] result = new Long[graph.vertexSet().size()];
        int count = 0;
        while (stack.size() > 0) {
            result[count++] = stack.pop();
        }
        return result;
    }

    private static void topologicalSortVisit(
            Graph graph,
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
}
