/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

/**
 * Simulation Obstacle objects
 * @author sam
 */
public class Obstacle extends SimulationObject{
    /**
     * The size/scale of the obstacle
     */
    private float diameter;
    /**
     * The height of the obstacle
     */
    private float height;

    /**
     * Constructor
     * @param x Starting x location
     * @param y Starting y location
     * @param diameter Size of the obstacle
     * @param height Height of the obstacle
     */
    public Obstacle(float x, float y, float diameter, float height) {
        super(x, y);
        this.diameter = diameter;
        this.height = height;
        this.collision = new Circle(x, y, diameter/2);
    }

    /**
     *
     * @return Size of the obstacle
     */
    public float getDiameter() {
        return diameter;
    }
    
    /**
     *
     * @return Height of the obstacle
     */
    public float getHeight() {
        return height;
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(92, 91, 87));
        g.fill(collision);
        g.setLineWidth(2);
        g.setColor(Color.black);
        g.draw(collision);
    }
    
}
