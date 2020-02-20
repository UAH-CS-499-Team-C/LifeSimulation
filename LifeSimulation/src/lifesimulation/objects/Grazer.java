/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

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

     
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.green);
        g.drawRect(x, y, 7, 7);
    }

    @Override
    public void Update(Environement e) {
        x++;
        y++;
    }
}
