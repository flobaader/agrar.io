package agrar.io;

import java.util.ArrayList;

public class Controller {
	private ArrayList<Circle> components;
	private static int view_range;
	private View view;
	
	
	public Controller(){
		components = new ArrayList<Circle>();
		view = new View(this);
	}
	
	public ArrayList<Circle> getNearObjects(Circle c){
		ArrayList<Circle> returntargets = new ArrayList<Circle>();
		
		for(Circle target: components){
			if(Utility.getDistance(c, target) < view_range){
				returntargets.add(target);
			}
		}
		
		return returntargets;
	}
	
	public ArrayList<Circle> getAllComponents(){
		return components;
	}
	
	
	
	
	
}
