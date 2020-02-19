//===================================================================================
// Life Simulation Data Parser Java
// File: LifeSimDataParserMain.java
//
// Author: Dr. Rick Coleman
// Date: January 2017
//
// Note: See comments in LifeSimDataParser.java about the lack of reference
//		parameters for functions in Java.
//===================================================================================
package pkgLifeSimDataParser;

public class LifeSimDataParserMain
{
	public String DATAFILE;
;
	public LifeSimDataParserMain(String d)
	{
                this.DATAFILE = d;
                
		int iVal = 0;
		int iPlantCount = 0, iGrazerCount = 0, iPredatorCount = 0, iObstacleCount = 0;
		double dVal = 0.0;
		
		LifeSimDataParser lsdp = LifeSimDataParser.getInstance();	// Get the singleton
		lsdp.initDataParser(DATAFILE);

		// Call all the simple get functions and test the results
		// World info functions
		dVal = lsdp.getWorldWidth();
		System.out.println("World width = " + dVal);
		if(dVal == 1000.0)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		dVal = lsdp.getWorldHeight();
		System.out.println("World height = " + dVal);
		if(dVal == 750.0)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");


		// Plant info functions
		iVal = lsdp.getInitialPlantCount();
		System.out.println("Plant count = " + iVal);
		if(iVal == 25)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");
		iPlantCount = iVal;

		dVal = lsdp.getPlantGrowthRate();
		System.out.println("Plant growth rate = " + dVal);
		if(dVal == 0.1)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		iVal = lsdp.getMaxPlantSize();
		System.out.println("Plant Max Size = " + iVal);
		if(iVal == 100)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		iVal = lsdp.getMaxSeedCastDistance();
		System.out.println("Plant seed cast diatance = " + iVal);
		if(iVal == 250)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		iVal = lsdp.getMaxSeedNumber();
		System.out.println("Plant max seed number = " + iVal);
		if(iVal == 10)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		dVal = lsdp.getSeedViability();
		System.out.println("Plant seed viability = " + dVal);
		if(dVal == .50)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		for(int i=0; i< iPlantCount; i++)
		{
			if(lsdp.getPlantData())
			{
				System.out.println("Plant " + i + " (" + lsdp.PlantX + ", " + lsdp.PlantY + ") diameter = " + lsdp.PlantDiameter);
			}
			else
			{
				System.out.println("Failed to read data for plant " + i);
			}
		}

		// Grazer info functions
		iVal = lsdp.getInitialGrazerCount();
		System.out.println("Grazer count = " + iVal);
		if(iVal == 15)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");
		iGrazerCount = iVal;

		iVal = lsdp.getGrazerEnergyInputRate();				// Energy input per minute when grazing
		System.out.println("Grazer energy input = " + iVal);
		if(iVal == 5)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		iVal = lsdp.getGrazerEnergyOutputRate();			// Energy output when moving each 5 DU
		System.out.println("Grazer energy output = " + iVal);
		if(iVal == 1)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		iVal = lsdp.getGrazerEnergyToReproduce();			// Energy level needed to reproduce
		System.out.println("Grazer energy to reproduce = " + iVal);
		if(iVal == 100)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		dVal = lsdp.getGrazerMaintainSpeedTime();		// Minutes of simulation to maintain max speed
		System.out.println("Grazer maintain speed time = " + dVal);
		if(dVal == 3.0)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		dVal = lsdp.getGrazerMaxSpeed();						// Max speed in DU per minute
		System.out.println("Grazer max speed = " + dVal);
		if(dVal == 20.0)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		for(int i=0; i< iGrazerCount; i++)
		{
			if(lsdp.getGrazerData())
			{
				System.out.println("Grazer " + i + " (" + lsdp.GrazerX + ", " + lsdp.GrazerY + ") energy level = " + lsdp.GrazerEnergy);
			}
			else
			{
				System.out.println("Failed to read data for grazer " + i);
			}
		}

		// Predator info functions
		iVal = lsdp.getInitialPredatorCount();
		System.out.println("Predator count = " + iVal);
		if(iVal == 5)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");
		iPredatorCount = iVal;

		dVal = lsdp.getPredatorMaxSpeedHOD();			// Get max speed for Homozygous Dominant FF
		System.out.println("Predator max speed HOD = " + dVal);
		if(dVal == 20.0)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		dVal = lsdp.getPredatorMaxSpeedHED();			// Get max speed for Heterozygous Dominant Ff
		System.out.println("Predator max speed HED = " + dVal);
		if(dVal == 18.0)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		dVal = lsdp.getPredatorMaxSpeedHOR();			// Get max speed for Homozygous Recessive ff
		System.out.println("Predator max speed HOR = " + dVal);
		if(dVal == 15.0)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		iVal = lsdp.getPredatorEnergyOutputRate();			// Energy output when moving each 5 DU
		System.out.println("Predator energy output = " + iVal);
		if(iVal == 10)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		iVal = lsdp.getPredatorEnergyToReproduce();			// Energy level needed to reproduce
		System.out.println("Predator energy to reproduce = " + iVal);
		if(iVal == 500)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		dVal = lsdp.getPredatorMaintainSpeedTime();		// Minutes of simulation to maintain max speed
		System.out.println("Predator maintain speed time = " + dVal);
		if(dVal == 4.0)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		iVal = lsdp.getPredatorMaxOffspring();				// Maximum number of offspring when reproducing
		System.out.println("Predator max offspring = " + iVal);
		if(iVal == 3)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		dVal = lsdp.getPredatorGestationPeriod();		// Gestation period in simulation days 
		System.out.println("Predator gestation period = " + dVal);
		if(dVal == 5.0)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		iVal = lsdp.getPredatorOffspringEnergyLevel();		// Energy level of offspring at birth
		System.out.println("Predator offspring energy = " + iVal);
		if(iVal == 100)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");

		for(int i=0; i< iPredatorCount; i++)
		{
			if(lsdp.getPredatorData())
			{
				System.out.println("Predator " + i + " (" + lsdp.PredatorX + ", " + lsdp.PredatorY + ") energy level = " + 
						lsdp.PredatorEnergy + ", genotype = " + lsdp.PredatorGenotype);
			}
			else
			{
				System.out.println("Failed to read data for predator " + i);
			}
		}

		// Obstacle info data
		iVal = lsdp.getObstacleCount();						// Number of obstacles
		System.out.println("Obstacle count = " + iVal);
		if(iVal == 15)
			System.out.println("\t Correct.\n");
		else
			System.out.println("\t Incorrect.\n");
		iObstacleCount = iVal;

		// Global variables for value return*
		/*
		
		public int ObstacleX;
		public int ObstacleY;
		public int ObstacleDiameter;
		public int ObstacleHeight;
*/
		for(int i=0; i< iObstacleCount; i++)
		{
			if(lsdp.getObstacleData())
			{
				System.out.println("Obstacle " + i + " (" + lsdp.ObstacleX + ", " + lsdp.ObstacleY + ") diameter = " + 
						lsdp.ObstacleDiameter + ", height = " + lsdp.ObstacleHeight);
			}
			else
			{
				System.out.println("Failed to read data for obstacle " + i);
			}
		}
	
	}
}
