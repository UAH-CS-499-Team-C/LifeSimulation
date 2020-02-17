/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

/**
 *
 * @author sam
 */
public class Predator extends SimulationObject implements LivingCreature{

    public Predator(float x, float y) {
        super(x, y);
    }

    @Override
    public void Update() {
        // Start Demo Code
        y -= 0.01;
        // End Demo Code
    }
    
}
