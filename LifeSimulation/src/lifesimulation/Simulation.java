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
public class Simulation extends BasicGameState implements InputProviderListener{
    
    /**
     * Background image for simulation
     */
    Image bg;
    /**
     * All objects in the simulation
     */
    Environment environment;
    
    private boolean paused = false;
    private int timeSpeed = 1;
    private boolean logicNeedUpdate = false;
    
    // Temporary Keyboard Inputs
    private InputProvider provider;
    private final Command pauseCommand = new BasicCommand("pauseCommand");
    private final Command time1Command = new BasicCommand("time1Command");
    private final Command time10Command = new BasicCommand("time10Command");
    private final Command time100Command = new BasicCommand("time100Command");
    
    public Simulation(int State) {
        
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
        bg = new Image("images/grid.png");
        
        environment = Environment.GetInstance();
        
        // Temporary Keyboard Inputs
        provider = new InputProvider(gc.getInput());
	provider.addListener(this);
        provider.bindCommand(new KeyControl(Input.KEY_A), pauseCommand);
        provider.bindCommand(new KeyControl(Input.KEY_1), time1Command);
        provider.bindCommand(new KeyControl(Input.KEY_2), time10Command);
        provider.bindCommand(new KeyControl(Input.KEY_3), time100Command);
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
        g.setColor(Color.black);
        g.drawRect(1001, 0, 350, 750);
        g.setColor(Color.white);
        g.drawString("GUI Test Controls:\n[a] Pause the simulation", 1000, 0);
        g.drawString("[1] 1x speed", 1000, 75);
        g.drawString("[2] 10x speed", 1000, 100);
        g.drawString("[3] 100x speed", 1000, 125);
        g.drawString("Number of Plants: "  + environment.getNumPlants(), 1000, 175);
        g.drawString("Number of Grazers: " + environment.getNumGrazers(), 1000, 200);
        g.drawString("Number of Predators: " + environment.getNumPredators(), 1000, 225);
        if(paused) {
            g.setColor(Color.red);
            g.drawString("Game Paused", 0, 0);
        }
        
        switch (timeSpeed) {
            case 1:
                g.setColor(Color.red);
                g.drawString("[1] 1x speed", 1000, 75);
                break;
            case 10:
                g.setColor(Color.red);
                g.drawString("[2] 10x speed", 1000, 100);
                break;
            default:
                g.setColor(Color.red);
                g.drawString("[3] 100x speed", 1000, 125);
                break;
        }
        // End Testing Code
        
    }
    
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if(!paused) {
            environment.update();
            
            // If speed needs updated
            if(logicNeedUpdate) {
                gc.setMinimumLogicUpdateInterval(1000/timeSpeed);
                logicNeedUpdate = false;
            }
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
        // Temporary Keyboard Inputs
        if (cmnd == pauseCommand && !paused) {
            paused = true;
        } else if (cmnd == pauseCommand && paused) {
            paused = false;
        } else if (cmnd == time1Command) {
            timeSpeed = 1;
            logicNeedUpdate = true;
        } else if (cmnd == time10Command) {
            timeSpeed = 10;
            logicNeedUpdate = true;
        } else if (cmnd == time100Command) {
            timeSpeed = 100;
            logicNeedUpdate = true;
        }
    }

    @Override
    public void controlReleased(Command cmnd) {}
}
