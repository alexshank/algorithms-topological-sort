package org.example.model;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class RunRecord {

    private RunType type;
    private long timeCount; // milliseconds
    private long vertexCount;
    private long edgeCount;

    public RunRecord(RunType type, long timeCount, DefaultDirectedGraph<Long, DefaultEdge> dag) {
        this.type = type;
        this.timeCount = timeCount / 1_000_000; // milliseconds
        this.vertexCount = dag.vertexSet().size();
        this.edgeCount = dag.edgeSet().size();
    }

    public static String getHeaderRow(){
        return "type,timeCount,vertexCount,edgeCount";
    }

    @Override
    public String toString() {
        return this.type + "," + this.timeCount + "," + this.vertexCount + "," + this.edgeCount;
    }
}
