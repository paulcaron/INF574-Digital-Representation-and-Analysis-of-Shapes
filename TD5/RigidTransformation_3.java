import Jcg.geometry.*;

/**
 * Abstract class for 3D body rigid transformation calculations
 *  
 * @author Luca Castelli Aleardi (INF555, 2014)
 *
 */
public abstract class RigidTransformation_3 {
	
	public RigidTransformation_3() {
	}

	/**
	 * The main computing the rotation
	 * To be implemented
	 */
	public abstract Rotation_3 getRotation(PointSet points1, PointSet p2);

	/**
	 * The main computing the translation
	 * To be implemented
	 */
	public abstract Translation_3 getTranslation(PointSet points1, PointSet p2);

}
