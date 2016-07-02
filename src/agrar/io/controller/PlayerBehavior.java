package agrar.io.controller;

import agrar.io.model.Circle;
import agrar.io.model.Player;
import agrar.io.util.Utility;
import agrar.io.util.Vector;

/**
 * Simulates the Behavior of a Player
 * 
 * @author Flo
 *
 */
public abstract class PlayerBehavior {
	// The Circle to be controlled
	protected Player parent;

	// The Game Controller
	protected Controller controller;

	// The Location of the nextTarget to move to
	protected Vector nextTarget;

	/**
	 * Create new Behavior for the selected player
	 * 
	 * @param player
	 *            the player to be controlled
	 * @param controller
	 *            The Location of the next Target to move to
	 */
	public PlayerBehavior(Player player, Controller controller) {
		this.parent = player;
		this.controller = controller;
	}

	/**
	 * Updates the Location of the Player
	 * 
	 * @param deltaT
	 *            The time, that has passed since the last movement
	 */
	public abstract void update(float deltaT);

	/**
	 * Checks if Circles can be eaten
	 */
	protected void tryToEat() {
		for (Circle c1 : controller.getObjectsInSight(parent)) {
			// Bigger Circle eats smaller Circle, when half of the smaller
			// circle is covered
			double distance = Utility.getDistance(parent, c1);
			if (c1.getSize() < parent.getSize() && (distance - c1.getRadius()) <= parent.getRadius()) {
				// Adds size of other circle to this oneF
				parent.setSize(parent.getSize() + c1.getSize());
				controller.deleteCircle(c1);
			}
		}

	}

	/**
	 * Returns the current Target of the Player
	 * 
	 * @return Target
	 */
	public Vector getTarget() {
		return nextTarget;
	}

	/**
	 * Sets the location of the parent one Step towards the targetLocation
	 */
	protected void moveToNewPosition(float deltaT) {
		// The relative location of the target
		float sizeDifference = Math.abs(parent.getSize() - controller.getPLAYER_START_SIZE()) / 1000;

		float movementFactor = (float) ((deltaT / (0.5 * (Math.sqrt(sizeDifference) + 1)))
				* controller.getMOVEMENT_SPEED());

		// Creates the unit vector and multiplies it with the speed ( =
		// movementFactor)
		Vector nextLoc = Utility.nextStepTowards(parent.getLocation(), nextTarget, movementFactor);

		// Check Bounds
		if (nextLoc.getX() > 0 && nextLoc.getX() < controller.getFIELD_SIZE() && nextLoc.getY() > 0
				&& nextLoc.getY() < controller.getFIELD_SIZE()) {
			// Sets Location
			parent.setLocation(nextLoc);
		}

	}
}
