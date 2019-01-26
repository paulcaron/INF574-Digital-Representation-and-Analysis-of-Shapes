import java.util.TreeSet;

import Jcg.geometry.*;
import Jcg.polyhedron.*;

/**
 * @author Luca Castelli Aleardi (INF555, 2012)
 *
 */
public class HalfedgeContraction extends MeshSimplification {
	
	public HalfedgeContraction(Polyhedron_3<Point_3> polyhedron3D) {
		super(polyhedron3D);
	}
	
	public void intializePQ() {
		
	}
	
	/**
	 * Basic example of simplification based on edge contractions
	 * Simply select at random edges to be contracted
	 */
	public void simplify() {
		if (this.polyhedron3D.halfedges.size()==0) return;
		int i = (int)(Math.random() *  this.polyhedron3D.halfedges.size());
		Halfedge<Point_3> halfedge = this.polyhedron3D.halfedges.get(i);
		if(!this.isLegal(halfedge)) return;
		edgeCollapse(halfedge);
		return;
	}
	
	
	/**
	 * Check whether a given halfedge can be contracted
	 */
	boolean isLegal(Halfedge<Point_3> h){
		TreeSet<Point_3> set = new TreeSet<Point_3>();
		Halfedge<Point_3> pEdge = h.opposite.next;
		while(!pEdge.equals(h)) {
			if(!pEdge.face.equals(h.face) && !pEdge.face.equals(h.opposite.face)) set.add(pEdge.vertex.getPoint());
			pEdge = pEdge.opposite.next;
			}
		Halfedge<Point_3> h_next = h.next;
		pEdge = h_next.opposite.next;
		while(!pEdge.equals(h_next)) {
			if(set.contains(pEdge.vertex.getPoint())) return false;
			pEdge = pEdge.opposite.next;
		}
		return true;
	}
	
}
