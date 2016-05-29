package agrar.io.interfaces;

import agrar.io.Vector;

public interface InputListener {

	/**
	 * Notifies the listener about a change of the movement vector that controls the local player
	 * @param v the new {@link agrar.io.Vector Vector} 
	 */
	public void updateMovementVector(Vector v);
	
	/**
	 * Notifies the listener about a key press
	 */
	public void keyPress();
}
