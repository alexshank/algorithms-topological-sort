package org.example.sorting;

import org.example.model.Graph;
import org.jgrapht.Graphs;

import java.util.PriorityQueue;
import java.util.Queue;

public class KhanTopologicalSort implements TopologicalSort {

    public Long[] sort(Graph graph) {
        Queue<Long> queue = new PriorityQueue<>();
        int visitedCount = 0;

        long[] inDegrees = new long[graph.vertexSet().size()];
        graph.vertexSet().forEach(v -> {
            int inDegree = graph.inDegreeOf(v);
            inDegrees[v.intValue()] = inDegree;
            if (inDegree == 0) {
                queue.add(v);
            }
        });

        Long[] result = new Long[graph.vertexSet().size()];
        while (!queue.isEmpty()) {
            long vertex = queue.remove();
            result[visitedCount++] = vertex;
            graph.outgoingEdgesOf(vertex);
            Graphs.successorListOf(graph, vertex).forEach(v -> {
                inDegrees[v.intValue()]--;
                if (inDegrees[v.intValue()] == 0) {
                    queue.add(v);
                }
            });
        }

        // assert there is no cycle
        assert (visitedCount == graph.vertexSet().size());
        return result;
    }
}
