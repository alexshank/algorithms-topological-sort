package org.example.sorting;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public interface TopologicalSort {
    Long[] sort(DefaultDirectedGraph<Long, DefaultEdge> dag);
}
