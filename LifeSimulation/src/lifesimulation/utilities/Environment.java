/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.utilities;

import java.util.ArrayList;
import java.util.Random;
import lifesimulation.objects.Grazer;
import lifesimulation.objects.Obstacle;
import lifesimulation.objects.Plant;
import lifesimulation.objects.Predator;

/**
 * A utilities class that holds all simulation objects. Provides a nice, clean
 *  point of contact for all of the needed functions.
 * @author sam
 */
public class Environment {
    /**
     * All Obstacle objects
     */
    private ArrayList<Obstacle> obstacles;
    /**
     * All Plant objects
     */
    private ArrayList<Plant> plants;
    /**
     * All Grazer objects
     */
    private ArrayList<Grazer> grazers;
    /**
     * All Predator objects
     */
    private ArrayList<Predator> predators;
    
    /**
     * Plants to be added to array
     */
    private ArrayList<Plant> plantsToAdd;
    
    /**
     * Plants to be removed from array
     */
    private ArrayList<Plant> plantsToRemove;
    
    /**

     * Grazers to be added to array
     */
    private ArrayList<Grazer> grazersToAdd;
    
    /**
     * Grazers to be deleted
     */
    private ArrayList<Grazer> deadGrazers = new ArrayList<>();
    
    /**
     * Grazers to be removed from array
     */
    private ArrayList<Grazer> grazersToRemove;
    
    /**
     * Predators to be added to array
     */
    private ArrayList<Predator> predatorsToAdd;
    
    /**
     * Predators to be removed from array
     */
    private ArrayList<Predator> predatorsToRemove;
    
    private int t;
    
    // Random used during predator fights
    private Random r;
    
    
    private LifeSimDataParser lsdp;

    /**
     * Constructor
     */
    public Environment() {
        obstacles = new ArrayList<>();
        plants = new ArrayList<>();
        grazers = new ArrayList<>();
        predators = new ArrayList<>();
        plantsToAdd = new ArrayList<>();
        plantsToRemove = new ArrayList<>();
        grazersToAdd = new ArrayList<>();
        grazersToRemove = new ArrayList<>();
        predatorsToAdd = new ArrayList<>();
        predatorsToRemove = new ArrayList<>();

        
        LoadData();
        
        t = 0;
        
        r = new Random();
    }
    
    /**
     * Loads data from parser into ArrayLists
     */
    private void LoadData() {
        // Setup parser
        LifeSimDataParser lsdp = LifeSimDataParser.getInstance();
        
        // Load all obstacles from parser
        int iObstacleCount = lsdp.getObstacleCount();
        for(int i=0; i < iObstacleCount; i++)
        {
            if(lsdp.getObstacleData()) {
                obstacles.add(new Obstacle(lsdp.ObstacleX, lsdp.ObstacleY, lsdp.ObstacleDiameter, lsdp.ObstacleHeight));
            } else {
                System.out.println("Error reading data for obstacle " + i);
            }
        }
        
        // Load all plants from the parser
        int iPlantCount = lsdp.getInitialPlantCount();
        float fGrowthRate = (float)lsdp.getPlantGrowthRate();
        float fMaxSize = (float)lsdp.getMaxPlantSize();
        float fMaxSeedNumber = (float)lsdp.getMaxSeedNumber();
        float fMaxSeedCastDistance = (float)lsdp.getMaxSeedCastDistance();
        float fSeedViability = (float)lsdp.getSeedViability();
        for(int i = 0; i < iPlantCount; i++) {
            if(lsdp.getPlantData()) {
                plants.add(new Plant(lsdp.PlantX, lsdp.PlantY, lsdp.PlantDiameter, fGrowthRate, fMaxSize, fMaxSeedNumber, fMaxSeedCastDistance, fSeedViability));
            } else {
                System.out.println("Error reading data for plant " + i);
            }
        }
        
        // Load all grazers from the parser
        int iGrazerCount = lsdp.getInitialGrazerCount();
        float fEnergyInput = lsdp.getGrazerEnergyInputRate();
        float fEnergyOutput = lsdp.getGrazerEnergyOutputRate();
        float fEnergyToReproduce = lsdp.getGrazerEnergyToReproduce();
        float fMaintainSpeed = (float)lsdp.getGrazerMaintainSpeedTime();
        float fMaxSpeed = (float)lsdp.getGrazerMaxSpeed();
        for(int i = 0; i < iGrazerCount; i++){
            if(lsdp.getGrazerData()) {
                System.out.println("added");
                grazers.add(new Grazer(lsdp.GrazerX, lsdp.GrazerY, lsdp.GrazerEnergy, fEnergyInput, fEnergyOutput, fEnergyToReproduce, fMaintainSpeed, fMaxSpeed));
                } else {
                System.out.println("Error reading data for grazer " + i);
            }
        }
        
        // Load all predators from parser
        int iPredatorCount = lsdp.getInitialPredatorCount();
        float fHOD = (float)lsdp.getPredatorMaxSpeedHOD();
        float fHED = (float)lsdp.getPredatorMaxSpeedHED();
        float fHOR = (float)lsdp.getPredatorMaxSpeedHOR();
        fMaintainSpeed = (float)lsdp.getPredatorMaintainSpeedTime();
        fEnergyOutput = lsdp.getPredatorEnergyOutputRate();
        fEnergyToReproduce = lsdp.getPredatorEnergyToReproduce();
        int iMaxOffspring = lsdp.getPredatorMaxOffspring();
        float fGestation = (float)lsdp.getPredatorGestationPeriod();
        float fOffspringEnergy = lsdp.getPredatorOffspringEnergyLevel();
        for(int i=0; i < iPredatorCount; i++) {
            if(lsdp.getPredatorData()){
                System.out.println("added");
                predators.add(new Predator(lsdp.PredatorX, lsdp.PredatorY, (int)lsdp.PredatorEnergy, lsdp.PredatorGenotype, fHOD, fHED, fHOR, fMaintainSpeed, fEnergyOutput, fEnergyToReproduce, iMaxOffspring, fGestation, fOffspringEnergy));
            } else{
                System.out.println("Error reading data for predator " + i);
            }
        }
        
    }
    
