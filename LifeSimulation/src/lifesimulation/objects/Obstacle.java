/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Simulation Obstacle objects
 * @author sam
 */
public class Obstacle extends SimulationObject{
    /**
     * The size/scale of the obstacle
     */
    private float size;

    /**
     * Constructor
     * @param x Starting x location
     * @param y Starting y location
     * @param size Size of the obstacle
     */
    public Obstacle(float x, float y, float size) {
        super(x, y);
        this.size = size;
    }

    /**
     *
     * @return Size of the obstacle
     */
    public float getSize() {
        return size;
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillOval(x, y, size, size);
    }
    
}
