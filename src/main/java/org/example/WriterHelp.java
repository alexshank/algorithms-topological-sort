package org.example;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.example.model.RunRecord;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class WriterHelp {

    private static String OUTPUT_DIR = Paths.get("").toAbsolutePath() + "/output/";

    // write a CSV of runtimes to the given file in the "output" directory
    public static void writeRecordsToCsv(
            List<RunRecord> runRecords,
            String fileName
    ) throws IOException {
        new File(OUTPUT_DIR).mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_DIR + fileName));
        writer.write(RunRecord.getHeaderRow() + "\n");
        for (RunRecord r : runRecords) {
            writer.write(r.toString() + "\n");
        }

        writer.close();
    }

    // write a PNG of the given graph to the given file in the "output" directory
    private static void writeGraphToFile(
            DefaultDirectedGraph<Long, DefaultEdge> graph,
            String fileName
    ) throws IOException {
        JGraphXAdapter<Long, DefaultEdge> graphAdapter = new JGraphXAdapter<Long, DefaultEdge>(graph);
        // we don't need edge labels on our graph
        graphAdapter.getEdgeToCellMap().forEach((edge, cell) -> cell.setValue(null));

        mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        BufferedImage image = mxCellRenderer.createBufferedImage(
                graphAdapter, null, 2, Color.BLACK, true, null
        );

        new File(OUTPUT_DIR).mkdirs();
        File imgFile = new File(OUTPUT_DIR + fileName);
        imgFile.createNewFile();
        ImageIO.write(image, "PNG", imgFile);
    }



}