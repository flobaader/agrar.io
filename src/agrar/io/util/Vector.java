package agrar.io.util;

import java.awt.Point;

/**
 * Represents a vector
 * @author FBaader
 *
 */
public class Vector {
	private double xCor;
	private double yCor;

	/**
	 * Initializes vector with two give coordinates
	 * @param x first coordinate
	 * @param y second coordinate
	 */
	public Vector(double x, double y) {
		xCor = x;
		yCor = y;
	}
	
	/**
	 * Creates new Vector from a point with the same coordinates
	 * @param p the Point to be converted
	 */
	public Vector(Point p){
		xCor = p.getX();
		yCor = p.getY();
	}
	
	/**
	 * Returns the Vector between the two given locations
	 * @param locFrom the coordinate of the first location
	 * @param locTo the coordinate of the target
	 */
	public Vector(Vector locFrom, Vector locTo){
		xCor = locTo.substractVector(locFrom).getX();
		yCor = locTo.substractVector(locFrom).getY();
	}

	/**
	 * Returns the x Coordinate of the Vector
	 * @return x Coordinate
	 */
	public double getX() {
		return xCor;
	}

	/**
	 * Returns the y Coordinate of the Vector
	 * @return y Coordinate
	 */
	public double getY() {
		return yCor;
	}
	
	/**
	 * Returns if the vector is equal
	 * @param v1 the vector to be checked
	 * @return true if vectors are equal
	 */
	public boolean equals(Vector v1){
		if(v1 != null){
			return (this.getX() == v1.getX() && this.getY() == v1.getY());
		}else{
			return false;
		}
		
	}
	
	/**
	 * Returns the length of the vector
	 * @return the Length
	 */
	public double getLength() {
		return Math.sqrt(Math.pow(xCor, 2) + Math.pow(yCor, 2));
	}

	/**
	 * Adds the given vector to the vector
	 * @param v2 The vector to be added
	 * @return new vector
	 */
	public  Vector addVector(Vector v2) {
		return new Vector(this.getX() + v2.getX(), this.getY() + v2.getY());
	}

	/**
	 * Substracts the given vector to the vector
	 * @param v2 The vector to be substract
	 * @return new vector
	 */
	public Vector substractVector(Vector v2) {
		return new Vector(this.getX() - v2.getX(), this.getY() - v2.getY());
	}

	/**
	 * Multiplys the vector with the given figure
	 * @param figure The factor
	 * @return new vector which length has changed by the factor
	 */
	public Vector multiplyVector(double figure) {
		return new Vector(this.getX() * figure, this.getY() * figure);
	}

	/**
	 * Divides the vector with the given figure
	 * @param figure The factor
	 * @return new vector which length has changed by the factor
	 */
	public Vector divideVector(double figure) {
		return new Vector(this.getX() / figure, this.getY() / figure);
	}

	/**
	 * Multiplys the vector with (-1) = Reverse direction
	 * @return new vector with changed direction
	 */
	public Vector reverseVector() {
		return this.multiplyVector(-1);
	}

	/**
	 * Divides Vector trough its length
	 * @return A vector with the same direction but length = 1
	 */
	public Vector getUnitVector() {
		return this.divideVector(this.getLength());
	}
	
	/**
	 * Converts the vector to a Point
	 * @return converted Point
	 */
	public Point ToPoint(){
		Point p = new Point();
		p.setLocation(this.getX(), this.getY());
		return p;
	}
}
