/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import lifesimulation.utilities.Environment;
import java.awt.geom.Point2D;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

/**
 * The behavior class for Plant objects
 * @author sam
 */
public class Plant extends SimulationObject implements LivingCreature {
    
    /**
     * The diameter of the plant
     */
    private float diameter;
    
    /**
     * The height of the plant - 1/2 of the diameter
     */
    private float height;
    
    /**
     * The rate at which the plant grows
     */
    private final float growthRate;
    
    /**
     * The maximum size of the plant
     */
    private final float maxSize;
    
    /**
     * The maximum number of seeds it can create each hour
     */
    private final float maxSeedNumber;
    
    /**
     * The maximum distance the plant can spread its seeds
     */
    private final float maxSeedCastDistance;
    
    /**
     * The percentage of seeds that will proceed to growth
     */
    private final float seedViability;
    
    /**
     * Determines if the plant is being eaten
     */
    private boolean beingEaten = false;
    
    /**
     * Determines if the plant has been eaten has needs to be deleted
     */
    private boolean toBeDeleted = false;
    
    /**
     * Used to keep track of how much time has passed since last action
     */
    private int secondsElapsed;

    /**
     * Constructor
     * @param x {@link Plant#x}
     * @param y {@link Plant#y}
     * @param diameter {@link Plant#diameter}
     * @param growthRate {@link Plant#growthRate}
     * @param maxSize {@link Plant#maxSize}
     * @param maxSeedNumber {@link Plant#maxSeedNumber}
     * @param maxSeedCastDistance {@link Plant#maxSeedCastDistance}
     * @param seedViability {@link Plant#seedViability}
     */
    public Plant(float x, float y, float diameter, float growthRate, float maxSize, float maxSeedNumber, float maxSeedCastDistance, float seedViability) {
        super(x, y);
        this.diameter = diameter;
        height = diameter / 2;
        this.collision = new Circle(x, y, diameter/2);
        this.growthRate = growthRate;
        this.maxSize = maxSize;
        this.maxSeedNumber = maxSeedNumber;
        this.maxSeedCastDistance = maxSeedCastDistance;
        this.seedViability = seedViability;
        secondsElapsed = 0;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(18, 97, 29));
        g.fill(collision);
        g.setLineWidth(2);
        g.setColor(Color.black);
        g.draw(collision);
    }

    @Override
    public void Update(Environment e) {
        // Spawn delay
        if(diameter == 0.01f && secondsElapsed < 10) {
            secondsElapsed++;
            return;
        }
        if(diameter == 0.01f && secondsElapsed == 10){
            secondsElapsed = 0;
        }
        
        // Growth
        if(diameter < maxSize && beingEaten == false) {
            diameter += growthRate * maxSize;
            if(diameter > maxSize) {
                diameter = maxSize;
            }
        }
        
        // Reduction from being eaten
        else if(diameter <= maxSize && beingEaten == true){
            
            if(diameter - growthRate > 0){
                diameter -= growthRate * maxSize;
            }
            else{
                toBeDeleted = true;
            }
            
        }

        // Wait for reproduction
        if(diameter == maxSize) {
            secondsElapsed++;
        }

        // Reproduce
        if(secondsElapsed == 60 * 60) {
            secondsElapsed = 0;
            Reproduce(e);
        }

        collision = new Circle(x, y, diameter/2);
        height = diameter/2;
    }
    
    /**
     * Reproduction logic - called by {@link Plant#Update(lifesimulation.objects.Environment)}
     * @param e Environment object
     */
    private void Reproduce(Environment e) {
        // How many seeds to create
        Random r = new Random();
        int numSeeds = r.nextInt((int)maxSeedNumber);
        
        System.out.println("Seeds being created: " + numSeeds);
        
        // For each seed being created
        for(int i = 0; i <= numSeeds; i++){
            // If seed's random value falls in viability range
            if(r.nextInt(100)+1 <= seedViability * 100){
                System.out.println("Seed Made");
                
                // Find square around plant's center
                float minX = Math.max(x - maxSeedCastDistance, 0);
                float maxX = Math.min(x + maxSeedCastDistance, 1000);
                float minY = Math.max(y - maxSeedCastDistance, 0);
                float maxY = Math.min(y + maxSeedCastDistance, 750);
                
                // Make tmp points impossible
                float tmpX = -5000, tmpY = -5000;
                boolean c = false;
                
                // Get random points until within distance
                while(new Point2D.Float(tmpX, tmpY).distance(new Point2D.Float(x, y)) > maxSeedCastDistance || c) {
                    tmpX = r.nextInt((int)maxX-(int)minX) + minX;
                    tmpY = r.nextInt((int)maxY-(int)minY) + minY;
                    c = false;
                    for(int j = 0; j < e.getNumObstacles(); j++){
                        c = c || new Circle(tmpX, tmpY, 0.01f).intersects(e.getObstacles().get(j).getCollision());
                    }
                }
                
                // Add the new plant
                e.addPlant(new Plant(tmpX, tmpY, 0.01f, growthRate, maxSize, maxSeedNumber, maxSeedCastDistance, seedViability));
            }
        }
    }
    
    // lets the plant know that it's being eaten
    public void isBeingEaten(){
        beingEaten = true;
    }
    
    public boolean needsDeleting(){
        if(toBeDeleted == true){
            return true;
        }
        else{
            return false;
        }
    }
    
    public float getDiameter() {
        return diameter;
    }
    
}
