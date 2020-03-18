/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;
import lifesimulation.utilities.Environment;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;

/**
 * Implementation class of the Predator Creature
 * @author sam
 */
public class Predator extends SimulationObject implements LivingCreature{
    
    private int EU;
    private String genotype;
    private float maxSpeedHOD;
    private float maxSpeedHED;
    private float maxSpeedHOR;
    private float maintainSpeed;
    private float energyOutput;
    private float energyToReProduce;
    private int maxOffspring;
    private float gestaion;
    private float offspringEnergy;
    
    private HashMap<Double, SimulationObject> allTargets;
    private SimulationObject currentTarget;

    /**
     * Constructor for predator class
     * @param x X location of the predator
     * @param y Y location of the predator
     * @param EU Predator's energy level
     * @param genotype Predator's genotype
     * @param maxSpeedHOD HOD predator's max speed
     * @param maxSpeedHED HED predator's max speed
     * @param maxSpeedHOR HOR predator's max speed
     * @param maintainSpeed Predator's standard speed
     * @param energyOutput Energy obtained from plant
     * @param energyToReProduce Energy needed to reproduce
     * @param maxOffspring Max number of offspring
     * @param gestaion Time from start of reproduction to childbirth
     * @param offspringEnergy Starting energy of the child
     */
    public Predator(float x, float y, int EU, String genotype, float maxSpeedHOD, float maxSpeedHED, float maxSpeedHOR, float maintainSpeed, float energyOutput, float energyToReProduce, int maxOffspring, float gestaion, float offspringEnergy) {
        super(x, y);
        this.EU = EU;
        this.genotype = genotype;
        this.maxSpeedHOD = maxSpeedHOD;
        this.maxSpeedHED = maxSpeedHED;
        this.maxSpeedHOR = maxSpeedHOR;
        this.maintainSpeed = maintainSpeed;
        this.energyOutput = energyOutput;
        this.energyToReProduce = energyToReProduce;
        this.maxOffspring = maxOffspring;
        this.gestaion = gestaion;
        this.offspringEnergy = offspringEnergy;
        this.collision = new Circle(x, y, 7);
        
        allTargets = new HashMap<>();
        currentTarget = null;
    }

    

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fill(collision);
        g.setLineWidth(2);
        g.setColor(Color.black);
        g.draw(collision);
        
        allTargets.values().forEach(t -> {
            g.drawLine(x, y, t.getX(), t.getY());
        });
        
        if(currentTarget != null){
            g.setColor(Color.red);
            g.drawLine(x,y, currentTarget.getX(), currentTarget.getY());
        }
    }

    @Override
    public void Update(Environment e) {
        findTargets(e);
        selectTarget();
        
        this.collision.setCenterX(x);
        this.collision.setCenterY(y);
    }
    
    /**
     * Get the predator's energy level
     * @return Current energy level
     */
    public int getEnergy() {
        return EU;
    }
    
    /**
     * Get the predator's genotype
     * @return Genotype string
     */
    public String getGenotype() {
        return genotype;
    }
    
    /**
     * Finds all valid targets within range and visible
     * @param e The environment that contains the predators, grazers, and rocks
     */
    private void findTargets(Environment e){
        // Clear the possible targets
        allTargets.clear();
        
        // For all grazers
        e.getGrazers().forEach(g -> {
            // If within the visibile distance
            if(Point2D.distance(x, y, g.getX(), g.getY()) <= 150) {
                // Make the tmp line of sight from pred to possbile target
                Line tmp = new Line(x, y, g.getX(), g.getY());
                // Save blocked flag
                boolean flag = true;
                
                // See if line of sight blocked by an obstacle
                for(int i = 0; i < e.getNumObstacles(); i++) {
                    if(tmp.intersects(e.getObstacles().get(i).collision)){
                        flag = false;
                        break;
                    }
                }
                
                // If not blocked by anything, add to possible targets
                if(flag){ allTargets.put(Point2D.distance(x, y, g.getX(), g.getY()), g); }
            }
        });
        
        if(genotype.charAt(0) == 'A'){
            // For all predators
            e.getPredators().forEach(p -> {
                // If within the visibile distance
                if(p != this && Point2D.distance(x, y, p.getX(), p.getY()) <= 150) {
                    // Make the tmp line of sight from pred to possbile target
                    Line tmp = new Line(x, y, p.getX(), p.getY());
                    // Save blocked flag
                    boolean flag = true;

                    // See if line of sight blocked by an obstacle
                    for(int i = 0; i < e.getNumObstacles(); i++) {
                        if(tmp.intersects(e.getObstacles().get(i).collision)){
                            flag = false;
                            break;
                        }
                    }

                    // If not blocked by anything, add to possible targets
                    if(flag){ allTargets.put(Point2D.distance(x, y, p.getX(), p.getY()), p); }
                }
            });
        }
    }
    
    /**
     * Selects which object from allTargets is the current target
     */
    private void selectTarget() {
        try{
            currentTarget = allTargets.get(Collections.min(allTargets.keySet()));
            
        }
        catch(NoSuchElementException e) {
            currentTarget = null;
        }
    }
    
}
