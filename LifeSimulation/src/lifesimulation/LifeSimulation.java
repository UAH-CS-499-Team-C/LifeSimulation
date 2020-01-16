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

    public static String gameName = "A Game of Life";
    public int gameState = 0;
    
    public LifeSimulation(String gameName){
        super(gameName);
        this.addState(new Game(gameState)); 
    }
    
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
            appgc.start();
            
        } catch(SlickException e){
            e.printStackTrace();
        }
        
    }
    
}
