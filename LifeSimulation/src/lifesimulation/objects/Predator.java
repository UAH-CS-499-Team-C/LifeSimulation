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
 * Implementation class of the Predator Creature
 * @author sam
 */
public class Predator extends SimulationObject implements LivingCreature{

    /**
     * 
     * @param x Starting x location
     * @param y Starting y location
     */
    public Predator(float x, float y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(x, y, 7, 7);
    }

    @Override
    public void Update(Environement e) {
        
    }
    
}
