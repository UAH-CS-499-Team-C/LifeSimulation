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
    
    ArrayList<Obstacle> obstacles = new ArrayList<>(); // Demo Code
    ArrayList<Grazer> grazers = new ArrayList<>(); // Demo Code
    ArrayList<Predator> predators = new ArrayList<>(); // Demo Code
    
    public Game(int State) {
        // Start Demo Code

        Random r = new Random();
        
        for(int i=0; i < 10; i++) {
            int x = r.nextInt(700);
            int y = r.nextInt(500);
            int s = r.nextInt(20) + 10;
            obstacles.add(new Obstacle(x, y, s));
        }
        
        for(int i=0; i < 10; i++) {
            int x = r.nextInt(700);
            int y = r.nextInt(500);
            grazers.add(new Grazer(x, y));
        }
        
        for(int i=0; i < 10; i++) {
            int x = r.nextInt(700);
            int y = r.nextInt(500);
            predators.add(new Predator(x, y));
        }
        
        // End Demo Code
    }
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
        
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
        
        /* Original code
        g.drawString("A Game of Life", 250, 0);
        g.drawOval(250, 100, 70, 70);
        */
        
        // Start Demo Code
        
        for (Grazer q : grazers){
            g.setColor(Color.green);
            g.drawRect(q.getX(), q.getY(), 7, 7);
            g.setColor(Color.white);
        }
        
        for (Predator q : predators){
            g.setColor(Color.red);
            g.drawRect(q.getX(), q.getY(), 7, 7);
            g.setColor(Color.white);
        }
        
        for (Obstacle q : obstacles){
            g.drawOval(q.getX(), q.getY(), q.getSize(), q.getSize());
        }
        
        // End Demo Code
        
    }
    
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        
        // Start Demo Code
        grazers.forEach(x -> x.Update());
        predators.forEach(x -> x.Update());
        // End Demo Code
    }
    
    public int getID() {
        return 0;
    }
    
}
