package agrar.io.model;
import java.awt.*;

import agrar.io.controller.Controller;
import agrar.io.util.Vector;

/**
 * The class represents the model and attributes of a Circle
 * @author Flo
 *
 */
public abstract class Circle {
	protected Vector location;
	protected int size;
	protected Color color;
	protected Controller parent;
	protected float radius;
	
	public Circle(Controller parent, Vector loc, int size, Color col){
		this.parent = parent;
		this.location = loc;
		this.setSize(size);
		this.color = col;
	}
	
	/**
	 * Returns the Location of the Circle
	 * @return Location of center of the Circle
	 */
	public Vector getLocation(){
		return location;
	}
	
	/**
	 * Returns the absolute size, which is the circular size
	 * @return the circular size
	 */
	public int getSize(){
		return size;
	}
	
	/**
	 * Sets the new absolute size of the Circle
	 * @param newsize the new absolute size
	 */
	public void setSize(int newsize){
		this.size = newsize;
		this.radius = (float) Math.sqrt((float)newsize/(Math.PI*2F));
	}
	
	/**
	 * Returns the radius of the Circle
	 * @return The radius
	 */
	public float getRadius(){
		return radius;
	}
	
	/**
	 * Returns the Color of the Circle
	 * @return The Color of the Circle
	 */
	public Color getColor(){
		return color;
	}
	
	/**
	 * Delets the Circle
	 */
	public void delete(){
		parent.deleteCircle(this);
	}
	
	/**
	 * Sets the color of the circle to the given one
	 * @param c The new Color
	 */
	public void setColor(Color c){
		this.color = c;
	}
	
	/**
	 * Returns if the Circle represents a Player or not
	 * @return is Circle a Player?
	 */
	public abstract boolean isPlayer();
	
}
