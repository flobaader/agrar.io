package agrar.io.model;
import java.awt.*;

import agrar.io.controller.Controller;

public abstract class Circle {
	protected Point location;
	protected int size;
	protected Color color;
	protected Controller parent;
	protected boolean visible;
	
	public Circle(Controller parent, Point loc, int size, Color col){
		this.parent = parent;
		this.location = loc;
		this.size = size;
		this.color = col;
		this.visible = true;
	}
	
	public Point getLocation(){
		return location;
	}
	
	public int getSize(){
		return size;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void delete(){
		parent.deleteCircle(this);
		//this.visible = false;
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public abstract boolean isPlayer();
	
}
