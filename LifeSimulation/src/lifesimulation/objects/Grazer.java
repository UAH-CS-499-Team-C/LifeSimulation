/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import java.awt.geom.Point2D;
import lifesimulation.utilities.Environment;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import java.util.Random;
import java.util.ArrayList;

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
    private Direction dir;
    private Plant target;
    private ArrayList <Plant> foodTargets = new ArrayList <Plant> ();
    private boolean found;
    
    
    public Grazer(float x, float y, int EU, float energyInput, float energyOutput, float energyToReproduce, float maintainSpeed, float maxSpeed) {
        super(x, y);
        this.EU = EU;
        this.energyInput = energyInput;
        this.energyOutput = energyOutput;
        this.energyToReproduce = energyToReproduce;
        this.maintainSpeed = maintainSpeed;
        this.maxSpeed = maxSpeed;
        this.collision = new Circle(x, y, 7);
        
        // give the grazer a random starting direction
        int r = rand.nextInt(4) + 1;
        
        if(r == 1){
            this.dir = Direction.left;
        }
        else if(r == 2){
            this.dir = Direction.right;
        }
        else if(r == 3){
            this.dir = Direction.up;
        }
        else if(r == 4){
            this.dir = Direction.down;
        }
        
        
    }
    
    // check if the grazer can see food
    private boolean seesFood(Environment e){
        
        foodTargets.clear();
        
        Plant p;
        
        for (int i = 0; i < e.getPlants().size(); i++){
            p = e.getPlants().get(i);
            
            // if facing left
            if(this.dir == Direction.left){
                if(Point2D.distance(x, y, p.x, p.y) <= this.x + 5 && x > p.x){
                    target = p;
                    found = true;
                    foodTargets.add(p);
                    break;
                }
            }
            // if facing right
            else if(this.dir == Direction.right){
                if(Point2D.distance(x, y, p.x, p.y) <= this.x + 5 && x < p.x){
                    target = p;
                    found = true;
                    foodTargets.add(p);
                    break;
                }
            }
            // if facing up
            else if(this.dir == Direction.up){
                if(Point2D.distance(x, y, p.x, p.y) <= this.y + 5 && y > p.y){
                    target = p;
                    found = true;
                    foodTargets.add(p);
                    break;
                }
            }
            // if facing down
            else if(this.dir == Direction.down){
                if(Point2D.distance(x, y, p.x, p.y) <= this.y + 5 && y < p.y){
                    target = p;
                    found = true;
                    foodTargets.add(p);
                    break;
                }
            }
            // if no food is found nearby and in line of sight
            else{
                found = false;
            }
            
        }
        
        if(found){
            System.out.println("Found Food");
            return true;
        }
        else{
            System.out.println("No Food Found");
            return false;
        }
        
        
    }
    
    // move toward the target plant
    private void goToTarget(){
        // if target is left of grazer
        if(this.dir == Direction.left && ((float) collision.getX() - (float) 3.0 > 0) && ((float) collision.getY() - 3.0 > 0) && ((float) collision.getY() + (float) 3.0 < 750)){
            // heading north west
            if(y > target.y){
                collision.setX((float) collision.getX() - (float) 3.0);
                x -= 3.0;
                collision.setY((float) collision.getY() - (float) 3.0);
                y -= 3.0;
            }
            // heading south west
            else if( y < target.y){
                collision.setX((float) collision.getX() - (float) 3.0);
                x -= 3.0;
                collision.setY((float) collision.getY() + (float) 3.0);
                y += 3.0;
            }
            // heading straight west
            else if(y == target.y){
                collision.setX((float) collision.getX() - (float) 3.0);
                x -= 3.0;
            }
        }
        // if target is right of grazer
        else if(this.dir == Direction.right && ((float) collision.getX() + (float) 3.0 < 1000) && ((float) collision.getY() - 3.0 > 0) && ((float) collision.getY() + (float) 3.0 < 750)){
            //heading north east
            if(y > target.y){
                collision.setX((float) collision.getX() + (float) 3.0);
                x += 3.0;
                collision.setY((float) collision.getY() - (float) 3.0);
                y -= 3.0;
            }
            // heading south east
            else if(y < target.y){
                collision.setX((float) collision.getX() + (float) 3.0);
                x += 3.0;
                collision.setY((float) collision.getY() + (float) 3.0);
                y += 3.0;
            }
            // heading straight east
            else if(y == target.y){
                collision.setX((float) collision.getX() + (float) 3.0);
                x += 3.0;
            }
        }
        // if target is above the grazer
        else if(this.dir == Direction.up && ((float) collision.getY() - 3.0 > 0) && ((float) collision.getX() - (float) 3.0 > 0) && ((float) collision.getX() + (float) 3.0 < 1000)){
            // heading north west
            if(x > target.x){
                collision.setX((float) collision.getX() - (float) 3.0);
                x -= 3.0;
                collision.setY((float) collision.getY() - (float) 3.0);
                y -= 3.0;
            }
            // heading north east
            else if(x < target.x){
                collision.setX((float) collision.getX() + (float) 3.0);
                x += 3.0;
                collision.setY((float) collision.getY() - (float) 3.0);
                y -= 3.0;
            }
            // heading straignt north
            else if(x == target.x){
                collision.setY((float) collision.getY() - (float) 3.0);
                y -= 3.0;
            }
        }
        // if target is below the grazer
        else if(this.dir == Direction.down && ((float) collision.getY() + (float) 3.0 < 750) && ((float) collision.getX() - (float) 3.0 > 0) && ((float) collision.getX() + (float) 3.0 < 1000)){
            // heading south west
            if(x > target.x){
                collision.setX((float) collision.getX() - (float) 3.0);
                x -= 3.0;
                collision.setY((float) collision.getY() + (float) 3.0);
                y += 3.0;
            }
            // heading south east
            else if(x < target.x){
                collision.setX((float) collision.getX() + (float) 3.0);
                x += 3.0;
                collision.setY((float) collision.getY() + (float) 3.0);
                y += 3.0;
            }
            // heading straight south
            else if(x == target.x){
                collision.setY((float) collision.getY() + (float) 3.0);
            }
        }
        
    }
    
    // wander around until the grazer finds some food
    private void wander(Environment e){
        int r1 = rand.nextInt(4) + 1;
        int r2 = rand.nextInt(5) + 1;
        
        
        switch(r1){
            case 1: this.dir = Direction.left;
                    break;
            case 2: this.dir = Direction.right;
                    break;
            case 3: this.dir = Direction.up;
                    break;
            case 4: this.dir = Direction.down;
                    break;
        }
        
        
        for(int i = 0; i < r2; i++){
            if(this.dir == Direction.left){
                
                
                if ((float) collision.getX() - (float) 3.0 > (float) 0){
                    collision.setX((float) collision.getX() - (float) 3.0);
                
                    if(this.seesFood(e)){ break;}
                }
                
            }
            else if(this.dir == Direction.right){
                
                
                if((float) collision.getX() + (float) 3.0 < (float) 1000){
                    collision.setX((float) collision.getX() + (float) 3.0);
                
                    if(this.seesFood(e)){ break;}
                }
                
            }
            else if(this.dir == Direction.up){
                
               
                if((float) collision.getY() - 3.0 > (float) 0){
                    collision.setY((float) collision.getY() - (float) 3.0);
                
                    if(this.seesFood(e)){ break;}
                }
            
            }
            else if(this.dir == Direction.down){
                
                
                if((float) collision.getY() + (float) 3.0 < (float) 750){
                    collision.setY((float) collision.getY() + (float) 3.0);
                
                    if(this.seesFood(e)){ break;}
                }
                
            }
        }
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.blue);
        g.fill(collision);
        g.setLineWidth(2);
        g.setColor(Color.black);
        g.draw(collision);
        
        foodTargets.forEach(p -> g.drawLine(x, y, p.x, p.y));
    }

    @Override
    public void Update(Environment e) {
        
        // wander the map in search of food
        
        
        if(this.seesFood(e)){
            this.goToTarget();
            
        }
        else{
            this.wander(e);
            
        }
    }
    
    
    
    public int getEnergy() {
        return EU;
    }
}
