package agrar.io.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import agrar.io.controller.Controller;
import agrar.io.controller.Controller.GameState;
import agrar.io.util.Vector;

public class GameWindow extends JFrame {

	private static final long serialVersionUID = -2967168291846161682L;
	private Controller controller;
	private GameView gameView;
	private MenuView menuView;
	private JPanel cardPanel;

	private final static String MENU = "menu", GAME = "game";

	// Mouse Location
	private Vector mouseMovement;
	private CardLayout cl;

	public GameWindow(Controller c) {

		super();

		controller = c;

		cardPanel = new JPanel();

		cl = new CardLayout();
		cardPanel.setLayout(cl);
		this.getContentPane().add(cardPanel);

		gameView = new GameView(controller);
		menuView = new MenuView();

		cardPanel.add(gameView, GAME);
		cardPanel.add(menuView, MENU);

		cl.show(cardPanel, GAME);

		this.setExtendedState(MAXIMIZED_BOTH);
		this.setTitle("agrar.io - (c) Florian Baader, Matthias Weirich");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		gameView.setMinimumSize(new Dimension(640, 460));
		this.setMinimumSize(new Dimension(640,460));
		
		// Report all keystrokes to the controller
		gameView.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
				System.out.println("keypress");
				
				//pause the game when the escape key is pressed
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
					if(controller.getState() == GameState.Playing){
						controller.pauseGame();
					}
				}
			}
		});
		
		// Report changes to the window state to the controller
		// Only actions that hide / close the window need to be reported, the
		// player can resume the game himself
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// ignore
			}

			@Override
			public void windowIconified(WindowEvent e) {
				controller.windowMinimized();
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// ignore
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				controller.windowMinimized();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				controller.windowClosed();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// ignore
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// ignore
			}
		});

		//Listen to all mouse movements
		gameView.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				updateMousePosition(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				updateMousePosition(e);
			}
		});

	}

	/**
	 * Recalculates the vector from the center of the frame to the mouse
	 * whenever the mouse is moved or dragged
	 * 
	 * @param e
	 *            the corresponding MouseEvent
	 */
	private void updateMousePosition(MouseEvent e) {
		// Subtracts the half Screen Size of the Vector to get a
		// normalized vector, which start point is located at the center
		// of the screen
		mouseMovement = new Vector(e.getX() - gameView.getWidth() / 2, e.getY() - gameView.getHeight() / 2);
	}

	/**
	 * Called by the controller to update and redraw the View
	 */
	public void refresh() {
		gameView.repaint();
	}

	public GameView getView() {
		return gameView;
	}

	public void showMenu() {
		cl.show(cardPanel, MENU);
		menuView.doLayout();
		cardPanel.doLayout();
		menuView.requestFocus();
	}

	public void hideMenu() {
		cl.show(cardPanel, GAME);
		gameView.requestFocus();
		cardPanel.repaint();
	}

	public Vector getMouseVector() {
		return mouseMovement;
	}

	public MenuView getMenuView() {
		return menuView;
	}

	public interface GameWindowListener {

		public void windowClosed();

		public void windowMinimized();
	}

}
