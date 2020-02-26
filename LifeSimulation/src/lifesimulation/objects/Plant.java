/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import java.awt.geom.Point2D;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

/**
 *
 * @author sam
 */
public class Plant extends SimulationObject implements LivingCreature {
    
    // Variables used for displaying and possibly other class
    private float diameter;
    private float height;
    
    // Variables used for other functions
    private float growthRate;
    private float maxSize;
    private float maxSeedNumber;
    private float maxSeedCastDistance;
    private float seedViability;
    private int secondsElapsed;

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
        g.fillOval(x-diameter/2, y-diameter/2, diameter, diameter);
        g.setLineWidth(2);
        g.setColor(Color.black);
        g.drawOval(x-diameter/2, y-diameter/2, diameter, diameter);
    }

    @Override
    public void Update(Environment e) {
        // Growth
        if(diameter < maxSize) {
            diameter += growthRate * maxSize;
            if(diameter > maxSize) {
                diameter = maxSize;
            }
        }
        
        // Wait for reproduction
        if(diameter == maxSize) {
            secondsElapsed++;
        }
        
        // Reproduce
        if(secondsElapsed == 5*60) {
            secondsElapsed = 0;
            Reproduce(e);
        }
    }
    
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
                
                // Get random points until within distance
                while(new Point2D.Float(tmpX, tmpY).distance(new Point2D.Float(x, y)) > maxSeedCastDistance) {
                    tmpX = r.nextInt((int)maxX-(int)minX) + minX;
                    tmpY = r.nextInt((int)maxY-(int)minY) + minY;
                }
                
                // Add the new plant
                boolean c = false;
                for(int j = 0; j < e.getNumObstacles(); j++){
                    if(e.getObstacles().get(j).collision.intersects(new Circle(tmpX, tmpY, 1))){
                        c = true;
                    }
                }
                
                e.addPlant(new Plant(tmpX, tmpY, 0.01f, growthRate, maxSize, maxSeedNumber, maxSeedCastDistance, seedViability));
            }
        }
    }
    
}
