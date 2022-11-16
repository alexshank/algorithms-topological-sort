package org.example.sorting;

import org.example.model.Graph;

import java.util.ArrayList;
import java.util.List;

public class BadTopologicalSort implements TopologicalSort {

    // TODO alex figure out why this is so slow (probably traversing edges way too many times)
    // TODO alex add to blog post
    public Long[] sort(Graph graph) {
        List<Long> result = new ArrayList<>();
        while (!graph.vertexSet().isEmpty()) {
            for (long j : graph.vertexSet()) {
                if (graph.outDegreeOf(j) == 0) {
                    graph.removeVertex(j);
                    result.add(j);
                    break;
                }
            }
        }
        return new Long[10];
    }
}
