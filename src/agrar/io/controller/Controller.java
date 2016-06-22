package agrar.io.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import agrar.io.model.*;
import agrar.io.util.Utility;
import agrar.io.util.Vector;
import agrar.io.view.GameWindow;

public class Controller {
	// Game Window
	private GameWindow window;

	// Timers
	private Timer gameRate;
	private Timer graphicsRate;

	// Lists of Game Objects
	private ArrayList<Player> players;
	private ArrayList<Food> food;
	private ArrayList<Circle> circlesToDelete = new ArrayList<Circle>();
	private LocalPlayer localPlayer;

	public Controller() {
		players = new ArrayList<Player>();
		food = new ArrayList<Food>();
		circlesToDelete = new ArrayList<Circle>();
		window = new GameWindow(this);
	}

	public void StartGame() {
		// Loads Players
		InitializePlayer();

		graphicsRate = new Timer(16, new ActionListener() { // TODO: performance
															// test, set back to
															// 30

			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.refresh();
			}
		});

		graphicsRate.start();

		// Starts Game Refresh Timer
		gameRate = new Timer(5, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RunGameCycle();
			}
		});
		gameRate.start();

	}

	private void SpawnPlayer() {
		// Count AIPlayers
		AIPlayer p = new AIPlayer(this, Utility.getRandomPoint(0, 2000), 5000, Utility.getRandomColor(), "AI-Player");
		players.add(p);
	}

	private void SpawnFood() {
		Food f = new Food(this, Utility.getRandomPoint(0, 2000), 200, Utility.getRandomColor());
		food.add(f);
	}

	private void InitializePlayer() {
		localPlayer = new LocalPlayer(this, Utility.getRandomPoint(0, 2000), 5000, Color.blue, "LocalPlayer");
		players.add(localPlayer);
	}

	public int getLocalPlayerScore() {
		return localPlayer.getSize();
	}

	public Circle getLocalPlayer() {
		return localPlayer;
	}

	private void RunGameCycle() {
		// One Game Cycle

		// Spawns Players if necessary
		while (players.size() < 20) {
			SpawnPlayer();
		}

		// Spawns Food if necessary
		while (food.size() < 200) {
			SpawnFood();
		}

		// Every player can move one step
		for (Player p : players) {
			
		}

		// Deletes removed circles
		for (Circle c : circlesToDelete) {
			players.remove(c);
			food.remove(c);
		}
		circlesToDelete.clear();

	}

	public ArrayList<Circle> getAllComponents() {
		ArrayList<Circle> comp = new ArrayList<Circle>();
		comp.addAll(food);
		comp.addAll(players);
		return comp;
	}

	public void deleteCircle(Circle c1) {
		circlesToDelete.add(c1);
	}

	public Vector getOffset() {
		return new Vector(window.getSize().getWidth() / 2, window.getSize().getHeight() / 2);
	}

	public Player getNearestPlayer(Circle c1) {
		Player p1 = null;
		double nearest_distance = Double.MAX_VALUE;
		for (Player p : players) {
			if (c1 != p && Utility.getDistance(c1, p) < nearest_distance) {
				nearest_distance = Utility.getDistance(c1, p);
				p1 = p;
			}
		}

		if (p1 != null) {
			return p1;
		} else {
			return localPlayer;
		}

	}

	public Food getNearestFood(Circle c1) {
		Food f1 = null;
		double nearest_distance = Double.MAX_VALUE;
		for (Food f : food) {
			if (c1 != f && Utility.getDistance(c1, f) < nearest_distance) {
				nearest_distance = Utility.getDistance(c1, f);
				f1 = f;
			}
		}
		return f1;

	}

	public Circle getNearestObject(Circle c1) {
		Circle c2 = null;
		double nearest_distance = Double.MAX_VALUE;
		for (Circle c : getAllComponents()) {
			if (c != c1) {
				if (Utility.getDistance(c1, c) < nearest_distance) {
					nearest_distance = Utility.getDistance(c1, c);
					c2 = c;
				}
			}

		}
		return c2;

	}
}
