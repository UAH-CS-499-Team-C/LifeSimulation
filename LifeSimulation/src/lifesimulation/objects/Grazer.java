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
import org.newdawn.slick.geom.Line;

/**
 * Implementation class of the Grazer Creature
 * @author sam
 */
public class Grazer extends SimulationObject implements LivingCreature{
    
    protected int EU;
    
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
    private ArrayList <Predator> possibleThreats = new ArrayList <Predator> ();
    private Plant target;
    private Predator threat;
    private boolean found;
    private Line line = new Line(0, 0, 0, 0); // default line to be changed as needed
    private boolean reached = false;
    private boolean eating = false;
    private float moved = 0;
    private float speed;
    private boolean running = false;
    private boolean avoidingObstacle = false;
    private boolean avoided = false;
    int currentTime;
    int maxRunTime;
    
    /**
     * Constructor for Grazer class object
     * @param x
     * @param y
     * @param EU
     * @param energyInput
     * @param energyOutput
     * @param energyToReproduce
     * @param maintainSpeed
     * @param maxSpeed 
     */
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
    
    /**
     * Grazer checks line of site for nearby predators
     * @param e 
     */
    private void checkForPredators(Environment e){
        
        // check all predators to see if any are in range
        for(int i = 0; i < e.getPredators().size(); i++){
            if(Point2D.distance(this.x, this.y, e.getPredators().get(i).x, e.getPredators().get(i).y) <= 25.0){
                possibleThreats.add(e.getPredators().get(i));
            }
        }
        
        // exit subroutine if no threats are found
        if(possibleThreats.size() == 0){
            return;
        }
        
        // sort out predators that are blocked my obstacles
        for(int i = 0; i < possibleThreats.size(); i++){
            line.set(this.x, this.y, possibleThreats.get(i).x, possibleThreats.get(i).y);
            
            for(int j = 0; j < e.getObstacles().size(); j++){
                if(line.intersects(e.getObstacles().get(j).collision)){
                    possibleThreats.remove(i);
                    break;
                }
            }
        }
        
        // exit subroutine if no theats are found
        if(possibleTargets.size() == 0){
            return;
        }
        
        // find the closest threat
        for(int i = 0; i < possibleThreats.size(); i++){
            if(i + 1 <= possibleThreats.size() - 1){
                if(Point2D.distance(this.x, this.y, possibleThreats.get(i).x, possibleThreats.get(i).y) > Point2D.distance(this.x, this.y, possibleThreats.get(i + 1).x, possibleThreats.get(i + 1).y)){
                    Predator temp = possibleThreats.get(i);
                    possibleThreats.set(i, possibleThreats.get(i + 1));
                    possibleThreats.set(i + 1, temp);
                }
            }
        }
        
        // set closest threat
        threat = possibleThreats.get(0);
        running = true;
        
        // clear the possibleThreats arraylist
        possibleThreats.clear();
        
    }
    
