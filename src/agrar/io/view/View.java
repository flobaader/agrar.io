package agrar.io.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import agrar.io.vector;
import agrar.io.controller.Controller;
import agrar.io.model.Circle;

public class View extends JPanel {
	private static final long serialVersionUID = 2126792581772053659L;
	private long lastPaint = 0;
	private double FPS;

	private Controller controller;
	private Circle localPlayer;

	
	public View(Controller p) {
		controller = p;
		localPlayer = p.getLocalPlayer();
	
	}

	@Override
	public void paint(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());

		Point offset = vector.vectorToPoint(controller.getLocalPlayer().getLocation());
		int offsetX = offset.x - this.getWidth()/2;
		int offsetY = offset.y - this.getHeight()/2;
		
		paintGrid(g2d, Math.abs(offsetX), Math.abs(offsetY));
		
		g2d.drawString("FPS: " + FPS, 50, 50);
		for (Circle c : controller.getAllComponents()) {

			g2d.setColor(c.getColor());
			int radius = (int) c.getRadius();
			int x = c.getLocation().getRoundedX();
			int y = c.getLocation().getRoundedY();
			g2d.fillOval(x - radius - offsetX, y - radius- offsetY, radius * 2, radius * 2 );

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
	
	/**
	 * Draws the grid for the game arena with a given offset
	 * @param g The graphics to draw with
	 * @param offsetX The offset in x direction
	 * @param offsetY The offset in y direction
	 */
	private void paintGrid(Graphics2D g, int offsetX, int offsetY){
		g.setColor(Color.GRAY);
		
		offsetX =  (offsetX % 20);
		offsetY = (offsetY % 20);
		
		for(int x = offsetX; x < this.getWidth(); x += 20){
			g.drawLine(x, 0, x, this.getHeight());
		}
		for(int y = offsetX; y < this.getHeight(); y += 20){
			g.drawLine(0, y, this.getWidth(), y);
		}
	}

}
