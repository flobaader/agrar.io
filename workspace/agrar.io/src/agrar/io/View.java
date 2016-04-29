package agrar.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

public class View {
	private JFrame frame;
	private Controller parent;
	private Timer timerRefresh;
	
	
	public View(Controller p){
		parent = p;
		frame = new JFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setTitle("agrar.io - (c) Florian Baader, Matthias Weirich");
		frame.setVisible(true);
	}
	
	public void StartView(){
		timerRefresh = new Timer(10, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				//Draws Circles
				drawComponents();
			}
		});
		timerRefresh.start();
	}
	
	public void StopView(){
		timerRefresh.stop();
		frame.setVisible(false);
	}
	
		
	public void drawComponents(){
		for( Circle c: parent.getAllComponents()){
			//TODO: Draw Circle
		}
		
	}
	
	
	
}
