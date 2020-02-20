/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation;

import java.util.ArrayList;
import lifesimulation.objects.Grazer;

import lifesimulation.objects.Obstacle;
import lifesimulation.objects.Predator;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import pkgLifeSimDataParser.LifeSimDataParser;

/**
 *
 * @author d4g0n
 */
public class Game extends BasicGameState{
    
    Image bg;
    ArrayList<Obstacle> obstacles = new ArrayList<>();
    ArrayList<Grazer> grazers = new ArrayList<>();
    ArrayList<Predator> predators = new ArrayList<>();
    
    public Game(int State) {
        
        // Setup parser
        LifeSimDataParser lsdp = LifeSimDataParser.getInstance();
        
        // Load all obstacles from parser
        int iObstacleCount = lsdp.getObstacleCount();
        for(int i=0; i< iObstacleCount; i++)
        {
            if(lsdp.getObstacleData()) {
                obstacles.add(new Obstacle(lsdp.ObstacleX, lsdp.ObstacleY, lsdp.ObstacleDiameter, lsdp.ObstacleHeight));
            } else {
                System.out.println("Error reading data for obstacle " + i);
            }
        }
        
        grazers.add(new Grazer(0, 0));
        
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
        obstacles.forEach(x -> x.draw(g));
        grazers.forEach(x -> x.draw(g));
        predators.forEach(x -> x.draw(g));
        
        
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
            grazers.forEach(x -> x.Update(obstacles, grazers, predators));
            predators.forEach(x -> x.Update(obstacles, grazers, predators));
        }
        gc.pause();
    }
    
    @Override
    public int getID() {
        return 0;
    }
    
}
