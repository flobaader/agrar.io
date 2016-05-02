package agrar.io.controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import agrar.io.interfaces.GameStateListener;
import agrar.io.model.Circle;
import agrar.io.model.Food;
import agrar.io.view.Window;

public class Controller implements GameStateListener{
	private ArrayList<Circle> components;
	private ArrayList<Circle> circlesToDelete = new ArrayList<Circle>();
	private LocalPlayer localPlayer;
	private static int VIEW_RANGE;
	private static int KI_PLAYERS = 10;
	private Timer gameRate;
	private Timer graphicsRate;
	private Timer foodSpawner;
	private Window window;

	public Controller() {
		components = new ArrayList<Circle>();
		window = new Window(this);
	}

	public void StartGame() {
		// Loads Players
		InitializePlayers();

		graphicsRate = new Timer(16, new ActionListener() { //TODO: performance test, set back to 30

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

		foodSpawner = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				SpawnFood();
			}

		});
		foodSpawner.start();

	}

	private void SpawnFood() {
		Food f = new Food(this, Utility.getRandomPoint(0, 2000), 200, Utility.getRandomColor());
		components.add(f);
	}

	private void InitializePlayers() {
		localPlayer = new LocalPlayer(this, new Point(0, 0), 500, Color.blue, "LocalPlayer");
		components.add(localPlayer);
		
		for(int x = 0; x < 10; x++){
			components.add(new KIPlayer(this, Utility.getRandomPoint(0, 1000), 500, Utility.getRandomColor(), "KI-Player"));
		}
	}
	
	public int getLocalPlayerScore(){
		return localPlayer.getSize();
	}
	
	public Circle getLocalPlayer(){
		return localPlayer;
	}

	private void RunGameCycle() {
		for (Circle c : components) {
			if (c.isPlayer()) {
				Player p = (Player) c;
				p.moveToNewPosition();
			}
		}
		
		//Deletes removed circles
		for(Circle c: circlesToDelete){
			components.remove(c);
		}
		circlesToDelete.clear();
	}

	public ArrayList<Circle> getNearObjects(Circle c) {
		ArrayList<Circle> returntargets = new ArrayList<Circle>();

		for (Circle target : components) {
			if (Utility.getDistance(c, target) < VIEW_RANGE) {
				returntargets.add(target);
			}
		}

		return returntargets;
	}

	public ArrayList<Circle> getAllComponents() {
		return components;
	}

	public void deleteCircle(Circle c1) {
		circlesToDelete.add(c1);
	}

	public Player getNearestPlayer(Circle c1) {
		Player p1 = null;
		int nearest_distance = Integer.MAX_VALUE;
		for (Circle c : components) {
			if (c.isPlayer()) {
				if (Utility.getDistance(c1, c) < nearest_distance) {
					nearest_distance = (int) Utility.getDistance(c1, c);
					p1 = (Player) c;
				}
			}
		}
		return p1;

	}

	public Circle getNearestObject(Circle c1) {
		Circle c2 = null;
		int nearest_distance = Integer.MAX_VALUE;
		for (Circle c : components) {
			if(c != c1){
				if (Utility.getDistance(c1, c) < nearest_distance) {
					nearest_distance = (int) Utility.getDistance(c1, c);
					c2 = c;
				}	
			}
			
		}
		return c2;

	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}
}
