import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.TreeSet;

import Jcg.geometry.*;
import Jcg.polyhedron.*;

/**
 * @author Luca Castelli Aleardi (INF555, 2012)
 *
 */
public class EdgeContraction extends MeshSimplification {

	PriorityQueue<HalfedgeComparable> queue;

	public EdgeContraction(Polyhedron_3<Point_3> polyhedron3d) {
		super(polyhedron3d);
		this.queue = new PriorityQueue<HalfedgeComparable>();
	}
	
	public void intializePQ() {
		for(Halfedge<Point_3> halfEdge : this.polyhedron3D.halfedges) {
			this.queue.add(new HalfedgeComparable(halfEdge));
		}
	}

	/**
	 * Basic example of simplification based on edge contractions
	 * Simply select at random edges to be contracted
	 */
/*
	public void simplify() {
		if (this.polyhedron3D.halfedges.size()==0) return;
		int i = (int)(Math.random() *  this.polyhedron3D.halfedges.size());
		Halfedge<Point_3> halfedge = this.polyhedron3D.halfedges.get(i);
		if(!this.isLegal(halfedge)) return;
		edgeCollapseWithNewLocation(halfedge);
		return;
	}
*/
	/*
	public void simplify() {
		if (this.polyhedron3D.halfedges.size()==0) return;
		int i = (int)(Math.random() *  this.polyhedron3D.halfedges.size());
		Halfedge<Point_3> halfedge = this.polyhedron3D.halfedges.get(i);
		if(!this.isLegal(halfedge)) return;
		edgeCollapseWithNewLocationAllBarycenters(halfedge);
		return;	
	}
	*/
	
	
	public void simplify() {
		if(queue.isEmpty())return;
		Halfedge<Point_3> e = this.queue.remove().halfEdge;
		if(!this.polyhedron3D.halfedges.contains(e) || !this.isLegal(e)) return;
		
		System.out.print("Performing edge collapse...");
		if(this.polyhedron3D==null || e==null)
			return;
		
		// retrieve the cells incident to edge e
		Face<Point_3> f1=e.getFace();
		Face<Point_3> f2=e.getOpposite().getFace();
		Vertex<Point_3> u=e.getOpposite().getVertex();
		Vertex<Point_3> v=e.getVertex();
		Vertex<Point_3> w=e.getNext().getVertex();
		Vertex<Point_3> z=e.getOpposite().getNext().getVertex();
		
		LinkedList<Point_3> l = new LinkedList<Point_3>();
		l.addLast(u.getPoint());
		l.addLast(v.getPoint());
		
		Halfedge<Point_3> pEdge = e.opposite.next;
		while(!pEdge.equals(e)) {
			l.addLast(pEdge.vertex.getPoint());
			pEdge = pEdge.opposite.next;
		}
		
		
		pEdge = e.next;
		while(!pEdge.equals(e.opposite)) {
			l.addLast(pEdge.vertex.getPoint());
			pEdge = pEdge.opposite.next;
		}
		
		
		Point_3[] points = new Point_3[l.size()];
		int index=0;
		for(Point_3 point : l) {
			points[index] = point;
			index++;
		}
		
		
		Point_3 newPoint = new Point_3();
		newPoint.barycenter(points);
		u.setPoint(newPoint);
		
		Halfedge<Point_3> eUW=e.prev.opposite;
		Halfedge<Point_3> eWV=e.next.opposite;
		Halfedge<Point_3> eZU=e.opposite.next.opposite;
		Halfedge<Point_3> eVZ=e.opposite.prev.opposite;
		
		// update references between neighboring cells
		eUW.opposite=eWV;
		eWV.opposite=eUW;
		eZU.opposite=eVZ;
		eVZ.opposite=eZU;
		this.queue.add(new HalfedgeComparable(eUW));
		this.queue.add(new HalfedgeComparable(eWV));
		this.queue.add(new HalfedgeComparable(eZU));
		this.queue.add(new HalfedgeComparable(eVZ));
		u.setEdge(eZU);
		w.setEdge(eUW);
		z.setEdge(eVZ);
		
		
		pEdge=eWV;
		while(pEdge!=eVZ.getOpposite()) {
			pEdge.setVertex(u);
			this.queue.add(new HalfedgeComparable(pEdge));
			pEdge=pEdge.getNext().getOpposite();
		}
		
		
		// remove old cells: 2 faces, 1 vertex, 6 halfedges
		this.polyhedron3D.vertices.remove(v);
		this.polyhedron3D.facets.remove(f1);
		this.polyhedron3D.facets.remove(f2);
		this.polyhedron3D.halfedges.remove(e);
		this.polyhedron3D.halfedges.remove(e.next);
		this.polyhedron3D.halfedges.remove(e.prev);
		this.polyhedron3D.halfedges.remove(e.opposite);
		this.polyhedron3D.halfedges.remove(e.opposite.next);
		this.polyhedron3D.halfedges.remove(e.opposite.prev);
		System.out.println("done");
		
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
	
	static class HalfedgeComparable implements Comparable<HalfedgeComparable> {
		Halfedge<Point_3> halfEdge;
		
		public HalfedgeComparable(Halfedge<Point_3> h) {
			this.halfEdge = h;
		}
		
		public int compareTo(HalfedgeComparable o){
			double val1, val2;
			val1 = Math.pow(this.halfEdge.vertex.getPoint().x-this.halfEdge.prev.vertex.getPoint().x, 2)+Math.pow(this.halfEdge.vertex.getPoint().y-this.halfEdge.prev.vertex.getPoint().y, 2)+Math.pow(this.halfEdge.vertex.getPoint().z-this.halfEdge.prev.vertex.getPoint().z, 2);
			val2 = Math.pow(o.halfEdge.vertex.getPoint().x-o.halfEdge.prev.vertex.getPoint().x, 2)+Math.pow(o.halfEdge.vertex.getPoint().y-o.halfEdge.prev.vertex.getPoint().y, 2)+Math.pow(o.halfEdge.vertex.getPoint().z-o.halfEdge.prev.vertex.getPoint().z, 2);
			if(val1<val2) return -1;
			return 1;
		}
		
		public boolean equals(Halfedge<Point_3> o){
			double val1, val2;
			val1 = Math.pow(this.halfEdge.vertex.getPoint().x-this.halfEdge.prev.vertex.getPoint().x, 2)+Math.pow(this.halfEdge.vertex.getPoint().y-this.halfEdge.prev.vertex.getPoint().y, 2)+Math.pow(this.halfEdge.vertex.getPoint().z-this.halfEdge.prev.vertex.getPoint().z, 2);
			val2 = Math.pow(o.vertex.getPoint().x-o.prev.vertex.getPoint().x, 2)+Math.pow(o.vertex.getPoint().y-o.prev.vertex.getPoint().y, 2)+Math.pow(o.vertex.getPoint().z-o.prev.vertex.getPoint().z, 2);
			return val1==val2;
		}
		
	}
}


