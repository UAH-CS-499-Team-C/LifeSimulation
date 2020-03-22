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
import java.lang.Math;
import java.util.ArrayList;
import org.newdawn.slick.geom.Line;

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
    private ArrayList <Plant> possibleTargets = new ArrayList <Plant> ();
    private Plant target;
    private boolean found;
    Line line;
    
    
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
        /*int r = rand.nextInt(4) + 1;
        
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
        }*/
        
        
    }
    
    // check if the grazer can see food
   /* private boolean seesFood(Environment e){
        
        Plant p;
        
        for (int i = 0; i < e.getPlants().size(); i++){
            p = e.getPlants().get(i);
            
            // if facing left
            if(this.dir == Direction.left){
                if(Point2D.distance(x, y, p.x, p.y) <= this.x + 5 && x > p.x){
                    target = p;
                    found = true;
                    break;
                }
            }
            // if facing right
            else if(this.dir == Direction.right){
                if(Point2D.distance(x, y, p.x, p.y) <= this.x + 5 && x < p.x){
                    target = p;
                    found = true;
                    break;
                }
            }
            // if facing up
            else if(this.dir == Direction.up){
                if(Point2D.distance(x, y, p.x, p.y) <= this.y + 5 && y > p.y){
                    target = p;
                    found = true;
                    break;
                }
            }
            // if facing down
            else if(this.dir == Direction.down){
                if(Point2D.distance(x, y, p.x, p.y) <= this.y + 5 && y < p.y){
                    target = p;
                    found = true;
                    break;
                }
            }
            // if no food is found nearby and in line of sight
            else{
                found = false;
            }
            
        }
        
        if(found){
            return true;
        }
        else{
            return false;
        }
        
        
    }*/
    
    // ***** ATTEMPT NUMBER 2 BEGIN *****
    
    // new attempt at finding food
    private void findFood(Environment e){
        
        // skip if food has already been found
        if(found == true){
            return;
        }
        
        Plant p;
        possibleTargets.clear();
        
        // find all possible targets for food
        for(int i = 0; i < e.getPlants().size(); i++){
            
            p = e.getPlants().get(i);
            
            if(Point2D.distance(this.x, this.y, p.x, p.y) <= 5){
                possibleTargets.add(p);
            }
            
        }
        
        // exit subroutine if no targets are found
        if(possibleTargets.size() == 0){
            found = false;
            return;
        }
        
        // find the possible target with the shortest distance
        for(int i = 0; i < possibleTargets.size(); i++){
            if(i + 1 <= possibleTargets.size() - 1){
                
                // sort the possible targets by distance
                if(Point2D.distance(this.x, this.y, possibleTargets.get(i).x, possibleTargets.get(i).y) > Point2D.distance(this.x, this.y, possibleTargets.get(i + 1).x, possibleTargets.get(i + 1).y)){
                    p = possibleTargets.get(i);
                    possibleTargets.set(i, possibleTargets.get(i + 1));
                    possibleTargets.set (i + 1, p);
                }
                
            }
        }
        
        // find closest target that isn't blocked by an obstacle
        for (int i = 0; i < possibleTargets.size(); i++){
            
            p = possibleTargets.get(i);
            line = new Line(this.x, this.y, p.x, p.y);
            
            for(int j = 0; j < e.getObstacles().size(); j++){
                if(line.intersects(e.getObstacles().get(j).collision)){
                    possibleTargets.remove(i); // remove the target from the list
                    line = null; // remove the line
                }
            }
        }
        
        // second check if the there are no targets
        if(possibleTargets.size() == 0){
            found = false;
            return;
        }
        else{
            // assign the target
            target = possibleTargets.get(0);
            line = new Line(this.x, this.y, target.x, target.y);
            found = true;
        }
        
    }
    
    // move toward the target
    private void moveToward(){
        
        // regular distance movement
        if(this.x < target.x && Point2D.distance(this.x, this.y, target.x, target.y) >= 3.0){
            this.collision.setX((float) collision.getX() + (float) 3.0);
            this.x += 3.0;
            line = null;
            line = new Line(this.x, this.y, target.x, target.y);
        }
        else if(this.x > target.x && Point2D.distance(this.x, this.y, target.x, target.y) >= 3.0){
            this.collision.setX((float) collision.getX() - (float) 3.0);
            this.x -= 3.0;
            line = null;
            line = new Line(this.x, this.y, target.x, target.y);
        }
        
        if(this.y < target.y && Point2D.distance(this.x, this.y, target.x, target.y) >= 3.0){
            this.collision.setY((float) collision.getY() + (float) 3.0);
            this.y += 3.0;
            line = null;
            line = new Line(this.x, this.y, target.x, target.y);
        }
        else if(this.y > target.y && Point2D.distance(this.x, this.y, target.x, target.y) >= 3.0){
            this.collision.setY((float) collision.getY() - (float) 3.0);
            this.y -= 3.0;
            line = null;
            line = new Line(this.x, this.y, target.x, target.y);
        }
        
        // closing in
        if(this.x < target.x && Point2D.distance(this.x, this.y, target.x, target.y) < 3.0){
            this.collision.setX((float) collision.getX() + (float) 1.0);
            this.x += 1.0;
            line = null;
            line = new Line(this.x, this.y, target.x, target.y);
        }
        else if(this.x > target.x && Point2D.distance(this.x, this.y, target.x, target.y) < 3.0){
            this.collision.setX((float) collision.getX() - (float) 1.0);
            this.x -= 1.0;
            line = null;
            line = new Line(this.x, this.y, target.x, target.y);
        }
        
        if(this.y < target.y && Point2D.distance(this.x, this.y, target.x, target.y) < 3.0){
            this.collision.setY((float) collision.getY() + (float) 1.0);
            this.y += 1.0;
            line = null;
            line = new Line(this.x, this.y, target.x, target.y);
        }
        else if(this.y > target.y && Point2D.distance(this.x, this.y, target.x, target.y) < 3.0){
            this.collision.setY((float) collision.getY() - (float) 1.0);
            this.y -= 1.0;
            line = null;
            line = new Line(this.x, this.y, target.x, target.y);
        }
        
    }
    
    private void wander(Environment e){
        
        int direction = rand.nextInt(4);
        int distance = rand.nextInt(5) + 1;
        
        // moving left
        if(direction == 0){
            for(int i = 0; i < distance; i++){
                
                if(this.x - 3.0 > 0){
                    this.collision.setX((float) collision.getX() - (float) 3.0);
                    this.x -= 3.0;
                    findFood(e);

                    if(found == true){
                        break;
                    }
                }
            }
        }
        
        // moving right
        else if(direction == 1){
            for(int i = 0; i < distance; i++){
                
                if(this.x + 3.0 < 1000){
                    this.collision.setX((float) collision.getX() + (float) 3.0);
                    this.x += 3.0;
                    findFood(e);

                    if(found == true){
                        break;
                    }
                }
            }
        }
        
        // moving up
        else if(direction == 2){
            for(int i = 0; i < distance; i++){
                
                if(this.y - 3.0 > 0){
                    this.collision.setY((float) collision.getY() - (float) 3.0);
                    this.y -= 3.0;
                    findFood(e);

                    if(found == true){
                        break;
                    }
                }
            }
        }
        
        // moving down
        else if(direction == 3){
            for(int i = 0; i < distance; i++){
                
                if(this.y + 3.0 < 750){
                    this.collision.setY((float) collision.getY() + (float) 3.0);
                    this.y += 3.0;
                    findFood(e);

                    if(found == true){
                        break;
                    }
                }
            }
        }
        
    }
    
    // ***** ATTEMPT NUMBER 2 END *****
    
    // returns if the target has been reached or not
   /* private boolean reached(Plant target){
        if(Point2D.distance(this.x, this.y, target.x, target.y) <= 7.0){
            return true;
        }
        else{
            return false;
        }
    }*/
    
    // move toward the target plant
   /* private void goToTarget(){
        if(!reached(target)){
        
            // if target is left of grazer
            if(this.dir == Direction.left && ((float) collision.getX() - (float) 3.0 > 0) && ((float) collision.getY() - 3.0 > 0) && ((float) collision.getY() + (float) 3.0 < 750) && !reached(target)){
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
            else if(this.dir == Direction.right && ((float) collision.getX() + (float) 3.0 < 1000) && ((float) collision.getY() - 3.0 > 0) && ((float) collision.getY() + (float) 3.0 < 750) && ! reached(target)){
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
            else if(this.dir == Direction.up && ((float) collision.getY() - 3.0 > 0) && ((float) collision.getX() - (float) 3.0 > 0) && ((float) collision.getX() + (float) 3.0 < 1000) && !reached(target)){
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
            else if(this.dir == Direction.down && ((float) collision.getY() + (float) 3.0 < 750) && ((float) collision.getX() - (float) 3.0 > 0) && ((float) collision.getX() + (float) 3.0 < 1000) && !reached(target)){
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
        
    }*/
    
    
    
    // wander around until the grazer finds some food
    /*private void wander(Environment e){
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
                else{
                    collision.setX((float) collision.getX() + (float) 3.0);
                    x += 3.0;
                    wander(e);
                    break;
                }
                
            }
            else if(this.dir == Direction.right){
                
                
                if((float) collision.getX() + (float) 3.0 < (float) 1000){
                    collision.setX((float) collision.getX() + (float) 3.0);
                
                    if(this.seesFood(e)){ break;}
                }
                else{
                    collision.setX((float) collision.getX() - (float) 3.0);
                    x -= 3.0;
                    wander(e);
                    break;
                }
                
            }
            else if(this.dir == Direction.up){
                
               
                if((float) collision.getY() - 3.0 > (float) 0){
                    collision.setY((float) collision.getY() - (float) 3.0);
                
                    if(this.seesFood(e)){ break;}
                }
                else{
                    collision.setY((float) collision.getY() + (float) 3.0);
                    y += 3.0;
                    wander(e);
                    break;
                }
                
            }
            else if(this.dir == Direction.down){
                
                
                if((float) collision.getY() + (float) 3.0 < (float) 750){
                    collision.setY((float) collision.getY() + (float) 3.0);
                
                    if(this.seesFood(e)){ break;}
                }
                else{
                    collision.setY((float) collision.getY() - (float) 3.0);
                    y -= 3.0;
                    wander(e);
                    break;
                }
                
            }
        }
    }*/
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.blue);
        g.fill(collision);
        g.setLineWidth(2);
        g.setColor(Color.black);
        g.draw(collision);
        
        
        if(found == true){
            g.drawLine(this.x, this.y, target.x, target.y);
        }
    }

    @Override
    public void Update(Environment e) {
        
        findFood(e);
        
        if(found == true){
            moveToward();
        }
        else{
            wander(e);
        }
        
    }
    
    
    
    public int getEnergy() {
        return EU;
    }
}
