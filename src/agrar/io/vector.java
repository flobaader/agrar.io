package agrar.io;

import java.awt.Point;

public class vector {
	private double xCor;
	private double yCor;

	public vector(double x, double y) {
		xCor = x;
		yCor = y;
	}

	public double getX() {
		return xCor;
	}

	public double getY() {
		return yCor;
	}
	
	public boolean equals(vector v1){
		if(v1 != null){
			return (this.getX() == v1.getX() && this.getY() == v1.getY());
		}else{
			return false;
		}
		
	}
	
	public int getRoundedX(){
		return Math.round((float)xCor);
	}
	
	public int getRoundedY(){
		return Math.round((float)yCor);
	}

	public double getLength() {
		return Math.sqrt(Math.pow(xCor, 2) + Math.pow(yCor, 2));
	}

	public static vector addVector(vector v1, vector v2) {
		return new vector(v1.getX() + v2.getX(), v1.getY() + v2.getY());
	}

	public static vector substractVector(vector v1, vector v2) {
		return new vector(v1.getX() - v2.getX(), v1.getY() - v2.getY());
	}

	public static vector multiplyVector(vector v1, double figure) {
		return new vector(v1.getX() * figure, v1.getY() * figure);
	}

	public static vector divideVector(vector v1, double figure) {
		return new vector(v1.getX() / figure, v1.getY() / figure);
	}

	public static vector reverseVector(vector v1) {
		return multiplyVector(v1, -1);
	}

	public static vector getUnitVector(vector v1) {
		return divideVector(v1, v1.getLength());
	}
	
	public static vector pointToVector(Point p){
		return new vector(p.getX(), p.getY());
	}
	
	public static Point vectorToPoint(vector v1){
		Point p = new Point();
		p.setLocation(v1.getX(), v1.getY());
		return p;
	}
	
	public static vector vectorFromTo(vector locFrom, vector locTo){
		return vector.substractVector(locTo, locFrom);
	}
	

}
