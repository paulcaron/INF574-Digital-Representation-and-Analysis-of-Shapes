import Jcg.geometry.*;
import Jcg.polyhedron.*;

import java.awt.PageAttributes.OriginType;
import java.util.*;

/**
 * Perform the Loop subdivision scheme
 * Only for triangular meshes (without boundary)
 * 
 * @author Luca Castelli Aleardi (INF555, 2012)
 * Student: Paul Caron
 *
 */
public class LoopSubdivision extends MeshSubdivision {
		
	public LoopSubdivision(Polyhedron_3<Point_3> polyhedron) {
		super(polyhedron);
		
	}
	
	/**
	 * The main method performing the subdivision process
	 * To be implemented
	 */
	public void subdivide() {
		HashSet<Vertex<Point_3>> originalVertices = new HashSet<Vertex<Point_3>>();
		for(Vertex<Point_3> v : this.polyhedron3D.vertices) originalVertices.add(v);
		HashMap<Halfedge<Point_3>, Point_3> edgePoints = this.computeEdgePoints();
		splitEdges(edgePoints);
		HashSet<Face<Point_3>> faceSet = new HashSet<>(this.polyhedron3D.facets);
        for (Face<Point_3> face : faceSet) {
        	this.subdivideFace(originalVertices, face);
        }

        Point_3 [] newOriginalVerticesLocations = computeNewVertexLocations(originalVertices);
        int i = 0;
        for (Vertex<Point_3> vertex : originalVertices) {
            vertex.setPoint(newOriginalVerticesLocations[i]);
            i++;
        }
	}

	/**
	 * Splits all edges by inserting a new vertex
	 */
	public void splitEdges(HashMap<Halfedge<Point_3>, Point_3> edgePoints) {
		System.out.print("Splitting edges...");
		for (Halfedge<Point_3> h : edgePoints.keySet()) {
			this.polyhedron3D.splitEdge(h, edgePoints.get(h));
		}
		System.out.println("done");
	}
	
	/**
	 * Perform the subdivision of a face into 4 triangular sub-faces
	 * Edges must already be split: the face has degree 3+3
	 */
	public void subdivideFace(HashSet<Vertex<Point_3>> originalVertices, Face<Point_3> f) {
		Halfedge<Point_3> h = f.getEdge();
		if(originalVertices.contains(h.getVertex())) h = h.getNext();
		for (int i = 0; i < 3; i++) {
            Halfedge<Point_3> g = h.next.next;
            h = this.polyhedron3D.splitFacet(h, g);
        }
	}

	/**
	 * Compute a new edge point (given the half-edge h)
	 */
	public Point_3 computeEdgePoint(Halfedge<Point_3> h) { 
		Point_3[] points = {h.getVertex().getPoint(), h.getOpposite().getVertex().getPoint(), h.getNext().getVertex().getPoint(), h.getOpposite().getNext().getVertex().getPoint()};
		Number[] coefficients = {3./8,3./8,1./8,1./8};
		return Point_3.linearCombination(points, coefficients);
	}

	/**
	 * Compute all new edge points and store the result in an HashMap
	 */
	public HashMap<Halfedge<Point_3>, Point_3> computeEdgePoints() {
		HashMap<Halfedge<Point_3>, Point_3> map = new HashMap<Halfedge<Point_3>, Point_3>();
		for(Halfedge<Point_3> halfedge : this.polyhedron3D.halfedges) {
			if(!map.containsKey(halfedge.getOpposite())) map.put(halfedge, this.computeEdgePoint(halfedge));
		}
		return map;
	}
	
	/**
	 * Compute the new coordinates for a vertex (already existing in the initial mesh)
	 */
	public Point_3 computeNewVertexLocation(Vertex<Point_3> v) {
		int n=1;
		Halfedge<Point_3> e = v.getHalfedge();
		Halfedge<Point_3> pe = e.getNext().getOpposite();
		while(!pe.equals(e)) {
			pe = pe.getNext().getOpposite();
			n++;
		}
		Point_3[] points = new Point_3[n+1];
		Number[] coefficients = new Number[n+1];
		double alpha;
		if(n==3) alpha = 9./16.;
		else alpha = 3./(8.*n);
		coefficients[0] = 1-alpha*n;
		points[0] = v.getPoint();
		pe = e.getNext().getOpposite();
		for(int i=1; i<=n; i++) {
			coefficients[i] = alpha;
			points[i] = pe.getOpposite().getVertex().getPoint();
			pe = pe.getNext().getOpposite();
		}
		return Point_3.linearCombination(points, coefficients);
	}

	/**
	 * Compute the new coordinates for all vertices of the initial mesh
	 */
	public Point_3[] computeNewVertexLocations(HashSet<Vertex<Point_3>> originalVertices) {
        int numOriginalVertices = originalVertices.size();
        Point_3 []newVerticesLocations = new Point_3[numOriginalVertices];
        int i = 0;
        for (Vertex<Point_3> vertex : originalVertices) newVerticesLocations[i++] = computeNewVertexLocation(vertex);
        return newVerticesLocations;
    }

}


