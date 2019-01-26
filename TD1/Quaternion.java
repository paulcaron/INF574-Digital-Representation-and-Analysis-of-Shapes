import processing.core.PApplet;

/**
 * A simple 3d viewer for visualizing cubes in 3D space
 * 
 * @author Luca Castelli Aleardi (INF574, 2018)
 * Student Paul Caron
 *
 */
public class Quaternion {

	float w, x, y, z;

	Quaternion() {
		w = 1.0f;
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	Quaternion(float w, float x, float y, float z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	void reset() {
		w = 1.0f;
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	void set(float w, Vector_3 v) {
		this.w = w;
		x = (float)v.getX();
		y = (float)v.getY();
		z = (float)v.getZ();
	}

	void set(Quaternion q) {
		w = q.w;
		x = q.x;
		y = q.y;
		z = q.z;
	}

	/**
	 * Perform the multiplication of two quaternions
	 */
	static Quaternion multiply(Quaternion q1, Quaternion q2) {
		float w = q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z;
		float x = q1.w*q2.x + q1.x*q2.w + q1.y*q2.z - q1.z*q2.y;
		float y = q1.w*q2.y + q1.y*q2.w + q1.z*q2.x - q1.x*q2.z;
		float z = q1.w*q2.z + q1.z*q2.w + q1.x*q2.y - q1.y*q2.x;
		return new Quaternion(w,x,y,z);
	}

	/**
	 * Transform a quaternion in a 4-uple: (w, x, y, z), where
	 * 'w' is a scalar and (x, y, z) are the components of a unitary vector.
	 * 
	 * @return  a vector of floats (with 4 components)
	 */
	float[] getValue() {
		float[] value = { 2 * (float)Math.acos(this.w), this.x, this.y, this.z };
		return value;
	}

}
