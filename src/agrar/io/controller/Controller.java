package agrar.io.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.Timer;

import agrar.io.model.*;
import agrar.io.util.Utility;
import agrar.io.util.Vector;
import agrar.io.view.GameWindow;

/**
 * The controller of the game, which starts, stops and manages the whole game
 * 
 * @author Flo
 *
 */
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

	private DatabaseAdapter dbAdapter;

	public Controller() {
		players = new ArrayList<Player>();
		food = new ArrayList<Food>();
		circlesToDelete = new ArrayList<Circle>();
		window = new GameWindow(this);
	}

	public void StartGame() {
		// Loads Players
		InitializePlayer();

		// Spawns Food and AI
		SpawnObjects();

		graphicsRate = new Timer(1, new ActionListener() { // TODO: performance
															// test, set back to
															// 30

			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.refresh();
			}
		});

		graphicsRate.start();

		// Starts Game Refresh Timer
		gameRate = new Timer(16, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				RunGameCycle();
			}
		});
		gameRate.start();

	}

	private void SpawnPlayer() {
		// Count AIPlayers
		AIPlayer p = new AIPlayer(this, Utility.getRandomPoint(0, 1000), 5000, Utility.getRandomColor(), "AI-Player");
		players.add(p);
	}

	private void SpawnFood() {
		Food f = new Food(this, Utility.getRandomPoint(0, 1000), 200, Utility.getRandomColor());
		food.add(f);
	}

	private void InitializePlayer() {
		localPlayer = new LocalPlayer(this, Utility.getRandomPoint(0, 1000), 5000, Color.blue, "LocalPlayer");
		players.add(localPlayer);
	}

	public int getLocalPlayerScore() {
		return localPlayer.getSize();
	}

	public Player getLocalPlayer() {
		return localPlayer;
	}

	private void SpawnObjects() {
		// Spawns Players if necessary
		while (players.size() < 5) {
			SpawnPlayer();
		}

		// Spawns Food if necessary
		while (food.size() < 10) {
			SpawnFood();
		}

	}

	private void RunGameCycle() {
		// One Game Cycle
		SpawnObjects();

		// Every player can move one step
		for (Player p : players) {
			p.getBehavior().update(1);
		}

		// Deletes removed circles
		for (Circle c : circlesToDelete) {
			players.remove(c);
			food.remove(c);
			System.out.println("Deleted Circle!");
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

	public ArrayList<Circle> getObjectsInSight(Circle c1) {
		ArrayList<Circle> inSight = new ArrayList<Circle>();
		// TODO: View Range
		for (Circle f : food) {
			if (f != c1 && Utility.getDistance(f, c1) < 500) {
				inSight.add(f);
			}
		}

		for (Circle p : players) {
			if (p != c1 && Utility.getDistance(p, c1) < 500) {
				inSight.add(p);
			}
		}

		return inSight;
	}

	public DatabaseAdapter getDatabaseAdapter() {
		if (dbAdapter == null) {
			dbAdapter = new DatabaseAdapter();
		}
		return dbAdapter;
	}

	public Vector getMouseVector() {
		return window.getView().getMouseVector();
	}

	public Score[] getLocalHighscores() {

		//Sort the players so the best players are first
		Collections.sort(players, new Comparator<Player>() {

			//-1 => p1 < p2
			//0  => p1 == p2
			//1  => p1 > p2
			@Override
			public int compare(Player p1, Player p2) {

				int s1 = p1.getSize(), s2 = p2.getSize();

				if (s1 == s2) {
					return 0;
				} else if (s1 < s2) {
				}
			}

		});

		Score[] bestplayers = new Score[5];
		
		//Convert players to Scores 
		for(int i = 0; i < Math.min(5, players.size()); i++){
			bestplayers[i] = new Score(players.get(i).getSize(), players.get(i).getName(), "baum");
		}
		
		return bestplayers;
	}
}
