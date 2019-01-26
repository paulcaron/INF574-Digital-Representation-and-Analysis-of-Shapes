/**
 * An implementation of a generic (undirected, possibly weighted) graph, 
 * using an adjacency matrix representation
 *
 * @author Luca Castelli Aleardi (INF574, 2018)
 * Student Paul Caron
 */
public class AdjacencyGraph implements Graph {

    int n=0; // number of vertices
    public int[][] adjacency; // adjacency matrix representation
    public Point_d[] vertices; // vertex locations

    public AdjacencyGraph() { }

    public AdjacencyGraph(int n) {
    	this.n  = n;
    	this.adjacency=new int[n][n];
    }

    public AdjacencyGraph(int[][] edges, Point_d[] points) {
    	this.n  = edges.length;
    	this.adjacency=edges;
    	this.vertices=points;
    }

    public AdjacencyGraph(int[][] adjacency) {
    	this.adjacency=adjacency;
    	this.n=adjacency[0].length;
    }

    public void addEdge(int d, int a) {
		if(d<0 || a<0) throw new Error("addEdge: vertex index error");
		adjacency[d][a]=1;
		adjacency[a][d]=1;
    }

    public void removeEdge(int d, int a) {
		if(d<0 || a<0) throw new Error("removeEdge: vertex index error");
		adjacency[d][a]=0;
		adjacency[a][d]=0;
    }

    /**
     * Apply a linear transformation to the graph (to all its vertices)
     */
    public void transformVertices(Transformation_3 t) {
    	Point_d[] points=new Point_d[this.vertices.length];
    	
    	for(int i=0;i<this.vertices.length;i++) {
    		this.vertices[i]=t.transform((Point_d)this.vertices[i]);
    	}
    }

    public Graph getTransformedGraph(Transformation_3 t) {
    	Point_d[] points=new Point_d[this.vertices.length];
    	
    	for(int i=0;i<this.vertices.length;i++) {
    		points[i]=t.transform((Point_d)this.vertices[i]);
    	}
    	
    	return new AdjacencyGraph(this.adjacency, points);
    }

    /**
     * Return the geometric (homogeneous) coordinates of the i-th vertex of the graph
     */
    public Point_d getVertex(int i) {
		if(i<0 || i>=this.vertices.length) throw new Error("getVertex: vertex index error");
		return this.vertices[i];
	}

    public boolean adjacent(int d, int a) {
		if(d<0 || a<0) throw new Error("adjacent: vertex index error");
		if(adjacency[d][a]==1 && adjacency[a][d]==1)
			return true;
		else if(adjacency[d][a]==0 && adjacency[a][d]==0)
			return false;
		else throw new Error("adjacent vertices: error");
    }

    public int sizeVertices() {
    	return adjacency[0].length;
    }

    public int degree(int index) {
    	int d=0;
    	for(int i=0;i<this.n;i++)
    		if(i!=index && adjacency[i][index]!=0) d++;
    	return d;
    }

    public int[] neighbors(int index) {
    	int[] result=new int[degree(index)];
    	int compt=0;
    	for(int i=0;i<this.n;i++) {
    		if(i!=index && adjacency[i][index]!=0) {
    			result[compt]=i;
    			compt++;
    		}
    	}
    	return result;
    }
 
    public int[][] getEdges() {
    	int numberOfEdges=0;
    	for(int i=0;i<this.n;i++)
    		numberOfEdges=numberOfEdges+degree(i);
    	numberOfEdges=numberOfEdges/2;
    	
    	int[][] result=new int[numberOfEdges][2];
    	int compt=0;
    	for(int i=0;i<this.n;i++) {
    		for(int j=i+1;j<this.n;j++){ 
    			if(adjacency[i][j]!=0) {
    				result[compt][0]=i;
    				result[compt][1]=j;
    				compt++;
    			}
    		}
    	}
    	return result;
    }

    public String toString() {
   		String result="adjacency matrix\n";
   		for(int i=0;i<sizeVertices();i++) {
   			for(int j=0;j<sizeVertices();j++)
   				result=result+" "+this.adjacency[i][j];
   			result=result+"\n";
   		}
   		return result;
    }
    
}
