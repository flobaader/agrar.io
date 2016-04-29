package agrar.io;

import java.awt.Color;
import java.util.Random;

public class Utility {

	public static double getDistance(Circle c1, Circle c2) {
		int y_dis = c1.getLocation().y - c2.getLocation().y;
		int x_dis = c1.getLocation().x - c2.getLocation().x;
		// c² = a² + b²
		return Math.sqrt(Math.pow(y_dis, 2) + Math.pow(x_dis, 2));
	}

	public static int getRandom(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}
	
	public static Color getRandomColor(){
		return new Color(getRandom(0,255),getRandom(0,255),getRandom(0,255));
	}

}
