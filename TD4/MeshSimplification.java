import java.util.LinkedList;

import Jcg.geometry.Point_3;
import Jcg.polyhedron.*;

/**
 * Abstract class defining methods for performing mesh simplification (via incremental decimation)
 * @author Luca Castelli Aleardi
 *
 */
public abstract class MeshSimplification {

	public Polyhedron_3<Point_3> polyhedron3D;
	
	public abstract void intializePQ();
	
	public MeshSimplification(Polyhedron_3<Point_3> polyhedron3D) {
		this.polyhedron3D=polyhedron3D;
	}

	/**
	 * The main method performing the simplification process
	 * To be implemented
	 */
	public abstract void simplify();

	/**
	 * Perform the collapse of edge e=(u,v)
	 * Edge e is shared by triangles (u,v,w) and (v,u,z)
	 */
	public void edgeCollapse(Halfedge<Point_3> e) {
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
		Halfedge<Point_3> eUW=e.prev.opposite;
		Halfedge<Point_3> eWV=e.next.opposite;
		Halfedge<Point_3> eZU=e.opposite.next.opposite;
		Halfedge<Point_3> eVZ=e.opposite.prev.opposite;
		
		// update references between neighboring cells
		eUW.opposite=eWV;
		eWV.opposite=eUW;
		eZU.opposite=eVZ;
		eVZ.opposite=eZU;
		u.setEdge(eZU);
		w.setEdge(eUW);
		z.setEdge(eVZ);
		
		Halfedge<Point_3> pEdge=eWV;
		while(pEdge!=eVZ.getOpposite()) {
			pEdge.setVertex(u);
			pEdge=pEdge.getNext().getOpposite();
		}
		
		Point_3 newPoint=new Point_3(u.getPoint()); // compute new point location
		u.setPoint(newPoint);
		
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
	
	public void edgeCollapseWithNewLocation(Halfedge<Point_3> e) {
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
		Halfedge<Point_3> eUW=e.prev.opposite;
		Halfedge<Point_3> eWV=e.next.opposite;
		Halfedge<Point_3> eZU=e.opposite.next.opposite;
		Halfedge<Point_3> eVZ=e.opposite.prev.opposite;
		
		// update references between neighboring cells
		eUW.opposite=eWV;
		eWV.opposite=eUW;
		eZU.opposite=eVZ;
		eVZ.opposite=eZU;
		u.setEdge(eZU);
		w.setEdge(eUW);
		z.setEdge(eVZ);
		
		Halfedge<Point_3> pEdge=eWV;
		while(pEdge!=eVZ.getOpposite()) {
			pEdge.setVertex(u);
			pEdge=pEdge.getNext().getOpposite();
		}
		
		Point_3[] points = {u.getPoint(),v.getPoint()};
		Point_3 newPoint = new Point_3(); // compute new point location
		newPoint.barycenter(points);
		 
		u.setPoint(newPoint);
		
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
	
	public void edgeCollapseWithNewLocationAllBarycenters(Halfedge<Point_3> e) {
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
		
		Halfedge<Point_3> eUW=e.prev.opposite;
		Halfedge<Point_3> eWV=e.next.opposite;
		Halfedge<Point_3> eZU=e.opposite.next.opposite;
		Halfedge<Point_3> eVZ=e.opposite.prev.opposite;
		
		// update references between neighboring cells
		eUW.opposite=eWV;
		eWV.opposite=eUW;
		eZU.opposite=eVZ;
		eVZ.opposite=eZU;
		u.setEdge(eZU);
		w.setEdge(eUW);
		z.setEdge(eVZ);
		
		
		pEdge=eWV;
		while(pEdge!=eVZ.getOpposite()) {
			pEdge.setVertex(u);
			pEdge=pEdge.getNext().getOpposite();
		}
		
		

		 
		u.setPoint(newPoint);
		
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

}
