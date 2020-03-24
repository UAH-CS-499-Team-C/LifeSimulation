/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Random;
import lifesimulation.utilities.Environment;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;

/**
 * Implementation class of the Predator Creature
 * @author sam
 */
public class Predator extends SimulationObject implements LivingCreature{
    
    private int EU;
    private String genotype;
    private float maxSpeedHOD;
    private float maxSpeedHED;
    private float maxSpeedHOR;
    private float maintainSpeed;
    private float energyOutput;
    private float energyToReProduce;
    private int maxOffspring;
    private float gestaion;
    private float offspringEnergy;
    
    // Target finding
    private HashMap<Double, SimulationObject> allTargets;
    private SimulationObject currentTarget;
    private Line line;
    
    // Variables used to give a smartish idle
    boolean leftOpen;
    boolean rightOpen;
    boolean upOpen;
    boolean downOpen;
    Random r;

    /**
     * Constructor for predator class
     * @param x X location of the predator
     * @param y Y location of the predator
     * @param EU Predator's energy level
     * @param genotype Predator's genotype
     * @param maxSpeedHOD HOD predator's max speed
     * @param maxSpeedHED HED predator's max speed
     * @param maxSpeedHOR HOR predator's max speed
     * @param maintainSpeed Predator's standard speed
     * @param energyOutput Energy obtained from plant
     * @param energyToReProduce Energy needed to reproduce
     * @param maxOffspring Max number of offspring
     * @param gestaion Time from start of reproduction to childbirth
     * @param offspringEnergy Starting energy of the child
     */
    public Predator(float x, float y, int EU, String genotype, float maxSpeedHOD, float maxSpeedHED, float maxSpeedHOR, float maintainSpeed, float energyOutput, float energyToReProduce, int maxOffspring, float gestaion, float offspringEnergy) {
        super(x, y);
        this.EU = EU;
        this.genotype = genotype;
        this.maxSpeedHOD = maxSpeedHOD;
        this.maxSpeedHED = maxSpeedHED;
        this.maxSpeedHOR = maxSpeedHOR;
        this.maintainSpeed = maintainSpeed;
        this.energyOutput = energyOutput;
        this.energyToReProduce = energyToReProduce;
        this.maxOffspring = maxOffspring;
        this.gestaion = gestaion;
        this.offspringEnergy = offspringEnergy;
        this.collision = new Circle(x, y, 7);
        
        allTargets = new HashMap<>();
        currentTarget = null;
        r = new Random();
    }

    /**
     * The drawing function for the predator
     * @param g The graphics object
     */
    @Override
    public void draw(Graphics g) {
        // Draw this predator
        g.setColor(Color.red);
        g.fill(collision);
        g.setLineWidth(2);
        g.setColor(Color.black);
        g.draw(collision);
        
        // Draw a black line to each valid target
        g.setColor(Color.black);
        allTargets.values().forEach(t -> {
            g.drawLine(x, y, t.getX(), t.getY());
        });
        
        // If there is a current target, draw a red line to it
        if(currentTarget != null){
            g.setColor(Color.red);
            g.drawLine(x,y, currentTarget.getX(), currentTarget.getY());
        }
    }

    /**
     * The update function for the predator
     * @param e The environment
     */
    @Override
    public void Update(Environment e) {
        // Find all valid targets
        findTargets(e);
        
        // Out of those targets, select the closest as the target
        selectTarget();
        
        // Move towards the current taget
        if(currentTarget == null) {
            idle(e);
        }
        else {
            moveTowards();
        }
        
        // Move the actual shape
        this.collision.setCenterX(x);
        this.collision.setCenterY(y);
        
        // Testing eat code
        if(currentTarget != null){
            if(x == currentTarget.getX() && y == currentTarget.getY()){
                if(currentTarget.getClass() == Grazer.class){
                    e.removeGrazer((Grazer)currentTarget);
                    currentTarget = null;
                }
                else{
                    e.removePredator((Predator)currentTarget);
                    currentTarget = null;
                }
            }
        }
    }
    
    /**
     * Get the predator's energy level
     * @return Current energy level
     */
    public int getEnergy() {
        return EU;
    }
    
    /**
     * Get the predator's genotype
     * @return Genotype string
     */
    public String getGenotype() {
        return genotype;
    }
    
