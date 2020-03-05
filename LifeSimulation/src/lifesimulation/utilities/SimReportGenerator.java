/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Frame that contains the printout information about the simulation
 * @author sam
 */
public class SimReportGenerator {

    String xmlFilePath;
    
    public SimReportGenerator(String s) {
        try {
            // Get cleaned up path to xml file
            this.xmlFilePath = new File(s).getCanonicalPath();
        } catch (Exception x) {}
    }
    
    public void Generate(Environment e) {
        try {
            // Get the local time
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH_mm_ss");
            LocalDateTime now = LocalDateTime.now();
            String timeNow = dtf.format(now);
            
            // Make the file with proper name
            FileWriter writer = new FileWriter("../Reports/SimReport_" + timeNow + ".txt", true);
            
            // Write info to file
            writer.write("=============== General Info ===============\n");
            writer.write("XML File Used: " + xmlFilePath + "\n");
            writer.write("Simulation Time: " + e.getTime() + " sec\n");
            writer.write("Number of Obstacles: " + e.getNumObstacles() + "\n");
            writer.write("Number of Plants: " + e.getNumPlants() + "\n");
            writer.write("Number of Grazers: " + e.getNumGrazers() + "\n");
            writer.write("Number of Predators: " + e.getNumPredators() + "\n");
            
            writer.write("\n=============== Obstacle Info ===============\n");
            for (int i = 0; i < e.getNumObstacles(); i++) {
                writer.write("Obstacle " + i + ":\n");
                writer.write("\t" + e.getObstacles().get(i).toString() + "\n");
            }
            
            // Close the file
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
