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
		throw new Error("to be completed: TD1");
	}

	/**
	 * Define a scaling by factors s1 and s2
	 */
	public Transformation_2(double s1, double s2) {
		double[][] array = {{s1, 0., 0.},{0., s2, 0.},{0.,0.,1.}}; 
		this.m=new Matrix(array);
	}

	/**
	 * Define a rotation of an angle theta
	 */
	public Transformation_2(double theta) {
		throw new Error("to be completed: TD1");	
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
	
	public static void main(String[] args) {
		Point_3 origin=new Point_3(0., 0., 1.);
		Transformation_2 t=new Transformation_2(new Vector_2(5., 2.));
		
		Point_3 q=t.transform(origin);
		System.out.println(origin+" ---> "+q);
		
	}

}
