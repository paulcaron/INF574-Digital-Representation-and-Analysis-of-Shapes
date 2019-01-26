/**
 * Simple scheme implementing linear interpolation
 * 
 * @author Luca Castelli Aleardi (INF555, 2012)
 * Student Paul Caron
 *
 */
public class LinearInterpolation extends Interpolation {
	
	public LinearInterpolation(Draw frame) {
		super(frame);
	}
	
	public void interpolate() {	
		for(int i=0 ; i<points.length-1;i++) {drawSegment(points[i],  points[i+1]); drawPoint(points[i]);}
		drawPoint(points[points.length-1]);
	}
	
}
