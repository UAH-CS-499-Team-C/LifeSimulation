/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.utilities;

import java.io.FileWriter;
import java.io.IOException;

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
            FileWriter writer = new FileWriter("../Reports/SimReport.txt", true);
            writer.write(xmlFilePath + "\n");
            writer.write("Good Bye!");
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
