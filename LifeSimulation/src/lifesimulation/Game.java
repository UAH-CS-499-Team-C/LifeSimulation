/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation;

import java.util.ArrayList;
import java.util.Random;

import lifesimulation.objects.Grazer;
import lifesimulation.objects.Obstacle;
import lifesimulation.objects.Predator;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 *
 * @author d4g0n
 */
public class Game extends BasicGameState{
    
    // Start Demo Code
    ArrayList<Obstacle> obstacles = new ArrayList<>(); // Demo Code
    ArrayList<Grazer> grazers = new ArrayList<>(); // Demo Code
    ArrayList<Predator> predators = new ArrayList<>(); // Demo Code
    // End Demo Code
    
    public Game(int State) {
        // Start Demo Code

        Random r = new Random();
        
        for(int i=0; i < 10; i++) {
            int x = r.nextInt(693);
            int y = r.nextInt(493);
            int s = r.nextInt(20) + 10;
            obstacles.add(new Obstacle(x, y, s));
        }
        
        for(int i=0; i < 10; i++) {
            int x = r.nextInt(693);
            int y = r.nextInt(493);
            grazers.add(new Grazer(x, y));
        }
        
        for(int i=0; i < 10; i++) {
            int x = r.nextInt(700);
            int y = r.nextInt(500);
            predators.add(new Predator(x, y));
        }
        
        // End Demo Code
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
        
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
        
        /* Original code
        g.drawString("A Game of Life", 250, 0);
        g.drawOval(250, 100, 70, 70);
        */
        
        // Start Demo Code
        grazers.forEach(x -> x.draw(g));
        predators.forEach(x -> x.draw(g));
        obstacles.forEach(x -> x.draw(g));
        // End Demo Code
        
    }
    
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        
        // Start Demo Code
        grazers.forEach(x -> x.Update(obstacles, grazers, predators));
        predators.forEach(x -> x.Update(obstacles, grazers, predators));
        // End Demo Code
    }
    
    @Override
    public int getID() {
        return 0;
    }
    
}
