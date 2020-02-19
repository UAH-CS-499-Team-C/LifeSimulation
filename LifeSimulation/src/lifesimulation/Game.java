/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation;

import java.util.ArrayList;

import lifesimulation.objects.Obstacle;

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
    
    public Game(int State) {
        
        // Setup code
        LifeSimDataParser lsdp = LifeSimDataParser.getInstance();
        int iObstacleCount = lsdp.getObstacleCount();
        for(int i=0; i< iObstacleCount; i++)
        {
            if(lsdp.getObstacleData()) {
                obstacles.add(new Obstacle(lsdp.ObstacleX, lsdp.ObstacleY, lsdp.ObstacleDiameter, lsdp.ObstacleHeight));
            } else {
                System.out.println("Error reading data for obstacle " + i);
            }
        }
        
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
        
    }
    
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        
        
    }
    
    @Override
    public int getID() {
        return 0;
    }
    
}
