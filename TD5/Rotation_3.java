import Jcg.geometry.*;

import Jama.Matrix;

/**
 * Define a 3D Rotation (using Euler angles)
 *
 * @author Luca Castelli Aleardi (INF555, 2014)
 */
public class Rotation_3 {

	Matrix m;
	
	/**
	 * The identity transformation
	 */
	public Rotation_3(Matrix m) {
		this.m=m;
	}

	/**
	 * The identity rotation
	 */
	public Rotation_3() {
		double[][] array = {{1.,0.,0.},
							{0.,1.,0.},
							{0.,0.,1.}}; 
		this.m=new Matrix(array);
	}

	/**
	 * Return a rotation of an angle theta, around X axis
	 */
	public static Rotation_3 rotationAxisX(double theta) {
		double[][] array = {
				{1.,0.,0.},
				{0.,Math.cos(theta),-Math.sin(theta)},
				{0.,Math.sin(theta),Math.cos(theta)}}; 
		
		return new Rotation_3(new Matrix(array));
	}

	/**
	 * Return a rotation of an angle theta, around Y axis
	 */
	public static Rotation_3 rotationAxisY(double theta) {
		throw new Error("to be completed: TD1");
	}

	/**
	 * Return a rotation of an angle theta, around Z axis
	 */
	public static Rotation_3 rotationAxisZ(double theta) {
		throw new Error("to be completed: TD1");
	}

	/**
	 * Rotate point p
	 */
	public Point_3 transform(Point_3 p) {
		double x=p.getCartesian(0).doubleValue();
		double y=p.getCartesian(1).doubleValue();
		double z=p.getCartesian(2).doubleValue();
		double[][] array = {{x}, {y}, {z}}; 
		Matrix v=new Matrix(array); // the vector
		
		Matrix result=this.m.times(v);
		double[] coord={result.get(0, 0), result.get(1, 0), result.get(2, 0)};
		return new Point_3(coord[0], coord[1], coord[2]);
	}

	/**
	 * Compose two rotations
	 */
	public Rotation_3 compose(Rotation_3 t) {
		Matrix M=t.m; 
		
		Matrix composition=this.m.times(M);
		return new Rotation_3(composition);
	}
	
}