    /**
     * Grazer runs away from predator
     * @param e 
     */
    private void run(Environment e){
        
        // only set these vaiables if the running sequence is beginning
        if(running == false){
            currentTime = e.getTime();
            maxRunTime = currentTime + ((int) maintainSpeed * 60);
        }
        
        // maintain max speed within the alloted time frame
        if(e.getTime() < maxRunTime && this.EU >= 25){
            
            // speed at maximum
            speed = maxSpeed;
            
            // if threat is to the left
            if(threat.x < this.x && this.x + speed < e.getWidth()){
                
                this.collision.setX((float) this.collision.getX() + speed);
                this.x += speed;
                expend(speed);
                
                // exit subroutine if far enough away from threat
                if(Point2D.distance(this.x,this.y, threat.x, threat.y) > 25){
                    running = false;
                    return;
                }
            }
            // if threat is to the right
            else if(threat.x > this.x && this.x - speed > 0){
                this.collision.setX((float) this.collision.getX() - speed);
                this.x -= speed;
                expend(speed);
                
                // exit subroutine if far enough away from threat
                if(Point2D.distance(this.x,this.y, threat.x, threat.y) > 25){
                    running = false;
                    return;
                }
            }
            // if threat is above
            else if(threat.y < this.y && this.y + speed < e.getHeight()){
                this.collision.setY((float) this.collision.getY() + speed);
                this.y += speed;
                expend(speed);
                
                // exit subroutine if far enough away from threat
                if(Point2D.distance(this.x,this.y, threat.x, threat.y) > 25){
                    running = false;
                    return;
                }
            }
            // if threat is below
            else if(threat.y > this.y && this.y - speed > 0){
                this.collision.setY((float) this.collision.getY() - speed);
                this.y -= speed;
                expend(speed);
                
                // exit subroutine if far enough away from threat
                if(Point2D.distance(this.x,this.y, threat.x, threat.y) > 25){
                    running = false;
                    return;
                }
            }
        }
        // back to normal speed if max run time is surpassed
        else if( e.getTime() > maxRunTime && this.EU >= 25){
            
            // speed at maximum
            speed = (float)(maxSpeed * 0.75);
            
            // if threat is to the left
            if(threat.x < this.x && this.x + speed < e.getWidth()){
                this.collision.setX((float) this.collision.getX() + speed);
                this.x += speed;
                expend(speed);
                
                // exit subroutine if far enough away from threat
                if(Point2D.distance(this.x,this.y, threat.x, threat.y) > 25){
                    running = false;
                    return;
                }
            }
            // if threat is to the right
            else if(threat.x > this.x && this.x - speed > 0){
                this.collision.setX((float) this.collision.getX() - speed);
                this.x -= speed;
                expend(speed);
                
                // exit subroutine if far enough away from threat
                if(Point2D.distance(this.x,this.y, threat.x, threat.y) > 25){
                    running = false;
                    return;
                }
            }
            // if threat is above
            else if(threat.y < this.y && this.y + speed < e.getHeight()){
                this.collision.setY((float) this.collision.getY() + speed);
                this.y += speed;
                expend(speed);
                
                // exit subroutine if far enough away from threat
                if(Point2D.distance(this.x,this.y, threat.x, threat.y) > 25){
                    running = false;
                    return;
                }
            }
            // if threat is below
            else if(threat.y > this.y && this.y - speed > 0){
                this.collision.setY((float) this.collision.getY() - speed);
                this.y -= speed;
                expend(speed);
                
                // exit subroutine if far enough away from threat
                if(Point2D.distance(this.x,this.y, threat.x, threat.y) > 25){
                    running = false;
                    return;
                }
            }
        }
        // slow down if EU falls below 25
        else if(this.EU < 25){
            
            // speed at maximum
            speed = 10;
            
            // if threat is to the left
            if(threat.x < this.x && this.x + speed < e.getWidth()){
                this.collision.setX((float) this.collision.getX() + speed);
                this.x += speed;
                expend(speed);
                
                // exit subroutine if far enough away from threat
                if(Point2D.distance(this.x,this.y, threat.x, threat.y) > 25){
                    running = false;
                    return;
                }
            }
            // if threat is to the right
            else if(threat.x > this.x && this.x - speed > 0){
                this.collision.setX((float) this.collision.getX() - speed);
                this.x -= speed;
                expend(speed);
                
                // exit subroutine if far enough away from threat
                if(Point2D.distance(this.x,this.y, threat.x, threat.y) > 25){
                    running = false;
                    return;
                }
            }
            // if threat is above
            else if(threat.y < this.y && this.y + speed < e.getHeight()){
                this.collision.setY((float) this.collision.getY() + speed);
                this.y += speed;
                expend(speed);
                
                // exit subroutine if far enough away from threat
                if(Point2D.distance(this.x,this.y, threat.x, threat.y) > 25){
                    running = false;
                    return;
                }
            }
            // if threat is below
            else if(threat.y > this.y && this.y - speed > 0){
                this.collision.setY((float) this.collision.getY() - speed);
                this.y -= speed;
                expend(speed);
                
                // exit subroutine if far enough away from threat
                if(Point2D.distance(this.x,this.y, threat.x, threat.y) > 25){
                    running = false;
                    return;
                }
            }
        }
        
        
    }
    
    /**
     * Grazer will reproduce if energyToReproduce is met
     * @param e 
     */
    private void reproduce(Environment e){
        
        // first offspring
        float newX = this.x - 5;
        float newY = this.y;
        int newEU = this.EU / 2;
        float newInput = this.energyInput;
        float newOutput = this.energyOutput;
        float newReproduce = this.energyToReproduce;
        float newMaintain = this.maintainSpeed;
        float newMax = this.maxSpeed;
        e.addGrazer(new Grazer(newX, newY, newEU, newInput, newOutput, newReproduce, newMaintain, newMax));
        
        // second offspring
        newX = this.x + 5;
        e.addGrazer(new Grazer(newX, newY, newEU, newInput, newOutput, newReproduce, newMaintain, newMax));
         
        // reduce EU due to energy expended during reproduction
        this.EU = this.EU / 2;
    }
    
