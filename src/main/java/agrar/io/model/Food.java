package agrar.io.model;

import java.awt.Color;

import agrar.io.controller.Controller;
import agrar.io.util.Vector;

/**
 * Represents food
 * @author Flo
 *
 */
public class Food extends Circle {
	
	/**
	 * Creates a new food circle
	 * @param parent The game controller
	 * @param loc The spawn location
	 * @param size The initial size
	 * @param col The color
	 */
	public Food(Controller parent, Vector loc, int size, Color col) {
		super(parent, loc, size, col);
	}

	/**
	 * Specifies that food is not a Player
	 */
	public boolean isPlayer() {
		return false;
	}
}
