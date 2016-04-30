package agrar.io;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Controller {
	private ArrayList<Circle> components;
	private Player local_player;
	private static int view_range;
	private static int KI_Players;
	private Timer GameRate;
	private View view;
	
	
	public Controller(){
		components = new ArrayList<Circle>();
		view = new View(this);
	}
	
	public void StartGame(){
		//Loads Players
		InitializePlayers();
		
		//Starts Game Refresh Timer
		GameRate = new Timer(100, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				RunGameCycle();
			}
		});
		GameRate.start();
		
		//Starts view
		view.StartView();
		
	}
	
	private void InitializePlayers(){
		components.add(new LocalPlayer(this, new Point(0,0), 100, Color.blue ));
	}
	
	private void RunGameCycle(){
		for(Circle c: components){
			if(c.isPlayer()){
				Player p = (Player)c;
				p.moveToNewPosition();
			}
		}
		
	}
	
	public ArrayList<Circle> getNearObjects(Circle c){
		ArrayList<Circle> returntargets = new ArrayList<Circle>();
		
		for(Circle target: components){
			if(Utility.getDistance(c, target) < view_range){
				returntargets.add(target);
			}
		}
		
		return returntargets;
	}
	
	public ArrayList<Circle> getAllComponents(){
		return components;
	}
	
	public void getMousePosition(){
		
		
	}
	
	
	
}
