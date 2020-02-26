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
        if(secondsElapsed == 5) {
            secondsElapsed = 0;
            Reproduce();
        }
    }
    
    private void Reproduce() {
        // How many seeds to create
        Random r = new Random();
        int numSeeds = r.nextInt((int)maxSeedNumber);
        for(int i = 0; i <= numSeeds; i++){
            if(r.nextInt(100)+1 <= seedViability * 100){
                // Find square around plant's center
                float minX = x - maxSeedCastDistance;
                float maxX = x + maxSeedCastDistance;
                float minY = y - maxSeedCastDistance;
                float maxY = y + maxSeedCastDistance;
                
                // Make tmp points impossible
                float tmpX = -5000, tmpY = -5000;
                while(new Point2D.Float(tmpX, tmpY).distance(new Point2D.Float(x, y)) > maxSeedCastDistance) {
                    tmpX = r.nextInt((int)maxX-(int)minX) + minX;
                    tmpY = r.nextInt((int)maxY-(int)minY) + minY;
                }
                Environment.GetInstance().addPlant(new Plant(tmpX, tmpY, 0.01f, growthRate, maxSize, maxSeedNumber, maxSeedCastDistance, seedViability));
            }
        }
    }
    
}
