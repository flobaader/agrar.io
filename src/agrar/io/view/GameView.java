
package agrar.io.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import agrar.io.controller.Controller;
import agrar.io.controller.Controller.GameState;
import agrar.io.model.Circle;
import agrar.io.model.Player;
import agrar.io.model.Score;
import agrar.io.util.Vector;

public class GameView extends JPanel {

	private static final long serialVersionUID = 2126792581772053659L;

	// FPS counter
	private long lastPaint = 0;
	private double FPS;
	private boolean showFPS = true;

	// Controller
	private Controller controller;

	// Objects for drawing
	private BufferedImage bottomRight, topRight, bottomLeft;
	private Rectangle scoreBackgroundSize;

	// Arena Translations
	private float offsetX, offsetY;
	private float zoomFactor;

	public GameView(Controller p) {
		controller = p;

		scoreBackgroundSize = new Rectangle();
		scoreBackgroundSize.height = 40;

		// load the images for the HUD
		try {
			bottomRight = ImageIO.read(GameView.class.getResource("/bottom_right.png"));
			topRight = ImageIO.read(GameView.class.getResource("/top_right.png"));
			bottomLeft = ImageIO.read(GameView.class.getResource("/bottom_left.png"));
		} catch (IOException e1) {
			// This should only happen if the images are not present or not
			// correctly packaged in the jar, so no need to handle it
		}

		zoomFactor = 1.0F; // No zoom per default
	}

	@Override
	public void paint(Graphics g) {

		if (controller.getState() == GameState.Playing) {
			// Setup drawing canvas
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.clearRect(0, 0, this.getWidth(), this.getHeight());

			// The zoom must be calculated first

			calculateZoom(controller.getLocalPlayer());
			calcOffsets(controller.getLocalPlayer());
			// Testing the Zoom
			// controller.getLocalPlayer().setSize(controller.getLocalPlayerScore()
			// + 100);

			drawArena(g2d); // Drawing the Game
			drawHUD(g2d); // Drawing the HUD on top

		}
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
		drawFPS(g);
		drawScoreList(g);
		drawHighscoreList(g);
	}

