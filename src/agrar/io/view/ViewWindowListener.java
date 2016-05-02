package agrar.io.view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import agrar.io.interfaces.GameStateListener;

/**
 * A WindowListener that rewrites window events to game state changes
 * 
 * @author Matthias
 *
 */
public class ViewWindowListener implements WindowListener {

	private GameStateListener listener;

	/**
	 * 
	 * @param l
	 *            The GameStateListener to redirect the state changes to
	 */
	public ViewWindowListener(GameStateListener l) {
		this.listener = l;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		System.out.println("windowActivated");
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		System.out.println("windowClosed");
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		System.out.println("windowClosing");

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		System.out.println("windowDeactivated");

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		System.out.println("windowDeiconified");

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		System.out.println("windowIconified");

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		System.out.println("windowOpened");

	}

}
