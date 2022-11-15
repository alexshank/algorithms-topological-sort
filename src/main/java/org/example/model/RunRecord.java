package org.example.model;

public class RunRecord {

    private RunType type;
    private long timeCount; // milliseconds
    private long vertexCount;
    private long edgeCount;

    // TODO alex convert to RunRecordManager and privatize the RunRecord as a Java 14 Record
    public RunRecord(RunType type, long timeCount, Graph dag) {
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
