import java.util.LinkedList;

public class Bezier extends Curve {

	Transformation_2 transformation;

	public Bezier(DrawCurve frame, LinkedList<Point_2> p) {
		super(frame, p);
		transformation=new Transformation_2(new Vector_2(100.,0.));
	}

	public Bezier(DrawCurve frame, LinkedList<Point_2> p, Transformation_2 transformation) {
		super(frame, p);
		this.transformation=transformation;
	}
	
	public Bezier(DrawCurve frame, Point_2[] points, Transformation_2 transformation) {
		super(frame, points);
		this.transformation=transformation;
	}

	/**
	 * Draw the control polygon
	 */
	public void drawControlPolygon() {
		this.frame.stroke(0, 0, 255);
	    for(int i=1;i<this.points.length;i++) {
	    	drawSegment(this.points[i], this.points[i-1]);
	    	drawSegment(this.transformation.transform(this.points[i]), this.transformation.transform(this.points[i-1]));
	    }
		this.frame.stroke(0, 0, 0);
	}

	/**
	 * Evaluate the curve for parameter t
	 * Return point (x(t), y(t))
	 */
	public Point_2 evaluate(double t) {
		//return recursiveDeCasteljau(this.points.length-1, 0, t);
		//return iterativeDeCasteljau(t);
		return BernsteinBezier(t);
	}

	/**
	 * Perform the subdivision (once) of the Bezier curve (with parameter t)
	 * Return two Bezier curves (with n control points each)
	 */
	public Bezier[] subdivide(double t) {
		int n=this.points.length-1; // degree and number of edges of the control polygon

		Point_2[] b0=new Point_2[n+1]; // first control polygon
		Point_2[] b1=new Point_2[n+1]; // second control polygon
		Bezier[] result=new Bezier[2]; // the pair of Bezier curves to return as result

		Point_2[] linPoints = new Point_2[2];
		double[] coeffs = {1-t,t};
		Point_2[] allPoints = new Point_2[this.points.length];
		for(int i=0; i<this.points.length; i++) {
			allPoints[i] = this.points[i];
		}
		for(int r=1; r<this.points.length; r++) {
			b0[r-1] = allPoints[0];
			b1[n-r+1] = allPoints[n-r+1];
			for(int i=0; i<this.points.length-r; i++) {
				linPoints[0] = allPoints[i];
				linPoints[1] = allPoints[i+1];
				allPoints[i] = Point_2.linearCombination(linPoints,coeffs);
			}
		}
		b0[n] = allPoints[0];
		b1[0] = allPoints[0];
		
		result[0] = new Bezier(this.frame, b0, this.transformation);
		result[1] = new Bezier(this.frame, b1, this.transformation);
		return result;
	}

	/**
	 * Plot the curve (in the frame), for t=0..1, with step dt
	 */
	public void plotCurve(double dt) {
		this.drawControlPolygon();
		this.drawControlPoints();
		Point_2 formerPoint = new Point_2();
		Point_2 currentPoint = this.points[0];
		Point_2 formerTransformedPoint = new Point_2();
		Point_2 currentTransformedPoint = this.transformation.transform(currentPoint);
		for(int i=1; i< (int) 1/dt; i++) {
			formerPoint = currentPoint;
			currentPoint = evaluate(i*dt);
			formerTransformedPoint = currentTransformedPoint;
			currentTransformedPoint = this.transformation.transform(currentPoint);
			drawSegment(formerPoint, currentPoint);
			drawSegment(formerTransformedPoint, currentTransformedPoint);
		}
		// to be completed TD INF574
	}

	/**
	 * Perform the rendering of the curve using subdivision approach
	 * Perform the subdivision n times
	 */
	public void subdivisionRendering(int n) {
		this.drawControlPolygon(); // draw original control polygon
		this.drawControlPoints(); // draw original control points
		LinkedList<Bezier> subCurves=new LinkedList<Bezier>();
		subCurves.add(this);
		// to be completed TD INF574:
		if(this.points.length<3) return;
		
		Bezier[] curves;
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<Math.pow(2, i); j++) {
				curves = subCurves.getLast().subdivide(0.5);
				subCurves.addFirst(curves[0]);
				subCurves.addFirst(curves[1]);
				subCurves.removeLast();
			}
		}
		for(Bezier curve : subCurves) {
			curve.drawControlPolygon();
		}
		

	}

	public Point_2 recursiveDeCasteljau(int r, int i, double t) {
		if(r==0) {return this.points[i];}
		Point_2 P1 = recursiveDeCasteljau(r-1, i, t);
		Point_2 P2 = recursiveDeCasteljau(r-1, i+1, t);
		return new Point_2((1-t)*P1.x+t*P2.x,(1-t)*P1.y+t*P2.y);
	}

	/**
	 * Perform the (iterative) De Casteljau algorithm to evaluate b(t)
	 */
	public Point_2 iterativeDeCasteljau(double t) {
		Point_2[] linPoints = new Point_2[2];
		double[] coeffs = {1-t,t};
		Point_2[] allPoints = new Point_2[this.points.length];
		for(int i=0; i<this.points.length; i++) {
			allPoints[i] = this.points[i];
		}
		for(int r=1; r<this.points.length; r++) {
			for(int i=0; i<this.points.length-r; i++) {
				linPoints[0] = allPoints[i];
				linPoints[1] = allPoints[i+1];
				allPoints[i] = Point_2.linearCombination(linPoints,coeffs);
			}
		}
		return allPoints[0];
	}
	
	Point_2 BernsteinBezier(double t) {
		
		double[] coefs = {1-t,1};
		double t_pow = 1;
		Point_2 result = this.points[0];
		Point_2[] linPoints = new Point_2[2];
		int binomialCoefficient = 1;
		for (int i = 1; i<this.points.length; i++) {
			binomialCoefficient = (binomialCoefficient * (this.points.length-i)) / i;
			t_pow*=t;
			coefs[1] = t_pow * binomialCoefficient;
			linPoints[0] = result;
			linPoints[1] = this.points[i];
			result = Point_2.linearCombination(linPoints, coefs);
		}

		return result;
		
		
	}

}
