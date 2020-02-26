/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

/**
 * Class which all objects in the simulation shall extend
 * @author sam
 */
public abstract class SimulationObject {
    /**
     * Location x value
     */
    protected float x;
    /**
     * Location y value
     */
    protected float y;
    
    /**
     * Collision shape of the object
     */
    protected Shape collision;
    
    /**
     * Constructor class for basic SimulationObject
     * @param x Starting x value
     * @param y Starting y value
     */
    public SimulationObject(float x, float y){
        this.x = x;
        this.y = y;
    }

    /**
     * 
     * @return Location x component
     */
    public float getX() {
        return x;
    }

    /**
     *
     * @return Location y component
     */
    public float getY() {
        return y;
    }
    
    /**
     * Function that each child class must implement
     * @param g Graphics object to be drawn to
     */
    public abstract void draw(Graphics g);
 }
