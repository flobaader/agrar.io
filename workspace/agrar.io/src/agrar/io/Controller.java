package agrar.io;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Controller {
	private ArrayList<Circle> components;
	private ArrayList<Circle> CirclesToDelete = new ArrayList<Circle>();
	private LocalPlayer local_player;
	private static int view_range;
	private static int KI_Players = 10;
	private Timer GameRate;
	private Timer GraphicsRate;
	private Timer FoodSpawner;
	private Window window;

	public Controller() {
		components = new ArrayList<Circle>();
		window = new Window(this);
	}

	public void StartGame() {
		// Loads Players
		InitializePlayers();

		GraphicsRate = new Timer(30, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.refresh();
			}
		});
		
		GraphicsRate.start();

		// Starts Game Refresh Timer
		GameRate = new Timer(5, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RunGameCycle();
			}
		});
		GameRate.start();

		FoodSpawner = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				SpawnFood();
			}

		});
		FoodSpawner.start();

	}

	private void SpawnFood() {
		Food f = new Food(this, Utility.getRandomPoint(0, 2000), 20, Utility.getRandomColor());
		components.add(f);
	}

	private void InitializePlayers() {
		local_player = new LocalPlayer(this, new Point(0, 0), 50, Color.blue, "LocalPlayer");
		components.add(local_player);
	}
	
	public int getLocalPlayerScore(){
		return local_player.getSize();
	}

	private void RunGameCycle() {
		for (Circle c : components) {
			if (c.isPlayer()) {
				Player p = (Player) c;
				p.moveToNewPosition();
			}
		}
		
		//Deletes removed circles
		for(Circle c: CirclesToDelete){
			components.remove(c);
		}
		CirclesToDelete.clear();
	}

	public ArrayList<Circle> getNearObjects(Circle c) {
		ArrayList<Circle> returntargets = new ArrayList<Circle>();

		for (Circle target : components) {
			if (Utility.getDistance(c, target) < view_range) {
				returntargets.add(target);
			}
		}

		return returntargets;
	}

	public ArrayList<Circle> getAllComponents() {
		return components;
	}

	public void deleteCircle(Circle c1) {
		CirclesToDelete.add(c1);
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
}
