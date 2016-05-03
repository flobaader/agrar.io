package agrar.io.model;
import java.awt.*;

import agrar.io.vector;
import agrar.io.controller.Controller;

public abstract class Circle {
	protected vector location;
	protected int size;
	protected Color color;
	protected Controller parent;
	protected boolean visible;
	protected float radius;
	
	public Circle(Controller parent, vector loc, int size, Color col){
		this.parent = parent;
		this.location = loc;
		this.setSize(size);
		this.color = col;
		this.visible = true;
	}
	
	public vector getLocation(){
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
