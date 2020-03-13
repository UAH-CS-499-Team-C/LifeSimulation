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
import java.util.Random;

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
    
    // Movement variables
    private Random rand = new Random(); // variable to randomly decide the grazer's direction
    private enum Direction {left, right, up, down};
    Direction dir;
    
    
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
        int r = rand.nextInt(4) + 1;
        
        // move left
        if (r == 1){
            if((float) collision.getX() - (float) 3.0 > 0){
                collision.setX((float) collision.getX() - (float) 3.0);
                dir = Direction.left;
                this.x -= 3.0;
            }
        }
        // move right
        else if (r == 2){
            if((float) collision.getX() + (float) 3.0 < 1000){
                collision.setX((float) collision.getX() + (float) 3.0);
                dir = Direction.right;
                this.x += 3.0;
            }
        }
        // move up
        else if (r == 3){
            if((float) collision.getY() - 3.0 > 0){
                collision.setY((float) collision.getY() - (float) 3.0);
                dir = Direction.up;
                this.y -= 3.0;
            }
        }
        // move down
        else if (r == 4){
            if((float) collision.getY() + (float) 3.0 < 750){
                collision.setY((float) collision.getY() + (float) 3.0);
                dir = Direction.down;
                this.y += 3.0;
            }
        }
        
        
        
        
    }
    
    
    
    public int getEnergy() {
        return EU;
    }
}
