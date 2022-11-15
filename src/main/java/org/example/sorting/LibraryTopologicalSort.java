package org.example.sorting;

import org.example.model.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

public class LibraryTopologicalSort implements TopologicalSort {

    public Long[] sort(Graph dag){
        TopologicalOrderIterator<Long, DefaultEdge> iter =
                new TopologicalOrderIterator<>(dag);
        Long[] result = new Long[dag.vertexSet().size()];
        int count = 0;
        while (iter.hasNext()) {
            long v = iter.next();
            result[count++] = v;
        }
        return result;
    }

}
