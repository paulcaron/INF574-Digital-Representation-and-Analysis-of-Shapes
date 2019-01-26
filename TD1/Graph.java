
/**
 * Define the abstract data type for a geometric graph 
 * (undirected, not weighted)
 *
 * @author Luca Castelli Aleardi (INF574, 2018)
 * Student Paul Caron
 */
public interface Graph {
	
	  /**
	   * Add an edge between nodes d and a
	   */
    public void addEdge(int d, int a);

	  /**
	   * Remove edge between nodes d and a
	   */
    public void removeEdge(int d, int a);

	  /**
	   * Return the geometric coordinates of vertex i
	   */
    public Point_d getVertex(int i);
    
	  /**
	   * Check whether two nodes are adjacent in the graph
	   */
    public boolean adjacent(int d, int a);

	  /**
	   * Return the degree of a node
	   */
    public int degree(int index);

	  /**
	   * Return the list of neighbors of a given node (having index i)
	   */
    public int[] neighbors(int i);

	  /**
	   * Return an array containing the list of all edges of the graph
	   */
    public int[][] getEdges();
    
	  /**
	   * Return a new geometric realization of the graph
	   * New vertex locations are computed according to transformation t (in the 3D space)
	   */
    public void transformVertices(Transformation_3 t);
    
	  /**
	   * Return the number of vertices of the graph
	   */
    public int sizeVertices();
    
}
