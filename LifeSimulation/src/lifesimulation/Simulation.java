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
import org.lwjgl.input.Mouse;

/**
 *
 * @author d4g0n
 */
public class Simulation extends BasicGameState implements InputProviderListener{
    
    /**
     * Background image for simulation
     */
    Image bg;
    Image pause;
    Image x1;
    Image x10;
    Image x100;
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
        pause = new Image("images/pause.png");
        x1 = new Image("images/x1.png");
        x10 = new Image("images/x10.png");
        x100 = new Image("images/x100.png");
        
        
        environment = new Environment();
        
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
        g.setColor(Color.white);
        //g.drawString("GUI Test Controls:\n[a] Pause the simulation", 1000, 0);
        g.drawImage(pause, 1000, 0);
        //g.drawString("[1] 1x speed", 1000, 75);
        g.drawImage(x1,1000, 100);
        //g.drawString("[2] 10x speed", 1000, 100);
        g.drawImage(x10, 1000, 135);
        //g.drawString("[3] 100x speed", 1000, 125);
        g.drawImage(x100, 1000, 165);
        g.drawString("Number of Plants: "  + environment.getNumPlants(), 1000, 195);
        g.drawString("Number of Grazers: " + environment.getNumGrazers(), 1000, 220);
        g.drawString("Number of Predators: " + environment.getNumPredators(), 1000, 245);
        if(paused) {
            g.setColor(Color.red);
            g.drawString("Game Paused", 0, 0);
        }
        
        switch (timeSpeed) {
            case 1:
                g.setColor(Color.red);
                g.drawString("1x speed", 1000, 75);
                break;
            case 10:
                g.setColor(Color.red);
                g.drawString("10x speed", 1000, 75);
                break;
            default:
                g.setColor(Color.red);
                g.drawString("100x speed", 1000, 75);
                break;
        }
        // End Testing Code
        
    }
    
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if(!paused) {
            environment.getPlants().forEach(x -> x.Update(environment));
            environment.getGrazers().forEach(x -> x.Update(environment));
            environment.getPredators().forEach(x -> x.Update(environment));
            
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
   /* @Override
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
    public void controlReleased(Command cmnd) {}*/
    
    @Override
    public void mouseClicked(int button, int x, int y, int ClickCount){
        
        if(button == Input.MOUSE_LEFT_BUTTON){
            
            if((x >1000 && x < 1000 + pause.getWidth()) && (y > 0 && y < 0 + pause.getHeight()) && !paused){
                paused = true;
            }
            else if((x >1000 && x < 1000 + pause.getWidth()) && (y > 0 && y < 0 + pause.getHeight()) && paused){
                paused = false;
            }
            else if((x > 1000 && x < 1000 + x1.getWidth()) && (y > 100 && y < 100 + x1.getHeight())){
                timeSpeed = 1;
                logicNeedUpdate = true;
            }
            else if((x > 1000 && x < 1000 + x10.getWidth()) && (y > 135 && y < 135 + x10.getHeight())){
                timeSpeed = 10;
                logicNeedUpdate = true;
            }
            else if((x > 1000 && x < 1000 + x100.getWidth()) && (y > 165 && y < 165 + x100.getHeight())){
                timeSpeed = 100;
                logicNeedUpdate = true;
            }
            
        }
        
    }
}
