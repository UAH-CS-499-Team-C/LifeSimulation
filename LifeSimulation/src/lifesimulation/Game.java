/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation;

import lifesimulation.objects.Environement;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 *
 * @author d4g0n
 */
public class Game extends BasicGameState implements InputListener{
    
    /**
     * Background image for simulation
     */
    Image bg;
    /**
     * All objects in the simulation
     */
    Environement environment = new Environement();
    
    public Game(int State) {
        
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
        bg = new Image("images/grid.png");
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
        
        // Draw background
        g.drawImage(bg, 0, 0);
        // Draw each obstacle
        environment.getObstacles().forEach(x -> x.draw(g));
        environment.getGrazers().forEach(x -> x.draw(g));
        environment.getPredators().forEach(x -> x.draw(g));
        
        
        // Testing Code
        g.setColor(Color.white);
        g.drawString("GUI Test Controls:\n[a] Pause the game", 1000, 0);
        if(gc.isPaused()) {
            g.setColor(Color.red);
            g.drawString("Game Paused", 0, 0);
        }
        // End Testing Code
        
    }
    
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if(!gc.isPaused()) {
            environment.getGrazers().forEach(x -> x.Update(environment));
            environment.getPredators().forEach(x -> x.Update(environment));
        }
    }
    
    @Override
    public int getID() {
        return 0;
    }
    
}
