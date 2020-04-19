/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
    
    // ===== Variables used for target finding =====
    private HashMap<Double, SimulationObject> allTargets;
    private SimulationObject currentTarget;
    private Line line;
    public HashSet<SimulationObject> ignoreTargets;
    
    // ===== Proper movement variables =====
    private float properMaxSpeed;
    private float currentMaxSpeed;
    private int timeRunning;
    private int timeCoolingDown;
    private boolean coolingDown;
    
    // ===== Predator fighting variable =====
    public boolean isFighting;
    
    // ===== Used to give a smartish idle =====
    private final Random r;
    private float idleX, idleY;
    
    // ===== Used to see how much his has moved since last update =====
    private float lastUpdateX, lastUpdateY;
    
    // ===== Birth variables =====
    public String mateGenotype;
    public boolean isMating;
    public boolean isBearing;
    public int tBearing;
    public int tBirth;

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
        
        // Set speed variables
        if(this.genotype.contains("FF")) {
            // Use Homozygous Dominant speed
            properMaxSpeed = this.maxSpeedHOD;
        } else if(this.genotype.contains("Ff")) {
            // Use Heterozygous Dominant speed
            properMaxSpeed = this.maxSpeedHED;
        } else {
            // Use Homozygous Recessive speed
            properMaxSpeed = this.maxSpeedHOR;
        }
        currentMaxSpeed = properMaxSpeed;
        timeRunning = 0;
        timeCoolingDown = 0;
        coolingDown = false;
        
        // Set target finding and idle variables
        allTargets = new HashMap<>();
        currentTarget = null;
        ignoreTargets = new HashSet<>();
        
        r = new Random();
        
        idleX = r.nextInt(1001);
        idleY = r.nextInt(751);
        
        isFighting = false;
        
        isMating = false;
        isBearing = false;
        tBearing = 0;
        tBirth = 0;
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
    }

    /**
     * The update function for the predator
     * @param e The environment
     */
    @Override
    public void Update(Environment e) {
        // Distance calculation stuff
        lastUpdateX = x;
        lastUpdateY = y;
        
        // Calculate movespeed
        if(timeRunning >= maintainSpeed && !coolingDown){
            coolingDown = true;
            timeRunning = 0;
            currentMaxSpeed = 1;
        } else if (timeCoolingDown >= 15){
            coolingDown = false;
            timeCoolingDown = 0;
            currentMaxSpeed = properMaxSpeed;
        }
        
        if(coolingDown){ timeCoolingDown++; }
        else{ timeRunning++; }
        
        // Is mating the thing that should be done
        if(!isMating && !isBearing && EU >= energyToReProduce)
        {
            isMating = true;
        }
        
        
        // Find all valid targets
        if(isMating)
        {
            findMate(e);
        }
        else
        {
            findTargets(e);
        }
        
        // Out of those targets, select the closest as the target
        selectTarget();
        
        // Update time since birth
        tBirth++;
        
        // Move towards the current taget or idle
        if(currentTarget == null || (isBearing && tBearing<=20) || tBirth <= 20) {
            idle(e);
        }
        else {
            moveTowards(currentTarget.getX(), currentTarget.getY(), e);
        }
        
        
        // Eat / mate code
        if(currentTarget != null){
            if(x == currentTarget.getX() && y == currentTarget.getY()){
                if(currentTarget.getClass() == Grazer.class){
                    e.PredatorFight(this, (Grazer)currentTarget);
                }
                else{
                    if(!isMating && !isFighting)
                    {
                        e.PredatorFight(this, (Predator)currentTarget);
                    }
                    else if(isMating)
                    {
                        // ==== This is the mating process =====
                        this.mateGenotype = ((Predator)currentTarget).getGenotype();
                        this.isMating = false;
                        this.isBearing = true;
                        this.ignoreTargets.add(currentTarget);
                    }
                }
            }
        }
        
        // Birth code
        if(isBearing)
        {
            tBearing++;
            if(tBearing >= (gestaion * 24 * 60 * 60))
            {
                GiveBirth(e);
                tBearing = 0;
            }
        }
        
        
        // Properly update energy usage
        double dist = Point2D.distance(x, y, lastUpdateX, lastUpdateY);
        // Delta energy is equal to
        // DU * EU/DU
        EU -= dist * (energyOutput / 5);
        
        // Delete self if no energy
        if(EU <= 0) {
            e.removePredator(this);
        }
        
        // After every update, the predator should not be fighting
        isFighting = false;
    }
    
    /**
     * Get the predator's energy level
     * @return Current energy level
     */
    public int getEnergy() {
        return EU;
    }
    
    /**
     * Set the predator's energy level
     * @param e energy
     */
    public void setEnergy(int e) {
        EU = e;
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
            if(!ignoreTargets.contains(g))
            {
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
            }
        });
        
        // If aggressive
        if(genotype.charAt(0) == 'A'){
            // For all predators
            e.getPredators().forEach(p -> {
                if(!ignoreTargets.contains(p))
                {
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
        float prevX = x;
        float prevY = y;
        
        moveTowards(idleX, idleY, e);
        
        boolean flag = false;
        
        for(int i = 0; i < e.getNumObstacles(); i++) {
            if(collision.intersects(e.getObstacles().get(i).collision)){
                flag = true;
                x = prevX;
                y = prevY;
                this.collision.setCenterX(x);
                this.collision.setCenterY(y);
                break;
            }
        }
        
        if((x==idleX && y==idleY) || flag){
            idleX = r.nextInt(1001);
            idleY = r.nextInt(751);
        }
    }
    
    /**
     * Move towards the objects current target
     * @param e The simulation environment variable
     */
    private void moveTowards(float targetX, float targetY, Environment e){
        int xDelta = 0;
        int yDelta = 0;
        // X direction
        if(x < targetX){
            if(targetX - x < currentMaxSpeed) {
                xDelta += targetX - x;
            }
            else {
                xDelta += currentMaxSpeed;
            }
        }
        else if(x > targetX) {
            if(x - targetX < currentMaxSpeed) {
                xDelta -= x - targetX;
            }
            else {
                xDelta -= currentMaxSpeed;
            }
        }

        // Y direction
        if(y < targetY){
            if(targetY - y < currentMaxSpeed) {
                yDelta += targetY - y;
            }
            else {
                yDelta += currentMaxSpeed;
            }
        }
        else if(y > targetY) {
            if(y - targetY < currentMaxSpeed) {
                yDelta -= y - targetY;
            }
            else {
                yDelta -= currentMaxSpeed;
            }
        }
        x += xDelta;
        if(x > e.getWidth()){x = (float) e.getWidth();} else if(x < 0) { x = 0;}
        y += yDelta;
        if(y > e.getHeight()){y = (float) e.getHeight();} else if(y < 0) {y = 0;}
        
        // Move the actual shape
        this.collision.setCenterX(x);
        this.collision.setCenterY(y);
    }
    
    /**
     * Gives birth to the proper number of children with the correct genotype
     * @param e The simulation environment variable
     */
    private void GiveBirth(Environment e)
    {
        int noChildren = r.nextInt(maxOffspring) + 1;
        for(int i = 0; i < noChildren; i++)
        {
            // Figure out aggression of child
            char tempArray1[] = {this.genotype.charAt(r.nextInt(2)), mateGenotype.charAt(r.nextInt(2))};
            Arrays.sort(tempArray1);
            String a = new String(tempArray1);

            // Figure out strength of child
            char tempArray2[] = {this.genotype.charAt(r.nextInt(2)+3), mateGenotype.charAt(r.nextInt(2)+3)};
            Arrays.sort(tempArray2);
            String s = new String(tempArray2);
            
            // Figure out speed of child
            char tempArray3[] = {this.genotype.charAt(r.nextInt(2)+6), mateGenotype.charAt(r.nextInt(2)+6)};
            Arrays.sort(tempArray3);
            String f = new String(tempArray3);
            
            String childGeneString = a + " " + s + " " + f;
            Predator myChild = new Predator(x, y, EU, childGeneString, maxSpeedHOD, maxSpeedHED, maxSpeedHOR, maintainSpeed, energyOutput, energyToReProduce, maxOffspring, gestaion, offspringEnergy);
            myChild.ignoreTargets.add(this);
            ignoreTargets.add(this);
            e.addPredator(myChild);
        }
     
        
        // Turn off the mating boolean
        isBearing = false;
    }
    
    /**
     * Find all possible mating targets
     * @param e The simulation environment variable
     */
    private void findMate(Environment e){
        // Clear the possible targets
        allTargets.clear();
        
        // For all predators
        e.getPredators().forEach(p -> {
            if(!ignoreTargets.contains(p))
            {
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
            }
        });
    }
    
}
