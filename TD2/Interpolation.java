/**
 * Abstract class defining methods for implementing point interpolation
 * 
 * @author Luca Castelli Aleardi (INF555, 2012)
 * Student Paul Caron
 *
 */
public abstract class Interpolation {
	
	Draw frame; // drawing frame
	Point_2[] points; // the input points to interpolate
	Vector_2[] slopeList;
	
	public Interpolation(Draw frame) {
		this.frame=frame;
		updateInputPoints();
		updateInputSlopes();
	}
	
	public void updateInputPoints() {
		this.points=new Point_2[frame.points.size()];
		int i=0;
		for(Point_2 p: frame.points) {
			this.points[i]=p;
			i++;
		}		
	}
	
	public void updateInputSlopes() {
		this.slopeList=new Vector_2[frame.slopeList.size()];
		int i=0;
		for(Vector_2 p: frame.slopeList) {
			this.slopeList[i]=p;
			i++;
		}		
	}

	/**
	 * Draw a segment between two points (in the given frame)
	 */
	public void drawSegment(Point_2 p, Point_2 q) {
		this.frame.line((float)p.getX(), (float)p.getY(), (float)q.getX(), (float)q.getY());
	}
	
	/**
	 * Return the string "a[0]+a[1]x+a[2]x^2+...+a[n]x^n"
	 * array a[] gives the coefficients of the polynomial expression
	 */
	public static String polynomialToString(double[] a) {
		if(a==null || a.length==0) return "polynome not defined";
		String result=""+round(a[0], 1000);
		for(int i=1;i<a.length;i++) {
			String signe="+";
			if(a[i]<0) signe="";
			result=result+" "+signe+round(a[i], 1000)+"x^"+i;
		}
		return result;
	}

	/**
	 * Return the value after truncation
	 */
	public static double round(double x, int precision) {
		return ((int)(x*precision)/(double)precision);
	}

	/**
	 * Draw a circle at given location in the frame
	 */
	public void drawPoint(Point_2 p) {
		this.frame.ellipse((float)p.getX(), (float)p.getY(), 5, 5);
	}
	
	/**
	 * Compute point interpolation and draw the curve (in the frame)
	 */
	public abstract void interpolate();
	
}
