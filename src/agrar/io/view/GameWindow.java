package agrar.io.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

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
		
		//Report any window state changes to the controller
		GameWindowListener listener = new GameWindowListener();
		this.addWindowListener(listener);
		this.addWindowFocusListener(listener);
		this.addWindowStateListener(listener);
		
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

	/**
	 * Implements all three Window interfaces, similar to WindowAdapter. Reports any (important) changes in the window's state to the controller.
	 * @author Matthias
	 *
	 */
	private class GameWindowListener implements WindowListener, WindowFocusListener, WindowStateListener {

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			System.exit(0);
		}

		@Override
		public void windowClosing(WindowEvent e) {
			controller.onStop();
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			controller.onPause();
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			controller.onResume();
		}

		@Override
		public void windowIconified(WindowEvent e) {
			controller.onPause();	
		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowStateChanged(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowGainedFocus(WindowEvent arg0) {
			controller.onResume();
		}

		@Override
		public void windowLostFocus(WindowEvent arg0) {
			controller.onPause();
		}
		

			}
}
