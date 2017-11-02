package agrar.io.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import agrar.io.model.Circle;
import agrar.io.model.Player;
import agrar.io.util.Utility;
import agrar.io.util.Vector;

/**
 * Provides basic behavior functions of a player
 * 
 * @author Flo
 *
 */
public abstract class PlayerBehavior {
	/**
	 * The circle to be controlled
	 */
	protected Player parent;

	/**
	 * The game controller
	 */
	protected Controller controller;

	/**
	 * The Location of the nextTarget to move to
	 */
	protected Vector nextTarget;

	/**
	 * The original Color of the Circle
	 */
	protected Color orgColor;

	/**
	 * The size of the player reduces if he does not get enough food.
	 * The gained size in the last minute is stored in this variable.
	 */
	private int gainedSizeInLastMinute = 0;
	
	/**
	 * Timer to monitor, if the player has gained enough food
	 */
	private Timer monitorFeedingTimer;

	/**
	 * The score of the Player = All gained size points
	 */
	private int score = 0;

	/**
	 *  The time, where the boost was activated
	 */
	private long lastBoostTime = 0;

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
		orgColor = player.getColor();

		monitorFeedingTimer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				monitorFeeding();
			}

		});
		monitorFeedingTimer.start();

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
			if (c1.getSize() < parent.getSize() && (distance - parent.getRadius() <= 0)) {
				
				// Adds size of other circle to this one
				parent.setSize(parent.getSize() + c1.getSize());
				
				//Adds size to feeding indicator
				gainedSizeInLastMinute += c1.getSize();
				
				//Adds gained size to score
				score += c1.getSize();

				//Deletes eaten circle from controller
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
		float sizeFactor = (float) parent.getSize() / (float) Controller.PLAYER_START_SIZE;

		float boostFactor = 1;
		if (isBoostActivated()) {
			boostFactor = 2;
		}

		float movementFactor = (float) ((deltaT / Math.pow(sizeFactor, 0.5)) * Controller.MOVEMENT_SPEED * boostFactor);

		// Creates the unit vector and multiplies it with the speed ( =
		// movementFactor)
		Vector nextLoc = Utility.nextStepTowards(parent.getLocation(), nextTarget, movementFactor);

		// Check Bounds
		if (nextLoc.getX() > 0 && nextLoc.getX() < Controller.FIELD_SIZE && nextLoc.getY() > 0
				&& nextLoc.getY() < Controller.FIELD_SIZE) {
			// Sets Location
			parent.setLocation(nextLoc);
		}

	}

	/**
	 * Ensures that the player had enough food during the last second and
	 * reduces size if not
	 */
	private void monitorFeeding() {
		// Size decreases if the feeding is less than 5% per Second
		if ((gainedSizeInLastMinute) < parent.getSize() * 0.05) {
			parent.setSize((int) (parent.getSize() * 0.98));
		}

		// Resets gained Size
		gainedSizeInLastMinute = 0;

		// Checks if size > 0
		checkSize();

	}

	/**
	 * @return the biggest size that the Player had
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Activates the Boost and reduces the size of the player by 1000
	 */
	public void activateBoost() {
		
		if(!isBoostActivated()){
			// Sets Boost time to now
			lastBoostTime = System.currentTimeMillis();

			// Reduces size by 1000 points
			parent.setSize(parent.getSize() - 1000);

			// Checks if size > 0
			checkSize();
		}
		
	}

	/**
	 * 
	 * @return Returns if the player has currently the boost enabled
	 */
	private boolean isBoostActivated() {
		// millis since last activation
		long millisElapsed = System.currentTimeMillis() - lastBoostTime;

		// if last activation was longer than 500 millis ago the boost is over
		return (millisElapsed < 500);
	}

	/**
	 * Checks if the size is greater than zero and deletes circle if not
	 */
	private void checkSize() {
		if (parent.getSize() <= 0) {
			controller.deleteCircle(parent);
		}

	}

}
