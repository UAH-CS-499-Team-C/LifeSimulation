/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lifesimulation;


import java.util.ArrayList;
import lifesimulation.gui.GuiComponent;
import lifesimulation.objects.Grazer;
import lifesimulation.objects.LivingCreature;
import lifesimulation.objects.Plant;
import lifesimulation.objects.Predator;
import lifesimulation.objects.SimulationObject;
import lifesimulation.utilities.Environment;
import lifesimulation.utilities.SimReportGenerator;
import org.newdawn.slick.*;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.*;
import lifesimulation.utilities.LifeSimDataParser;
import org.newdawn.slick.geom.Shape;

/**
 *
 * @author d4g0n
 */
public class Simulation extends BasicGameState{
    
    /**
     * Palette for UI
     */
    
    static Color cBackground = new Color(0x121212);
    static Color cText = new Color(0xFFFFFF);
    
    /**
     * Background image for simulation
     */
    Image bg;
    Image pause;
    Image play;
    Image x1;
    Image x10;
    Image x100;
    Image printReport;
    
    Image fontImage;
    Font font;
    
    /**
     * All objects in the simulation
     */
    Environment environment;
    
    private ArrayList<GuiComponent> guiComps;
    private GuiComponent playButton;
    private GuiComponent x1Button;
    private GuiComponent x10Button;
    private GuiComponent x50Button;
    private GuiComponent x100Button;
    private GuiComponent printButton;
    
    
    private boolean paused = true;
    private int timeSpeed = 1;
    private boolean logicNeedUpdate = false;
    private final SimReportGenerator simReportGenerator;
    
    private SimulationObject selection = null;
    
    // Temporary Keyboard Inputs
    private InputProvider provider;
    
    public Simulation(int State, String s) {
        simReportGenerator = new SimReportGenerator(s);
       
    }
    
   
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
        
        guiComps = new ArrayList<>();
        
        bg = new Image("images/grid.png");
        pause = new Image("images/pauseBtn.png");
        play = new Image("images/playBtn.png");
        x1 = new Image("images/button_1x-speed.png");
        x10 = new Image("images/button_10x-speed.png");
        x100 = new Image("images/button_100x-speed.png");
        printReport = new Image("images/button_print-report.png");
        
        // Create GUI components
        playButton = new GuiComponent(10, 750, play);
        x1Button = new GuiComponent(50, 750, "images/x1.png");
        x10Button = new GuiComponent(90, 750, "images/x10.png");
        x50Button = new GuiComponent(130, 750, "images/x10.png");
        x100Button = new GuiComponent(170, 750, "images/x100.png");
        printButton = new GuiComponent(950, 750, "images/print.png");
        
        // Highlight the 1x button since that's the default time scale
        x1Button.getImage().setImageColor(0.7f, 0f, 0f);
        
        // Create event listeners for GUI components
        playButton.setListener((int button, int x, int y, int ClickCount) -> {
            if(paused) {
                paused = false;
                playButton.setImage(pause);
            } else {
                paused = true;
                playButton.setImage(play);
            }
        });
        
        x1Button.setListener((int button, int x, int y, int ClickCount) -> {
            timeSpeed = 1;
            logicNeedUpdate = true;
            
            // Update button highlights
            x1Button.getImage().setImageColor(0.7f, 0f, 0f);
            x10Button.getImage().setImageColor(1f, 1f, 1f);
            x100Button.getImage().setImageColor(1f, 1f, 1f);
            x50Button.getImage().setImageColor(1f, 1f, 1f);
        });
        
        x10Button.setListener((int button, int x, int y, int ClickCount) -> {
            timeSpeed = 10;
            logicNeedUpdate = true;
            
            // Update button highlights
            x10Button.getImage().setImageColor(0.7f, 0f, 0f);
            x1Button.getImage().setImageColor(1f, 1f, 1f);
            x100Button.getImage().setImageColor(1f, 1f, 1f);
            x50Button.getImage().setImageColor(1f, 1f, 1f);
        });
        
        x50Button.setListener((int button, int x, int y, int ClickCount) -> {
            timeSpeed = 50;
            logicNeedUpdate = true;
            
            // Update button highlights
            x50Button.getImage().setImageColor(0.7f, 0f, 0f);
            x10Button.getImage().setImageColor(1f, 1f, 1f);
            x1Button.getImage().setImageColor(1f, 1f, 1f);
            x100Button.getImage().setImageColor(1f, 1f, 1f);
        });
        
        x100Button.setListener((int button, int x, int y, int ClickCount) -> {
            timeSpeed = 100;
            logicNeedUpdate = true;
            
            // Update button highlights
            x100Button.getImage().setImageColor(0.7f, 0f, 0f);
            x1Button.getImage().setImageColor(1f, 1f, 1f);
            x10Button.getImage().setImageColor(1f, 1f, 1f);
            x50Button.getImage().setImageColor(1f, 1f, 1f);
        });
        
