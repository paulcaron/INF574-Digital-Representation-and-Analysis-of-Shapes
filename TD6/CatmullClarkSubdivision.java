import Jcg.geometry.*;
import Jcg.polyhedron.*;

import java.util.*;

/**
 * 
 * Student: Paul Caron
 * I thonk that the structure is good. But the code does not work.
 * There are still elements to complete...
 */ 
public class CatmullClarkSubdivision extends MeshSubdivision {
	
	public CatmullClarkSubdivision(Polyhedron_3<Point_3> polyhedron) {
		super(polyhedron);
		
	}
	
	/**
	 * The main method performing the subdivision process
	 * To be implemented
	 */
	public void subdivide() {
		HashSet<Vertex<Point_3>> originalVertices = new HashSet<Vertex<Point_3>>();
		for(Vertex<Point_3> v : this.polyhedron3D.vertices) originalVertices.add(v);
		HashMap<Face<Point_3>, Point_3> facePoints = this.computeCentroids();
		HashMap<Halfedge<Point_3>, Point_3> edgePoints = this.computeEdgePoints(facePoints);
		splitEdges(edgePoints);	
		ArrayList<Face<Point_3>> newFaces = new ArrayList<Face<Point_3>>();
		for(Face<Point_3> f : this.polyhedron3D.facets) {
			Vertex<Point_3> center = new Vertex<Point_3>(); center.setPoint(facePoints.get(f));
	
			Halfedge<Point_3> e = f.getEdge();
			if(originalVertices.contains(e.getVertex())) e = e.getNext();
			Halfedge<Point_3> pe = e.getNext().getNext();
			Halfedge<Point_3> g1 = new Halfedge<Point_3>();
			Halfedge<Point_3> g2 = new Halfedge<Point_3>();
			g1.setPrev(e);
			g1.setVertex(center);
			g2.setVertex(e.getVertex());
			g2.setNext(e.getNext());
			g1.setOpposite(g2);
			g2.setOpposite(g1);
			e.getNext().setPrev(g2);
			e.setNext(g1);
			Halfedge<Point_3> g2p = g1;
			int n=0;
			while(!pe.equals(e)) {
				n++; System.out.println(n);
				Halfedge<Point_3> pen = pe.getNext().getNext();
				Halfedge<Point_3> g1n = new Halfedge<Point_3>();
				Halfedge<Point_3> g2n = new Halfedge<Point_3>();
				Face<Point_3> newF = new Face<Point_3>();
				newF.setEdge(pe);
				g1n.setFace(newF); g1n.setNext(g2p);g1n.setPrev(pe);g1n.setVertex(center); g1n.setOpposite(g2n);
				g2n.setFace(f); g2n.setNext(pe.getNext());g2n.setVertex(pe.getVertex()); g2n.setOpposite(g1n);
				g2p.setFace(newF); pe.getPrev().setFace(newF);pe.setFace(newF);
				g2p.setPrev(g1n);
				g2p = g2n;
				pe.getNext().setPrev(g2n);
				pe.setNext(g1n);
				newFaces.add(newF);
				this.polyhedron3D.halfedges.add(g1n); this.polyhedron3D.halfedges.add(g2n);
				pe = pen;
			}
			g1.setNext(g2p);
			g2p.setPrev(g1);
			this.polyhedron3D.halfedges.add(g1);
		}
		
		
		for(Face<Point_3> f : newFaces) this.polyhedron3D.facets.add(f);
	}
	
	public Point_3 computeCentroid(Face<Point_3> f) {
		
		Halfedge<Point_3> e = f.getEdge();
		Halfedge<Point_3> ep = e.next;
		int n=1;
		while(!ep.equals(e)) {
			n++;
			ep = ep.next;
		}
		Point_3[] points = new Point_3[n];
		Number[] coefficients = new Number[n];
		for(int i=0; i<n; i++) {
			points[i] = e.getVertex().getPoint();
			coefficients[i] = 1./n;
			ep = ep.next;
		}
		return Point_3.linearCombination(points, coefficients);
	}
	
	public HashMap<Face<Point_3>, Point_3> computeCentroids(){
		HashMap<Face<Point_3>, Point_3> map = new HashMap<Face<Point_3>, Point_3>();
		for(Face<Point_3> f : this.polyhedron3D.facets) map.put(f, computeCentroid(f));
		return map;
	}
	
	public Point_3 computeEdgePoint(Halfedge<Point_3> h, HashMap<Face<Point_3>, Point_3> face_map) { 
		Point_3[] points = {h.getVertex().getPoint(), h.getOpposite().getVertex().getPoint(), face_map.get(h.getFace()), face_map.get(h.getOpposite().getFace())};
		Number[] coefficients = {1./4,1./4,1./4,1./4};
		return Point_3.linearCombination(points, coefficients);
	}

	
	public HashMap<Halfedge<Point_3>, Point_3> computeEdgePoints(HashMap<Face<Point_3>, Point_3> face_map) {
		HashMap<Halfedge<Point_3>, Point_3> edge_map = new HashMap<Halfedge<Point_3>, Point_3>();
		for(Halfedge<Point_3> halfedge : this.polyhedron3D.halfedges) {
			edge_map.put(halfedge, this.computeEdgePoint(halfedge, face_map));
		}
		return edge_map;
	}
	
	public void splitEdges(HashMap<Halfedge<Point_3>, Point_3> edgePoints) {
		System.out.print("Splitting edges...");
		for (Halfedge<Point_3> h : edgePoints.keySet()) {
			this.polyhedron3D.splitEdge(h, edgePoints.get(h));
		}
		System.out.println("done");
	}
	
	public void subdivideFace(Face<Point_3> f) {
		Halfedge<Point_3> h = f.getEdge();
		this.polyhedron3D.splitFacet(h, h.getNext().getNext());
		this.polyhedron3D.splitFacet(h.getNext().getNext(), h.getNext().getNext().getNext().getNext());
		this.polyhedron3D.splitFacet(h.getNext().getNext(), h);
	}

	
}

