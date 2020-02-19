/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

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
        
        try {
            AppGameContainer appgc = new AppGameContainer(new LifeSimulation(gameName));
            
            appgc.setDisplayMode(700, 500, false);
            
            // Start Demo Code
            //appgc.setShowFPS(false); // Turn off FPS counter
            appgc.setMaximumLogicUpdateInterval(1000); // Max. 1000 miliseconds can pass
            appgc.setMinimumLogicUpdateInterval(1000/gameSpeed); // Min. 10 miliseconds must pass
            // End Demo Code
            
            appgc.start();
            
        } catch(SlickException e){
            e.printStackTrace();
        }
        
    }
    
}
