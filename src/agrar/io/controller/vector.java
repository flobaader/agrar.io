package agrar.io;

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

}
