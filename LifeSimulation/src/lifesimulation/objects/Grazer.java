/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

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
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.green);
        g.drawRect(x, y, 7, 7);
    }
}
