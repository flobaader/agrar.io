package agrar.io.controller;

import java.awt.Color;

import agrar.io.Vector;
import agrar.io.model.Circle;

public class Utility {

	public static double getDistance(Circle c1, Circle c2) {

		Vector distance = c2.getLocation().substractVector(c1.getLocation());

		return distance.getLength();
	}

	public static int getRandom(int min, int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}

	public static Color getRandomColor() {
		return new Color(getRandom(0, 255), getRandom(0, 255), getRandom(0, 255));
	}

	public static Vector nextStepTowards(Vector loc, Vector target) {
		Vector nextStep = new Vector(loc, target);
		nextStep = loc.addVector(nextStep.getUnitVector());

		return nextStep;
	}

	public static Vector getRandomPoint(int min, int max) {
		return new Vector(getRandom(min, max), getRandom(min, max));
	}

}
