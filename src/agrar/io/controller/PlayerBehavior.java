package agrar.io.controller;

import agrar.io.model.Circle;

public abstract class PlayerBehavior {

	public PlayerBehavior() {
	}

	public abstract void update(float deltaT);

	public void tryToEat(Circle c){
		
	}
	
	public void moveToNewPosition(){
		
	}
}
