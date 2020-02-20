/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation;

import javax.swing.JFileChooser;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import pkgLifeSimDataParser.LifeSimDataParser;

/**
 * The Primary class of the simulation.
 * Creates important variables then loads up a {@link lifesimulation.Game} object
 * @author d4g0n
 */
public class LifeSimulation extends StateBasedGame{

    public static String gameName = "Coleman's Game of Life";
    public int gameState = 0;
    
    public static int gameSpeed = 100; // Demo Code
    
    public LifeSimulation(String gameName){
        super(gameName);
        this.addState(new Game(gameState)); 
    }
    
    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        this.getState(gameState).init(gc, this);
        this.enterState(gameState);
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
            AppGameContainer appgc = new AppGameContainer(new LifeSimulation(gameName));
            
            // Set the width/height to values from parser
            appgc.setDisplayMode((int)lsdp.getWorldWidth(), (int)lsdp.getWorldHeight(), false);
            
            appgc.setShowFPS(false); // Turn off FPS counter
            appgc.setMaximumLogicUpdateInterval(1000); // Max. 1000 miliseconds can pass
            appgc.setMinimumLogicUpdateInterval(1000/gameSpeed); // Min. 10 miliseconds must pass
            
            // Start the app
            appgc.start();
            
        } catch(SlickException e){
            e.printStackTrace();
        }
        
    }
    
}
