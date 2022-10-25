package org.example;


import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting application...");

        DefaultDirectedGraph<String, DefaultEdge> g =
                new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

        String x1 = "x1";
        String x2 = "x2";
        String x3 = "x3";

        g.addVertex(x1);
        g.addVertex(x2);
        g.addVertex(x3);

        g.addEdge(x1, x2);
        g.addEdge(x2, x3);
        g.addEdge(x3, x1);

        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(g);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(
                graphAdapter, null, 2, Color.WHITE, true, null
        );

        System.out.println(Paths.get("").toAbsolutePath() + "/output");
        new File(Paths.get("").toAbsolutePath() + "/output").mkdirs();
        File imgFile = new File(Paths.get("").toAbsolutePath() + "/output/example.png");
        imgFile.createNewFile();
        ImageIO.write(image, "PNG", imgFile);
    }
}
