/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation;

import javax.swing.JFileChooser;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import lifesimulation.utilities.LifeSimDataParser;

/**
 * The Primary class of the simulation.
 * Creates important variables then loads up a {@link lifesimulation.Simulation} object
 * @author d4g0n
 */
public class LifeSimulation extends StateBasedGame{

    public static String gameName = "Coleman's Game of Life";
    public int gameState = 0;
    
    
   
    
    // Testing Code
    static int bonusWidth = 250;
    static int bonusHeight = 50;
    // End Testing Code
    
    public LifeSimulation(String gameName, String s){
        super(gameName);
        this.addState(new Simulation(gameState, s)); 
    }
    
    
    
    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        // Turns out the following code shouldn't be here
        // This causes the init function to be called twice (which is wrong)
        // Refer to this:
        // http://www.java-gaming.org/index.php/topic,26825
        
        //this.getState(gameState).init(gc, this);
        //this.enterState(gameState);
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        
        // Create a file chooser window
        final JFileChooser fc = new JFileChooser(System.getProperty("user.dir") + "/..");
        String filePath = ""; // xml filePath to be recorded shortly
        int returnVal = fc.showDialog(fc, "Open");
        if (returnVal == JFileChooser.APPROVE_OPTION) { // If we selected a file
            filePath = fc.getSelectedFile().getAbsolutePath(); // Get it's absolute path
        } else { // If we didn't select a file
            System.exit(0); // Close immediately
        }
        
        // Setup Parser Stuff
        LifeSimDataParser lsdp = LifeSimDataParser.getInstance();
        lsdp.initDataParser(filePath);
        
        
        
        // Slick Game code
        try {
            // Create an app container
            AppGameContainer appgc = new AppGameContainer(new LifeSimulation(gameName, filePath));
            
            // Set the width/height to values from parser
            appgc.setDisplayMode((int)lsdp.getWorldWidth() + bonusWidth, (int)lsdp.getWorldHeight() + bonusHeight, false);
            
            appgc.setShowFPS(false); // Turn off FPS counter
            appgc.setMaximumLogicUpdateInterval(Integer.MAX_VALUE); // Max. 1000 miliseconds can pass
            appgc.setMinimumLogicUpdateInterval(1000); // Min. 10 miliseconds must pass
            
            // Start the app
            appgc.start();
            
        } catch(SlickException e){
            e.printStackTrace();
        }
        
    }
    
}
