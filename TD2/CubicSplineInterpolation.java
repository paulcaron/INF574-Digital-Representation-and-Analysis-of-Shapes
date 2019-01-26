import Jama.*;

/**
 * Cubic spline interpolation
 * 
 * @author Luca Castelli Aleardi (INF555, 2014)
 * Student Paul Caron
 *
 */
public class CubicSplineInterpolation extends Interpolation {
	
	double[] coeff=null;

	public CubicSplineInterpolation(Draw frame) {
		super(frame);
	}
	
	public static double[] computeCoefficients(Point_2[] P) {
		double[][] M = new double[4*(P.length-1)][4*(P.length-1)];
		double[][] y = new double[4*(P.length-1)][1];
		for(int i=0;i<P.length-1;i++) {
			for(int j=0;j<4;j++) {
				M[4*i+1][4*i+j] = Math.pow(P[i].x,j);
				M[4*i+2][4*i+j] = Math.pow(P[i+1].x,j);
			}
			y[4*i+1][0] = P[i].y;
			y[4*i+2][0] = P[i+1].y;
		}
		for(int i=0;i<P.length-2;i++) {
			for(int j=1;j<4;j++) {
				M[4*i+3][4*i+j] = j*Math.pow(P[i+1].x,j-1);
				M[4*i+4][4*i+j] = j*(j+1)*Math.pow(P[i+1].x,j-2);
				M[4*i+3][4*i+4+j] = -j*Math.pow(P[i+1].x,j-1);
				M[4*i+4][4*i+4+j] = -j*(j+1)*Math.pow(P[i+1].x,j-2);
			}
		}
		for(int j=1;j<4;j++) {
			M[0][j] = j*Math.pow(P[0].x, j-1);
			M[4*(P.length-1)-1][4*(P.length-1)-4+j] = j*Math.pow(P[P.length-1].x, j-1);
		}
		
		Matrix matrixM = new Matrix(M);
		Matrix matrixY = new Matrix(y);
		
		return matrixM.solve(matrixY).getRowPackedCopy();
	}
	
	/**
	 * Evaluate polynomial a[0]+a[1]x+a[2]x^2+...+a[n]x^n, at point x
	 */
	public static double evaluate(double[] a, double x) {
		if(a==null || a.length==0) throw new Error("polynomial not defined");
		double result=a[0];
		double p=1.;
		for(int i=1;i<a.length;i++) {
			p=p*x;
			//System.out.println(""+a[i]+"*"+p+"="+(a[i]*p));
			result=result+(a[i]*p);
		}
		return result;
	}

	public void interpolate() {
		if(this.points.length>2)  {
			double differenceCoeff = Math.abs(this.points[this.points.length-1].x-this.points[0].x) / (2*this.points.length) ;
			this.coeff=computeCoefficients(this.points);
			for(int i=0; i<this.points.length-1; i++) {
				frame.stroke( (int)(4*255*i/this.points.length)%256,(int)(2*255*i/this.points.length)%256,(int)(255*i/this.points.length)%256);
				double[] coeffs = {this.coeff[4*i],this.coeff[4*i+1],this.coeff[4*i+2],this.coeff[4*i+3]};
				plotPolynomial(coeffs, this.points[i].getX(), this.points[i+1].getX(), 300);
				if(i>0) {
					double[] derivateCoeffs = {this.coeff[4*i+1],2*this.coeff[4*i+2],3*this.coeff[4*i+3]};
					double derivate = evaluate(derivateCoeffs, this.points[i].x);
					double len = Math.sqrt(1+derivate*derivate);
					Point_2 q = new Point_2(this.points[i].x+differenceCoeff/len,this.points[i].y+differenceCoeff*derivate/len);
					drawSegment(this.points[i],q);
				}
			}
		}

		for(Point_2 q:this.points) {
	    	drawPoint(q); // draw input points
	    }
	}
	
	public void plotPolynomial(double[] a, double min, double max, int n) {
		double dx=(max-min)/n;
		
		double x=min;
		for(int i=0;i<n;i++) {
			Point_2 p=new Point_2(x, evaluate(a, x));
			Point_2 q=new Point_2(x+dx, evaluate(a, x+dx));
			this.drawSegment(p, q);
			x=x+dx;
		}
		
	}

}
