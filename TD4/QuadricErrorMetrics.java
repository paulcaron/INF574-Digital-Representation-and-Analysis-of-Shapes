import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import Jama.Matrix;
import Jcg.geometry.Point_3;
import Jcg.polyhedron.Face;
import Jcg.polyhedron.Halfedge;
import Jcg.polyhedron.Polyhedron_3;
import Jcg.polyhedron.Vertex;

/**
 * @author Luca Castelli Aleardi (INF555, 2012)
 *
 */
public class QuadricErrorMetrics extends MeshSimplification{
	
	public void intializePQ() {
		
	}

	public QuadricErrorMetrics(Polyhedron_3<Point_3> polyhedron3d) {
		super(polyhedron3d);
	}

	/**
	 * Basic example of simplification based on edge contractions
	 * Simply select at random edges to be contracted
	 */
	public void simplify() {
		System.out.println("To be completed");		
	}
	
	
	/**
	 * Check whether a given halfedge can be contracted
	 */
	boolean isLegal(Halfedge<Point_3> h){
		throw new Error("To be completed");
	}

}


