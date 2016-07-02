package agrar.io.controller;

import java.awt.Color;

import agrar.io.controller.PlayerBehavior;
import agrar.io.model.AIPlayer;
import agrar.io.model.Circle;
import agrar.io.util.Utility;
import agrar.io.util.Vector;

/**
 * Simulates the behavior of another player
 * 
 * @author Flo
 *
 */
public class AIPlayerBehavior extends PlayerBehavior {

	private int LEVEL;
	private int FLEE_THRESHOLD;
	
	/**
	 * Creates new behavior for the given AIPlayer
	 * 
	 * @param parent  The AIPlayer to control
	 * @param controller The controller of the game
	 * @param The Level of Experience of the Player (from 0 to 10)
	 * @throws Exception Throws Exception if the Level is not in range between 0 and 10
	 */
	public AIPlayerBehavior(AIPlayer player, Controller controller, int Level) {
		super(player, controller);
		
		//Ensures that the level argument is in the range
		if(Level < 0 || Level > 10){
			Level = 1;
		}else{
			LEVEL = Level;	
		}
		
		//Ramdomizes the point, where 
		FLEE_THRESHOLD = Utility.getRandom(-3, -1);
		
	}
	

	private double misjudgeCircleSize(double Size){
		
		//Decreases with level
		int range = 10 - LEVEL; //in Percent
		
		//Max and min of misjugment is 10 % of size
		int randomFactor = Utility.getRandom(-1 * range, range); //in Percent
		
		double rndAbs = (double) randomFactor / 100; //Absolute not percent
		
		double estimatedSize = Size + Size * rndAbs; 
		
		return estimatedSize;
	}
	
	
	
	@Override
	public void update(float deltaT) {

		// The highest value of a Circle found near the Player
		double bestValue = Double.MIN_VALUE;
		double worstValue = Double.MAX_VALUE;

		// The next target
		Circle bestTarget = null;
		Circle worstTarget = null;

		// Iterates trough all given Objects in Sight and saves the best one and the worst one
		for (Circle c : controller.getObjectsInSight(parent)) {
			
			double curVal = evaluateCircle(c);// + 0.5 * evaluateLocationOfCircle(c);
			
			//Checks if this is currently the best one
			if (curVal > bestValue) {
				bestValue = curVal;
				bestTarget = c;
			}

			//Checks if this is currently the worst one
			if (curVal < worstValue) {
				worstValue = curVal;
				worstTarget = c;
			}

		}

		if(bestTarget == null || worstTarget == null){
			
			//Did not find any Circle in Sight
			nextTarget = Utility.getRandomPoint(controller.getFIELD_SIZE(), controller.getFIELD_SIZE());
			parent.setColor(Color.BLACK);
			
		}else if (bestValue > 0 && worstValue > FLEE_THRESHOLD){
			
			//Found a good Circle and is not in Danger
			nextTarget = bestTarget.getLocation();
			parent.setColor(orgColor);
			
		}else{
			
			//is in Danger or did not find good target --> flees in opposite direction of the worst target
			nextTarget = new Vector(parent.getLocation(), worstTarget.getLocation()).multiplyVector(-1).addVector(parent.getLocation());
			parent.setColor(Color.RED);
		}
		
		//Moves the next step(s) to the selected target
		moveToNewPosition(deltaT);
		
		//Checks if Circles can be eaten
		tryToEat();

	}

	/**
	 * The function assigns a value to the given Circle, which represents the
	 * Points and Distance relationship between Hunter and Target
	 * 
	 * @param c
	 *            Circle to be evaluated
	 * @return value from -infinity to +infinity
	 */
	public double evaluateCircle(Circle c) {
		// Requirements:
		// The value increases with the size difference (parent > c)
		// The value decreases with the size difference (parent < c)
		// The value decreases with the distance
		
		//Value is negative if Target is bigger than parent
		double isSmaller = 1;
		
		double estimatedCircleSize = misjudgeCircleSize(c.getSize());
		
		//NOTE: uses misjudged Size of Circle
		if (estimatedCircleSize > parent.getSize()) {
			isSmaller = -1;
		}

		double sizeFactor = estimatedCircleSize;
		double distanceFactor = (1 / Math.pow(Utility.getDistance(parent, c), 2));

		return (isSmaller * sizeFactor * distanceFactor);
	}

	/**
	 * Describes the value of the location of the given Circle with determines
	 * if there is food or good targets around
	 * 
	 * @param c
	 *            The Circle, which location should be evaluated
	 * @return The Value of the Location
	 */
	public double evaluateLocationOfCircle(Circle c) {
		double value = 0;

		for (Circle c1 : controller.getObjectsInSight(c)) {
			value = value + evaluateCircle(c1);
		}

		value = value / controller.getObjectsInSight(c).size();
		return value;
	}
}
