import Jcg.geometry.PointCloud_3;
import Jcg.geometry.Point_3;

import java.util.*;

/**
 * Define methods for manipulating a point cloud in 3D
 *
 * @author Luca Castelli Aleardi (INF555, 2014)
 */
public class PointSet extends PointCloud_3 {
	
	public PointSet() {
		super();
	}
	
	/**
	 * Load a point cloud from a text file
	 */
	public PointSet(String filename) {
		super();
    	System.out.print("Creating a point cloud (from OFF file): ");
    	System.out.println(filename);
    	
    	double x, y, z;
    	Jcg.io.IO.readTextFile(filename);
    	String line;
            
            line=Jcg.io.IO.readLine(); // first line is empty
            line=Jcg.io.IO.readLine();
            String[] w=Jcg.io.IO.wordsFromString(line);
            int sizePoints=Integer.parseInt(w[0]);
                        
            int i=0;
            Point_3 point;
            System.out.print("\tReading coordinates...");
            
            while(i<sizePoints) {
                line=Jcg.io.IO.readLine();
                w = Jcg.io.IO.wordsFromString(line);
                if(w!=null && w.length>0 && w[0].charAt(0)!='#') { // skip empty lines an comments
                	x=(new Double(w[0])).doubleValue();
                	y=(new Double(w[1])).doubleValue();
                	z=(new Double(w[2])).doubleValue();
                
                	point=new Point_3(x,y,z);
                	this.add(point);
                	i++;
                }
            }
            System.out.println("done "+sizePoints+" points");
            
           Jcg.io.IO.readStandardInput();
	}
	
	/**
	 * Return an array containing all points in the point cloud
	 */
	public Point_3[] toArray() {
		Point_3[] tab = new Point_3[points.size()];
		
		int i=0;
		for(Point_3 p : this.points) {
			tab[i] = p;
			i++;
		}
		
		return tab;
	}
	
	/**
	 * Export the point cloud to a text file
	 */
	public void toFile(String filename) {
    	System.out.print("Exporting point set to OFF file...");
    	Jcg.io.IO.writeNewTextFile(filename);
    	
    	int nVertices=this.size();
    	int nFaces=0;
    	
    	// file header
    	Jcg.io.IO.println("OFF");
    	Jcg.io.IO.println(nVertices+" "+nFaces+" "+0);
    	    	
    	// writing point coordinates
    	for(Point_3 p: this.listOfPoints()) { // iterate over all points
    		double x=p.getX().doubleValue();
    		double y=p.getY().doubleValue();
    		double z=p.getZ().doubleValue();
    		Jcg.io.IO.println(""+x+" "+y+" "+z);
    	}
    	
    	Jcg.io.IO.writeStandardOutput();
    	System.out.println("done");
	}

	/**
	 * Return the centroid of the point cloud
	 */
	public Point_3 getBarycenter() {
		
		Point_3 res = new Point_3(0, 0, 0);
		
		for(Point_3 p : this.listOfPoints())
		{
			res.x += p.x;
			res.y += p.y;
			res.z += p.z;
		}
		
		res.multiply(1/(double)this.size());
		
		return res;
	}

	/**
	 * Return a new point set, obtained by applying a rotation and a translation
	 */
	public PointSet transformPoints(Rotation_3 r, Translation_3 t) {
		System.out.print("Applying rigid transformation to point set...");
		PointSet result=new PointSet();
		
		for(Point_3 p: this.listOfPoints()) {
			Point_3 q=r.transform(p);
			Point_3 z=t.transform(q);
			result.add(z);
		}
		System.out.println("done");
		return result;
	}

	/**
	 * Permute input points
	 */
	public void permute() {
		System.out.print("Permuting the point set...");
		
		int n=this.size();
		int i=(int)(Math.random()*n);
		int j=(int)(Math.random()*n);

		if(i!=j) {
			List<Point_3> list=(List<Point_3>)this.listOfPoints();
			Point_3 p=list.get(i);
			Point_3 q=list.get(j);
			
			list.set(i, q);
			list.set(j, p);
		}
		
		System.out.println("done");
	}

	/**
	 * Return the closest points to q, at distance at most d <p>
	 * <p>
	 * Warning: naive method, based on a linear scan
	 * 
	 * @param sqRad  square of the distance (sqRad=d*d)
	 * @param q  point query
	 */
	public List<Point_3> getClosestPoints(Point_3 q, double sqRad) {
		List<Point_3> res = new LinkedList<Point_3>();
		for(int i=0; i< this.toArray().length; i++) {
			if(Math.pow((double)this.toArray()[i].distanceFrom(q),2)<sqRad) res.add(this.toArray()[i]);
		}
		return res;
	}

	/**
	 * Return the closest point to q, among the points of the point cloud
	 * <p>
	 * Warning: naive method, based on a linear scan
	 * 
	 * @param q  point query
	 */
	public Point_3 getClosestPoint(Point_3 q) {
		Point_3 p = this.toArray()[0];
		double dist = (double)this.toArray()[0].distanceFrom(q);
		for(int i=1; i< this.toArray().length; i++) {
			if((double)this.toArray()[i].distanceFrom(q)<dist) {
				dist = (double)this.toArray()[i].distanceFrom(q);
				p = this.toArray()[i];
			}
		}
		return p;
	}
	
	/**
	 * Return the k-nearest neighbors to point p in the point cloud
	 * <p>
	 * Warning: naive method (complexity n*k), based on a linear scan
	 * 
	 * @param q  point query
	 * @param k  number of points to return
	 */
	public Point_3[] getKNearestNeighbors(Point_3 q, int k) {
		PointSet set = new PointSet();
		Point_3 p;
		for(int i=0; i<this.toArray().length; i++) {
			set.add(this.toArray()[i]);
		}
		Point_3[] res = new Point_3[k];
		for(int j=0; j<k;j++) {
			p = set.getClosestPoint(q);
			set.remove(p);
			res[j] = p;
		}
		return res;
	}
	
	/**
	 * Compute and return the maximal distance from origin
	 */
	public double getMaxDistanceFromOrigin() {
		double maxDistance=0.;
		Point_3 origin=new Point_3(0., 0., 0.);
		for(Point_3 p: this.points) {
			double distance=Math.sqrt(p.squareDistance(origin).doubleValue());
			maxDistance=Math.max(maxDistance, distance);
		}
		return maxDistance;
	}

}
