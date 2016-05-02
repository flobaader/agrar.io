package agrar.io;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.RenderingHints;

public class View extends Panel {
	private static final long serialVersionUID = 2126792581772053659L;
	private long lastPaint = 0;
	private double FPS;

	private Controller parent;

	public View(Controller p) {
		parent = p;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());

		g2d.drawString("FPS: " + FPS, 50, 50);
		for (Circle c : parent.getAllComponents()) {

			g2d.setColor(c.getColor());
			int radius = c.getSize();
			int x = c.getLocation().x;
			int y = c.getLocation().y;
			g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
			
			// TODO: Display name and size
		}



		int millis_passed = (int) (System.currentTimeMillis() - lastPaint);
		if (millis_passed != 0) {
			FPS = 1000 / millis_passed;
		} else {
			FPS = 1000;
		}
		lastPaint = System.currentTimeMillis();

	}

}
