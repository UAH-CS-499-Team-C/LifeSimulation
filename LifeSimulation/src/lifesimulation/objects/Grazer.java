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
    private Plant target;
    private boolean found;
    private Line line = new Line(0, 0, 0, 0); // default line to be changed as needed
    private boolean reached = false;
    private boolean eating = false;
    private float moved = 0;
    
    
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
    
    // expend energy when moving
    private void expend(){
        moved += maintainSpeed;
        
        // reduce EU if moved 5 or more DU
        if(moved >= 5.0){
            this.EU -= (int) this.energyOutput;
            moved -= 5.0;
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
                if(line.intersects(e.getObstacles().get(j).collision)){
                    possibleTargets.remove(i); // remove the target from the list
                    //line = null; // remove the line
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
    
    // move toward the target
    private void moveToward(){
        
        // regular distance movement
        if(this.x < target.x && Point2D.distance(this.x, this.y, target.x, target.y) >= maintainSpeed){
            this.collision.setX((float) collision.getX() + maintainSpeed);
            this.x += maintainSpeed;
            
            line.set(this.x, this.y, target.x, target.y);
        }
        else if(this.x > target.x && Point2D.distance(this.x, this.y, target.x, target.y) >= maintainSpeed){
            this.collision.setX((float) collision.getX() - maintainSpeed);
            this.x -= maintainSpeed;
            
            line.set(this.x, this.y, target.x, target.y);
        }
        
        if(this.y < target.y && Point2D.distance(this.x, this.y, target.x, target.y) >= maintainSpeed){
            this.collision.setY((float) collision.getY() + maintainSpeed);
            this.y += maintainSpeed;
            
            line.set(this.x, this.y, target.x, target.y);
        }
        else if(this.y > target.y && Point2D.distance(this.x, this.y, target.x, target.y) >= maintainSpeed){
            this.collision.setY((float) collision.getY() - maintainSpeed);
            this.y -= maintainSpeed;
            
            line.set(this.x, this.y, target.x, target.y);
        }
        
        // closing in
        if(this.x < target.x && Point2D.distance(this.x, this.y, target.x, target.y) < maintainSpeed){
            this.collision.setX((float) collision.getX() + (float) 1.0);
            this.x += 1.0;
            
            line.set(this.x, this.y, target.x, target.y);
        }
        else if(this.x > target.x && Point2D.distance(this.x, this.y, target.x, target.y) < maintainSpeed){
            this.collision.setX((float) collision.getX() - (float) 1.0);
            this.x -= 1.0;
            
            line.set(this.x, this.y, target.x, target.y);
        }
        
        if(this.y < target.y && Point2D.distance(this.x, this.y, target.x, target.y) < maintainSpeed){
            this.collision.setY((float) collision.getY() + (float) 1.0);
            this.y += 1.0;
            
            line.set(this.x, this.y, target.x, target.y);
        }
        else if(this.y > target.y && Point2D.distance(this.x, this.y, target.x, target.y) < maintainSpeed){
            this.collision.setY((float) collision.getY() - (float) 1.0);
            this.y -= 1.0;
            
            line.set(this.x, this.y, target.x, target.y);
        }
        
        // if the target has been reached
        if(this.x == target.x && this.y == target.y){
            reached = true;
        }
        
    }
    
    // eat the target plant
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
            }
            
            // reset the boolean so that the grazer can find another target
            eating = false;
            reached = false;
            found = false;
        }
        
    }
    
    // wander the map in search of food
    
    private void wander(Environment e){
        
        int direction = rand.nextInt(4) + 1;
        int distance = rand.nextInt(5) + 1;
        
        // DELETE THIS AFTER TESTING
        System.out.println("maintainSpeed: " + maintainSpeed);
        System.out.println("maxSpeed: " + maxSpeed);
        
        // moving left
        if(direction == 1){
            
            line.set(this.x, this.y, this.x - (distance * maintainSpeed), this.y);
            
            // ensure no obstacle collision
            for(int i = 0; i < e.getObstacles().size(); i++){
                if(line.intersects(e.getObstacles().get(i).collision)){
                    return;
                }
            }
            
            // otherwise, start moving
            for(int i = 0; i < distance; i++){
               if(this.x - maintainSpeed > 0){
                    collision.setX(collision.getX() - maintainSpeed);
                    this.x -= maintainSpeed;
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
            
            line.set(this.x, this.y, this.x + (distance * maintainSpeed), this.y);
            
            // ensure no obstacle collision
            for(int i = 0; i < e.getObstacles().size(); i++){
                if(line.intersects(e.getObstacles().get(i).collision)){
                    return;
                }
            }
            
            // otherwise, start moving
            for(int i = 0; i < distance; i++){
                if(this.x + maintainSpeed < e.getWidth()){
                    collision.setX(collision.getX() + maintainSpeed);
                    x += maintainSpeed;
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
            
            line.set(this.x, this.y, this.x, this.y - (distance * maintainSpeed));
            
            // ensure no obstacle collision
            for(int i = 0; i < e.getObstacles().size(); i++){
                if(line.intersects(e.getObstacles().get(i).collision)){
                    return;
                }
            }
            
            // otherwise, start moving
            for(int i = 0; i < distance; i++){
                if(this.y - maintainSpeed > 0){
                    collision.setY(collision.getY() - maintainSpeed);
                    this.y -= maintainSpeed;
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
           
            line.set(this.x, this.y, this.x, this.y + (distance * maintainSpeed));
            
            // ensure no obstacle collision
            for(int i = 0; i < e.getObstacles().size(); i++){
                if(line.intersects(e.getObstacles().get(i).collision)){
                    return;
                }
            }
            
            // otherwise, start moving
            for(int i = 0; i < distance; i++){
                if(this.y + maintainSpeed < e.getHeight()){
                    collision.setY(collision.getY() + maintainSpeed);
                    this.y += maintainSpeed;
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
        
        
        if(found == true){
            g.drawLine(this.x, this.y, target.x, target.y);
        }
    }

    @Override
    public void Update(Environment e) {
        
        findFood(e);
        
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
    
    
    
    public int getEnergy() {
        return EU;
    }
}
