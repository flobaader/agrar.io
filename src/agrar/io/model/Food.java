package agrar.io.model;

import java.awt.Color;
import java.awt.Point;

import agrar.io.Vector;
import agrar.io.controller.Controller;

public class Food extends Circle {
	
	public Food(Controller parent, Vector loc, int size, Color col) {
		super(parent, loc, size, col);
	}

	public boolean isPlayer() {
		return false;
		
	}
}
