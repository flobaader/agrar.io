package agrar.io.controller;

import java.awt.Color;

import agrar.io.model.AIPlayer;
import agrar.io.model.Circle;
import agrar.io.util.Utility;
import agrar.io.util.Vector;

/**
 * Simulates the behavior of a player
 * 
 * @author Flo
 *
 */
public class AIPlayerBehavior extends PlayerBehavior {

	/**
	 * In a range from 0 to 10, the Level simulates the experience of the AI
	 * player to estimate the size of other players
	 */
	private int LEVEL;

	/**
	 * The flee threshold of the player
	 */
	private int FLEE_THRESHOLD;

	/**
	 * The boost threshold of the player
	 */
	private int BOOST_THRESHOLD;

	/**
	 * Creates new behavior for the given AIPlayer
	 * 
	 * @param parent
	 *            The AIPlayer to control
	 * @param controller
	 *            The controller of the game
	 * @param The
	 *            Level of Experience of the Player (from 0 to 10)
	 * @throws Exception
	 *             Throws Exception if the Level is not in range between 0 and
	 *             10
	 */
	public AIPlayerBehavior(AIPlayer player, Controller controller, int Level) {
		super(player, controller);

		// Ensures that the level argument is in the range
		if (Level < 0 || Level > 10) {
			Level = 1;
		} else {
			LEVEL = Level;
		}

		// Randomizes the point, where the player decides to flee
		FLEE_THRESHOLD = Utility.getRandom(-3, -1);

		// Randomizes the point, where the player decides to activate the boost
		BOOST_THRESHOLD = Utility.getRandom(1, 5);

	}

	/**
	 * Simulates the estimating of an other circles size
	 * 
	 * @param Size
	 *            The real size of the other circle
	 * @return The misjudged size regarding the LEVEL of the player
	 */
	private double misjudgeCircleSize(double Size) {

		// Decreases with level
		int range = 10 - LEVEL; // in Percent

		// Max and min of misjudgment is 10 % of size
		int randomFactor = Utility.getRandom(-1 * range, range); // in Percent

		double rndAbs = (double) randomFactor / 100; // Absolute not percent

		// Estimated size
		double estimatedSize = Size + Size * rndAbs;

		return estimatedSize;
	}

	@Override
	/**
	 * Updates the location of the player to a new one
	 */
	public void update(float deltaT) {

		// The circle with the highest value
		Circle bestTarget = null;
		double bestValue = Double.MIN_VALUE;

		// The circle with the lowest value
		Circle worstTarget = null;
		double worstValue = Double.MAX_VALUE;

		// Iterates trough all given Objects in Sight and saves the best one and
		// the worst one
		for (Circle c : controller.getObjectsInSight(parent)) {

			double curVal = evaluateCircle(c);// + 0.5 *
												// evaluateLocationOfCircle(c);

			// Checks if this is currently the best one
			if (curVal > bestValue) {
				bestValue = curVal;
				bestTarget = c;
			}

			// Checks if this is currently the worst one
			if (curVal < worstValue) {
				worstValue = curVal;
				worstTarget = c;
			}

		}

		if (bestTarget == null || worstTarget == null) {
			// Did not find any Circle in Sight

			// Sets target to a random point
			nextTarget = Utility.getRandomPoint(Controller.FIELD_SIZE, Controller.FIELD_SIZE);

			// Colors Circle if in Debug Mode
			if (controller.isInDebugMode()) {
				parent.setColor(Color.BLACK);
			}

		} else if (bestValue > 0 && worstValue > FLEE_THRESHOLD) {

			// Found a good Circle and is not in Danger

			// Sets target to the location of the best target
			nextTarget = bestTarget.getLocation();

			// Colors circle if in debug mode
			if (controller.isInDebugMode()) {
				parent.setColor(orgColor);
			}

			// Boost decision
			// if high value and size difference is bigger than 1000 (boost
			// takes 1000 points)
			if (bestValue > BOOST_THRESHOLD && (parent.getSize() - bestTarget.getSize()) > 1000) {
				activateBoost();
			}

		} else {

			// is in Danger or did not find good target --> flees in opposite
			// direction of the worst target
			nextTarget = new Vector(parent.getLocation(), worstTarget.getLocation()).multiplyVector(-1)
					.addVector(parent.getLocation());

			// Colors Circle if in debug mode
			if (controller.isInDebugMode()) {
				parent.setColor(Color.RED);
			}

			// Boost Decision
			if (worstValue < -1 * BOOST_THRESHOLD) {
				activateBoost();
			}

		}

		// Moves the next step(s) to the selected target
		moveToNewPosition(deltaT);

		// Checks if Circles can be eaten
		tryToEat();

	}

	/**
	 * The function assigns a value to the given Circle, which represents the
	 * Points and Distance relationship between Hunter and Target
	 * 
	 * @param c
	 *            Circle to be evaluated
	 * @return value from -infinity to +infinity
	 */
	public double evaluateCircle(Circle c) {
		// Requirements:
		// The value increases with the size difference (parent > c)
		// The value decreases with the size difference (parent < c)
		// The value decreases with the distance

		// Value is negative if Target is bigger than parent
		double isSmaller = 1;

		// The AI Player does not get the real size of the Circle, that would
		// not be fair
		double estimatedCircleSize = misjudgeCircleSize(c.getSize());

		// NOTE: uses misjudged Size of Circle
		if (estimatedCircleSize > parent.getSize()) {
			isSmaller = -1;
		}

		double distanceFactor = (1 / Math.pow(Utility.getDistance(parent, c), 2));

		return (isSmaller * estimatedCircleSize * distanceFactor);
	}

	/**
	 * Describes the value of the location of the given Circle with determines
	 * if there is food or good targets around
	 * 
	 * @param c
	 *            The Circle, which location should be evaluated
	 * @return The Value of the Location
	 */
	public double evaluateLocationOfCircle(Circle c) {
		double value = 0;

		for (Circle c1 : controller.getObjectsInSight(c)) {
			value = value + evaluateCircle(c1);
		}

		value = value / controller.getObjectsInSight(c).size();
		return value;
	}
}
