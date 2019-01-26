import java.util.LinkedList;
import java.util.Random;

import Jama.Matrix;

public class HermiteSplineInterpolation extends Interpolation{
	
	double[] coeffX=null;
	double[] coeffY=null;

	public HermiteSplineInterpolation(Draw frame) {
		super(frame);
	}
	

	@Override
	public void interpolate() {
		int pointsSize = this.points.length;
		
		for(int i=0;i<pointsSize;i++)
	    	drawPoint(this.points[i]);
				
		plotHermite(points, slopeList);
		
	}
	
	
	public void plotHermite(Point_2[] p, Vector_2[] sL){
		if(this.points.length>2)  {
			double differenceCoeff = Math.abs(this.points[this.points.length-1].x-this.points[0].x) / (2*this.points.length) ;
			computeCoefficients(this, p);
			for(int i=0; i<this.points.length-1; i++) {
				double[] coeffXplot = {this.coeffX[4*i],this.coeffX[4*i+1],this.coeffX[4*i+2],this.coeffX[4*i+3]};
				double[] coeffYplot = {this.coeffY[4*i],this.coeffY[4*i+1],this.coeffY[4*i+2],this.coeffY[4*i+3]};
				frame.stroke( (int)(4*255*i/this.points.length)%256,(int)(2*255*i/this.points.length)%256,(int)(255*i/this.points.length)%256);
				plotPolynomialCurve(coeffXplot, coeffYplot, 300);
				double len = Math.sqrt(getSlope(i).x*getSlope(i).x+getSlope(i).y*getSlope(i).y);
				Point_2 q = new Point_2(this.points[i].x+differenceCoeff*getSlope(i).x/len,this.points[i].y+differenceCoeff*getSlope(i).y/len);
				drawSegment(this.points[i],q);
			}
		}
		
	}
	
	public Vector_2 getSlope(int pointIndex){
		return slopeList[pointIndex];
	}
	
	void plotPolynomialCurve(double[] coeffX, double[] coeffY, int n) {
		Point_2[] plotPoints = new Point_2[n+1];
		double space = (double)1./n;
		
		for(int i=0;i<=n;i++){
			double t = (double)i*space;
			double xValue = 0;
			for(int l=0;l<coeffX.length;l++)
				xValue+=coeffX[l]*Math.pow(t,l);

			double yValue = 0;
			for(int l=0;l<coeffY.length;l++)
				yValue+=coeffY[l]*Math.pow(t,l);
			
			plotPoints[i]=new Point_2(xValue,yValue);
		}
			
		for(int i=0;i<n;i++)
			drawSegment(plotPoints[i], plotPoints[i+1]);
	}
	
	public void computeCoefficients(HermiteSplineInterpolation interpolation, Point_2[] P) {
		double[][] M = new double[4*(P.length-1)][4*(P.length-1)];
		double[][] Vx = new double[4*(P.length-1)][1];
		double[][] Vy = new double[4*(P.length-1)][1];
		for(int i=0;i<P.length-1;i++) {
			M[4*i][4*i] = 1;
			M[4*i+1][4*i] = 1;
			M[4*i+1][4*i+1] = 1;
			M[4*i+1][4*i+2] = 1;
			M[4*i+1][4*i+3] = 1;
			M[4*i+2][4*i+1] = 1;
			M[4*i+3][4*i+1] = 1;
			M[4*i+3][4*i+2] = 2;
			M[4*i+3][4*i+3] = 3;
			Vx[4*i][0] = P[i].x;
			Vx[4*i+1][0] = P[i+1].x;
			Vx[4*i+2][0] = interpolation.getSlope(i).x;
			Vx[4*i+3][0] = interpolation.getSlope(i+1).x;
			Vy[4*i][0] = P[i].y;
			Vy[4*i+1][0] = P[i+1].y;
			Vy[4*i+2][0] = interpolation.getSlope(i).y;
			Vy[4*i+3][0] = interpolation.getSlope(i+1).y;
		}
		Matrix matrixM = new Matrix(M);
		Matrix matrixVx = new Matrix(Vx);
		Matrix matrixVy = new Matrix(Vy);
		interpolation.coeffX = matrixM.solve(matrixVx).getColumnPackedCopy();
		interpolation.coeffY = matrixM.solve(matrixVy).getColumnPackedCopy();
	}
	
	
	
}
