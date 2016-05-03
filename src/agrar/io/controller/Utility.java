package agrar.io.controller;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import agrar.io.vector;
import agrar.io.model.Circle;

public class Utility {

	public static double getDistance(Circle c1, Circle c2) {
		// double y_dis = c1.getLocation().getX() - c2.getLocation().getY();
		// double x_dis = c1.getLocation().getX() - c2.getLocation().getY();
		// // c² = a² + b²
		// return Math.sqrt(Math.pow(y_dis, 2) + Math.pow(x_dis, 2));
		vector distance = vector.substractVector(c2.getLocation(), c1.getLocation());
		return distance.getLength();
		
	}

	public static int getRandom(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static Color getRandomColor() {
		return new Color(getRandom(0, 255), getRandom(0, 255), getRandom(0, 255));
	}

	public static vector nextStepTowards(vector loc, vector target) {
		// m * x + t
		// First Step ... calculate m
		// Point p3 = new Point();
		//
		// if(p2.x > p1.x){
		// p3.x = p1.x + 1;
		// }else if(p2.x == p1.x){
		// p3.x = p1.x;
		// }else{
		// p3.x = p1.x -1;
		// }
		//
		// if(p2.y > p1.y){
		// p3.y = p1.y +1;
		// }else if( p2.y == p1.y){
		// p3.y = p1.y;
		// }else{
		// p3.y = p1.y -1;
		// }
		//
		// return p3;

		vector nextStep = vector.substractVector(target, loc);
		nextStep = vector.addVector(loc, vector.getUnitVector(nextStep));
		return nextStep;

	}

	public static vector getRandomPoint(int min, int max) {
		return new vector(getRandom(min, max), getRandom(min, max));

	}

}
