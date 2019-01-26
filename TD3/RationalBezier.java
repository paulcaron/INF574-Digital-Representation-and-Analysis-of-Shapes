import java.util.LinkedList;

public class RationalBezier extends Curve {

	Transformation_2 transformation;
	double[][] newWeigths;

	public RationalBezier(DrawCurve frame, LinkedList<Point_2> p) {
		super(frame, p);
		transformation = null;
		newWeigths = new double[this.points.length][this.points.length];
		for(int i=0; i<this.points.length; i++) newWeigths[0][i] = weights[i];
	}

	public RationalBezier(DrawCurve frame, LinkedList<Point_2> p,
			Transformation_2 transformation) {
		super(frame, p);
		this.transformation = transformation;
		newWeigths = new double[this.points.length][this.points.length];
		for(int i=0; i<this.points.length; i++) newWeigths[0][i] = weights[i];
	}

	public Point_2 evaluate(double t) {
		return rationalDeCasteljau(this.points.length - 1, 0, t, this.points);
	}

	public void plotCurve(double dt) {
		this.drawControlPolygon();
		this.drawControlPoints();
		Point_2 formerPoint = new Point_2();
		Point_2 currentPoint = this.points[0];
		for(int i=1; i< (int) 1/dt; i++) {
			newWeigths = new double[this.points.length][this.points.length];
			for(int j=0; j<this.points.length; j++) newWeigths[0][j] = weights[j];
			formerPoint = currentPoint;
			currentPoint = evaluate(i*dt);
			drawSegment(formerPoint, currentPoint);
		}
	}
	
	// apply the transformation to all points (control points and curve)
		public void plotCurveAffine(double dt) {
			throw new Error("TD INF574: to be completed");
		}
		
		//apply the transformation only to the control points, and then compute the curve
		public void plotControl(double dt) {
			throw new Error("TD INF574: to be completed");
		}

	public void subdivisionRendering(int n) {
	}

	Point_2 rationalDeCasteljau(int r, int i, double t, Point_2[] pnts) {
		if(r==0) {
			return pnts[i];
		}
		Point_2 b1 = rationalDeCasteljau(r-1, i,  t, pnts);
		Point_2 b2 = rationalDeCasteljau(r-1, i+1,  t, pnts);
		newWeigths[r][i] = (1-t) * newWeigths[r-1][i] + t * newWeigths[r-1][i+1];
		double[] coefs = {(1-t) * newWeigths[r-1][i] / newWeigths[r][i] , t * newWeigths[r-1][i+1] /newWeigths[r][i] };
		Point_2[] linPoints = {b1,b2};
		return Point_2.linearCombination(linPoints, coefs);
	
	}
}
