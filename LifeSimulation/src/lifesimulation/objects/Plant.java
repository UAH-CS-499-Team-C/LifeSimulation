/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import pkgLifeSimDataParser.LifeSimDataParser;

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
    private ArrayList<Point2D.Float> seedLocations = new ArrayList<>();
    private int secondsElapsed = 0;

    public Plant(float x, float y, float diameter) {
        super(x, y);
        this.diameter = diameter;
        height = diameter / 2;
        
        
        // Setup parser
        LifeSimDataParser lsdp = LifeSimDataParser.getInstance();
        growthRate = (float)lsdp.getPlantGrowthRate();
        maxSize = (float)lsdp.getMaxPlantSize();
        maxSeedNumber = (float)lsdp.getMaxSeedNumber();
        maxSeedCastDistance = (float)lsdp.getMaxSeedCastDistance();
        seedViability = (float)lsdp.getSeedViability();
        // Save all viable seed locations
        for(float i = Math.max(x - maxSeedCastDistance, 0.0f);
                i <= Math.min(x + maxSeedCastDistance, lsdp.getWorldWidth()); i++){
            for(float j = Math.max(y - maxSeedCastDistance, 0.0f);
                j <= Math.min(y + maxSeedCastDistance, lsdp.getWorldHeight()); j++){
                seedLocations.add(new Point2D.Float(i, j));
            }
        }
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
        if(secondsElapsed == 60) {
            secondsElapsed = 0;
            Reproduce();
        }
    }
    
    private void Reproduce() {
        
    }
    
}
