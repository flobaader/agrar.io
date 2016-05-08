package agrar.io.controller;

import java.awt.Color;

import agrar.io.vector;
import agrar.io.model.Circle;

public class Utility {

	public static double getDistance(Circle c1, Circle c2) {
		vector distance = vector.substractVector(c2.getLocation(), c1.getLocation());
		return distance.getLength();
	}

	public static int getRandom(int min, int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}

	public static Color getRandomColor() {
		return new Color(getRandom(0, 255), getRandom(0, 255), getRandom(0, 255));
	}

	public static vector nextStepTowards(vector loc, vector target) {
		vector nextStep = vector.vectorFromTo(loc, target);
		nextStep = vector.addVector(loc, vector.getUnitVector(nextStep));
		return nextStep;

	}

	public static vector getRandomPoint(int min, int max) {
		return new vector(getRandom(min, max), getRandom(min, max));
	}

}
