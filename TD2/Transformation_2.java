import Jama.Matrix;

/**
 * Define a 2D transformation (in the projective plane, using homogeneous coordinates)
 *
 * @author Luca Castelli Aleardi
 * Student Paul Caron
 */
public class Transformation_2 {

	Matrix m;
	static final double precision=100.;
	
	/**
	 * The identity transformation
	 */
	public Transformation_2(Matrix m) {
		this.m=m;
	}

	/**
	 * The identity transformation
	 */
	public Transformation_2() {
		double[][] array = {{1.,0.,0.},{0.,1.,0.},{0.,0.,1.}}; 
		this.m=new Matrix(array);
	}

	/**
	 * Define a translation by a vector v
	 */
	public Transformation_2(Vector_2 v) {
		double[][] array = {{1.,0.,v.x},{0.,1.,v.y},{0.,0.,1.}}; 
		this.m=new Matrix(array);
	}

	/**
	 * Define a scaling by factors s1 and s2
	 */
	public Transformation_2(double s1, double s2) {
		double[][] array = {{s1,0.,0.},{0.,s2,0.},{0.,0.,1.}}; 
		this.m=new Matrix(array);
	}

	/**
	 * Define a rotation of an angle theta (with the origin as center)
	 */
	public Transformation_2(double theta) {
		double[][] array = {{Math.cos(theta),-Math.sin(theta),0.},{Math.sin(theta),Math.cos(theta),0.},{0.,0.,1.}}; 
		this.m=new Matrix(array);
	}

	/**
	 * Define a rotation of an angle theta and a given center
	 */
	public Transformation_2(double theta, Point_2 center) {
		double[][] array1 = {{1.,0.,-center.x},{0.,1.,-center.y},{0.,0.,1.}}; 
		Matrix matrix1 =new Matrix(array1);
		double[][] array2 = {{Math.cos(theta),-Math.sin(theta),0.},{Math.sin(theta),Math.cos(theta),0.},{0.,0.,1.}}; 
		Matrix matrix2 = new Matrix(array2);
		double[][] array3 = {{1.,0.,center.x},{0.,1.,center.y},{0.,0.,1.}}; 
		Matrix matrix3 =new Matrix(array3);
		this.m = matrix3.times(matrix2.times(matrix1));
	}

	/**
	 * Apply the transformation to point p (having homogeneous coordinates)
	 */
	public Point_3 transform(Point_3 p) {
		double x=approx(p.getX());
		double y=approx(p.getY());
		double z=approx(p.getZ());
		double[][] array = {{x}, {y}, {z}}; 
		Matrix v=new Matrix(array); // the vector
		
		Matrix result=this.m.times(v);
		return new Point_3(result.get(0, 0), result.get(1, 0), result.get(2, 0));
	}

	/**
	 * Apply the transformation to point p (having cartesian coordinates)
	 */
	public Point_2 transform(Point_2 c) {
		Point_3 p=(Point_3)c.toHomogeneous(); // homogeneous coordinates
		double x=approx(p.getX());
		double y=approx(p.getY());
		double z=approx(p.getZ());
		double[][] array = {{x}, {y}, {z}}; 
		Matrix v=new Matrix(array); // the vector
		
		Matrix result=this.m.times(v);
		return (Point_2)(new Point_3(result.get(0, 0), result.get(1, 0), result.get(2, 0))).toCartesian();
	}

	/**
	 * Perform a central projection of point p (in 3D) on the plane z=f
	 */
	public Point_3 projectPoint(Point_3 p, double f) {
		double x=approx(p.getCartesian(0));
		double y=approx(p.getCartesian(1));
		double z=approx(p.getCartesian(2));
		double w=1.;
		double[][] homP = {{x}, {y}, {z}, {w}}; // homogeneous coordinates
		Matrix v=new Matrix(homP); // point to be projected
		
		Matrix result=this.m.times(v);
		return new Point_3(f*result.get(0, 0)/z, f*result.get(1, 0)/z, 1.);
	}

	/**
	 * Compose two transformations
	 */
	public Transformation_2 compose(Transformation_2 t) {
		Matrix M=t.m; 
		
		Matrix composition=this.m.times(M);
		return new Transformation_2(composition);
	}
	
	/**
	 * Compute the matrix corresponding to a central projection, on plane z=f
	 */
	public static Transformation_2 centralProjectionMatrix(double f) {
		double[][] array = {{f,0.,0., 0.},
							{0.,f,0., 0.},
							{0.,0.,1.,0.}}; 
		Matrix result=new Matrix(array);
		return new Transformation_2(result);
	}
	
	public static double approx(double d) {
		int rounded=(int)(d*precision);
		return rounded/precision;
	}
	
}
