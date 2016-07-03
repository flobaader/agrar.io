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
	
	//UI
	private GameWindow window;

	// Timers
	private Timer gameRate;
	private Timer graphicsRate;

	// Game Statics
	private static int PLAYER_START_SIZE = 5000;
	private static int FOOD_SIZE = 200;
	private static int VIEWRANGE = 500;
	private static int AI_PLAYER_COUNT = 10;
	private static int FOOD_COUNT = 200;
	private static int FIELD_SIZE = 2000;
	private static int VIEW_REFRESH_RATE = 1;
	private static int GAME_REFERSH_RATE = 1;
	private static float MOVEMENT_SPEED = 0.4F;

	// Lists of Game Objects
	private ArrayList<Player> players;
	private ArrayList<Food> food;
	private ArrayList<Circle> circlesToDelete = new ArrayList<Circle>();
	private LocalPlayer localPlayer;

	private DatabaseAdapter dbAdapter;

	private long lastUpdateTime;

	public Controller() {
		players = new ArrayList<Player>();
		food = new ArrayList<Food>();
		circlesToDelete = new ArrayList<Circle>();
		window = new GameWindow(this);
	}

	/**
	 * Starts the game
	 */
	public void startGame() {
		// Loads Players
		initializeLocalPlayer();

		// Spawns Food and AI
		spawnAllGameObjects();

		graphicsRate = new Timer(VIEW_REFRESH_RATE, new ActionListener() { // TODO:
																			// performance
			// test, set back to
			// 30

			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.refresh();
			}
		});

		graphicsRate.start();

		// Sets First Update Time
		lastUpdateTime = System.currentTimeMillis();

		// Starts Game Refresh Timer
		gameRate = new Timer(GAME_REFERSH_RATE, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				runGameCycle();
			}
		});
		gameRate.start();

	}

	/**
	 * Spawns local Player
	 */
	private void initializeLocalPlayer() {
		localPlayer = new LocalPlayer(this, Utility.getRandomPoint(0, FIELD_SIZE), PLAYER_START_SIZE, Color.blue,
				"LocalPlayer");
		players.add(localPlayer);
	}

	/**
	 * Spawns one Player
	 */
	private void spawnAIPlayer() {
		// Count AIPlayers

		int level = Utility.getRandom(0, 10);

		AIPlayer p = new AIPlayer(this, Utility.getRandomPoint(0, FIELD_SIZE), PLAYER_START_SIZE,
				Utility.getRandomColor(), "AI-Player", level);
		players.add(p);
	}

	/**
	 * Spawns one food circle
	 */
	private void spawnFood() {
		Food f = new Food(this, Utility.getRandomPoint(0, FIELD_SIZE), FOOD_SIZE, Utility.getRandomColor());

		food.add(f);
	}

	/**
	 * Spawns Food and AI Players till the spawn limit is reached
	 */
	private void spawnAllGameObjects() {
		// Spawns Players if necessary
		while (players.size() < AI_PLAYER_COUNT) {
			spawnAIPlayer();
		}

		// Spawns Food if necessary
		while (food.size() < FOOD_COUNT) {
			spawnFood();
		}

	}

	/**
	 * Simulates one Game Cycle, where every Player can move one time and eat other Players and objects are spawned and deleted
	 */
	private void runGameCycle() {
		
		//The time the last circle took
		long millisSinceLastUpdate = System.currentTimeMillis() - lastUpdateTime;

		// Spawn AI Players and Food if necessary
		spawnAllGameObjects();

		// Every player can move one step
		for (Player p : players) {
			p.getBehavior().update(millisSinceLastUpdate);
		}
		
		// Deletes eaten Circles
		clearDeletedCircles();

		//Sets update Time
		lastUpdateTime = System.currentTimeMillis();

	}

	/**
	 * The Circles, which are requested to be removed, are saved in a list and will now be removed from the other lists
	 */
	private void clearDeletedCircles() {
		//Iterates through all saved circles
		for (Circle c : circlesToDelete) {
			
			//.remove Command only removes if the object exists
			
			//If the Circle is in of these lists he gets removed
			players.remove(c);
			food.remove(c);
		}
		//Clears list
		circlesToDelete.clear();

	}

	/**
	 * Requests to delete the selected circle. The circle will be stored in a list and removed after the whole game cycle
	 * @param c1 The circle to be deleted
	 */
	public void deleteCircle(Circle c1) {
		circlesToDelete.add(c1);
	}

	/**
	 * 
	 * @return The LocalPlayer as a Player
	 */
	public Player getLocalPlayer() {
		return localPlayer;
	}

	/**
	 * 
	 * @return The current Size of the localplayer
	 */
	public int getLocalPlayerScore() {
		return localPlayer.getSize();
	}

	/**
	 * 
	 * @return All circles which are shown in the game
	 */
	public ArrayList<Circle> getAllGameObjects() {
		ArrayList<Circle> comp = new ArrayList<Circle>();
		comp.addAll(food);
		comp.addAll(players);
		return comp;
	}

	/**
	 * 
	 * @return Returns the center of the screen as a vector
	 */
	public Vector getOffset() {
		return new Vector(window.getSize().getWidth() / 2, window.getSize().getHeight() / 2);
	}


	/**
	 * Returns all objects which are in the view range of the circle
	 * @param c1 The circle, which looks for other Objects
	 * @return List of Objects with a distance <= view range
	 */
	public ArrayList<Circle> getObjectsInSight(Circle c1) {
		ArrayList<Circle> inSight = new ArrayList<Circle>();
		for (Circle f : food) {
			if (f != c1 && Utility.getDistance(f, c1) < VIEWRANGE) {
				inSight.add(f);
			}
		}

		for (Circle p : players) {
			if (p != c1 && Utility.getDistance(p, c1) < VIEWRANGE) {
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

	/**
	 * 
	 * @return The vector from the center of the screen to the mouse
	 */
	public Vector getMouseVector() {
		return window.getView().getMouseVector();
	}

	public Score[] getLocalHighscores() {

		// Sort the players so the best players are first
		Collections.sort(players, new Comparator<Player>() {

			// -1 => p1 < p2
			// 0 => p1 == p2
			// 1 => p1 > p2
			@Override
			public int compare(Player p1, Player p2) {

				int s1 = p1.getSize(), s2 = p2.getSize();

				if (s1 == s2) {
					return 0;
				} else if (s1 < s2) {
					return 1;
				}
				return -1;
			}

		});

		Score[] bestplayers = new Score[5];

		// Convert players to Scores
		for (int i = 0; i < Math.min(5, players.size()); i++) {
			bestplayers[i] = new Score(players.get(i).getSize(), players.get(i).getName(), "baum");
		}

		return bestplayers;
	}

	/**
	 * @return Returns the specified size of a player when he starts
	 */
	public int getPLAYER_START_SIZE() {
		return PLAYER_START_SIZE;
	}

	/**
	 * @return Returns the view range of a player in which other circles are recognized
	 */
	public int getVIEWRANGE() {
		return VIEWRANGE;
	}

	/**
	 * @return Returns the amount of AI Players
	 */
	public int getAI_PLAYER_COUNT() {
		return AI_PLAYER_COUNT;
	}

	/**
	 * @return Returns the length and width of the game field
	 */
	public int getFIELD_SIZE() {
		return FIELD_SIZE;
	}

	/**
	 * @return Returns the movement speed factor
	 */
	public double getMOVEMENT_SPEED() {
		return MOVEMENT_SPEED;
	}
}
