/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import lifesimulation.utilities.Environment;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

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
    
    private ArrayList<SimulationObject> allTargets;
    private LivingCreature currentTarget;

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
        
        allTargets = new ArrayList<>();
        currentTarget = null;
    }

    

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fill(collision);
        g.setLineWidth(2);
        g.setColor(Color.black);
        g.draw(collision);
        
        allTargets.forEach(t -> {
            g.drawLine(x, y, t.x, t.y);
        });
    }

    @Override
    public void Update(Environment e) {
        findTargets(e);
        
        this.collision = new Circle(x, y, 7);
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
        // Check all grazers
        e.getGrazers().forEach(g -> {
            if(Point2D.distance(x, y, g.x, g.y) <= 150) {
                allTargets.add(g);
            }
        });
        
        // Only check predators if AA or Aa genotype
        if(genotype.charAt(0) == 'A'){
            e.getPredators().forEach(p -> {
                if(Point2D.distance(x, y, p.x, p.y) <= 150) {
                    allTargets.add(p);
                }
            });
        }
    }
    
    
}
