package agrar.io.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import agrar.io.controller.Controller;
import agrar.io.model.Circle;
import agrar.io.model.Player;
import agrar.io.util.Vector;

public class View extends JPanel {
	private static final long serialVersionUID = 2126792581772053659L;
	private long lastPaint = 0;
	private double FPS;

	private Controller controller;
	private Point offset;
	private BufferedImage scoreBackground;
	private Rectangle scoreBackgroundSize;

	private boolean showFPS = true;

	public View(Controller p) {
		controller = p;
		offset = new Point();

		scoreBackgroundSize = new Rectangle();
		scoreBackgroundSize.height = 40;

		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				Vector v = new Vector(e.getX() - getWidth() / 2, e.getY() - getHeight() / 2);
				// controller.updateMovementVector(v);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});

		try {
			scoreBackground = ImageIO.read(View.class.getResource("/bottom_left.png"));
		} catch (IOException e1) {
		}
	}

	@Override
	public void paint(Graphics g) {

		// Setup drawing canvas
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());

		calcOffsets(controller.getLocalPlayer());

		// TODO: scale view to fit circle

		drawGrid(g2d);

		// Draw circles
		for (Circle c : controller.getAllComponents()) {

			drawCircle(g2d, c);

			// only players have a name
			if (c.isPlayer()) {
				drawName(g2d, c);
			}
		}

		drawHUD(g2d);

	}

	/**
	 * Draws the HUD over the GameArena
	 * 
	 * @param g
	 *            GraphicsD for drawing
	 */
	private void drawHUD(Graphics2D g) {
		drawPlayerScore(g, controller.getLocalPlayer());
		drawFPS(g);
	}

	private void drawArena(Graphics2D g){
		
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
	private void drawGrid(Graphics2D g) {
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
	private void drawName(Graphics2D g, Circle c) {
		// Measure String first to draw it centered above the player
		String name = ((Player) c).getName();
		Vector playerLocation = c.getLocation();
		Dimension d = measureString(g, name);

		int x = (int) (playerLocation.getX() - (int) (((float) d.width) / 2F) - offset.x);
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
	private void drawCircle(Graphics2D g, Circle c) {
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
	 * @param localPlayer
	 *            the local player
	 */
	private void drawPlayerScore(Graphics2D g, Player localPlayer) {

		String score = String.valueOf(localPlayer.getSize());

		g.setFont(new Font("Arial", Font.BOLD, 25));
		Dimension stringSize = measureString(g, score);

		scoreBackgroundSize.height = (int) ((stringSize.height + 20) * 1.25);
		scoreBackgroundSize.y = (int) (this.getHeight() - scoreBackgroundSize.getHeight());
		scoreBackgroundSize.width = (int) ((stringSize.width + 30) * 1.0833);
		scoreBackgroundSize.x = (int) (this.getWidth() - scoreBackgroundSize.getWidth());

		drawImage(g, scoreBackground, scoreBackgroundSize);
		g.drawString(score, getWidth() - (stringSize.width + 10), (int) (getHeight() - 10));

	}

	/**
	 * Draws the fps counter in the top left corner if drawFPS is set to true
	 * 
	 * @param g2d
	 */
	private void drawFPS(Graphics2D g2d) {
		if (showFPS) {
			int millis_passed = (int) (System.currentTimeMillis() - lastPaint);
			if (millis_passed != 0) {
				FPS = 1000 / millis_passed;
			} else {
				FPS = 1000;
			}
			lastPaint = System.currentTimeMillis();

			g2d.setColor(Color.BLACK);
			g2d.drawString("FPS: " + FPS, 50, 50);
		}
	}

	/**
	 * @param g
	 *            the graphics to measure the string with
	 * @param s
	 *            the string to measure
	 * @return the width of the string in pixels
	 */
	private static Dimension measureString(Graphics2D g, String s) {
		FontMetrics metrics = g.getFontMetrics();
		return new Dimension(metrics.stringWidth(s), metrics.getHeight());

	}

	/**
	 * 
	 * @param i
	 *            buffered image to draw
	 * @param dest
	 *            Region of the image to draw
	 * @param src
	 *            Region on the canvas to draw to
	 */

	private void drawImage(Graphics2D g, BufferedImage i, Rectangle dest, Rectangle src) {
		g.drawImage(i, dest.x, dest.y, dest.x + dest.width, dest.y + dest.height, src.x, src.y, src.x + src.width,
				src.y + src.height, null);
	}

	/**
	 * Draws a complete BufferedImage to the canvas
	 * 
	 * @param i
	 *            BufferedImage to draw
	 * @param dest
	 *            Region on the canvas to draw to
	 */
	private void drawImage(Graphics2D g, BufferedImage i, Rectangle dest) {
		Rectangle source = new Rectangle();
		source.x = 0;
		source.y = 0;
		source.width = i.getWidth();
		source.height = i.getHeight();

		drawImage(g, i, dest, source);

	}
}
