/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import java.util.ArrayList;

/**
 * Interface that all living, breathing, and moving creatures must implement
 * @author sam
 */
public interface LivingCreature{
    /**
     * 
     * @param o All obstacle objects
     * @param g All grazer object
     * @param p All predator objects
     */
    public void Update(ArrayList<Obstacle> o, ArrayList<Grazer> g, ArrayList<Predator> p);
}