    /**
     * Update all objects in environment
     */
    public void update() {
        plants.forEach(x -> x.Update(this));
        grazers.forEach(x -> x.Update(this));
        predators.forEach(x -> x.Update(this));
        
        plants.addAll(plantsToAdd);

        grazers.addAll(grazersToAdd);
        
        plantsToAdd.clear();
        grazersToAdd.clear();
        
        if(!deadGrazers.isEmpty()){
            deleteDeadGrazers();
        }

        plants.removeAll(plantsToRemove);
        grazers.addAll(grazersToAdd);
        grazers.removeAll(grazersToRemove);
        predators.addAll(predatorsToAdd);
        predators.removeAll(predatorsToRemove);
        
        plantsToAdd.clear();
        plantsToRemove.clear();
        grazersToAdd.clear();
        grazersToRemove.clear();
        predatorsToAdd.clear();
        predatorsToRemove.clear();

        
        t++;
    }
    
    /**
     * Calculates what happens when a predator fights a grazer
     * @param p1 One of the predators
     * @param p2 The other predator
     */
    public void PredatorFight(Predator p1, Predator p2)
    {
        // Set both predators to fighting so this will not be called twice
        p1.isFighting = true;
        p2.isFighting = true;
        
        // Get strength value from genotype
        String s1 = p1.getGenotype().substring(3, 5);
        String s2 = p2.getGenotype().substring(3, 5);
        
        // ===== Case 1, both strengths are the same =====
        if(s1.equals(s2))
        {
            // Should they fight?
            if(r.nextBoolean())
            {
                // Should p1 win?
                if(r.nextBoolean())
                {
                    predatorsToRemove.add(p2);
                    p1.setEnergy(p1.getEnergy() + (int)(0.9 * p2.getEnergy()));
                }
                else
                {
                    predatorsToRemove.add(p1);
                    p2.setEnergy(p2.getEnergy() + (int)(0.9 * p1.getEnergy()));
                }
            }
            else // Not fight
            {
                p1.ignoreTargets.add(p2);
                p2.ignoreTargets.add(p1);
            }
        }
        
        // ===== Case 2, p1 is SS =====
        else if(s1.equals("SS"))
        {
            if(s2.equals("Ss"))
            {
                if(r.nextInt(100) < 75)
                {
                    predatorsToRemove.add(p2);
                    p1.setEnergy(p1.getEnergy() + (int)(0.9 * p2.getEnergy()));
                    System.out.println(p1 + " ate " + p2);
                }
                else
                {
                    predatorsToRemove.add(p1);
                    p2.setEnergy(p2.getEnergy() + (int)(0.9 * p1.getEnergy()));
                    System.out.println(p2 + " ate " + p1);
                }
            }
            else
            {
                if(r.nextInt(100) < 95)
                {
                    predatorsToRemove.add(p2);
                    p1.setEnergy(p1.getEnergy() + (int)(0.9 * p2.getEnergy()));
                    System.out.println(p1 + " ate " + p2);
                }
                else
                {
                    predatorsToRemove.add(p1);
                    p2.setEnergy(p2.getEnergy() + (int)(0.9 * p1.getEnergy()));
                    System.out.println(p2 + " ate " + p1);
                }
            }
        }
        else if(s1.equals("Ss"))
        {
            if(s2.equals("SS"))
            {
                if(r.nextInt(100) < 25)
                {
                    predatorsToRemove.add(p2);
                    p1.setEnergy(p1.getEnergy() + (int)(0.9 * p2.getEnergy()));
                    System.out.println(p1 + " ate " + p2);
                }
                else
                {
                    predatorsToRemove.add(p1);
                    p2.setEnergy(p2.getEnergy() + (int)(0.9 * p1.getEnergy()));
                    System.out.println(p2 + " ate " + p1);
                }
            }
            else
            {
                if(r.nextInt(100) < 75)
                {
                    predatorsToRemove.add(p2);
                    p1.setEnergy(p1.getEnergy() + (int)(0.9 * p2.getEnergy()));
                    System.out.println(p1 + " ate " + p2);
                    
                }
                else
                {
                    predatorsToRemove.add(p1);
                    p2.setEnergy(p2.getEnergy() + (int)(0.9 * p1.getEnergy()));
                    System.out.println(p2 + " ate " + p1);
                }
            }
        }
        else
        {
            if(s2.equals("SS"))
            {
                if(r.nextInt(100) < 5)
                {
                    predatorsToRemove.add(p2);
                    p1.setEnergy(p1.getEnergy() + (int)(0.9 * p2.getEnergy()));
                    System.out.println(p1 + " ate " + p2);
                }
                else
                {
                    predatorsToRemove.add(p1);
                    p2.setEnergy(p2.getEnergy() + (int)(0.9 * p1.getEnergy()));
                    System.out.println(p2 + " ate " + p1);
                }
            }
            else
            {
                if(r.nextInt(100) < 25)
                {
                    predatorsToRemove.add(p2);
                    p1.setEnergy(p1.getEnergy() + (int)(0.9 * p2.getEnergy()));
                    System.out.println(p1 + " ate " + p2);
                }
                else
                {
                    predatorsToRemove.add(p1);
                    p2.setEnergy(p2.getEnergy() + (int)(0.9 * p1.getEnergy()));
                    System.out.println(p2 + " ate " + p1);
                }
            }
        }
    }
    
