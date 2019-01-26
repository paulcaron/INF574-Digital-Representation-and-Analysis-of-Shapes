import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import Jcg.geometry.*;

public class QuaternionsICP extends RigidTransformation_3 {
	
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
		
		double[][] array_S = {
				{0.,0.,0.},
				{0.,0.,0.},
				{0.,0.,0.}};
		
		
		
		for(int i=0; i<points1.toArray().length; i++) {
			array_S[0][0]+= new_points2[i].x * new_points1[i].x;
			array_S[0][1]+= new_points2[i].x * new_points1[i].y;
			array_S[0][2]+= new_points2[i].x * new_points1[i].z;
			array_S[1][0]+= new_points2[i].y * new_points1[i].x;
			array_S[1][1]+= new_points2[i].y * new_points1[i].y;
			array_S[1][2]+= new_points2[i].y * new_points1[i].z;
			array_S[2][0]+= new_points2[i].z * new_points1[i].x;
			array_S[2][1]+= new_points2[i].z * new_points1[i].y;
			array_S[2][2]+= new_points2[i].z * new_points1[i].z;
		}
		
		
		
		double[][] array_P = {
				{array_S[0][0]+array_S[1][1]+array_S[2][2],array_S[1][2]-array_S[2][1],array_S[2][0]-array_S[0][2],array_S[0][1]-array_S[1][0]},
				{array_S[1][2]-array_S[2][1],array_S[0][0]-array_S[1][1]-array_S[2][2],array_S[0][1]+array_S[1][0],array_S[2][0]+array_S[0][2]},
				{array_S[2][0]-array_S[0][2],array_S[0][1]+array_S[1][0],array_S[1][1]-array_S[0][0]-array_S[2][2],array_S[1][2]+array_S[2][1]},
				{array_S[0][1]-array_S[1][0],array_S[2][0]+array_S[0][2],array_S[1][2]+array_S[2][1],array_S[2][2]-array_S[0][0]-array_S[1][1]}
		};
		
		Matrix P = new Matrix(array_P);
		
		EigenvalueDecomposition e = P.eig();
		double[] realEigenvalues = e.getRealEigenvalues();
		int index = 0;
		double max = realEigenvalues[index];
		for (int j=1; j<realEigenvalues.length; j++){
			if (realEigenvalues[j] > max ){
				max = realEigenvalues[j];
				index = j;
			}
		}
		double[] q = new double[4];
		q[0] = e.getV().get(0, index);
		q[1] = e.getV().get(1, index);
		q[2] = e.getV().get(2, index);
		q[3] = e.getV().get(3, index);
		
		double[][] array_R = {
				{q[0]*q[0]+q[1]*q[1]-q[2]*q[2]-q[3]*q[3],2*(q[1]*q[2]-q[0]*q[3]), 2*(q[1]*q[3]+q[0]*q[2])},
				{2*(q[2]*q[1]+q[0]*q[3]),q[0]*q[0]-q[1]*q[1]+q[2]*q[2]-q[3]*q[3],2*(q[2]*q[3]-q[0]*q[1])},
				{2*(q[3]*q[1]-q[0]*q[2]),2*(q[3]*q[2]+q[0]*q[1]),q[0]*q[0]-q[1]*q[1]-q[2]*q[2]+q[3]*q[3]}
				};

		return new Rotation_3(new Matrix(array_R));
	}

	
	public Translation_3 getTranslation(PointSet points1, PointSet p2){
		Rotation_3 R = getRotation(points1, p2);
		Point_3 bar1 = points1.getBarycenter();
		Point_3 bar2 = p2.getBarycenter();
		Vector_3 v = new Vector_3();
		v.x = bar1.x - R.transform(bar2).x;
		v.y = bar1.y - R.transform(bar2).y;
		v.z = bar1.z - R.transform(bar2).z;
		return new Translation_3(v);
	}

}
