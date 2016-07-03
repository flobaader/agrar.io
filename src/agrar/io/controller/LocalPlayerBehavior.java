package agrar.io.controller;

import agrar.io.model.LocalPlayer;

/**
 * The control of the local player
 * @author Flo
 *
 */
public class LocalPlayerBehavior extends PlayerBehavior {
	
	public LocalPlayerBehavior(LocalPlayer parent, Controller controller) {
		super(parent, controller);
	}

	@Override
	public void update(float deltaT) {
		
		//The next target is the mouse location
		nextTarget = parent.getLocation().addVector(controller.getMouseVector());
		
		//Moves one step to the target
		moveToNewPosition(deltaT);
		
		//Tries to eat other circles
		tryToEat();
	}

}
