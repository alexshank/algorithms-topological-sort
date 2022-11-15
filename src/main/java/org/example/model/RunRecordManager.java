package org.example.model;

import org.example.WriterHelp;

import java.util.ArrayList;
import java.util.List;

public class RunRecordManager {

    private List<RunRecord> runRecords = new ArrayList<>();

    public RunRecordManager() {}

    public void addRecord(RunType runType, long elapsedTime, Graph dag){
        this.runRecords.add(new RunRecord(runRecords.size(), runType, elapsedTime, dag));
    }

    public static String getHeaderRow(){
        return "runNumber, type,timeCount,vertexCount,edgeCount";
    }

    public void printRunRecords(){
        System.out.println();
        System.out.println(getHeaderRow());
        for (RunRecord record : runRecords) {
            System.out.println(record.toString());
        }
    }

    public void writeRecordsToCsv(){
        WriterHelp.writeRecordsToCsv(runRecords, "many_edges_data_01.csv");
    }
}
