/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

/**
 * Interface that all living, breathing, and moving creatures must implement
 * @author sam
 */
public interface LivingCreature{
    /**
     * Method that all concrete classes must implement
     */
    public void Update();
}
