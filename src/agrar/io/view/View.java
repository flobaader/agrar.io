package agrar.io.view;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import agrar.io.controller.Controller;
import agrar.io.controller.Player;
import agrar.io.model.Circle;
import sun.font.FontManager;

public class View extends JPanel {
	private static final long serialVersionUID = 2126792581772053659L;
	private long lastPaint = 0;
	private double FPS;

	private Controller controller;
	private Point offset;

	public View(Controller p) {
		controller = p;
		offset = new Point();
	}

	@Override
	public void paint(Graphics g) {

		// Setup drawing canvas
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());

		calcOffsets(controller.getLocalPlayer());

		// Draw Background
		paintGrid(g2d);

		// Draw circles
		for (Circle c : controller.getAllComponents()) {

			paintCircle(g2d, c);

			// only players have a name
			if (c.isPlayer()) {
				paintName(g2d, c);
			}
			// TODO: view size?
		}

		// Draw HUD
		g2d.drawString("FPS: " + FPS, 50, 50);

		// Recalc fps
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
	 * 
	 * @param g
	 *            The graphics to draw with
	 * @param offsetX
	 *            The offset in x direction
	 * @param offsetY
	 *            The offset in y direction
	 */
	private void paintGrid(Graphics2D g) {
		g.setColor(Color.GRAY);

		int offsetX = (offset.x * -1) % 40;
		int offsetY = (offset.y * -1) % 40;

		for (int x = offsetX; x < this.getWidth(); x += 40) {
			g.drawLine(x, 0, x, this.getHeight());
		}
		for (int y = offsetY; y < this.getHeight(); y += 40) {
			g.drawLine(0, y, this.getWidth(), y);
		}
	}

	/**
	 * Paints the name of a Circle above it
	 * @param c the circle to paint the name of
	 * @param g Graphics used for drawing
	 */
	private void paintName(Graphics2D g, Circle c){
		
		//Get FontMetrics
		FontMetrics metrics = g.getFontMetrics();
		
		String name = ((Player)c).getName();
		Point playerLocation = c.getLocation();
		float width = metrics.stringWidth(name);
		
		int x = playerLocation.x - (int)(((float) width) /2F) - offset.x;
		int y = (int) (playerLocation.y - c.getRadius() - 10 - offset.y);
		
		g.drawString(name, x, y);
		
	}

	/**
	 * Paints a circle
	 * 
	 * @param g
	 *            Graphics to paint with
	 * @param c
	 *            Circle to be painted
	 */
	private void paintCircle(Graphics2D g, Circle c) {
		g.setColor(c.getColor());
		int radius = (int) c.getRadius();
		int x = c.getLocation().x;
		int y = c.getLocation().y;
		g.fillOval(x - radius - offset.x, y - radius - offset.y, radius * 2, radius * 2);
	}

	/**
	 * Calculates the offsets used for drawing
	 * 
	 * @param player
	 *            Circle that represents local player
	 */
	private void calcOffsets(Circle player) {
		Point playerOffset = controller.getLocalPlayer().getLocation();
		offset.x = playerOffset.x - this.getWidth() / 2;
		offset.y = playerOffset.y - this.getHeight() / 2;
	}

}
