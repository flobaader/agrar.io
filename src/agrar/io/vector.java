package agrar.io;

import java.awt.Point;

public class Vector {
	private double xCor;
	private double yCor;

	public Vector(double x, double y) {
		xCor = x;
		yCor = y;
	}
	
	public Vector(Point p){
		xCor = p.getX();
		yCor = p.getY();
	}
	
	public Vector(Vector locFrom, Vector locTo){
		xCor = locTo.substractVector(locFrom).getX();
		yCor = locTo.substractVector(locFrom).getY();
	}

	public double getX() {
		return xCor;
	}

	public double getY() {
		return yCor;
	}
	
	public boolean equals(Vector v1){
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

	public  Vector addVector(Vector v2) {
		return new Vector(this.getX() + v2.getX(), this.getY() + v2.getY());
	}

	public Vector substractVector(Vector v2) {
		return new Vector(this.getX() - v2.getX(), this.getY() - v2.getY());
	}

	public Vector multiplyVector(double figure) {
		return new Vector(this.getX() * figure, this.getY() * figure);
	}

	public Vector divideVector(double figure) {
		return new Vector(this.getX() / figure, this.getY() / figure);
	}

	public Vector reverseVector() {
		return this.multiplyVector(-1);
	}

	public Vector getUnitVector() {
		return this.divideVector(this.getLength());
	}
	
	public Point ToPoint(){
		Point p = new Point();
		p.setLocation(this.getX(), this.getY());
		return p;
	}
	
	
}