	/**
	 * Draw the Game arena with all players
	 * 
	 * @param g
	 */
	private void drawArena(Graphics2D g) {

		drawGrid(g);

		// Draw circles
		for (Circle c : controller.getAllGameObjects()) {

			drawCircle(g, c);
			// only players have a name
			if (c.isPlayer()) {
				drawName(g, c);

				// Draws target line if in debug mode
				if (controller.isInDebugMode()) {
					drawTargetLine(g, c);
				}

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

		for (float x = oX; x < this.getWidth(); x += 40F * zoomFactor) {

			line.setLine(x, 0, x, this.getHeight());
			g.draw(line);
		}
		for (float y = oY; y < this.getHeight(); y += 40F * zoomFactor) {

			line.setLine(0, y, this.getWidth(), y);
			g.draw(line);
		}
	}

	private void drawTargetLine(Graphics2D g, Circle c) {

		// The target of the player
		Vector target = ((Player) c).getBehavior().getTarget();

		// If the target is the local player draws the mouse vector
		/*	if (c == controller.getLocalPlayer()) {
			Vector offset = controller.getOffset();
			Vector mouseLocation = controller.getMouseVector().addVector(offset);

			Line2D.Float line = new Line2D.Float((float) offset.getX(), (float) offset.getY(),
					(float) mouseLocation.getX(), (float) mouseLocation.getY());
			g.draw(line);

		}*/	

		if (target != null) {
			// Draws line between circle and target

			FloatPoint start = getTransformedPosition(c);
			FloatPoint end = getTransformedPosition(target);
			Line2D.Float line = new Line2D.Float(start.x, start.y, end.x, end.y);
			g.draw(line);
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

		FloatPoint pos = getTransformedPosition(c);

		float x = pos.x - (d.width / 2F);
		float y = pos.y - ((c.getRadius() * zoomFactor) + 10);

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

		float radius = c.getRadius() * zoomFactor;
		float diameter = radius * 2F;

		FloatPoint pos = getTransformedPosition(c);

		Ellipse2D.Float oval = new Ellipse2D.Float(pos.x - radius, pos.y - radius, diameter, diameter);
		g.fill(oval);
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

		drawImage(g, bottomRight, scoreBackgroundSize);
		g.setColor(Color.white);

		g.drawString(score, getWidth() - (stringSize.width + 10), (int) (getHeight() - 10));

	}

	/**
	 * Draws the score list with the best players in the current game, including
	 * background
	 * 
	 * @param g
	 */
	private void drawScoreList(Graphics2D g) {

		// Create an array of score strings
		String[] scores = createScoreList(controller.getLocalHighscores());

		// calculate the width of the background needed
		Dimension listSize = measureScoreList(g, scores);

		double textheight = measureString(g, scores[0]).getHeight();

		// Add padding for the Background:

		// width + 30 px of padding multiplied by 110% because of the margin in
		// the image
		int width = (int) ((listSize.getWidth() + 30) * 1.1F);

		// height + 10 px of vertical padding * 5/70 because of the margin in
		// the image
		int height = (int) ((listSize.getHeight() + 30) * (1 + (1F / 7F)));

		Rectangle dest = new Rectangle(this.getWidth() - width, 0, width, height);

		// Draw background for list
		drawImage(g, topRight, dest);

		int y = (int) (textheight + 5); // Baseline of the first string
		// X coordinate for Score strings
		int x = (int) (this.getWidth() - (listSize.getWidth() + 15));

		// Draw the score strings
		for (String s : scores) {
			g.drawString(s, x, y);

			// Next String will be drawn one line height lower + 10 px padding
			y += textheight + 10;
		}
	}

	/**
	 * Draws a list of the 5 best players in the Database
	 * 
	 * @param g
	 */
	private void drawHighscoreList(Graphics2D g) {

		// Draws no highscore list if offline
		if (controller.isOffline()) {
			return;
		}

		String[] scores = createScoreList(controller.getGlobalHighscores());

		Dimension listSize = measureScoreList(g, scores);

		double lineHeight = measureString(g, scores[0]).getHeight();

		// Add padding for the background
		int width = (int) ((listSize.getWidth() + 30) * 1.1F);
		int height = (int) ((listSize.getHeight() + 30) * (1 + (1F / 7F)));

		Rectangle dest = new Rectangle(0, this.getHeight() - height, width, height);

		// Draw Background
		drawImage(g, bottomLeft, dest);

		// Start coordinates for the list
		int x = 15; // 15 px of padding left
		int y = (int) ((this.getHeight() - listSize.getHeight()) + lineHeight);

		for (String s : scores) {
			g.drawString(s, x, y);

			y += lineHeight + 10;
		}
	}

	/**
	 * Turns an Array of Scores into an array of Strings corresponding to the
	 * scores. The format is [name]: [score].
	 * 
	 * @param highscores
	 *            the Scores to convert
	 * @return Array of Strings based on the scores
	 */
	private String[] createScoreList(Score[] highscores) {

		String[] scoreStr = new String[highscores.length];

		for (int i = 0; i < highscores.length; i++) {
			scoreStr[i] = highscores[i].getName() + ": " + highscores[i].getScore();

		}

		return scoreStr;

	}

	/**
	 * Measures the space required to draw the score list
	 * 
	 * @param g
	 * @param scoreStr
	 *            Array of the score strings
	 * @return a Dimension that represents the space required for the list
	 */
	private Dimension measureScoreList(Graphics2D g, String[] scoreStr) {

		int width = 0; // the minimum width of the score list
		int height = 0; // The minimum height of the score list

		for (int i = 0; i < scoreStr.length; i++) {

			Dimension d = measureString(g, scoreStr[i]);
			width = (int) Math.max(width, d.getWidth());
			height += d.getHeight() + 10;
		}

		return new Dimension(width, height);
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
	 * Draw src region of BufferedImage to destination region on canvas
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
	private FloatPoint getTransformedPosition(Circle c) {

		return getTransformedPosition((float) c.getLocation().getX(), (float) c.getLocation().getY());

	}

	/**
	 * Applies translate and scale to a vector
	 * 
	 * @param v
	 * @return
	 */
	private FloatPoint getTransformedPosition(Vector v) {
		return getTransformedPosition((float) v.getX(), (float) v.getY());
	}

	/**
	 * Applies zoom and translate to any point in the game arena
	 * 
	 * @param x
	 *            x coordinate of the point to transform
	 * @param y
	 *            y coordinate of the point to transform
	 * @return the transformed point
	 */
	private FloatPoint getTransformedPosition(float x, float y) {

		// Translated position is the difference of localplayer and original
		// Position multiplied with zoomFactor, then translated by the offset
		float tX = (x * zoomFactor) + offsetX;
		float tY = (y * zoomFactor) + offsetY;

		return new FloatPoint(tX, tY);
	}

	/**
	 * container class for an x and y coordinate that is a float
	 * 
	 * @author Matthias
	 *
	 */
	public class FloatPoint {
		public float x;
		public float y;

		public FloatPoint(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}
}
