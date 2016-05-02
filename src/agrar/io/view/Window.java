package agrar.io.view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import agrar.io.controller.Controller;

public class Window extends JFrame{

	private static final long serialVersionUID = -2967168291846161682L;
	private Controller parent;
	private View view;
	
	public Window(Controller c1){
		super();
		parent = c1;
		view = new View(parent);
		this.add(view);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
		this.setTitle("agrar.io - (c) Florian Baader, Matthias Weirich");
		this.setVisible(true);
		
	}
	
	public void refresh(){
		view.repaint();
	}
	
	
	
}


