import java.awt.Color;

import processing.core.PApplet;

public class Cube extends AdjacencyGraph {

	Color color=null;
	  /**
	   * Initialize a cube of a given size
	   */
	  public Cube(double width) {
		  	double x=0., y=0., z=0.;
	    	this.n  = 8;
	    	this.adjacency=new int[n][n];
			this.vertices=new Point_d[n];
			
			this.vertices[0]=new Point_d(new double[]{x, y, z, 1.});
			this.vertices[1]=new Point_d(new double[]{x+width, y, z, 1.});
			this.vertices[2]=new Point_d(new double[]{x+width, y+width, z, 1.});
			this.vertices[3]=new Point_d(new double[]{x, y+width, z, 1.});

			this.vertices[4]=new Point_d(new double[]{x, y, z+width, 1.});
			this.vertices[5]=new Point_d(new double[]{x+width, y, z+width, 1.});
			this.vertices[6]=new Point_d(new double[]{x+width, y+width, z+width, 1.});
			this.vertices[7]=new Point_d(new double[]{x, y+width, z+width, 1.});

			this.addEdge(0,1); this.addEdge(1,2); this.addEdge(2,3); this.addEdge(3,0);
			this.addEdge(4,5); this.addEdge(5,6); this.addEdge(6,7); this.addEdge(7,4);
			this.addEdge(0,4); this.addEdge(1,5); this.addEdge(2,6); this.addEdge(3,7);
	  }
	  
	  public void setColor(Color c) {
		  this.color=c;
	  }
	  
}
