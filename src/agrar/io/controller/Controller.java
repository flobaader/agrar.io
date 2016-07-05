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
import agrar.io.model.AIPlayer;
import agrar.io.model.Circle;
import agrar.io.model.Food;
import agrar.io.model.LocalPlayer;
import agrar.io.model.Player;
import agrar.io.model.Score;
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
	public enum GameState {
		Playing, Paused, Stopped
	}

	// Game Statics
	/**
	 * The size of a player when he starts
	 */
	public static final int PLAYER_START_SIZE = 5000;
	/**
	 * The size of food
	 */
	public static final int FOOD_SIZE = 200;

	/**
	 * The view range for AI players
	 */
	public static final int VIEWRANGE = 500;
	/**
	 * Maximum amount of AI players
	 */
	public static final int AI_PLAYER_COUNT = 10;
	/**
	 * Maximum amount of Food
	 */
	public static final int FOOD_COUNT = 200;
	/**
	 * Size of the game arena
	 */
	public static final int FIELD_SIZE = 2000;
	/**
	 * The timer rate in milliseconds, which refreshes the View
	 */
	public static final int VIEW_REFRESH_RATE = 1;
	/**
	 * The timer rate of the game cycle
	 */
	public static final int GAME_REFERSH_RATE = 1;
	/**
	 * Movement speed factor
	 */
	public static final float MOVEMENT_SPEED = 0.4F;
	// Game Window
	private GameWindow window;
	// Timers
	private Timer gameRate;

	private Timer graphicsRate;
	// Lists of Game Objects
	private ArrayList<Player> players;
	private ArrayList<Food> food;
	private ArrayList<Circle> circlesToDelete = new ArrayList<Circle>();

	/**
	 * Referecen to the local player
	 */
	private LocalPlayer localPlayer;

	/**
	 * Adapter to the Database
	 */
	private DatabaseAdapter dbAdapter = new DatabaseAdapter();

	private long lastUpdateTime;

	/**
	 * The current State of the Game
	 */
	private GameState currentState = GameState.Stopped;

	/**
	 * If true, no scores are saved in the database
	 */
	private boolean playOffline;

	/**
	 * If true, AI behavior is displayed in the view
	 */
	private boolean inDebugMode;

	// Score that contains player name and password
	private Score login;

	private MenuController menuController;

	private Score[] globalHighscores;

	public Controller() {
		players = new ArrayList<Player>();
		food = new ArrayList<Food>();
		circlesToDelete = new ArrayList<Circle>();
		window = new GameWindow(this);

		menuController = new MenuController(this, window.getMenuView(), window);
		menuController.showMainMenu();
	}

	/**
	 * The Circles, which are requested to be removed, are saved in a list and
	 * will now be removed from the other lists
	 */
	private void clearDeletedCircles() {
		// Iterates through all saved circles
		for (Circle c : circlesToDelete) {

			// .remove Command only removes if the object exists

			// If the Circle is in of these lists he gets removed
			players.remove(c);
			food.remove(c);
		}
		// Clears list
		circlesToDelete.clear();

	}

	/**
	 * Requests to delete the selected circle. The circle will be stored in a
	 * list and removed after the whole game cycle
	 * 
	 * @param c1
	 *            The circle to be deleted
	 */
	public void deleteCircle(Circle c1) {

		// Local Player eaten -> game over
		if (c1 == localPlayer) {
			gameOver(new Score(localPlayer.getScore(), login.getName(), login.getPassword()));
		}
		circlesToDelete.add(c1);
	}

	private void gameOver(Score score) {
		stopGame();
		menuController.showDeathMenu(score);

		if (!playOffline) {
			// Inserts Score to database if not offline
			try {
				dbAdapter.insert(score);
			} catch (SQLException e) {
				e.printStackTrace();
				menuController.showConnectionError(e);
			} catch (InvalidPasswordException e) {
				// Should not happen since the password has been confirmed in
				// the
				// beginning
				e.printStackTrace();
			}
		}
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

	public DatabaseAdapter getDatabaseAdapter() {
		if (dbAdapter == null) {
			dbAdapter = new DatabaseAdapter();
		}
		return dbAdapter;
	}

	/**
	 * Get the best players of all time from the database
	 * 
	 * @return the 5 or less best players from the database
	 */
	public Score[] getGlobalHighscores() {
		return globalHighscores;
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

	public Player getLocalPlayer() {
		return localPlayer;
	}

	public int getLocalPlayerScore() {
		return localPlayer.getScore();
	}

	/**
	 * 
	 * @return The vector from the center of the screen to the mouse
	 */
	public Vector getMouseVector() {
		return window.getMouseVector();
	}

	/**
	 * Returns all objects which are in the view range of the circle
	 * 
	 * @param c1
	 *            The circle, which looks for other Objects
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

	/**
	 * 
	 * @return Returns the center of the screen as a vector
	 */
	public Vector getOffset() {
		return new Vector(window.getSize().getWidth() / 2, window.getSize().getHeight() / 2);
	}

	public GameState getState() {
		return currentState;
	}

	/**
	 * Spawns local Player
	 */
	private void initializeLocalPlayer(String name) {
		localPlayer = new LocalPlayer(this, Utility.getRandomPoint(0, FIELD_SIZE), PLAYER_START_SIZE, Color.blue, name);
		players.add(localPlayer);
	}

	/**
	 * Pause the game (to display menu etc.)
	 */
	public void pauseGame() {

		// Stop graphics and simulation cycle
		gameRate.stop();
		graphicsRate.stop();

		// show menu
		menuController.showPauseMenu();

		currentState = GameState.Paused;// switch state last

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

	/**
	 * resumes a paused game. Throws illegalStateException if the GameState is
	 * not paused
	 */
	public void resumeGame() {

		// Only a paused game can be resumed
		if (currentState != GameState.Paused) {
			throw new IllegalStateException("You can only resume a paused game!");
		}

		// restart the timers
		graphicsRate.restart();
		gameRate.start();

		// Hide menu, show game arena
		window.hideMenu();

		// switch to playing state last
		currentState = GameState.Playing;
	}

	/**
	 * Simulates one Game Cycle, where every Player can move one time and eat
	 * other Players and objects are spawned and deleted
	 */
	private void runGameCycle() {

		// The time the last circle took
		long millisSinceLastUpdate = System.currentTimeMillis() - lastUpdateTime;

		// Spawn AI Players and Food if necessary
		spawnAllGameObjects();

		// Every player can move one step
		for (Player p : players) {
			p.getBehavior().update(millisSinceLastUpdate);
		}

		// Deletes eaten Circles
		clearDeletedCircles();

		// Sets update Time
		lastUpdateTime = System.currentTimeMillis();

	}

	/**
	 * Spawns one Player
	 */
	private void spawnAIPlayer() {
		// Count AIPlayers

		int level = Utility.getRandom(0, 10);
		
		
		String name;
		//Uses random name from database if connected		
		if(!isOffline()){
			name = dbAdapter.getRandomPlayerName();
		}else{
			name = "AI_Player";
		}
		
		AIPlayer p = new AIPlayer(this, Utility.getRandomPoint(0, FIELD_SIZE), PLAYER_START_SIZE,
				Utility.getRandomColor(), name , level);
		players.add(p);
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
	 * Spawns one food circle
	 */
	private void spawnFood() {
		Food f = new Food(this, Utility.getRandomPoint(0, FIELD_SIZE), FOOD_SIZE, Utility.getRandomColor());

		food.add(f);
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

		// update highscore list

		try {
			globalHighscores = dbAdapter.getHighscores();
		} catch (SQLException e) {
		}

		// Save name & password for later
		this.login = s;

		// Display the game arena
		window.hideMenu();

		// Sets First Update Time
		lastUpdateTime = System.currentTimeMillis();

		// Loads Players
		initializeLocalPlayer(s.getName());

		// Spawns Food and AI
		spawnAllGameObjects();

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
				runGameCycle();
			}
		});

		// Start game timers
		gameRate.start();
		graphicsRate.start();

		// Switch to playing state
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

	@Override
	public void windowClosed() {
		switch (currentState) {
		case Playing:
			pauseGame();// Pause the game if playing
		case Paused:
			// Ask user if he really wants to quit
			int sure = menuController.confirmQuit();
			if (sure == 0) {
				quit();
			}
			break;
		case Stopped:
			// Just quit
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

	public void setPlayOffline(boolean status) {
		playOffline = status;
	}

	public boolean isOffline() {
		return playOffline;
	}

	public void SpacebarPressed() {
		// activates Boost
		localPlayer.getBehavior().activateBoost();
	}

	public boolean isInDebugMode() {
		return inDebugMode;
	}

	public void setDebugMode(boolean mode) {
		inDebugMode = mode;
	}
}
