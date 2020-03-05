/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.utilities;

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
        this.xmlFilePath = s;
    }
    
    public void Generate(Environment e) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH_mm_ss");
            LocalDateTime now = LocalDateTime.now();
            String timeNow = dtf.format(now);
            
            FileWriter writer = new FileWriter("../Reports/SimReport_" + timeNow + ".txt", true);
            writer.write(xmlFilePath + "\n");
            writer.write("Good Bye!");
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
