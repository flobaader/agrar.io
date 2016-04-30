package agrar.io;
import java.awt.*;

public abstract class Circle {
	protected Point location;
	protected int size;
	protected Color color;
	protected Controller parent;
	
	public Circle(Controller parent, Point loc, int size, Color col){
		this.parent = parent;
		this.location = loc;
		this.size = size;
		this.color = col;
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
		
	}
	
	public abstract boolean isPlayer();
	
}
