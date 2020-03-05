/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation;


import lifesimulation.utilities.Environment;
import lifesimulation.utilities.SimReportGenerator;
import org.newdawn.slick.*;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.*;

/**
 *
 * @author d4g0n
 */
public class Simulation extends BasicGameState{
    
    /**
     * Background image for simulation
     */
    Image bg;
    Image pause;
    Image play;
    Image x1;
    Image x10;
    Image x100;
    /**
     * All objects in the simulation
     */
    Environment environment;
    
    private boolean paused = true;
    private int timeSpeed = 1;
    private boolean logicNeedUpdate = false;
    private int t;
    
    // Temporary Keyboard Inputs
    private InputProvider provider;
    
    public Simulation(int State) {
        
    }
    
   
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
        
        bg = new Image("images/grid.png");
        pause = new Image("images/button_pause.png");
        play = new Image("images/button_play.png");
        x1 = new Image("images/button_1x-speed.png");
        x10 = new Image("images/button_10x-speed.png");
        x100 = new Image("images/button_100x-speed.png");
        
        t = 0;
        
        environment = new Environment();
        
        
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
        
        
        // ============================ UI Code ==================================
        // Draw background rect
        g.setColor(Color.black);
        g.fillRect(1000, 0, 250, 750);
        
        // Draw pause/play button
        if(paused){
            g.drawImage(play, 1000, 0);
        } else {
            g.drawImage(pause, 1000, 0);
        }
        
        // Draw time
        g.setColor(Color.white);
        g.drawString("Seconds passed: " + t, 1000, 75);
        
        // Draw number of each object
        g.setColor(Color.white);
        g.drawString("Number of Plants: "  + environment.getNumPlants(), 1000, 125);
        g.drawString("Number of Grazers: " + environment.getNumGrazers(), 1000, 150);
        g.drawString("Number of Predators: " + environment.getNumPredators(), 1000, 175);
        
        // Draw time controls
        g.drawImage(x1, 1000, 250);
        g.drawImage(x10, 1000, 305);
        g.drawImage(x100, 1000, 360);
        switch(timeSpeed){
            case 1:
                g.drawString("<-", 1155, 270);
                break;
            case 10:
                g.drawString("<-", 1155, 325);
                break;
            case 100:
                g.drawString("<-", 1155, 380);
        }
        
        // Draw key
        g.setColor(Color.white);
        g.drawString("--- Key ---", 1000, 500);
        g.drawString("Grazer:", 1000, 525);
        g.setColor(Color.blue);
        g.fill(new Circle(1100, 535, 7, 7));
        g.setColor(Color.white);
        g.drawString("Predator:", 1000, 550);
        g.setColor(Color.red);
        g.fill(new Circle(1100, 560, 7, 7));
        
    }
    
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if(!paused) {
            t++;
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

   

    // Control the simulation via GUI buttons
    
    @Override
    public void mouseClicked(int button, int x, int y, int ClickCount){
        
        if(button == Input.MOUSE_LEFT_BUTTON){
            
            if((x >1000 && x < 1000 + pause.getWidth()) && (y > 0 && y < 0 + pause.getHeight()) && !paused){
                paused = true;
            }
            else if((x >1000 && x < 1000 + pause.getWidth()) && (y > 0 && y < 0 + pause.getHeight()) && paused){
                paused = false;
            }
            else if((x > 1000 && x < 1000 + x1.getWidth()) && (y > 250 && y < 250 + x1.getHeight())){
                timeSpeed = 1;
                logicNeedUpdate = true;
            }
            else if((x > 1000 && x < 1000 + x10.getWidth()) && (y > 305 && y < 305 + x10.getHeight())){
                timeSpeed = 10;
                logicNeedUpdate = true;
            }
            else if((x > 1000 && x < 1000 + x100.getWidth()) && (y > 360 && y < 360 + x100.getHeight())){
                timeSpeed = 100;
                logicNeedUpdate = true;
            }
            
        }
        
    }
}
