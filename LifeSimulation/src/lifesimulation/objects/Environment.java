/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation.objects;

import java.util.ArrayList;
import pkgLifeSimDataParser.LifeSimDataParser;

/**
 * A utilities class that holds all simulation objects. Provides a nice, clean
 *  point of contact for all of the needed functions.
 * @author sam
 */
public class Environment {
    
    /**
     * Singleton instance
     */
    private static Environment instance = null;
    
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
     * Singleton GetInstance function
     * @return The instance
     */
    public static Environment GetInstance() 
    { 
        if (instance == null) 
            instance = new Environment(); 
  
        return instance; 
    } 

    /**
     * Constructor
     */
    private Environment() {
        obstacles = new ArrayList<>();
        plants = new ArrayList<>();
        grazers = new ArrayList<>();
        predators = new ArrayList<>();
        
        plantsToAdd = new ArrayList<>();
        
        LoadData();
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
    }
    
    /**
     * Update all objects in environment
     */
    public void update() {
        plants.forEach(x -> x.Update(this));
        grazers.forEach(x -> x.Update(this));
        predators.forEach(x -> x.Update(this));
        
        plants.addAll(plantsToAdd);
        
        plantsToAdd.clear();
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
    
}
