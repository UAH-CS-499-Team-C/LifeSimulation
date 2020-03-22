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
            
            if(Point2D.distance(this.x, this.y, p.x, p.y) <= 5 + p.getDiameter()){
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
                    
                    // stop if grazer hits an object
                    for(int j = 0; j < e.getObstacles().size(); j++){
                        if(this.collision.intersects(e.getObstacles().get(j).collision)){
                            return;
                        }
                    }
                    
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
                    
                    // stop if grazer hits an object
                    for(int j = 0; j < e.getObstacles().size(); j++){
                        if(this.collision.intersects(e.getObstacles().get(j).collision)){
                            return;
                        }
                    }
                    
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
                    
                    // stop if grazer hits an object
                    for(int j = 0; j < e.getObstacles().size(); j++){
                        if(this.collision.intersects(e.getObstacles().get(j).collision)){
                            return;
                        }
                    }
                    
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
                    
                    // stop if grazer hits an object
                    for(int j = 0; j < e.getObstacles().size(); j++){
                        if(this.collision.intersects(e.getObstacles().get(j).collision)){
                            return;
                        }
                    }
                    
                    findFood(e);

                    if(found == true){
                        break;
                    }
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
