package agrar.io.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.Timer;

import agrar.io.controller.DatabaseAdapter.InvalidPasswordException;
import agrar.io.model.*;
import agrar.io.util.Utility;
import agrar.io.util.Vector;
import agrar.io.view.GameWindow;
import agrar.io.view.GameWindow.GameWindowListener;

/**
 * The controller of the game, which starts, stops and manages the whole game
 * 
 * @author Flo, Matthias
 *
 */
public class Controller implements GameWindowListener {
	// Game Window
	private GameWindow window;

	// Timers
	private Timer gameRate;
	private Timer graphicsRate;

	// Game Statics
	private static int PLAYER_START_SIZE = 5000;
	private static int FOOD_SIZE = 200;
	private static int VIEWRANGE = 500;
	private static int AI_PLAYER_COUNT = 10;
	private static int FOOD_COUNT = 100;
	private static int FIELD_SIZE = 1000;
	private static int VIEW_REFRESH_RATE = 1;
	private static int GAME_REFERSH_RATE = 8;
	private static double MOVEMENT_SPEED = 0.1;

	// Lists of Game Objects
	private ArrayList<Player> players;
	private ArrayList<Food> food;
	private ArrayList<Circle> circlesToDelete = new ArrayList<Circle>();
	private LocalPlayer localPlayer;

	private DatabaseAdapter dbAdapter = new DatabaseAdapter();

	private long lastUpdateTime;

	public enum GameState {
		Playing, Paused, Stopped
	};

	private GameState currentState = GameState.Stopped;

	// Score that contains player name and password
	private Score login;

	private MenuController menuController;

	public Controller() {
		players = new ArrayList<Player>();
		food = new ArrayList<Food>();
		circlesToDelete = new ArrayList<Circle>();
		window = new GameWindow(this);

		menuController = new MenuController(this, window.getMenuView(), window);

		menuController.showMainMenu();
	}

