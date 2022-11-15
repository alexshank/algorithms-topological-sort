package org.example.sorting;

import org.example.model.Graph;

public interface TopologicalSort {
    Long[] sort(Graph dag);
}
