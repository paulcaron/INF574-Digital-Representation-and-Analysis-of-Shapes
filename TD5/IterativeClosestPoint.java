import Jcg.geometry.*;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import Jama.SingularValueDecomposition;

/**
 * Perform the Iterative Closest Point algorithm
 * 
 * @author Luca Castelli Aleardi (INF555, 2014)
 *
 */
public class IterativeClosestPoint extends RigidTransformation_3 {
		
	public IterativeClosestPoint() {
		super();
	}
		
	/**
	 * The main method computing the rotation<p>
	 * To be implemented for the TD
	 */
	public Rotation_3 getRotation(PointSet points1, PointSet points2) {
		double dist = 0;
		Number[] coefficients = {1,-1};
		Point_3 bar1 = points1.getBarycenter();
		Point_3 bar2 = points2.getBarycenter();
		Point_3[] new_points1 = new Point_3[points1.toArray().length];
		Point_3[] new_points2 = new Point_3[points1.toArray().length];
		
		for(int i=0; i<points1.toArray().length; i++) {
			Point_3 closest = points2.getClosestPoint(points1.toArray()[i]);
			Point_3[] lin1 = {points1.toArray()[i], bar1};
			Point_3[] lin2 = {closest, bar2};
			new_points1[i] = Point_3.linearCombination(lin1, coefficients);
			new_points2[i] = Point_3.linearCombination(lin2, coefficients);
			dist += (double) new_points1[i].distanceFrom(new_points2[i]);
		}
		
		System.out.println("Current distance:" + dist);
		double[][] array = {{0.,0.,0.},
				{0.,0.,0.},
				{0.,0.,0.}};
		
		Matrix H = new Matrix(array);
		
		for(int i=0; i<points1.toArray().length; i++) {
			double[][] array_1 = {{new_points1[i].x},{new_points1[i].y},{new_points1[i].z}};
			double[][] array_2 = {{new_points2[i].x},{new_points2[i].y},{new_points2[i].z}};
			Matrix p1 = new Matrix(array_1);
			Matrix p2 = new Matrix(array_2);
			H.plusEquals(p2.times(p1.transpose()));
		}
		
		SingularValueDecomposition s = H.svd();
		Matrix U = s.getU();
		Matrix V = s.getV();
		
		return new Rotation_3(V.times(U.transpose()));
	}

	/**
	 * The main method computing the rotation
	 * To be implemented
	 */
	public Translation_3 getTranslation(PointSet points1, PointSet points2) {
		Rotation_3 R = getRotation(points1, points2);
		Point_3 bar1 = points1.getBarycenter();
		Point_3 bar2 = points2.getBarycenter();
		Vector_3 v = new Vector_3();
		v.x = bar1.x - R.transform(bar2).x;
		v.y = bar1.y - R.transform(bar2).y;
		v.z = bar1.z - R.transform(bar2).z;
		return new Translation_3(v);
	}
}

/*
Number[] coefficients = {1,-1};
Point_3 bar1 = points1.getBarycenter();
Point_3 bar2 = points2.getBarycenter();
Point_3[] new_points1 = new Point_3[points1.toArray().length];
Point_3[] new_points2 = new Point_3[points1.toArray().length];
for(int i=0; i<points1.toArray().length; i++) {
	Point_3 closest = points2.getClosestPoint(points1.toArray()[i]);
	Point_3[] lin1 = {points1.toArray()[i], bar1};
	Point_3[] lin2 = {closest, bar2};
	new_points1[i] = Point_3.linearCombination(lin1, coefficients);
	new_points2[i] = Point_3.linearCombination(lin2, coefficients);
}
double[][] array = {{0.,0.,0.},
		{0.,0.,0.},
		{0.,0.,0.}};
*/