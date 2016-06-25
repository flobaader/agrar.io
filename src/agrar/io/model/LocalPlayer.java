package agrar.io.model;

import java.awt.Color;

import agrar.io.controller.Controller;
import agrar.io.controller.LocalPlayerBehavior;
import agrar.io.util.Vector;

public class LocalPlayer extends Player {

	public LocalPlayer(Controller parent, Vector loc, int size, Color col, String name) {
		super(parent, loc, size, col, name);
		this.behavior = new LocalPlayerBehavior(this, parent);
	}

}
