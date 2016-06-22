package agrar.io.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import javax.swing.JFrame;

import agrar.io.controller.Controller;

/**
 * JFrame that hosts a single {@link agrar.io.view.View View} panel and listens for mouse and keyboard input.
 * @author Matthias
 *
 */
public class GameWindow extends JFrame {

	private static final long serialVersionUID = -2967168291846161682L;
	private Controller controller;
	private View view;

	public GameWindow(Controller c) {
		super();
		controller = c;
		view = new View(controller);
		this.add(view);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setTitle("agrar.io - (c) Florian Baader, Matthias Weirich");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//Report all keystrokes to the controller
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		
	}

	/**
	 * Called by the controller to update and redraw the View
	 */
	public void refresh() {
		view.repaint();
	}

}
