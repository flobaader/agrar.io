package agrar.io.model;
import java.awt.*;

import agrar.io.controller.Controller;
import agrar.io.util.Vector;

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
	
	public Vector getLocation(){
		return location;
	}
	
	public int getSize(){
		return size;
	}
	
	public void setSize(int newsize){
		this.size = newsize;
		this.radius = (float) Math.sqrt((float)newsize/(Math.PI*2F));
	}
	
	public float getRadius(){
		return radius;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void delete(){
		parent.deleteCircle(this);
	}
	
		
	public abstract boolean isPlayer();
	
}
