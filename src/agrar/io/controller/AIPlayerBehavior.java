package agrar.io.controller;

import agrar.io.controller.PlayerBehavior;
import agrar.io.model.AIPlayer;
import agrar.io.model.Circle;
import agrar.io.util.Utility;

/**
 * Simulates the behavior of another player
 * @author Flo
 *
 */
public class AIPlayerBehavior extends PlayerBehavior {
	private AIPlayer parent;
	private Controller contr;

	/**
	 * Creates new behavior for the given AIPlayer
	 * @param parent The AIPlayer to control
	 */
	public AIPlayerBehavior(AIPlayer player, Controller controller) {
		super(player, controller);
	}

	@Override
	public void update(float deltaT) {
		
		//The highest value of a Circle found near the Player
		double value = 0;
		
		//The next target
		Circle bestTarget = null;
		
		//Iterates trough all given Objects in Sight and saves the best one 
		for (Circle c : contr.getObjectsInSight(parent)) {
			double val = evaluateCircle(c) + evaluateLocationOfCircle(c);
			if (val > value) {
				value = val;
				bestTarget = c;
			}
		}

		
		if (bestTarget != null) {
			//Sets Location one step towards the target
			nextTarget = bestTarget.getLocation();
			moveToNewPosition();
		}

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

		return (c.getSize() - parent.getSize())
				* (1 / Math.pow(Utility.getDistance(parent, c), 2));
	}

	/**
	 * Describes the value of the location of the given Circle with determines if there is food or good targets around
	 * @param c The Circle, which location should be evaluated
	 * @return The Value of the Location
	 */
	public double evaluateLocationOfCircle(Circle c) {
		double value = 0;

		for (Circle c1 : contr.getObjectsInSight(c)) {
			value += evaluateCircle(c1);
		}
		return value;
	}
}
