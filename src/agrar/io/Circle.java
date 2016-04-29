package agrar.io;
import java.awt.*;

public abstract class Circle {
	protected Point location;
	protected int size;
	protected Color color;
	
	public Point getLocation(){
		return location;
	}
	
	public int getSize(){
		return size;
	}
	
	public Color getColor(){
		return color;
	}
	
}
