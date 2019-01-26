/**
 * @author Luca Castelli Aleardi (INF555, 2014)
 * 
 * Compute a Bezier Patch surface in 3D (with tensor product approach)
 */
public class BezierPatchTensorProduct extends Surface {
	
	public BezierPatchTensorProduct(DrawSurface frame) {
		super(frame);
	}

	/**
	 * Initialize control polygon
	 */
	public void initialize(int N, int M){
		points = new Point_3[N][N];
		for(int i=0; i<N ; i++){
			for(int j=0; j<N ; j++){
				double x=((double)i)/N;
				double y=((double)j)/N;
				double z=(Math.pow(1.8*(0.2f-x), 3))*Math.pow(1.8*(0.5f-y), 3);
				points[i][j]=new Point_3(x, y, z);
				//System.out.print(" "+points[i][j]);
			}
			System.out.println();
		}
	}

	public Point_3 evaluate(double u, double v) {
		return tensorProduct(this.points.length-1,this.points.length-1,u,v);
	}
	
	/**
	 * Perform the (iterative) De Casteljau algorithm (to compute a 3D curve)
	 */
	public Point_3 iterativeDeCasteljau(Point_3[] controlPolygon, double t) {
		Point_3[] linPoints = new Point_3[2];
		double[] coeffs = {1-t,t};
		Point_3[] allPoints = new Point_3[controlPolygon.length];
		for(int i=0; i<controlPolygon.length; i++) {
			allPoints[i] = controlPolygon[i];
		}
		for(int r=1; r<controlPolygon.length; r++) {
			for(int i=0; i<controlPolygon.length-r; i++) {
				linPoints[0] = allPoints[i];
				linPoints[1] = allPoints[i+1];
				allPoints[i] = Point_3.linearCombination(linPoints,coeffs);
			}
		}
		return allPoints[0];
	}

	/**
	 * Compute the tensor product of two Bezier curves B(u), B(v)
	 */
	public Point_3 tensorProduct(int m, int n, double u, double v){
		double[] Bmu = new double[m+1];
		double[] Bnv = new double[n+1];
		Bmu[0] = Math.pow(1-u, m);
		Bnv[0] = Math.pow(1-v, n);
		for(int i=1; i<m+1; i++) {
			Bmu[i] = Bmu[i-1] * (n-i+1) * u /(i * (1-u));
		}
		for(int i=1; i<n+1; i++) {
			Bnv[i] = Bnv[i-1] * (n-i+1) * v /(i * (1-v));
		}
		double x=0;
		double y=0;
		double z=0;
		for(int i=0; i<m+1; i++) {
			for(int j=0; j<n+1; j++) {
				x+=Bmu[i]*Bnv[j]*this.points[i][j].x;
				y+=Bmu[i]*Bnv[j]*this.points[i][j].y;
				z+=Bmu[i]*Bnv[j]*this.points[i][j].z;
			}
		}
		return new Point_3(x,y,z);
	}
}