        printButton.setListener((int button, int x, int y, int ClickCount) -> {
            simReportGenerator.Generate(environment);
        });
        
        // Add GUI components to our list
        guiComps.add(playButton);
        guiComps.add(x1Button);
        guiComps.add(x10Button);
        guiComps.add(x50Button);
        guiComps.add(x100Button);
        guiComps.add(printButton);
        
        // Set up our font
        fontImage = new Image("images/roboto_0.tga");
        font = new AngelCodeFont("images/roboto.fnt", fontImage);
        
        environment = new Environment();
        
        
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
        
        // Draw background
        g.drawImage(bg, 0, 0);
        // Draw each obstacle
        environment.getObstacles().forEach(x -> x.draw(g));
        environment.getPlants().forEach(x -> x.draw(g));
        environment.getGrazers().forEach(x -> x.draw(g));
        environment.getPredators().forEach(x -> x.draw(g));
        
        
        // ============================ UI Code ==================================
        // Draw background rect
        g.setColor(cBackground);
        g.fillRect(1000, 0, 250, 750);
        g.fillRect(0, 750, 1250, 50);
        
        // Draw frame rect
        g.setLineWidth(12);
        g.drawRoundRect(0, 0, 1000, 750, 4);
        
        // Draw all GUI components
        guiComps.forEach(x -> x.draw(g));
        
        g.setFont(font);
        
        // Draw time
        g.setColor(cText);
        g.drawString("Seconds passed: " + environment.getTime(), 1000, 75);
        
        // Draw number of each object
        g.setColor(Color.white);
        g.drawString("Number of Plants: "  + environment.getNumPlants(), 1000, 125);
        g.drawString("Number of Grazers: " + environment.getNumGrazers(), 1000, 150);
        g.drawString("Number of Predators: " + environment.getNumPredators(), 1000, 175);
        
        // Draw selection info
        
        if(selection != null)
        {
            Shape baseShape = selection.getCollision();
            float rad = baseShape.getBoundingCircleRadius();
            
            g.setLineWidth(2);
            g.setColor(Color.black);
            g.drawRect(baseShape.getMinX(), baseShape.getMinY(), baseShape.getWidth(), baseShape.getHeight());
            
            Class c = selection.getClass();
            
            g.setColor(Color.white);
            g.drawString("Selected: " + c.getSimpleName(), 1000, 250);
            g.drawString("Position: (" + selection.getX() + ", " + selection.getY() + ")", 1000, 275);
            
            // Class-specific information printed here
            if(c == Plant.class)
            {
                Plant obj = (Plant)selection;
                g.drawString("Diameter: " + obj.getDiameter(), 1000, 300);
            }
            else if(c == Grazer.class)
            {
                Grazer obj = (Grazer)selection;
                g.drawString("Energy: " + obj.getEnergy(), 1000, 300);
            }
            else if(c == Predator.class)
            {
                Predator obj = (Predator)selection;
                g.drawString("Energy: " + obj.getEnergy(), 1000, 300);
                g.drawString("Genotype: " + obj.getGenotype(), 1000, 325);
            }
        }
        
        // Draw key
        g.setColor(Color.white);
        g.drawString("--- Key ---", 1000, 500);
        g.drawString("Grazer:", 1000, 525);
        g.setColor(Color.blue);
        g.fill(new Circle(1100, 535, 7, 7));
        g.setColor(Color.white);
        g.drawString("Predator:", 1000, 550);
        g.setColor(Color.red);
        g.fill(new Circle(1100, 560, 7, 7));
    }
    
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if(!paused) {
            environment.update();
            
            // If speed needs updated
            if(logicNeedUpdate) {
                gc.setMinimumLogicUpdateInterval(1000/timeSpeed);
                logicNeedUpdate = false;
            }
            
            environment.getGrazers().forEach(x -> x.Update(environment));
        }
        
        
    }
    
    @Override
    public int getID() {
        return 0;
    }

   

    // Control the simulation via GUI buttons
    
    @Override
    public void mouseClicked(int button, int x, int y, int ClickCount){
        java.util.function.Consumer<SimulationObject> selector;
        
        // Check all our GUI components to see if they've been clicked
        guiComps.forEach(comp -> {
            if(comp.pointCollide(x, y)) comp.click(button, x, y, ClickCount);
        });
        
        // Function to check if we're clicking a creature
        selector = creature -> {
            if(creature.getCollision().contains((float)x, (float)y)) selection = creature;
        };
        
        // Clear out selection so we can deselect by clicking empty space,
        // but save it in case we've clicked somewhere that's outside the simulation region.
        SimulationObject s = selection;
        selection = null;
        
        environment.getPlants().forEach(selector);
        environment.getGrazers().forEach(selector);
        environment.getPredators().forEach(selector);
        
        // Restore selection if we clicked over GUI regions
        if(selection == null && (x >= 1000 || y >= 750)) selection = s; 
    }
}
