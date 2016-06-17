package agrar.io.model;

import java.awt.Color;

import agrar.io.Vector;
import agrar.io.controller.Controller;

public class Player extends Circle {

	public Player(Controller parent, Vector loc, int size, Color col) {
		super(parent, loc, size, col);
	}

	@Override
	public boolean isPlayer() {
		return true;
	}

}
