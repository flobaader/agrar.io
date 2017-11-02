package agrar.io.model;

import java.awt.Color;

import agrar.io.controller.Controller;
import agrar.io.controller.LocalPlayerBehavior;
import agrar.io.util.Vector;

/**
 * Represents a LocalPlayer
 * @author FBaader
 *
 */
public class LocalPlayer extends Player {

	/**
	 * Creates a new local player
	 * @param parent The game controller
	 * @param loc The spawn location
	 * @param size The initial size
	 * @param col The color of the circle
	 * @param name The name of the player
	 */
	public LocalPlayer(Controller parent, Vector loc, int size, Color col, String name) {
		super(parent, loc, size, col, name);
		
		//LocalPlayer has LocalPlayerBehavior
		this.behavior = new LocalPlayerBehavior(this, parent);
	}

}
