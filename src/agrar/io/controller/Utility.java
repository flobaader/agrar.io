package agrar.io.controller;

import java.awt.Color;

import agrar.io.Vector;
import agrar.io.model.Circle;

public class Utility {

	public static double getDistance(Circle c1, Circle c2) {
		Vector distance = Vector.substractVector(c2.getLocation(), c1.getLocation());
		return distance.getLength();
	}

	public static int getRandom(int min, int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}

	public static Color getRandomColor() {
		return new Color(getRandom(0, 255), getRandom(0, 255), getRandom(0, 255));
	}

	public static Vector nextStepTowards(Vector loc, Vector target) {
		Vector nextStep = Vector.vectorFromTo(loc, target);
		nextStep = Vector.addVector(loc, Vector.getUnitVector(nextStep));
		return nextStep;

	}

	public static Vector getRandomPoint(int min, int max) {
		return new Vector(getRandom(min, max), getRandom(min, max));
	}

}
