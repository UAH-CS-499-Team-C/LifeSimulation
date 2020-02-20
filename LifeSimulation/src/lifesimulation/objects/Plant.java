/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import pkgLifeSimDataParser.LifeSimDataParser;

/**
 *
 * @author sam
 */
public class Plant extends SimulationObject implements LivingCreature {
    
    private float diameter;
    private float height;
    private float growthRate;
    private float maxSize;
    private float maxSeedNumber;
    private float maxSeedCastDistance;
    private float seedViability;

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
        
    }
    
}