    // grazer dies if EU falls to 0 or below
    private void die(Environment e){
        e.addDeadGrazer(this);
    }
    
    // expend energy when moving
    private void expend(float distance){
        
        this.moved += distance;
        
        while(this.moved >= 5.0){
            this.moved -= 5.0;
            this.EU -= this.energyOutput;
        }
        
    }
    
    // grazers attempt to find nearby food
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
            
            if(Point2D.distance(this.x, this.y, p.x, p.y) <= 150 + p.getDiameter()){
                possibleTargets.add(p);
            }
            
        }
        
        // exit subroutine if no targets are found
        if(possibleTargets.isEmpty()){
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
            
            line.set(this.x, this.y, p.x, p.y);
            
            for(int j = 0; j < e.getObstacles().size(); j++){
                if(line.intersects(e.getObstacles().get(j).collision) && !possibleTargets.isEmpty()){
                    possibleTargets.remove(i); // remove the target from the list
                }
            }
        }
        
        // second check if the there are no targets
        if(possibleTargets.isEmpty()){
            found = false;
            return;
        }
        else{
            // assign the target
            target = possibleTargets.get(0);
            
            line.set(this.x, this.y, target.x, target.y);
            found = true;
        }
        
    }
    
    /**
     * Grazer will move toward the target plant
     */
    private void moveToward(){
        
        // set speed
        if(this.EU >= 25.0){
            speed = (float) (maxSpeed * 0.75);
        }
        else if(this.EU < 25 && this.EU > 0){
            speed = (float) 10.0;
        }
        
        
        // regular distance movement
        if(this.x < target.x && Point2D.distance(this.x, this.y, target.x, target.y) >= speed){
            this.collision.setX((float) collision.getX() + speed);
            this.x += speed;
            expend(speed);
            line.set(this.x, this.y, target.x, target.y);
        }
        else if(this.x > target.x && Point2D.distance(this.x, this.y, target.x, target.y) >= speed){
            this.collision.setX((float) collision.getX() - speed);
            this.x -= speed;
            expend(speed);
            line.set(this.x, this.y, target.x, target.y);
        }
        
        if(this.y < target.y && Point2D.distance(this.x, this.y, target.x, target.y) >= speed){
            this.collision.setY((float) collision.getY() + speed);
            this.y += speed;
            expend(speed);
            line.set(this.x, this.y, target.x, target.y);
        }
        else if(this.y > target.y && Point2D.distance(this.x, this.y, target.x, target.y) >= speed){
            this.collision.setY((float) collision.getY() - speed);
            this.y -= speed;
            expend(speed);
            line.set(this.x, this.y, target.x, target.y);
        }
        
        // closing in
        if(this.x < target.x && Point2D.distance(this.x, this.y, target.x, target.y) < speed){
            this.collision.setX((float) collision.getX() + (float) 1.0);
            this.x += 1.0;
            expend(1);
            line.set(this.x, this.y, target.x, target.y);
        }
        else if(this.x > target.x && Point2D.distance(this.x, this.y, target.x, target.y) < speed){
            this.collision.setX((float) collision.getX() - (float) 1.0);
            this.x -= 1.0;
            expend(1);
            line.set(this.x, this.y, target.x, target.y);
        }
        
        if(this.y < target.y && Point2D.distance(this.x, this.y, target.x, target.y) < speed){
            this.collision.setY((float) collision.getY() + (float) 1.0);
            this.y += 1.0;
            expend(1);
            line.set(this.x, this.y, target.x, target.y);
        }
        else if(this.y > target.y && Point2D.distance(this.x, this.y, target.x, target.y) < speed){
            this.collision.setY((float) collision.getY() - (float) 1.0);
            this.y -= 1.0;
            expend(1);
            line.set(this.x, this.y, target.x, target.y);
        }
        
        // if the target has been reached
        if(this.x == target.x && this.y == target.y){
            reached = true;
        }
        
    }
    
    /**
     * Grazer will eat the target plant when it is reached
     * @param e 
     */
    private void eat(Environment e){
        
        // keep eating until the plant is gone
        if(!target.needsDeleting()){
            target.isBeingEaten();
            eating = true;
        }
        // delete the plant once it is eaten
        else{
            for(int i = 0; i < e.getPlants().size(); i++){
                if(e.getPlants().get(i) == target){
                    e.getPlants().remove(i);
                }
                
                // gain EU when finished eating
                this.EU  += this.energyInput;
            }
            
            // reset the boolean so that the grazer can find another target
            eating = false;
            reached = false;
            found = false;
        }
        
    }
    
    /**
     * Grazer will wander the map until food is found
     * @param e 
     */
    private void wander(Environment e){
        
        // set speed
        if(this.EU > 25){
            speed = (float) (maxSpeed * 0.75);
        }
        else if(this.EU < 25 && this.EU > 0){
            speed = (float) 10.0;
        }
        
        
        int direction = rand.nextInt(4) + 1;
        int distance = rand.nextInt(5) + 1;
        
        // moving left
        if(direction == 1){
            
            line.set(this.x, this.y, this.x - (distance * speed), this.y);
            
            // ensure no obstacle collision
            for(int i = 0; i < e.getObstacles().size(); i++){
                if(line.intersects(e.getObstacles().get(i).collision)){
                    return;
                }
            }
            
            // otherwise, start moving
            for(int i = 0; i < distance; i++){
               if(this.x - speed > 0){
                    collision.setX(collision.getX() - speed);
                    this.x -= speed;
                    expend(speed);
               }
               else{return;}
                
                findFood(e);
                
                if(found == true){
                    return;
                }
                
            }
        }
        
        // moving right
        if(direction == 2){
            
            line.set(this.x, this.y, this.x + (distance * speed), this.y);
            
            // ensure no obstacle collision
            for(int i = 0; i < e.getObstacles().size(); i++){
                if(line.intersects(e.getObstacles().get(i).collision)){
                    return;
                }
            }
            
            // otherwise, start moving
            for(int i = 0; i < distance; i++){
                if(this.x + speed < e.getWidth()){
                    collision.setX(collision.getX() + speed);
                    x += speed;
                    expend(speed);
                }
                else{return;}
                
                findFood(e);
                
                if(found == true){
                    return;
                }
            }
        }
        
        // moving up
        if(direction == 3){
            
            line.set(this.x, this.y, this.x, this.y - (distance * speed));
            
            // ensure no obstacle collision
            for(int i = 0; i < e.getObstacles().size(); i++){
                if(line.intersects(e.getObstacles().get(i).collision)){
                    return;
                }
            }
            
            // otherwise, start moving
            for(int i = 0; i < distance; i++){
                if(this.y - speed > 0){
                    collision.setY(collision.getY() - speed);
                    this.y -= speed;
                    expend(speed);
                }
                else{return;}
                
                findFood(e);
                
                if(found == true){
                    return;
                }
            }
        }
        
        // moving down
        if(direction == 4){
           
            line.set(this.x, this.y, this.x, this.y + (distance * speed));
            
            // ensure no obstacle collision
            for(int i = 0; i < e.getObstacles().size(); i++){
                if(line.intersects(e.getObstacles().get(i).collision)){
                    return;
                }
            }
            
            // otherwise, start moving
            for(int i = 0; i < distance; i++){
                if(this.y + speed < e.getHeight()){
                    collision.setY(collision.getY() + speed);
                    this.y += speed;
                    expend(speed);
                }
                else{return;}
                
                findFood(e);
                
                if(found == true){
                    return;
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
    }

    @Override
    public void Update(Environment e) {
        

        // grazer dies if energy falls to 0 or below
        if(this.EU <= 0){
            die(e);
        }
        
        // run from predators if necessary
        checkForPredators(e);
        
        if(running == true){
            run(e);
        }
        
        // reproduce if able
        if(this.EU >= this.energyToReproduce){
            reproduce(e);
        }
        
        // search for food
        findFood(e);
        
        // determine how to proceed when food is/isn't found
        if(found == true && reached == false){
            moveToward();
        }
        else if(found == true && reached == true){
            eat(e);
        }
        else{
            wander(e);
        }

        
    }
    
    
    /**
     * Return the amount of energy units the Grazer currently has
     * @return 
     */
    public int getEnergy() {
        return EU;
    }
}
