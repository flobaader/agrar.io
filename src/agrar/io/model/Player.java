package agrar.io.model;

import java.awt.Color;

import agrar.io.controller.Controller;
import agrar.io.util.Vector;

public class Player extends Circle {

	private String name;
	private Vector movementDirection;

	public Player(Controller parent, Vector loc, int size, Color col, String name) {
		super(parent, loc, size, col);
		this.name = name;
		this.movementDirection = new Vector(0, 0);
	}

	@Override
	public boolean isPlayer() {
		return true;
	}

	public Vector getMovementDirection() {
		return this.movementDirection;
	}

	public String getName() {
		return this.name;
	}
}
