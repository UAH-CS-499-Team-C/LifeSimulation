/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation;

import lifesimulation.objects.Environment;
import org.newdawn.slick.*;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.InputProviderListener;
import org.newdawn.slick.command.KeyControl;
import org.newdawn.slick.state.*;

/**
 *
 * @author d4g0n
 */
public class Game extends BasicGameState implements InputProviderListener{
    
    /**
     * Background image for simulation
     */
    Image bg;
    /**
     * All objects in the simulation
     */
    Environment environment;
    
    // Testing Code
    private boolean paused = false;
    private InputProvider provider;
    private final Command pauseCommand = new BasicCommand("pauseCommand");
    private final Command resetCommand = new BasicCommand("resetCommand");
    // End Testing Code
    
    public Game(int State) {
        
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
        bg = new Image("images/grid.png");
        
        environment = new Environment();
        
        provider = new InputProvider(gc.getInput());
	provider.addListener(this);
        provider.bindCommand(new KeyControl(Input.KEY_A), pauseCommand);
        provider.bindCommand(new KeyControl(Input.KEY_R), resetCommand);
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
        
        // Draw background
        g.drawImage(bg, 0, 0);
        // Draw each obstacle
        environment.getObstacles().forEach(x -> x.draw(g));
        environment.getPlants().forEach(x -> x.draw(g));
        environment.getGrazers().forEach(x -> x.draw(g));
        environment.getPredators().forEach(x -> x.draw(g));
        
        
        // Testing Code
        g.setColor(Color.white);
        g.drawString("GUI Test Controls:\n[a] Pause the simulatio \n[r] Reset the simulation", 1000, 0);
        if(paused) {
            g.setColor(Color.red);
            g.drawString("Game Paused", 0, 0);
        }
        // End Testing Code
        
    }
    
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if(!paused) {
            environment.getPlants().forEach(x -> x.Update(environment));
            environment.getGrazers().forEach(x -> x.Update(environment));
            environment.getPredators().forEach(x -> x.Update(environment));
        }
    }
    
    @Override
    public int getID() {
        return 0;
    }

    /**
     * Watches for user input.
     * Allows us to pause the game.
     * @param cmnd 
     */
    @Override
    public void controlPressed(Command cmnd) {
        if (cmnd == pauseCommand && !paused) {
            paused = true;
        } else if (cmnd == pauseCommand && paused) {
            paused = false;
        } else if (cmnd == resetCommand) {
            environment = new Environment();
        }
    }

    @Override
    public void controlReleased(Command cmnd) {
        
    }
    
}
