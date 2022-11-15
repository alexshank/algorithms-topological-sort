package org.example.model;

public class RunRecord {

    private int runNumber;
    private RunType type;
    private long timeCount; // milliseconds
    private long vertexCount;
    private long edgeCount;

    public RunRecord(int runNumber, RunType type, long timeCount, Graph dag) {
        this.runNumber = runNumber;
        this.type = type;
        this.timeCount = timeCount / 1_000_000; // milliseconds
        this.vertexCount = dag.vertexSet().size();
        this.edgeCount = dag.edgeSet().size();
    }

    @Override
    public String toString() {
        return this.runNumber + "," + this.type + "," + this.timeCount + "," + this.vertexCount + "," + this.edgeCount;
    }
}
