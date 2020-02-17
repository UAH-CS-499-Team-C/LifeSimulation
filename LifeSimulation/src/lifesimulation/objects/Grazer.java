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
public class Grazer extends SimulationObject implements LivingCreature{

    public Grazer(float x, float y) {
        super(x, y);
    }

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
