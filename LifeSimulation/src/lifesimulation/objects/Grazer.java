/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import java.util.ArrayList;

/**
 * Implementation class of the Grazer Creature
 * @author sam
 */
public class Grazer extends SimulationObject implements LivingCreature{
    
    /**
     * Grazer Movement direction variables
     */
    int xDirection = 1, yDirection = 1;
    
    /**
     * 
     * @param x Starting x location
     * @param y Starting y location
     */
    public Grazer(float x, float y) {
        super(x, y);
    }

     /**
     * Grazer implementation of the update function
     */
    @Override
    public void Update(ArrayList<Obstacle> o, ArrayList<Grazer> g, ArrayList<Predator> p) {
        /*
        Grazer specifc update code goes here
        */
        
        // Start Demo Code
        x += 5 * xDirection;
        y += 5 * yDirection;
        
        if(x+7 >= 700 || x <= 0){
            xDirection *= -1;
        }
        if(y+7 >= 500 || y <= 0){
            yDirection *= -1;
        }
        
        for(Obstacle q : o){
            if(x+7 >= q.getX() && x <= q.getX()+q.getSize() && y+7 >= q.getY() && y <= q.getY()+q.getSize()){
                xDirection *= -1;
                yDirection *= -1;
            }
        }
        // End Demo Code
    }
}
