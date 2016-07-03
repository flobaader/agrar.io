package agrar.io;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class Test extends JFrame implements WindowListener {

	public Test() throws HeadlessException {
		this.setSize(new Dimension(400, 400));
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		addWindowListener(this);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		System.out.println("activated");
	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.out.println("closed");
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("closing");
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		System.out.println("deactivated");
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		System.out.println("deiconified");
	}

	@Override
	public void windowIconified(WindowEvent e) {
		System.out.println("iconified");
	}

	@Override
	public void windowOpened(WindowEvent e) {
		System.out.println("opened");
	}

	public static void main(String[] args) {
		new Test();
	}
	
}
