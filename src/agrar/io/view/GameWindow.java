package agrar.io.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import agrar.io.controller.Controller;
import agrar.io.model.menu.Menu;
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
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setMinimumSize(new Dimension(640, 460));

		// Report all keystrokes to the controller
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

		cardPanel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// Subtracts the half Screen Size of the Vector to get a
				// normalized vector, which start point is located at the center
				// of the screen
				mouseMovement = new Vector(e.getX() - getWidth() / 2, e.getY() - getHeight() / 2);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});

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

	public void showMenu(Menu nameMenu) {
		cl.show(cardPanel, MENU);
	}
	
	public void hideMenu(){
		cl.show(cardPanel, GAME);
	}

	public Vector getMouseVector() {
		return mouseMovement;
	}

}
