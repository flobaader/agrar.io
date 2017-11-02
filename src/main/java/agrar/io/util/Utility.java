package agrar.io.util;

import java.awt.Color;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import agrar.io.model.Circle;

/**
 * Basic Functionality Class
 * @author FBaader
 *
 */
public class Utility {

	/**
	 * Returns the distance between two Circles
	 * @param c1 The first Circle
	 * @param c2 The second Circle
	 * @return The distance between the Circles
	 */
	public static double getDistance(Circle c1, Circle c2) {
		Vector distance = c2.getLocation().substractVector(c1.getLocation());
		return distance.getLength();
	}

	/**
	 * Returns a random value between the given integers
	 * @param min The minimum value of the given random
	 * @param max The maximum value of the given random
	 * @return The random value
	 */
	public static int getRandom(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	/**
	 * Returns a random RGB Color between (0,0,0) and (255,255,255)
	 * @return The random Color
	 */
	public static Color getRandomColor() {
		return new Color(getRandom(0, 255), getRandom(0, 255), getRandom(0, 255));
	}

	/**
	 * Generates the unit vector of the vector between the first circle and the target and adds it to the location of the first vector
	 * @param loc The Location of the first Vector
	 * @param target The Location of the Target
	 * @param length The length of the next Step / the Speed of the Circle
	 * @return The Unitvector between those two locations
	 */
	public static Vector nextStepTowards(Vector loc, Vector target, double length) {
		Vector nextStep = new Vector(loc, target);
		nextStep = loc.addVector(nextStep.getUnitVector().multiplyVector(length));
		return nextStep;
	}

	/**
	 * Returns a random point in the range of min and max value
	 * Note: Both coordinates have the same range
	 * @param min The min value of the coordinates
	 * @param max The max value of the coordinates
	 * @return Random point
	 */
	public static Vector getRandomPoint(int min, int max) {
		return new Vector(getRandom(min, max), getRandom(min, max));
	}

	/**
	 * Returns the SHA-512 HASH of the given string
	 * @param input The string to be hashed
	 * @return The hash of the given string
	 */
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
