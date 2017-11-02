package agrar.io.model;

import java.awt.Color;

import agrar.io.controller.AIPlayerBehavior;
import agrar.io.controller.Controller;
import agrar.io.util.Vector;

/**
 * Represents the model of an AI-Player
 * @author Flo
 *
 */
public class AIPlayer extends Player{

	/**
	 * Creates a new AI Player Model
	 * @param parent The game controller
	 * @param loc The spawn location
	 * @param size The initial size of the Player
	 * @param col The color of the Player
	 * @param name The name of the Player
	 */
	public AIPlayer(Controller parent, Vector loc, int size, Color col, String name, int Level) {
		super(parent, loc, size, col, name);
		this.behavior = new AIPlayerBehavior(this, parent,Level);
	}


}
