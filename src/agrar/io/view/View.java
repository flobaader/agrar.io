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
import java.awt.geom.Line2D;
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

	// FPS counter
	private long lastPaint = 0;
	private double FPS;
	private boolean showFPS = true;

	// Controller
	private Controller controller;

	// Objects for drawing
	private BufferedImage scoreBackground;
	private Rectangle scoreBackgroundSize;

	// Arena Translations
	private float offsetX, offsetY;
	private float zoomFactor;

	// Mouse Location
	private Vector mouseMovement;

	public View(Controller p) {
		controller = p;

		scoreBackgroundSize = new Rectangle();
		scoreBackgroundSize.height = 40;

		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// Subtracts the half Screen Size of the Vector to get a
				// normalized vector, which start point is located at the center
				// of the screen
				Vector v = new Vector(e.getX() - getWidth() / 2, e.getY() - getHeight() / 2);
				setMouseMovement(v);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});

		// load the images for the HUD

		try {
			scoreBackground = ImageIO.read(View.class.getResource("/bottom_left.png"));
		} catch (IOException e1) {
		}

		zoomFactor = 1.0F; // No zoom per default
	}

	@Override
	public void paint(Graphics g) {

		// Setup drawing canvas
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());

		// The zoom must be calculated first
		calculateZoom(controller.getLocalPlayer());
		calcOffsets(controller.getLocalPlayer());

		// Testing the Zoom
		// controller.getLocalPlayer().setSize(controller.getLocalPlayerScore()
		// + 5);

		drawArena(g2d); // Drawing the Game
		drawHUD(g2d); // Drawing the HUD on top

	}

	/**
	 * Calculate the amount of zoom necessary to fit the local player in the
	 * view. The View is always zoomed that the player is 15% of the length of
	 * the shorter side of the view.
	 * 
	 * @param localPlayer
	 *            the local Player
	 */
	private void calculateZoom(Player localPlayer) {

		// Target size on Screen is 10% of the shorter side of the view
		float displaySize = Math.min(this.getHeight() * 0.10F, this.getWidth() * 0.10F);
		zoomFactor = displaySize / localPlayer.getRadius();

	}

	/**
	 * Draws the HUD over the GameArena
	 * 
	 * @param g
	 *            GraphicsD for drawing
	 */
	private void drawHUD(Graphics2D g) {
		drawPlayerScore(g);
		// drawHighscoreList(g);
		drawFPS(g);
	}

	/**
	 * Draw the Game arena with all players
	 * 
	 * @param g
	 */
	private void drawArena(Graphics2D g) {

		drawGrid(g);

		// Draw circles
		for (Circle c : controller.getAllComponents()) {

			drawCircle(g, c);
			// only players have a name
			if (c.isPlayer()) {
				drawName(g, c);
				drawTargetLine(g, c);
			}
			
		}

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

		float oX = (offsetX) % (40F * zoomFactor);
		float oY = (offsetY) % (40F * zoomFactor);

		Line2D line = new Line2D.Float();

		for (float x = oX; x < (float) (this.getWidth()) * zoomFactor; x += 40F * zoomFactor) {

			line.setLine(x, 0, x, this.getHeight());
			g.draw(line);
		}
		for (float y = oY; y < (float) (this.getHeight()) * zoomFactor; y += 40F * zoomFactor) {

			line.setLine(0, y, this.getWidth(), y);
			g.draw(line);
		}
	}

	private void drawTargetLine(Graphics2D g, Circle c) {

		Vector target = ((Player) c).getBehavior().getTarget();

		if (target != null) {
			Point start = getTransformedPosition(c);
			Vector end = getTransformedPosition(target);
			g.drawLine(start.x, start.y, (int) end.getX(), (int) end.getY());
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
		Dimension d = measureString(g, name);

		Point pos = getTransformedPosition(c);

		int x = pos.x - (int) (d.width / 2F);
		int y = pos.y - (int) ((c.getRadius() * zoomFactor) + 10);

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

		int radius = (int) (c.getRadius() * zoomFactor);
		int diameter = (int) (radius * 2F);

		Point pos = getTransformedPosition(c);

		g.fillOval(pos.x - radius, pos.y - radius, diameter, diameter);
	}

	/**
	 * Calculates the offsets used for drawing
	 * 
	 * @param player
	 *            Circle that represents local player
	 */
	private void calcOffsets(Circle player) {
		Vector playerOffset = controller.getLocalPlayer().getLocation();
		offsetX = ((this.getWidth() / 2) - (float) playerOffset.getX() * zoomFactor);
		offsetY = ((this.getHeight() / 2) - (float) playerOffset.getY() * zoomFactor);
	}

	/**
	 * Draw the score of the local player in the bottom left corner
	 * 
	 * @param localPlayer
	 *            the local player
	 */
	private void drawPlayerScore(Graphics2D g) {

		String score = String.valueOf(controller.getLocalPlayerScore());

		g.setFont(new Font("Arial", Font.BOLD, 25));
		Dimension stringSize = measureString(g, score);

		scoreBackgroundSize.height = (int) ((stringSize.height + 20) * 1.25);
		scoreBackgroundSize.y = (int) (this.getHeight() - scoreBackgroundSize.getHeight());
		scoreBackgroundSize.width = (int) ((stringSize.width + 30) * 1.0833);
		scoreBackgroundSize.x = (int) (this.getWidth() - scoreBackgroundSize.getWidth());

		drawImage(g, scoreBackground, scoreBackgroundSize);
		g.setColor(Color.white);

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
	 * Draw src region of BufferedImage to dest region on canvas
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

	/**
	 * Applies translate and scale to the position of the circle
	 * 
	 * @param c
	 *            the circle to transform
	 * @return a Point that contains the transformed position
	 */
	private Point getTransformedPosition(Circle c) {

		int x = (int) c.getLocation().getX();
		int y = (int) c.getLocation().getY();

		// Translated position is the difference of localplayer and original
		// Position multiplied with zoomFactor, then translated by the offset
		float tX = (x * zoomFactor) + offsetX;
		float tY = (y * zoomFactor) + offsetY;

		return new Point((int) tX, (int) tY);

	}

	private Vector getTransformedPosition(Vector v) {

		int x = (int) v.getX();
		int y = (int) v.getY();

		// Translated position is the difference of localplayer and original
		// Position multiplied with zoomFactor, then translated by the offset
		float tX = (x * zoomFactor) + offsetX;
		float tY = (y * zoomFactor) + offsetY;

		return new Vector(tX, tY);
	}

	private void setMouseMovement(Vector v) {
		mouseMovement = v;
	}

	public Vector getMouseVector() {
		return mouseMovement;
	}

}
