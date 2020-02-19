/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation;

import java.io.File;
import javax.swing.JFileChooser;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import pkgLifeSimDataParser.LifeSimDataParser;

/**
 *
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
        
        // Create a file chooser
        final JFileChooser fc = new JFileChooser();
        // Get the selected file's name
        String filePath = "";
        int returnVal = fc.showDialog(fc, "Open");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            filePath = file.getAbsolutePath();
        } else {
            System.exit(0);
        }
        System.err.println(filePath);
        
        // Setup Parser Stuff
        LifeSimDataParser lsdp = LifeSimDataParser.getInstance();
        lsdp.initDataParser(filePath);
        
        // Slick Game code
        try {
            AppGameContainer appgc = new AppGameContainer(new LifeSimulation(gameName));
            
            appgc.setDisplayMode((int)lsdp.getWorldWidth(), (int)lsdp.getWorldHeight(), false);
            
            // Start Demo Code
            appgc.setShowFPS(false); // Turn off FPS counter
            appgc.setMaximumLogicUpdateInterval(1000); // Max. 1000 miliseconds can pass
            appgc.setMinimumLogicUpdateInterval(1000/gameSpeed); // Min. 10 miliseconds must pass
            // End Demo Code
            
            appgc.start();
            
        } catch(SlickException e){
            e.printStackTrace();
        }
        
    }
    
}
