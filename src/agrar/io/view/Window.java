package agrar.io.view;

import javax.swing.JFrame;

import agrar.io.controller.Controller;

public class Window extends JFrame {

	private static final long serialVersionUID = -2967168291846161682L;
	private Controller parent;
	private View view;

	public Window(Controller c1) {
		super();
		parent = c1;
		view = new View(parent);
		this.add(view);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("agrar.io - (c) Florian Baader, Matthias Weirich");
		this.setVisible(true);
		this.addWindowListener(new ViewWindowListener(parent));
	}

	public void refresh() {
		view.repaint();
	}

}
