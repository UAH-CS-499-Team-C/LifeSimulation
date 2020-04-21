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
import lifesimulation.objects.Grazer;
import lifesimulation.objects.Plant;
import lifesimulation.objects.Predator;

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
            FileWriter writer = new FileWriter("Reports/SimReport_" + timeNow + ".txt", true);
            
            // Write info to file
            writer.write("=============== General Info ===============\n");
            writer.write("XML File Used: " + xmlFilePath + "\n");
            writer.write("Simulation Time: " + e.getTime() + " sec\n");
            writer.write("Number of Obstacles: " + e.getNumObstacles() + "\n");
            writer.write("Number of Plants: " + e.getNumPlants() + "\n");
            writer.write("Number of Grazers: " + e.getNumGrazers() + "\n");
            writer.write("Number of Predators: " + e.getNumPredators() + "\n");
            
            writer.write("\n=============== Plant Info ===============\n");
            for (int i = 0; i < e.getNumPlants(); i++) {
                Plant p = e.getPlants().get(i);
                writer.write("Plant " + i + ":\n");
                writer.write("\tLocation: (" + (int)p.getX() + ", " + (int)p.getY() + ")\n");
                writer.write("\tDiameter: " + p.getDiameter() + "\n");
            }
            
            writer.write("\n=============== Grazer Info ===============\n");
            for (int i = 0; i < e.getNumGrazers(); i++) {
                Grazer g = e.getGrazers().get(i);
                writer.write("Grazer " + i + ":\n");
                writer.write("\tLocation: (" + (int)g.getX() + ", " + (int)g.getY() + ")\n");
                writer.write("\tEnergy: " + g.getEnergy()+ "\n");
            }
            
            writer.write("\n=============== Predator Info ===============\n");
            for (int i = 0; i < e.getNumPredators(); i++) {
                Predator p = e.getPredators().get(i);
                writer.write("Predator " + i + ":\n");
                writer.write("\tLocation: (" + (int)p.getX() + ", " + (int)p.getY() + ")\n");
                writer.write("\tEnergy: " + p.getEnergy()+ "\n");
                writer.write("\tGenotype: " + p.getGenotype() + "\n");
            }
            
            // Close the file
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