    /**
     * Finds all valid targets within range and visible
     * @param e The environment that contains the predators, grazers, and rocks
     */
    private void findTargets(Environment e){
        // Clear the possible targets
        allTargets.clear();
        
        // For all grazers
        e.getGrazers().forEach(g -> {
            // If within the visibile distance
            if(Point2D.distance(x, y, g.getX(), g.getY()) <= 150) {
                // Make the tmp line of sight from pred to possbile target
                line = new Line(x, y, g.getX(), g.getY());
                // Save blocked flag
                boolean flag = true;
                
                // See if line of sight blocked by an obstacle
                for(int i = 0; i < e.getNumObstacles(); i++) {
                    if(line.intersects(e.getObstacles().get(i).collision)){
                        flag = false;
                        break;
                    }
                }
                line = null;
                
                // If not blocked by anything, add to possible targets
                if(flag || Point2D.distance(x, y, g.getX(), g.getY()) <= 25){ allTargets.put(Point2D.distance(x, y, g.getX(), g.getY()), g); }
            }
        });
        
        // If aggressive
        if(genotype.charAt(0) == 'A'){
            // For all predators
            e.getPredators().forEach(p -> {
                // If within the visibile distance
                if(p != this && Point2D.distance(x, y, p.getX(), p.getY()) <= 150) {
                    // Make the tmp line of sight from pred to possbile target
                    line = new Line(x, y, p.getX(), p.getY());
                    // Save blocked flag
                    boolean flag = true;

                    // See if line of sight blocked by an obstacle
                    for(int i = 0; i < e.getNumObstacles(); i++) {
                        if(line.intersects(e.getObstacles().get(i).collision)){
                            flag = false;
                            break;
                        }
                    }
                    
                    line = null;

                    // If not blocked by anything, add to possible targets
                    if(flag || Point2D.distance(x, y, p.getX(), p.getY()) <= 25){ allTargets.put(Point2D.distance(x, y, p.getX(), p.getY()), p); }
                }
            });
        }
    }
    
    /**
     * Selects which object from allTargets is the current target
     */
    private void selectTarget() {
        try{
            currentTarget = allTargets.get(Collections.min(allTargets.keySet()));
            
        }
        catch(NoSuchElementException e) {
            currentTarget = null;
        }
    }
    
    /**
     * Semi-random movement if there is not target
     */
    private void idle(Environment e) {
        leftOpen = true;
        rightOpen = true;
        upOpen = true;
        downOpen = true;
        
        float d = 3.5f + maintainSpeed;
        
        for(int i = 0; i < e.getNumObstacles(); i++){
            Obstacle o = e.getObstacles().get(i);
            if(o.collision.contains(x-d, y)){leftOpen = false;}
            if(o.collision.contains(x+d, y)){rightOpen = false;}
            if(o.collision.contains(x, y-d)){upOpen = false;}
            if(o.collision.contains(x, y+d)){downOpen = false;}
            if(!leftOpen && !rightOpen && !downOpen && !upOpen){break;}
        }
        
        float xDelta = 0;
        if(leftOpen && rightOpen){
            xDelta = r.nextInt(3) - 1f; // Range -1,1
        } else if(leftOpen){
            xDelta = r.nextInt(2) - 1f; // Range -1,0
        } else if(rightOpen) {
            xDelta = r.nextInt(2); // Range 0, 1
        }
        
        float yDelta = 0;
        if(downOpen && upOpen){
            yDelta = r.nextInt(3) - 1f; // Range -1,1
        } else if(upOpen){
            yDelta = r.nextInt(2) - 1f; // Range -1,0
        } else if (downOpen) {
            yDelta = r.nextInt(2); // Range 0, 1
        }
        
        x += xDelta * maintainSpeed;
        if(x > 1000){x = 1000;} else if(x < 0) { x = 0;}
        y += yDelta * maintainSpeed;
        if(y > 750){y = 750;} else if(y < 0) {y = 0;}

    }
    
    /**
     * Move towards the objects current target
     * @param e 
     */
    private void moveTowards(){
        int xDelta = 0;
        int yDelta = 0;
        // X direction
        if(x < currentTarget.getX()){
            if(currentTarget.getX() - x < maintainSpeed) {
                xDelta += currentTarget.getX() - x;
            }
            else {
                xDelta += maintainSpeed;
            }
        }
        else if(x > currentTarget.getX()) {
            if(x - currentTarget.getX() < maintainSpeed) {
                xDelta -= x - currentTarget.getX();
            }
            else {
                xDelta -= maintainSpeed;
            }
        }

        // Y direction
        if(y < currentTarget.getY()){
            if(currentTarget.getY() - y < maintainSpeed) {
                yDelta += currentTarget.getY() - y;
            }
            else {
                yDelta += maintainSpeed;
            }
        }
        else if(y > currentTarget.getY()) {
            if(y - currentTarget.getY() < maintainSpeed) {
                yDelta -= y - currentTarget.getY();
            }
            else {
                yDelta -= maintainSpeed;
            }
        }
        x += xDelta;
        if(x > 1000){x = 1000;} else if(x < 0) { x = 0;}
        y += yDelta;
        if(y > 750){y = 750;} else if(y < 0) {y = 0;}
    }
    
}
