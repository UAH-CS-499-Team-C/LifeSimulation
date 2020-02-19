//===================================================================================
// Life Simulation Data Parser Java
// File: LifeSimDataParser.java
//
// Author: Dr. Rick Coleman
// Date: January 2017
//===================================================================================
package pkgLifeSimDataParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class LifeSimDataParser
{
	private static LifeSimDataParser theInstance = new LifeSimDataParser();
	
	private String 			m_sFileName;
	private FileReader		inFile;
	private	BufferedReader	bufReader = null;

	private String m_sLine;	// Global string for returning m_sLines from getNextLine 
	// Map data
	private double m_dWorldWidth;
	private double m_dWorldHeight;
	// Plant data
	private int m_iNumPlants;
	private int m_iPlantCount;
	private double m_dPlantGrowthRate;
	private int m_iMaxPlantSize;
	private int m_iMaxSeedCast;
	private int m_iMaxSeedNumber;
	private double m_dSeedViability;
	// Grazer data
	private int m_iNumGrazers;
	private int m_iGrazerCount;
	private int m_iGEInputRate;
	private int m_iGEOutputRate;
	private int m_iGEReproduce;
	private double m_dGMaintainSpeed;
	private double m_dGMaxSpeed;
	// Predator data
	private int m_iNumPredators;
	private int m_iPredatorCount;
	private double m_dMaxSpeedHOD;
	private double m_dMaxSpeedHED;
	private double m_dMaxSpeedHOR;
	private int m_iPEOutputRate;
	private int m_iPEReproduce;
	private double m_dPMaintainSpeed;
	private int m_iPMaxOffspring;
	private double m_dPGestation;
	private int m_iPOffspringEnergy;

	// Obstacle data
	private int m_iNumObstacles;
	private int m_iObstacleCount;

	//--------------------------------------------------------------
	// Global variables for value return*
	public int PlantX;
	public int PlantY;
	public int PlantDiameter;
	
	public int GrazerX;
	public int GrazerY;
	public int GrazerEnergy;
	
	public int PredatorX;
	public int PredatorY;
	public int PredatorEnergy;
	public String PredatorGenotype;
	
	public int ObstacleX;
	public int ObstacleY;
	public int ObstacleDiameter;
	public int ObstacleHeight;
	
	// Note: Java does not have anyway of handling Pass-By-Reference
	//		functions.  Thus you cannot do something like this in C++
	//		bool someFunction(int& iVal, double& dVal)
	//		Where the command iVal = 2 and dVal = 2.5 will set values
	//		in variables back in the calling function.  All Java
	//		function parameters are Pass-By-Value. Therefore the
	//		above public variables are provided for fetching those
	//		values for Plant, Grazer, Predator, and Obstacle objects.
	//--------------------------------------------------------------

	//------------------------------------------
	// Default constructor
	//------------------------------------------
	private LifeSimDataParser()
	{
		// Map data
		m_dWorldWidth = 0.0;
		m_dWorldHeight = 0.0;
		// Plant data
		m_iNumPlants = 0;
		m_iPlantCount = 0;
		m_dPlantGrowthRate = 0.0;
		m_iMaxPlantSize = 0;
		m_iMaxSeedCast = 0;
		m_iMaxSeedNumber = 0;
		m_dSeedViability = 0.0;
		// Grazer data
		m_iNumGrazers = 0;
		m_iGrazerCount = 0;
		m_iGEInputRate = 0;
		m_iGEOutputRate = 0;
		m_iGEReproduce = 0;
		m_dGMaintainSpeed = 0.0;
		m_dGMaxSpeed = 0.0;
		// Predator data
		m_iNumPredators = 0;
		m_iPredatorCount = 0;
		m_dMaxSpeedHOD = 0.0;
		m_dMaxSpeedHED = 0.0;
		m_dMaxSpeedHOR = 0.0;
		m_iPEOutputRate = 0;
		m_iPEReproduce = 0;
		m_dPMaintainSpeed = 0.0;
		m_iPMaxOffspring = 0;
		m_dPGestation = 0.0;
		m_iPOffspringEnergy = 0;
		// Obstacle data
		m_iNumObstacles = 0;
		m_iObstacleCount = 0;
		
		m_sLine = new String();
	}

	//------------------------------------------
	// Get the singleton instance of this class
	//------------------------------------------
	public static LifeSimDataParser getInstance()
	{
		return theInstance;
	}

	//------------------------------------------
	// Initiaize the data parser with data file name
	//------------------------------------------
	public void initDataParser(String filename)
	{
		m_sFileName = filename;		// Save the file name
		
		try
		{
			inFile = new FileReader(m_sFileName);
		}
		catch(FileNotFoundException e1) // If we failed to opened it
		{
			System.out.println("***** FAILED TO OPEN THE DATA FILE *****");
			System.out.println("You will not be able to read simulation data.\n");
			System.out.println("Data file must be in the directory" + System.getProperty("user.dir"));
			return;
		}

		try
		{
			bufReader = new BufferedReader(inFile);

			// Get the basic information
			while(getNextLine())
			{
				if(m_sLine.equals("<WIDTH>"))
				{
					if(getNextLine()) // Get map width
					{
						m_dWorldWidth = Double.parseDouble(m_sLine);
					}
					else
					{
						throw new Exception();
					}
				}
				else if(m_sLine.equals("<HEIGHT>"))
				{
					if(getNextLine())  // Get map height
						m_dWorldHeight = Double.parseDouble(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<INITIAL_PLANT_COUNT>"))
				{
					if(getNextLine())
						m_iNumPlants = Integer.parseInt(m_sLine); // Get plant count
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<GROWTH_RATE>"))
				{
					if(getNextLine()) // Get plant growth rate
						m_dPlantGrowthRate = Double.parseDouble(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<MAX_SIZE>"))
				{
					if(getNextLine()) // Get plant max size
						m_iMaxPlantSize = Integer.parseInt(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<MAX_SEED_NUMBER>"))
				{
					if(getNextLine()) // Get max number of seeds a plant can produce
						m_iMaxSeedNumber = Integer.parseInt(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<SEED_VIABILITY>"))
				{
					if(getNextLine()) // Get seed viability percentage
						m_dSeedViability = Double.parseDouble(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<MAX_SEED_CAST_DISTANCE>"))
				{
					if(getNextLine()) // Get seed cast distance
						m_iMaxSeedCast = Integer.parseInt(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<INITIAL_GRAZER_COUNT>"))
				{
					if(getNextLine()) // Get number of grazers
						m_iNumGrazers = Integer.parseInt(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<G_ENERGY_INPUT>"))
				{
					if(getNextLine()) // Get grazer energy obtained when eating
						m_iGEInputRate = Integer.parseInt(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<G_ENERGY_OUTPUT>"))
				{
					if(getNextLine()) // Get grazer energy output when moving
						m_iGEOutputRate = Integer.parseInt(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<G_ENERGY_TO_REPRODUCE>"))
				{
					if(getNextLine()) // Get grazer energy to reproduce
						m_iGEReproduce = Integer.parseInt(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<G_MAINTAIN_SPEED>"))
				{
					if(getNextLine()) // Get grazer time to maintain speed
						m_dGMaintainSpeed = Double.parseDouble(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<G_MAX_SPEED>"))
				{
					if(getNextLine()) // Get grazer max speed
						m_dGMaxSpeed = Double.parseDouble(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<INITIAL_PREDATOR_COUNT>"))
				{
					if(getNextLine()) // Get initial predator count
						m_iNumPredators = Integer.parseInt(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<MAX_SPEED_HOD>"))
				{
					if(getNextLine()) // Get predator max speed when homozygous dominant FF
						m_dMaxSpeedHOD = Double.parseDouble(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<MAX_SPEED_HED>"))
				{
					if(getNextLine()) // Get predator max speed when heterozygous dominant Ff
						m_dMaxSpeedHED = Double.parseDouble(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<MAX_SPEED_HOR>"))
				{
					if(getNextLine()) // Get predator max speed when homozygous recessive ff
						m_dMaxSpeedHOR = Double.parseDouble(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<P_ENERGY_OUTPUT>"))
				{
					if(getNextLine()) // Get predator energy output when moving
						m_iPEOutputRate = Integer.parseInt(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<P_MAINTAIN_SPEED>"))
				{
					if(getNextLine()) // Get predator time to maintain speed
						m_dPMaintainSpeed = Double.parseDouble(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<P_ENERGY_TO_REPRODUCE>"))
				{
					if(getNextLine()) // Get predator energy to reproduce
						m_iPEReproduce = Integer.parseInt(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<P_MAX_OFFSPRING>"))
				{
					if(getNextLine()) // Get predator max offspring count
						m_iPMaxOffspring = Integer.parseInt(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<P_GESTATION>"))
				{
					if(getNextLine()) // Get predator gestation time
						m_dPGestation = Double.parseDouble(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<P_OFFSPRING_ENERGY>"))
				{
					if(getNextLine()) // Get predator offspring level at birth
						m_iPOffspringEnergy = Integer.parseInt(m_sLine);
					else
						throw new Exception();
				}
				else if(m_sLine.equals("<INITIAL_OBSTACLE_COUNT>"))
				{
					if(getNextLine()) // Get initial obstacle count
						m_iNumObstacles = Integer.parseInt(m_sLine);
					else
						throw new Exception();
				}
			}  // end while getNextLine
			bufReader.close();
			
		} // end Try
		catch(Exception ex)
		{
			System.out.println("***** FAILED TRYING TO READ THE DATA FILE *****");
			System.out.println("You will not be able to use the simulation data.\n");
			System.out.println("Data file must be in the directory" + System.getProperty("user.dir"));
			return;
			
		}
	}

	//------------------------------------------
	// Get the map width in Distance Units (DU)
	//------------------------------------------
	public double getWorldWidth()
	{
		return m_dWorldWidth;
	}

	//------------------------------------------
	// Get the map height in Distance Units (DU)
	//------------------------------------------
	public double getWorldHeight()
	{
		return m_dWorldHeight;
	}

	//------------------------------------------
	// Get the initial number of plants
	//------------------------------------------
	public int getInitialPlantCount()
	{
		return m_iNumPlants;
	}

	//----------------------------------------------
	// Get plant growth rate in Distance Units (DU)
	//----------------------------------------------
	double getPlantGrowthRate()
	{
		return m_dPlantGrowthRate;
	}

	//------------------------------------------
	// Get max seed casting distance when a plant
	// produces seeds, in Distance Units (DU)
	//------------------------------------------
	public int getMaxSeedCastDistance()
	{
		return m_iMaxSeedCast;
	}

	//------------------------------------------
	// Get max plant size in Distance Units (DU)
	//------------------------------------------
	public int getMaxPlantSize()
	{
		return m_iMaxPlantSize;
	}

	//----------------------------------------------
	// Get max number of seeds a plant can produce.
	//----------------------------------------------
	public int getMaxSeedNumber()
	{
		return m_iMaxSeedNumber;
	}

	//------------------------------------------
	// Get seed viability as percent of seeds
	//	a plant produces that will germinate
	//	and grow.
	//------------------------------------------
	public double getSeedViability()
	{
		return m_dSeedViability;
	}

	//----------------------------------------------------------------------------
	// Get all the data on a single plant
	// Data is accessible via the global variables
	//		PlantX - int variable to hold X coordinate mid-point
	//		PlantY - int variable to hold Y coordinate mid-point
	//		PlantDiameter - int variable to hold plant diameter at start of simulation
	//				All measures are in Distance Units (DU)
	// Returns: true if a new plant was read
	//			false when all plants have been read
	// X,Y positions of Plants are given as the center of the plant
	//----------------------------------------------------------------------------
	public boolean getPlantData()
	{
		int dNum = 0;
		
		// Reopen the file
		try
		{
			inFile = new FileReader(m_sFileName);
		}
		catch(FileNotFoundException e1) // If we failed to opened it
		{
			System.out.println("***** FAILED TO OPEN THE DATA FILE *****");
			System.out.println("You will not be able to read Plant data.\n");
			System.out.println("Data file must be in the directory" + System.getProperty("user.dir"));
			return false;
		}

		try
		{
			bufReader = new BufferedReader(inFile);

			// Read to the the current Plant count
			while(getNextLine())
			{
				if(m_sLine.length() == 0) throw new Exception();

				if(m_sLine.equals("<PLANT>")) // Got one
				{
					if(dNum == m_iPlantCount)
					{
						
						// Get data on this one
						while(getNextLine())
						{
							// Get the X position
							if(m_sLine.equals("<X_POS>"))
							{
								if(getNextLine())
								{
									PlantX = Integer.parseInt(m_sLine); // Set the X position
								}
								else
									throw new Exception();
							}
							else if(m_sLine.equals("<Y_POS>"))
							{
								if(getNextLine())
									PlantY = Integer.parseInt(m_sLine); // Set the Y position
								else
									throw new Exception();
							}
							else if(m_sLine.equals("<P_DIAMETER>"))
							{
								if(getNextLine())
									PlantDiameter = Integer.parseInt(m_sLine); // Set the radius
								else
									throw new Exception();
							}
							else if(m_sLine.equals("</PLANT>"))
							{
								m_iPlantCount++; // Increment for next call to this function
								bufReader.close();
								return true; // Got it
							}
						} // end while
					} // end if(dNum == m_iPlantCount)
					else
					{
						dNum++; // Check the next one
					}
				}
			}
			bufReader.close();
		} // end Try
		catch(Exception ex)
		{
			System.out.println("***** FAILED TRYING TO READ THE DATA FILE *****");
			System.out.println("You will not be able to use the simulation data.\n");
			return false;
			
		}
		return false; // If we get here we have got all the Plants
	}

	//------------------------------------------
	// Get initial number of grazers
	//------------------------------------------
	public int getInitialGrazerCount()
	{
		return m_iNumGrazers;
	}

	//--------------------------------------------
	// Get the energy input per simulation minute 
//			when a grazer is grazing
	//--------------------------------------------
	public int getGrazerEnergyInputRate()
	{
		return m_iGEInputRate;
	}

	//---------------------------------------------
	// Get the energy output when a grazer is
//			moving.  This is amount of energy expended
//			for each 5 Distance Units (DU) moved.
	//---------------------------------------------
	public int getGrazerEnergyOutputRate()
	{
		return m_iGEOutputRate;
	}

	//---------------------------------------------------
	// Get the energy level a grazer needs to reproduce
	//---------------------------------------------------
	public int getGrazerEnergyToReproduce()
	{
		return m_iGEReproduce;
	}

	//----------------------------------------------
	// Get the minutes of simulation time a grazer
//			can maintain its' max speed when fleeing
//			a predator
	//----------------------------------------------
	public double getGrazerMaintainSpeedTime()
	{
		return m_dGMaintainSpeed;
	}

	//------------------------------------------
	// Get the max speed in Distance Units (DU)
//			per minute a grazer can run.
	//------------------------------------------
	public double getGrazerMaxSpeed()
	{
		return m_dGMaxSpeed;
	}

	//-------------------------------------------------------------
	// Get all the data on a single grazer
	// Data is accessible via the global variables
	//		GrazerX - int variable to hold X coordinate
	//		GrazerY - int variable to hold Y coordinate
	//		GrazerEnergy - int variable to hold grazer
	//							energy at start of simulation
	// Returns: true if a new Grazer was read
	//			false when all Grazers have been read
	// X,Y positions of Grazers are given as the upper left corner
	//		of its' bounding rectangle.
	//-------------------------------------------------------------
	public boolean getGrazerData()
	{
		int dNum = 0;
		// Reopen the file
		try
		{
			inFile = new FileReader(m_sFileName);
		}
		catch(FileNotFoundException e1) // If we failed to opened it
		{
			System.out.println("***** FAILED TO OPEN THE DATA FILE *****");
			System.out.println("You will not be able to read simulation data.\n");
			System.out.println("Data file must be in the directory" + System.getProperty("user.dir"));
			return false;
		}

		try
		{
			bufReader = new BufferedReader(inFile);

			// Read to the the current Grazer count
			while(getNextLine())
			{
				if(m_sLine.length() == 0) throw new Exception();
				
				if(m_sLine.equals("<GRAZER>")) // Got one
				{
					if(dNum == m_iGrazerCount)
					{
						// Get data on this one
						while(getNextLine())
						{
							if(m_sLine.length() == 0) throw new Exception();
							// Get the X position
							if(m_sLine.equals("<X_POS>"))
							{
								if(getNextLine())
									GrazerX = Integer.parseInt(m_sLine); // Set the X position
								else
									throw new Exception();
							}
							else if(m_sLine.equals("<Y_POS>"))
							{
								if(getNextLine())
									GrazerY = Integer.parseInt(m_sLine); // Set the Y position
								else
									throw new Exception();
							}
							else if(m_sLine.equals("<G_ENERGY_LEVEL>"))
							{
								if(getNextLine())
									GrazerEnergy = Integer.parseInt(m_sLine); // Set the energy level
								else
									throw new Exception();
							}
							else if(m_sLine.equals("</GRAZER>"))
							{
								m_iGrazerCount++; // Increment for next call to this function
								return true; // Got it
							}
						} // end while
					} // end if(dNum == m_iGrazerCount)
					else
					{
						dNum++; // Check the next one
					}
				}
			}
			bufReader.close();
		} // end Try
		catch(Exception ex)
		{
			System.out.println("***** FAILED TRYING TO READ THE DATA FILE *****");
			System.out.println("You will not be able to use the simulation data.\n");
			return false;
			
		}
		return false; // If we get here we have got all the grazers
	}

	//------------------------------------------
	// Get initial number of predators
	//------------------------------------------
	public int getInitialPredatorCount()
	{
		return m_iNumPredators;
	}

	//-----------------------------------------------
	// Get max speed of a predator with homozygous
//			dominant genotype for speed of FF
	//-----------------------------------------------
	public double getPredatorMaxSpeedHOD()
	{
		return m_dMaxSpeedHOD;
	}

	//-----------------------------------------------
	// Get max speed of a predator with heterozygous
//			dominent genotype for speed of Ff
	//-----------------------------------------------
	public double getPredatorMaxSpeedHED()
	{
		return m_dMaxSpeedHED;
	}

	//-----------------------------------------------
	// Get max speed of a predator with homozygous
//			recessive genotype for speed of ff
	//-----------------------------------------------
	public double getPredatorMaxSpeedHOR()
	{
		return m_dMaxSpeedHOR;
	}

	//---------------------------------------------
	// Get the energy output when a predator is
//			moving.  This is amount of energy expended
//			for each 5 Distance Units (DU) moved.
	//---------------------------------------------
	public int getPredatorEnergyOutputRate()
	{
		return m_iPEOutputRate;
	}

	//-----------------------------------------------------
	// Get the energy level a predator needs to reproduce
	//-----------------------------------------------------
	public int getPredatorEnergyToReproduce()
	{
		return m_iPEReproduce;
	}

	//----------------------------------------------
	// Get the minutes of simulation time a predator
//			can maintain its' max speed when pursuing
//			a grazer
	//----------------------------------------------
	public double getPredatorMaintainSpeedTime()
	{
		return m_dPMaintainSpeed;
	}

	//---------------------------------------------------
	// Get maximum number of offspring a predator will
//			produce.  Actual count will be a randome number
	//  between 1 and this number
	//---------------------------------------------------
	public int getPredatorMaxOffspring()
	{
		return m_iPMaxOffspring;
	}

	//-------------------------------------------------
	// Get number of simulation days in a predator's
	//  gestation period (time after mating before
//			the offspring are born.
	//-------------------------------------------------
	public double getPredatorGestationPeriod()
	{
		return m_dPGestation;
	}

	//------------------------------------------
	// Get the energy level of offspring at birth
	//------------------------------------------
	public int getPredatorOffspringEnergyLevel()
	{
		return m_iPOffspringEnergy;
	}

	//---------------------------------------------------------------
	// Get all the data on a single predator
	// Data is accessible via the global variables
	//				PredatorX - int variable to hold X coordinate
	//				PredatorY - int variable to hold Y coordinate
	//				PredatorEnergy - int variable to hold grazer
	//							energy at start of simulation
	//				PredatorGenotype - String to hold the predator's   
	//					genotype. This will be in the form
	//					"AA SS FF" giving the gene pairs for aggression,
	//					strength, and speed.  A, S, and F are dominant
	//					genes while a, s, and f are recessive.
	// Returns: true if a new Predator was read
	//			false when all Predators have been read
	// X,Y positions of Predators are given as the upper left corner
	//		of its' bounding rectangle.
	//---------------------------------------------------------------
	public boolean getPredatorData()
	{
		int dNum = 0;
		// Reopen the file
		try
		{
			inFile = new FileReader(m_sFileName);
		}
		catch(FileNotFoundException e1) // If we failed to opened it
		{
			System.out.println("***** FAILED TO OPEN THE DATA FILE *****");
			System.out.println("You will not be able to read simulation data.\n");
			System.out.println("Data file must be in the directory" + System.getProperty("user.dir"));
			return false;
		}

		try
		{
			bufReader = new BufferedReader(inFile);

			// Read to the the current Grazer count
			while(getNextLine())
			{
				if(m_sLine.length() == 0) throw new Exception();
				
				if(m_sLine.equals("<PREDATOR>")) // Got one
				{
					if(dNum == m_iPredatorCount)
					{					
						// Get data on this one
						while(getNextLine())
						{
							if(m_sLine.length() == 0) throw new Exception();
							
							// Get the X position
							if(m_sLine.equals("<X_POS>"))
							{
								if(getNextLine())
									PredatorX = Integer.parseInt(m_sLine); // Set the X position
								else
									throw new Exception();
							}
							else if(m_sLine.equals("<Y_POS>"))
							{
								if(getNextLine())
									PredatorY = Integer.parseInt(m_sLine); // Set the Y position
								else
									throw new Exception();
							}
							else if(m_sLine.equals("<P_ENERGY_LEVEL>"))
							{
								if(getNextLine())
									PredatorEnergy = Integer.parseInt(m_sLine); // Set the energy level
								else
									throw new Exception();
							}
							else if(m_sLine.equals("<GENOTYPE>"))
							{
								if(getNextLine())
									PredatorGenotype = m_sLine; // Set the genotype
								else
									throw new Exception();
							}
							else if(m_sLine.equals("</PREDATOR>"))
							{
								m_iPredatorCount++; // Increment for next call to this function
								return true; // Got it
							}
						} // end while
					} // end if(dNum == m_iPredatorCount)
					else
					{
						dNum++; // Check the next one
					}
				}
			}
			bufReader.close();
		} // end Try
		catch(Exception ex)
		{
			System.out.println("***** FAILED TRYING TO READ THE DATA FILE *****");
			System.out.println("You will not be able to use the simulation data.\n");
			return false;
			
		}
		return false; // If we get here we have got all the predators
	}

	//------------------------------------------
	// Get the number of obstacles
	//------------------------------------------
	public int getObstacleCount()
	{
		return m_iNumObstacles;
	}

					
	//------------------------------------------------------------------------
	// Get all the data on a single obstacle
	// Data is accessible via the global variables
	//				ObstacleX - int variable to hold X coordinate mid-point
	//				ObstacleY - int variable to hold Y coordinate mid-point
	//				ObstacleDiameter - int variable to hold obstacle diameter
	//				ObstacleHeight - int variable to hold obstacle height
	//				All measures are in Distance Units (DU)
	// Returns: true if a new Obstacle was read
	//			false when all Obstacles have been read
	// X,Y positions of Obstacles are given as the center of the Obstacle
	//------------------------------------------------------------------------
	public boolean getObstacleData()
	{
		int dNum = 0;
		// Reopen the file
		try
		{
			inFile = new FileReader(m_sFileName);
		}
		catch(FileNotFoundException e1) // If we failed to opened it
		{
			System.out.println("***** FAILED TO OPEN THE DATA FILE *****");
			System.out.println("You will not be able to read simulation data.\n");
			System.out.println("Data file must be in the directory" + System.getProperty("user.dir"));
			return false;
		}

		try
		{
			bufReader = new BufferedReader(inFile);

			// Read to the the current Grazer count
			while(getNextLine())
			{
				if(m_sLine.length() == 0) throw new Exception();
				
				if(m_sLine.equals("<OBSTACLE>")) // Got one
				{
					if(dNum == m_iObstacleCount)
					{
						// Get data on this one
						while(getNextLine())
						{
							if(m_sLine.length() == 0) throw new Exception();

							// Get the X position
							if(m_sLine.equals("<X_POS>"))
							{
								if(getNextLine())
									ObstacleX = Integer.parseInt(m_sLine); // Set the X position
								else
									throw new Exception();
							}
							else if(m_sLine.equals("<Y_POS>"))
							{
								if(getNextLine())
									ObstacleY = Integer.parseInt(m_sLine); // Set the Y position
								else
									throw new Exception();
							}
							else if(m_sLine.equals("<O_DIAMETER>"))
							{
								if(getNextLine())
									ObstacleDiameter = Integer.parseInt(m_sLine); // Set the radius of this obstacle
								else
									throw new Exception();
							}
							else if(m_sLine.equals("<O_HEIGHT>"))
							{
								if(getNextLine())
									ObstacleHeight = Integer.parseInt(m_sLine); // Set the radius of this obstacle
								else
									throw new Exception();
							}
							else if(m_sLine.equals("</OBSTACLE>"))
							{
								m_iObstacleCount++; // Increment for next call to this function
								return true; // Got it
							}
						} // end while
					} // end if(dNum == m_iObstacleCount)
					else
					{
						dNum++; // Check the next one
					}
				}
			}
			bufReader.close();
		} // end Try
		catch(Exception ex)
		{
			System.out.println("***** FAILED TRYING TO READ THE DATA FILE *****");
			System.out.println("You will not be able to use the simulation data.\n");
			return false;
			
		}
		return false; // If we get here we have got all the obstacles
	}

	//------------------------------------------------
	// Function: getNextLine()
	// Purpose: Reads m_sLines from a file and places
	//   them in buffer, removing any leading white
	//   space.  Skips blank m_sLines. Ignors comment
	//   m_sLines starting with <!-- and ending with -->
	//   
	// Returns: True if a m_sLine was successfully read,
	//		    false if the end of file was encountered.
	// Notes: Function provided by instructor.
	//------------------------------------------------
	private boolean getNextLine()
	{
	    boolean    done = false;
		String    tempBuf = new String();
		try
		{
		    while(!done)
		    {
		    	tempBuf = bufReader.readLine();
	            if(tempBuf.length() == 0)     // Skip any blank m_sLines
	               continue;
			    else if(tempBuf.contains("<!--")) // Skip comment m_sLines
				   continue;
	            else done = true;    // Got a valid data m_sLine so return with this m_sLine
		    } // end while
			// Remove white space from beginning and end of string
		    m_sLine = tempBuf.trim();
		} // end try
		catch(Exception ex)
		{
			m_sLine =  "";
			return false;
		}
	    return true;  // Flag a successful read
	}
}
