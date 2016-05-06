package agrar.io.controller;

import java.awt.Color;
import agrar.io.vector;

public class KIPlayer extends Player {
	private vector targetLocation;

	public KIPlayer(Controller parent, vector loc, int size, Color col, String name) {
		super(parent, loc, size, col, name);
		// TODO Auto-generated constructor stub
	}

	private void selectTarget() {

		// Trys to get nearest Player
		Player nearPlayer = parent.getNearestPlayer(this);
		double distance = Utility.getDistance(this, nearPlayer) - nearPlayer.getRadius() - this.getRadius();

		boolean isNear = distance < (this.getRadius() * 3);
		boolean isSmaller = (nearPlayer.getSize() - this.size) < 10;

		if (isNear && !isSmaller) {
			// Flee
			targetLocation = vector.reverseVector(vector.vectorFromTo(this.getLocation(), nearPlayer.getLocation()));
			this.color = Color.yellow;
		} else if (isNear && isSmaller) {
			// Attack
			targetLocation = nearPlayer.getLocation();
			this.color = Color.red;
		} else {
			// Searches for food
			targetLocation = parent.getNearestFood(this).getLocation();
			this.color = Color.green;

		}

	}

	public void moveToNewPosition() {

		// gets new Target
		selectTarget();
		// Moves to the next position towards target
		this.location = Utility.nextStepTowards(getLocation(), targetLocation);

		// Trys to eat nearest object
		tryToEat(parent.getNearestObject(this));
	}

	public boolean isPlayer() {
		return true;
	}

}
