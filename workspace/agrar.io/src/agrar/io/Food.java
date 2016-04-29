package agrar.io;

import java.awt.Point;

public class Food extends Circle {
	private static int food_size = 20;
	
	public Food(Point loc){
		location = loc;
		
		//Sets fixed size of food
		size = food_size;
		
		//Sets Random Color
		color = Utility.getRandomColor();
	}
	
}
