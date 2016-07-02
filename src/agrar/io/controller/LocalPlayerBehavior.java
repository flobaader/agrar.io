package agrar.io.controller;

import agrar.io.model.LocalPlayer;

public class LocalPlayerBehavior extends PlayerBehavior {

	
	public LocalPlayerBehavior(LocalPlayer parent, Controller controller) {
		super(parent, controller);
	}

	@Override
	public void update(float deltaT) {
		nextTarget = parent.getLocation().addVector(controller.getMouseVector());
		moveToNewPosition(deltaT);
		tryToEat();
	}

}
