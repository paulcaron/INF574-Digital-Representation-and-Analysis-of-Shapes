import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import Jcg.geometry.Point_3;

/**
 * A class providing methods for the computatino of outliers in a 3D point cloud
 * 
 * @author Luca Castelli Aleardi (INF574, 2018)
 *
 */
public class OutliersComputation {

	/**
	 * Compute the outliers in the point cloud
	 * 
	 * @param points  input point cloud
	 * @param sqRad  distance parameter (sqRad=d*d)
	 * @param epsilon  threshold parameter
	 */
	public static boolean[] computeOutliers(PointSet points, double sqRad, double epsilon) {
		boolean[] res = new boolean[points.size()];
		for(int i=0; i<points.size(); i++) {
			Matrix C = NormalEstimator.getCovarianceMatrix(points, points.toArray()[i], sqRad);
			EigenvalueDecomposition e = C.eig();
			double[] realEigenvalues = e.getRealEigenvalues();
			int i1, i2, i3;
			if(realEigenvalues[0]>=realEigenvalues[1] && realEigenvalues[0]>=realEigenvalues[2]) {
				if(realEigenvalues[1]>=realEigenvalues[2]) {
					i1 = 2;
					i2 = 1;
					i3 = 0;
				}
				else {
					i1 = 1;
					i2 = 2;
					i3 = 0;
				}
			}
			else if(realEigenvalues[1]>=realEigenvalues[0] && realEigenvalues[1]>=realEigenvalues[2]) {
				if(realEigenvalues[0]>=realEigenvalues[2]) {
					i1 = 2;
					i2 = 0;
					i3 = 1;
				}
				else {
					i1 = 0;
					i2 = 2;
					i3 = 1;
				}
			}
			else {
				if(realEigenvalues[0]>=realEigenvalues[1]) {
					i1 = 1;
					i2 = 0;
					i3 = 2;
				}
				else {
					i1 = 0;
					i2 = 1;
					i3 = 2;
				}
			}
			res[i] = realEigenvalues[i2] > epsilon * realEigenvalues[i3];
		}
		return res;
	}

	/**
	 * Compute the outliers in the point cloud
	 * 
	 * @param points  input point cloud
	 * @param k  number of closest neighbor
	 */
	public static boolean[] computeOutliers(PointSet points, int k) {
		throw new Error("To be completed");
	}

}
