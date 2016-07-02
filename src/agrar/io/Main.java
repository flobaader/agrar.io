package agrar.io;

import agrar.io.controller.Controller;
import agrar.io.model.Score;

public class Main {
	public static void main(String[] args) {
		Controller c = new Controller();
		c.StartGame(new Score(0, "Matze", "aslökdfj"));
	}
}