	/**
	 * Starts a new game
	 * 
	 * @param s
	 * 
	 */
	public void StartGame(Score s) {

		// The game can only be started when it's stopped
		if (currentState != GameState.Stopped) {
			throw new IllegalStateException("You can only start the game if it is stopped!");
		}

		// Save name & password for later
		this.login = s;

		// Display the game arena
		window.hideMenu();

		// Sets First Update Time
		lastUpdateTime = System.currentTimeMillis();

		// Loads Players
		InitializePlayer(s.getName());

		// Spawns Food and AI
		SpawnObjects();

		graphicsRate = new Timer(VIEW_REFRESH_RATE, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.refresh();
			}

		});

		// Starts Game Refresh Timer
		gameRate = new Timer(GAME_REFERSH_RATE, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				RunGameCycle();
			}
		});

		// Start game timers
		gameRate.start();
		graphicsRate.start();

		// Switch to playing state
		currentState = GameState.Playing;

	}

	/**
	 * Pause the game (to display menu etc.)
	 */
	public void pauseGame() {
		
		//Stop graphics and simulation cycle
		gameRate.stop();
		graphicsRate.stop();
		
		//show menu
		menuController.showPauseMenu();
		
		currentState = GameState.Paused;//switch state last
	}

	/**
	 * resumes a paused game. Throws illegalStateException if the GameState is
	 * not paused
	 */
	public void resumeGame() {
		
		//Only a paused game can be resumed
		if (currentState != GameState.Paused) {
			throw new IllegalStateException("You can only resume a paused game!");
		}

		//restart the timers
		graphicsRate.restart();
		gameRate.start();

		//Hide menu, show game arena
		window.hideMenu();

		//switch to playing state last
		currentState = GameState.Playing;
	}

	/**
	 * Stop the game and tidy up
	 */
	public void stopGame() {
		if (currentState == GameState.Stopped) {
			throw new IllegalStateException("The game is already stopped!");
		}
		currentState = GameState.Stopped;
		gameRate.stop();
		graphicsRate.stop();
	}

	public GameState getState() {
		return currentState;
	}

	private void SpawnPlayer() {
		// Count AIPlayers

		int level = Utility.getRandom(0, 10);

		AIPlayer p = new AIPlayer(this, Utility.getRandomPoint(0, FIELD_SIZE), PLAYER_START_SIZE,
				Utility.getRandomColor(), "AI-Player", level);
		players.add(p);
	}

	private void SpawnFood() {
		Food f = new Food(this, Utility.getRandomPoint(0, FIELD_SIZE), FOOD_SIZE, Utility.getRandomColor());

		food.add(f);
	}

	private void InitializePlayer(String name) {
		localPlayer = new LocalPlayer(this, Utility.getRandomPoint(0, FIELD_SIZE), PLAYER_START_SIZE, Color.blue, name);
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
		while (players.size() < AI_PLAYER_COUNT) {
			SpawnPlayer();
		}

		// Spawns Food if necessary
		while (food.size() < FOOD_COUNT) {
			SpawnFood();
		}

	}

	private void RunGameCycle() {
		long timePassedSinceLastUpdate = System.currentTimeMillis() - lastUpdateTime;

		// One Game Cycle
		SpawnObjects();

		// Every player can move one step
		for (Player p : players) {
			p.getBehavior().update(timePassedSinceLastUpdate);
		}

		// Deletes removed circles
		for (Circle c : circlesToDelete) {
			players.remove(c);
			food.remove(c);
			// System.out.println("Deleted Circle!");
		}
		circlesToDelete.clear();

		lastUpdateTime = System.currentTimeMillis();

	}

	public ArrayList<Circle> getAllComponents() {
		ArrayList<Circle> comp = new ArrayList<Circle>();
		comp.addAll(food);
		comp.addAll(players);
		return comp;
	}

	public void deleteCircle(Circle c1) {
		
		//Local Player eaten -> game over
		if(c1 == localPlayer){
			
			gameOver(new Score(localPlayer.getSize(), login.getName(), login.getPassword()));
		}
		circlesToDelete.add(c1);
	}

	private void gameOver(Score score) {
		stopGame();
		menuController.showDeathMenu(score);
		try {
			dbAdapter.insert(score);
		} catch (SQLException e) {
			menuController.showConnectionError();
		} catch (InvalidPasswordException e) {
			//Should not happen since the password has been confirmed in the beginning
			e.printStackTrace();
		}
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

	public Vector getMouseVector() {
		return window.getMouseVector();
	}

	/**
	 * Get the best players in the current game
	 * 
	 * @return An array of the 5 (or less) best players in the game
	 */

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

		Score[] bestplayers = new Score[Math.min(5, players.size())];

		// Convert players to Scores
		for (int i = 0; i < bestplayers.length; i++) {
			bestplayers[i] = new Score(players.get(i).getSize(), players.get(i).getName(), "baum");
		}

		return bestplayers;
	}

	/**
	 * Get the best players of all time from the database
	 * 
	 * @return the 5 or less best players from the database
	 */
	public Score[] getGlobalHighscores() {
		try {
			return dbAdapter.getHighscores();
		} catch (SQLException e) {
			// TODO handle errors properly
			return null;
		}
	}

	/**
	 * @return the pLAYER_START_SIZE
	 */
	public int getPLAYER_START_SIZE() {
		return PLAYER_START_SIZE;
	}

	/**
	 * @return the vIEWRANGE
	 */
	public int getVIEWRANGE() {
		return VIEWRANGE;
	}

	/**
	 * @return the aI_PLAYER_COUNT
	 */
	public int getAI_PLAYER_COUNT() {
		return AI_PLAYER_COUNT;
	}

	/**
	 * @return the length and width of the game field
	 */
	public int getFIELD_SIZE() {
		return FIELD_SIZE;
	}

	/**
	 * @return the mOVEMENT_SPEED
	 */
	public double getMOVEMENT_SPEED() {
		return MOVEMENT_SPEED;

	}

	/**
	 * Stop the game, close Window & DB connection, then exit
	 */
	public void quit() {
		if (currentState != GameState.Stopped) {
			stopGame();
		}
		dbAdapter.disconnect();
		window.dispose();
		System.exit(0);
	}

	public void start() {
		menuController.showNameMenu();
	}

	@Override
	public void windowClosed() {
		switch (currentState) {
		case Playing:
			pauseGame();//Pause the game if playing
		case Paused:
			//Ask user if he really wants to quit
			int sure = menuController.confirmQuit();
			if (sure == 0) {
				quit();
			}
			break;
		case Stopped:
			//Just quit
			quit();
			break;
	
		}
	}

	@Override
	public void windowMinimized() {
		if (currentState == GameState.Playing) {
			pauseGame();
		}

	}
}
