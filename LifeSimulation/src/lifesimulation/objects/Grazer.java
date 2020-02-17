/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

/**
 * Implementation class of the Grazer Creature
 * @author sam
 */
public class Grazer extends SimulationObject implements LivingCreature{

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
    public void Update() {
        /*
        Grazer specifc update code goes here
        */
        
        // Start Demo Code
        x += 0.005;
        y += 0.005;
        // End Demo Code
    }
    
}
