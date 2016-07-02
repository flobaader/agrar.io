package agrar.io.controller;

import java.awt.Color;

import agrar.io.controller.PlayerBehavior;
import agrar.io.model.AIPlayer;
import agrar.io.model.Circle;
import agrar.io.util.Utility;

/**
 * Simulates the behavior of another player
 * 
 * @author Flo
 *
 */
public class AIPlayerBehavior extends PlayerBehavior {

	/**
	 * Creates new behavior for the given AIPlayer
	 * 
	 * @param parent
	 *            The AIPlayer to control
	 */
	public AIPlayerBehavior(AIPlayer player, Controller controller) {
		super(player, controller);
	}

	@Override
	public void update(float deltaT) {

		// The highest value of a Circle found near the Player
		double bestValue = Double.MIN_VALUE;

		// The next target
		Circle bestTarget = null;

		// Iterates trough all given Objects in Sight and saves the best one
		for (Circle c : controller.getObjectsInSight(parent)) {
			double curVal = evaluateCircle(c) + 0.5 * evaluateLocationOfCircle(c);
			if (curVal > bestValue) {
				bestValue = curVal;
				bestTarget = c;
			}
		}

		if (bestTarget != null) {
			// Sets Location one step towards the target
			nextTarget = bestTarget.getLocation();
			moveToNewPosition();
		} else {
			parent.setColor(Color.RED);
		}

		tryToEatNearestCircle();

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
		double isSmaller = 1;
		if (c.getSize() > parent.getSize()) {
			isSmaller = -1;
		}

		double sizeFactor = Math.abs(c.getSize() - parent.getSize());
		double distanceFactor = (1 / Math.pow(Utility.getDistance(parent, c), 2));

		return (isSmaller * sizeFactor * distanceFactor);
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
		return value;
	}
}
