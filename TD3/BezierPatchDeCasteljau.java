import Jama.Matrix;
/**
 * @author Luca Castelli Aleardi (INF555, 2014)
 * 
 * Compute a Bezier Patch surface in 3D (iterative De Casteljau)
 */
public class BezierPatchDeCasteljau extends Surface {
	
	public BezierPatchDeCasteljau(DrawSurface frame) {
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
		double[][] arrayu = {{1.-u,u}};
		double[][] arrayv = {{1.-v},{v}};
		double[][] arrayx = new double[2][2];
		double[][] arrayy = new double[2][2];
		double[][] arrayz = new double[2][2];
		Matrix Mu = new Matrix(arrayu);
		Matrix Mv = new Matrix(arrayv);
		Matrix Mx;
		Matrix My;
		Matrix Mz;
		Point_3[][] b_1 = new Point_3[this.points.length][this.points.length];
		Point_3[][] b_2 = new Point_3[this.points.length][this.points.length];
		for(int i=0; i<this.points.length; i++) {
			for(int j=0; j<this.points.length; j++) {
				b_1[i][j] = this.points[i][j];
			}
		}
		for(int r=1; r<points.length; r++) {
			if(r%2==1) {
				for(int i=0; i<this.points.length-r; i++) {
					for(int j=0; j<this.points.length-r; j++) {
						arrayx[0][0] = b_1[i][j].x;
						arrayx[0][1] = b_1[i][j+1].x;
						arrayx[1][0] = b_1[i+1][j].x;
						arrayx[1][1] = b_1[i+1][j+1].x;
						arrayy[0][0] = b_1[i][j] .y;
						arrayy[0][1] = b_1[i][j+1].y;
						arrayy[1][0] = b_1[i+1][j].y;
						arrayy[1][1] = b_1[i+1][j+1].y;
						arrayz[0][0] = b_1[i][j] .z;
						arrayz[0][1] = b_1[i][j+1].z;
						arrayz[1][0] = b_1[i+1][j].z;
						arrayz[1][1] = b_1[i+1][j+1].z;
						Mx = new Matrix(arrayx);
						My = new Matrix(arrayy);
						Mz = new Matrix(arrayz);
						b_2[i][j] = new Point_3(((Mu.times(Mx)).times(Mv)).get(0, 0),((Mu.times(My)).times(Mv)).get(0, 0),((Mu.times(Mz).times(Mv))).get(0, 0));
					}
				}
			}
			else {
				for(int i=0; i<this.points.length-r; i++) {
					for(int j=0; j<this.points.length-r; j++) {
						arrayx[0][0] = b_2[i][j].x;
						arrayx[0][1] = b_2[i][j+1].x;
						arrayx[1][0] = b_2[i+1][j].x;
						arrayx[1][1] = b_2[i+1][j+1].x;
						arrayy[0][0] = b_2[i][j] .y;
						arrayy[0][1] = b_2[i][j+1].y;
						arrayy[1][0] = b_2[i+1][j].y;
						arrayy[1][1] = b_2[i+1][j+1].y;
						arrayz[0][0] = b_2[i][j] .z;
						arrayz[0][1] = b_2[i][j+1].z;
						arrayz[1][0] = b_2[i+1][j].z;
						arrayz[1][1] = b_2[i+1][j+1].z;
						Mx = new Matrix(arrayx);
						My = new Matrix(arrayy);
						Mz = new Matrix(arrayz);
						b_1[i][j] = new Point_3(Mu.times(Mx).times(Mv).get(0, 0),Mu.times(My).times(Mv).get(0, 0),Mu.times(Mz).times(Mv).get(0, 0));
					}
				}
			}
		}
		
		if(points.length%2==0) return b_1[0][0];
		return b_2[0][0];

	}
	

}
