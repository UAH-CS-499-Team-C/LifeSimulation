/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

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

    /**
     * Grazer implementation of the update function
     */
    @Override
    public void Update() {
        // Start Demo Code
        y -= 0.01;
        // End Demo Code
    }
    
}
