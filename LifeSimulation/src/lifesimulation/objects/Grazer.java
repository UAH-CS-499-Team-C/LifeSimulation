/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import lifesimulation.utilities.Environment;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

/**
 * Implementation class of the Grazer Creature
 * @author sam
 */
public class Grazer extends SimulationObject implements LivingCreature{
    
    protected final int EU;
    
    private final float energyInput;
    private final float energyOutput;
    private final float energyToReproduce;
    private final float maintainSpeed;
    private final float maxSpeed;
    
    
    public Grazer(float x, float y, int EU, float energyInput, float energyOutput, float energyToReproduce, float maintainSpeed, float maxSpeed) {
        super(x, y);
        this.EU = EU;
        this.energyInput = energyInput;
        this.energyOutput = energyOutput;
        this.energyToReproduce = energyToReproduce;
        this.maintainSpeed = maintainSpeed;
        this.maxSpeed = maxSpeed;
        this.collision = new Circle(x, y, 7);
    }

     
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.blue);
        g.fill(collision);
        g.setLineWidth(2);
        g.setColor(Color.black);
        g.draw(collision);
    }

    @Override
    public void Update(Environment e) {
        x+=3.0;
        y+=3.0;
    }
    
    public int getEnergy() {
        return EU;
    }
}
