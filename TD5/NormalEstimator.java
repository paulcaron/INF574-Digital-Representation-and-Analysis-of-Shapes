import java.util.Arrays;
import java.util.List;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;

/**
 * A class providing methods for the estimation of vertex normal in a 3D point cloud
 * 
 * @author Luca Castelli Aleardi (INF574, 2018)
 *
 */
public class NormalEstimator {

	/**
	 * Compute the outliers in the point cloud
	 * 
	 * @param points  input point cloud
	 * @param k  number of closest neighbor
	 */
	public static double[][] computeNormals(PointSet points, int k) {
		double[][] res = new double[points.toArray().length][3];
		for(int i=0; i<points.toArray().length; i++) {
			double[][] array = {{0.,0.,0.},
					{0.,0.,0.},
					{0.,0.,0.}};
			
			Matrix C = new Matrix(array);
			
			Point_3[] kNearestNeighbors = points.getKNearestNeighbors(points.toArray()[i], k);
			
			for(Point_3 point : kNearestNeighbors) {
				double[][] array_2 = {{point.x-points.toArray()[i].x},{point.y-points.toArray()[i].y},{point.z-points.toArray()[i].z}};
				Matrix P = new Matrix(array_2);
				C.plusEquals(P.times(P.transpose()));
			}
			EigenvalueDecomposition e = C.eig();
			double[] realEigenvalues = e.getRealEigenvalues();
			int index = 0;
			double min = realEigenvalues[index];
			for (int j=1; j<realEigenvalues.length; j++){
				if (realEigenvalues[j] < min ){
					min = realEigenvalues[j];
					index = j;
				}
			}
			res[i][0] = e.getV().get(0, index);
			res[i][1] = e.getV().get(1, index);
			res[i][2] = e.getV().get(2, index);

		}
		return res;
	}
	
	/**
	 * Compute the normals for all points in the point cloud
	 * 
	 * @param points  input point cloud
	 * @param sqRad  distance parameter (sqRad=d*d)
	 */
	public static double[][] computeNormals(PointSet points, double sqRad) {
		double[][] res = new double[points.toArray().length][3];
		for(int i=0; i<points.toArray().length; i++) {
			double[][] array = {{0.,0.,0.},
					{0.,0.,0.},
					{0.,0.,0.}};
			
			Matrix C = new Matrix(array);
			
			List<Point_3> closestPoints = points.getClosestPoints(points.toArray()[i], sqRad);
			
			for(Point_3 point : closestPoints) {
				double[][] array_2 = {{point.x-points.toArray()[i].x},{point.y-points.toArray()[i].y},{point.z-points.toArray()[i].z}};
				Matrix P = new Matrix(array_2);
				C.plusEquals(P.times(P.transpose()));
			}
			EigenvalueDecomposition e = C.eig();
			double[] realEigenvalues = e.getRealEigenvalues();
			int index = 0;
			double min = realEigenvalues[index];
			for (int j=1; j<realEigenvalues.length; j++){
				if (realEigenvalues[j] < min ){
					min = realEigenvalues[j];
					index = j;
				}
			}
			res[i][0] = e.getV().get(0, index);
			res[i][1] = e.getV().get(1, index);
			res[i][2] = e.getV().get(2, index);

		}
		return res;
	}
	
	/**
	 * Given a point p and a distance d, <p>
	 * compute the matrix $C=\sum_{i}^{k} [(p_i-P)(p_i-P)^t]$<p>
	 * <p>
	 * where $k$ is the number of points ${p_i}$ at distance at most $d$ from point $p$
	 * 
	 * @param points  input point cloud
	 * @param p  the query point (for which we want to compute the normal)
	 * @param sqRad  squared distance (sqRad=d*d)
	 */
	public static Matrix getCovarianceMatrix(PointSet points, Point_3 p, double sqRad) {
		double[][] array = {
				{0.,0.,0.},
				{0.,0.,0.},
				{0.,0.,0.}
				};
		
		Matrix C = new Matrix(array);
		
		List<Point_3> closestPoints = points.getClosestPoints(p, sqRad);
		
		for(Point_3 point : closestPoints) {
			double[][] array_2 = {{point.x-p.x},{point.y-p.y},{point.z-p.z}};
			Matrix P = new Matrix(array_2);
			C.plusEquals(P.times(P.transpose()));
		}
		return C;
	}

	/**
	 * Return the distance parameter (a rough approximation of the average distance between neighboring points)
	 * 
	 * @param points  input point cloud
	 */
	public static double estimateAverageDistance(PointSet points) {
		int n=(int)Math.sqrt(points.size());
		double maxDistance=points.getMaxDistanceFromOrigin();
		return maxDistance*4/n;
	}

}
