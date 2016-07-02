package agrar.io.model;

import java.awt.Color;

import agrar.io.controller.AIPlayerBehavior;
import agrar.io.controller.Controller;
import agrar.io.util.Vector;

public class AIPlayer extends Player{

	/**
	 * 
	 * @param parent
	 * @param loc
	 * @param size
	 * @param col
	 * @param name
	 */
	public AIPlayer(Controller parent, Vector loc, int size, Color col, String name, int Level) {
		super(parent, loc, size, col, name);
		this.behavior = new AIPlayerBehavior(this, parent,Level);
	}


}
