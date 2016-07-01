package agrar.io.util;

import java.awt.Color;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import agrar.io.model.Circle;

public class Utility {

	public static double getDistance(Circle c1, Circle c2) {
		Vector distance = c2.getLocation().substractVector(c1.getLocation());
		return distance.getLength();
	}

	public static int getRandom(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
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

	public static byte[] getHash(String input) {
		
		byte[] bytes = null;
		try {
			// Get algorithm and hash string
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			bytes = md.digest(input.getBytes("UTF-8"));

		}
		// These should not occur at runtime, so we can ignore them
		catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return bytes;
	}

}
