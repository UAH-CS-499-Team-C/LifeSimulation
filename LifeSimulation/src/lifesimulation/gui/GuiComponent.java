package lifesimulation.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Jordan Gilbreath
 */
public class GuiComponent {

    protected boolean visible = true;
    
    protected float x;
    protected float y;

    protected Image image;
    
    protected GuiEventListener listener;

    /**
     * Create a new GUIComponent using an existing Image
     * @param x X position
     * @param y Y position
     * @param image Image object to use for drawing
     */
    public GuiComponent(float x, float y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }
    
    /**
     * Create a new GUIComponent using the image from the given path
     * @param x X position
     * @param y Y position
     * @param imagePath Path to the image file to use
     * @throws SlickException See Image
     */
    public GuiComponent(float x, float y, java.lang.String imagePath) throws SlickException {
        this.x = x;
        this.y = y;
        this.image = new Image(imagePath);
    }
    
    /**
     * Draw the GUI component at its position
     * @param g Graphics object to draw to
     */
    public void draw(Graphics g) {
        if(!visible) return;
        
        g.drawImage(image, x, y);
    }
    
    /**
     * Set the GUI component's visibility
     * @param val 
     */
    public void setVisible(boolean val) {
        visible = val;
    }
    
    /**
     * Reposition the GUI component
     * @param x New X position
     * @param y New Y position
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Change the GUI component's image
     * @param i New Image object
     */
    public void setImage(Image i) {
        this.image = i;
    }
    
    /**
     * 
     * @return This component's Image object
     */
    public Image getImage() {
        return image;
    }
    
    /**
     * 
     * @return GUI component's X position
     */
    public float getX() {
        return x;
    }
    
    /**
     * 
     * @return GUI component's Y position
     */
    public float getY() {
        return y;
    }
    
    /**
     * 
     * @return GUI component's width
     */
    public float getWidth() {
        return image.getWidth();
    }
    
    /**
     * 
     * @return GUI component's height
     */
    public float getHeight() {
        return image.getHeight();
    }
    
    /**
     * Set up an event listener on this component
     * @param l GuiEventListener to use
     */
    public void setListener(GuiEventListener l) {
        listener = l;
    }
    
    /**
     * Test if a given point is within the GUI component
     * @param px
     * @param py
     * @return 
     */
    public boolean pointCollide(float px, float py) {
        // Test if the given point is within our X bounds
        if(px < x || px > (x + getWidth())) return false;
        // Test if the given point is within our Y bounds
        if(py < y || py > (y + getHeight())) return false;
        // If the other two tests passed, a collision has occurred!
        return true;
    }
    
    /**
     * Handle a mouse click on this component
     * See BasicGameState.mouseClicked
     * @param button
     * @param x
     * @param y
     * @param ClickCount 
     */
    public void click(int button, int x, int y, int ClickCount) {
        if(listener != null) listener.onClick(button, x, y, ClickCount);
    }
}
