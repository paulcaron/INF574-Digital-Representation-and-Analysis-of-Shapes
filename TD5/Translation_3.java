import Jcg.geometry.*;

/**
 * Define a translation in 3D
 *
 * @author Luca Castelli Aleardi (INF555, 2014)
 */
public class Translation_3 {

	Vector_3 v;
	
	/**
	 * Initialize the translation
	 */
	public Translation_3(Vector_3 v) {
		this.v=v;
	}

	/**
	 * Translate point p by vector v (return a new point q)
	 */
	public Point_3 transform(Point_3 p) {
		return p.sum(this.v);
	}

}
