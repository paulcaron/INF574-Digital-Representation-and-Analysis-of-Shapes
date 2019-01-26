import processing.core.PApplet;


/**
 * 
 * Class for rendering a surface triangle mesh
 * @author Luca Castelli Aleardi (INF555, 2012)
 * Student Paul Caron
 *
 */
public class Draw3DCube {
	
	double scaleFactor=10; // scaling factor: useful for 3d rendering
	PApplet view;
	
	/**
	 * Create a 3D cube
	 */	
	public Draw3DCube(PApplet view) {
		this.view=view;
	}
	
	/**
	 * Draw a segment between two points
	 */	
	public void drawSegment(Point_3 p, Point_3 q) {
		float s=(float)this.scaleFactor;
		this.view.line(	(float)p.getX()*s, (float)p.getY()*s, 
				(float)p.getZ()*s, (float)q.getX()*s, 
				(float)q.getY()*s, (float)q.getZ()*s);
	}

	/**
	 * Draw the entire mesh
	 */
	public void draw(Cube cube) {
		this.drawAxis();
		
		view.strokeWeight(2); // line width (for edges)
		
		if(cube.color==null) // no color defined
			view.stroke(200,200,200);
		else
			view.stroke(cube.color.getRed(), cube.color.getGreen(), cube.color.getBlue());
		
		  int[][] edges=cube.getEdges();
		  //parent.noStroke();
	    
	    for(int i=0;i<edges.length;i++) {
	    	// find the two vertices v0 and v1 of an edge: with homogeneous coordinates
	    	Point_d v0=(Point_d)cube.getVertex(edges[i][0]);
	    	Point_d v1=(Point_d)cube.getVertex(edges[i][1]);
	    	
	    	// convert from homogeneous to cartesian coordinates (in 3D)
	    	Point_d cartesianV0=(Point_d)v0.toCartesian();
	    	Point_d cartesianV1=(Point_d)v1.toCartesian();
	    	
	    	float x0=(float)cartesianV0.getCartesian(0);
	    	float y0=(float)cartesianV0.getCartesian(1);
	    	float z0=(float)cartesianV0.getCartesian(2);
	    	float x1=(float)cartesianV1.getCartesian(0);
	    	float y1=(float)cartesianV1.getCartesian(1);
	    	float z1=(float)cartesianV1.getCartesian(2);
	    	
	    	this.view.line(x0, y0, z0, x1, y1, z1);
	    	//System.out.println(i+": "+v0+" - "+v1);
	    }
		view.strokeWeight(1);
	}
	
	/**
	 * Draw all the meshes
	 */
	public void draw(Cube[] cubes) {
		for(Cube cube: cubes)
			this.draw(cube);
	}

	
	/**
	 * Draw the X, Y and Z axis
	 */
	public void drawAxis() {
		double s=50;
		Point_3 p000=new Point_3(0., 0., 0.);
		Point_3 p100=new Point_3(s, 0., 0.);
		Point_3 p010=new Point_3(0.,s, 0.);
		Point_3 p011=new Point_3(0., 0., s);
		
		view.strokeWeight(3); // line width (for edges)
		view.stroke(0, 0, 200);
		drawSegment(p000, p100);
		drawSegment(p000, p010);
		drawSegment(p000, p011);
		view.strokeWeight(1); // line width (for edges)
	}


	/**
	 * Return the value after truncation
	 */
	public static double round(double x, int precision) {
		return ((int)(x*precision)/(double)precision);
	}
	
}
