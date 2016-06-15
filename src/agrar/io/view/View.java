package agrar.io.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import agrar.io.Vector;
import agrar.io.controller.Controller;
import agrar.io.controller.Player;
import agrar.io.model.Circle;

public class View extends JPanel {
	private static final long serialVersionUID = 2126792581772053659L;
	private long lastPaint = 0;
	private double FPS;

	private Controller controller;
	private Point offset;

	public View(Controller p) {
		controller = p;
		offset = new Point();

		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				Vector v = new Vector(e.getX() - getWidth() / 2, e.getY() - getHeight() / 2);
				controller.updateMovementVector(v);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
	}

	@Override
	public void paint(Graphics g) {

		// Setup drawing canvas
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());

		calcOffsets(controller.getLocalPlayer());

		//TODO: scale view to fit circle
		
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
		g2d.setColor(Color.BLACK);
		g2d.drawString("FPS: " + FPS, 50, 50);
		drawPlayerScore(g2d, controller.getLocalPlayer());

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
	 * 
	 * @param c
	 *            the circle to paint the name of
	 * @param g
	 *            Graphics used for drawing
	 */
	private void paintName(Graphics2D g, Circle c) {
		// Measure String first to draw it centered above the player
		String name = ((Player) c).getName();
		Vector playerLocation = c.getLocation();
		float width = measureString(g, name);

		int x = (int) (playerLocation.getX() - (int) (((float) width) / 2F) - offset.x);
		int y = (int) (playerLocation.getY() - c.getRadius() - 10 - offset.y);

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
		int x = (int) c.getLocation().getX();
		int y = (int) c.getLocation().getY();
		g.fillOval(x - radius - offset.x, y - radius - offset.y, radius * 2, radius * 2);
	}

	/**
	 * Calculates the offsets used for drawing
	 * 
	 * @param player
	 *            Circle that represents local player
	 */
	private void calcOffsets(Circle player) {
		Vector playerOffset = controller.getLocalPlayer().getLocation();
		offset.x = (int) (playerOffset.getX() - this.getWidth() / 2);
		offset.y = (int) (playerOffset.getY() - this.getHeight() / 2);
	}

	/**
	 * Draw the score of the local player in the bottom left corner
	 * 
	 * @param player
	 *            the local player
	 */
	private void drawPlayerScore(Graphics2D g, Circle player) {
		String score = String.valueOf(player.getSize());
		g.setFont(new Font("Arial", Font.BOLD, 25));

		int width = measureString(g, score);
		
		g.drawString(score, getWidth()-(width+10), getHeight()-35);
	}

	/**
	 * @param g
	 *            the graphics to measure the string with
	 * @param s
	 *            the string to measure
	 * @return the width of the string in pixels
	 */
	private int measureString(Graphics2D g, String s) {
		FontMetrics metrics = g.getFontMetrics();
		return metrics.stringWidth(s);

	}
}
