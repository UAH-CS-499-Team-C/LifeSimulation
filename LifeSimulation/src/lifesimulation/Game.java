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
public class Game extends BasicGameState{
    
    public Game(int State) {
        
    }
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
        
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
        
        g.drawString("A Game of Life", 250, 0);
        g.drawOval(250, 100, 70, 70);
    }
    
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        
    }
    
    public int getID() {
        return 0;
    }
    
}