    /**
     * Calculates what happens when a predator fights a grazer
     * @param p The predator
     * @param g The grazer
     */
    public void PredatorFight(Predator p, Grazer g)
    {
        // Find the predator's strength
        String s = p.getGenotype().substring(3, 5);
        
        if(s.equals("SS"))
        {
            if(r.nextInt(100) < 95)
            {
                grazersToRemove.add(g);
                p.setEnergy(p.getEnergy() + (int)(0.9 * g.getEnergy()));
                System.out.println(p + " ate " + g);
            }
        }
        else if(s.equals("Ss"))
        {
            if(r.nextInt(100) < 75)
            {
                grazersToRemove.add(g);
                p.setEnergy(p.getEnergy() + (int)(0.9 * g.getEnergy()));
                System.out.println(p + " ate " + g);
            }
        }
        else
        {
            if(r.nextInt(100) < 50)
            {
                grazersToRemove.add(g);
                p.setEnergy(p.getEnergy() + (int)(0.9 * g.getEnergy()));
                System.out.println(p + " ate " + g);
            }
        }
    }
    
    /**
     * 
     * @return The number of obstacles in the simulation
     */
    public int getNumObstacles() {
        return obstacles.size();
    }
    
    /**
     * 
     * @return The number of plants in the simulation
     */
    public int getNumPlants() {
        return plants.size();
    }
    
    /**
     * 
     * @return The number of grazer objects in the simulation
     */
    public int getNumGrazers() {
        return grazers.size();
    }
    
    /**
     * 
     * @return The number of predators in the simulation
     */
    public int getNumPredators() {
        return predators.size();
    }
    
    /**
     * 
     * @return The ArrayList of obstacles
     */
    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }
    
    /**
     * 
     * @return The ArrayList of plants
     */
    public ArrayList<Plant> getPlants() {
        return plants;
    }

    /**
     * 
     * @return The ArrayList of grazer objects
     */
    public ArrayList<Grazer> getGrazers() {
        return grazers;
    }

    /**
     * 
     * @return The ArrayList of predators
     */
    public ArrayList<Predator> getPredators() {
        return predators;
    }
    
    /**
     * 
     * @param p Plant to be added
     */
    public void addPlant(Plant p) {
        plantsToAdd.add(p);
    }
    
    /**
     * 
     * @param   Grazer to be added 
     */
    public void addGrazer(Grazer g){
        grazersToAdd.add(g);
    }
    
    public void addDeadGrazer(Grazer g){
        deadGrazers.add(g);
    }
    
    private void deleteDeadGrazers(){
        deadGrazers.forEach(g -> grazers.remove(g));
    }
    
    
    
    /**
     * 
     * @param g Grazer to be removed
     */
    public void removeGrazer(Grazer g) {
        grazersToRemove.add(g);
    }
    
    /**
     * 
     * @param p Predator to be added
     */
    public void addPredator(Predator p) {
        predatorsToAdd.add(p);
    }
    
    /**
     * 
     * @param p Predator to be removed
     */
    public void removePredator(Predator p) {
        predatorsToRemove.add(p);
    }
    
    /**
     * Gets the simulation time
     * @return Current simulation time
     */
    public int getTime() {
        return t;
    }
    
    // get the world width
    public double getWidth(){
        return lsdp.getInstance().getWorldWidth();
    }
    
    // get the world height
    public double getHeight(){
        return lsdp.getInstance().getWorldHeight();
    }
}
