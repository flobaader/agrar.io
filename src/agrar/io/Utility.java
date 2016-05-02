package agrar.io;

import java.awt.Color;
import java.awt.Point;
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
	
	public static Point nextStepTowards(Point p1, Point p2){
		// m * x + t
		//First Step ... calculate m
		Point p3 = new Point();
		
		if(p2.x > p1.x){
			p3.x = p1.x + 1;
		}else if(p2.x == p1.x){
			p3.x = p1.x;
		}else{
			p3.x = p1.x -1;
		}
		
		if(p2.y > p1.y){
			p3.y = p1.y +1;
		}else if( p2.y == p1.y){
			p3.y = p1.y;
		}else{
			p3.y = p1.y -1;
		}
		
		return p3;
		
	}
	
	public static Point getRandomPoint(int min, int max){
		Point p = new Point();
		p.x = getRandom(min, max);
		p.y = getRandom(min, max);
		return p;
		
	}
	
	public static Point mirrorPoint(Point main, Point target){
		Point p = new Point();
		p.x = main.x - (target.x - main.x);
		p.y = main.x - (target.y - main.y);
		return p;
	}

}
