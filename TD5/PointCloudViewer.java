import processing.core.*;

import java.io.File;

import Jcg.geometry.*;

/**
 * A simple 3d viewer for visualizing point clouds (based on Processing). It deals with methods for<p>
 * -) normals estimation <p>
 * -) outliers computation<p>
 * 
 * @author Luca Castelli Aleardi (INF555, 2016)
 *
 */
public class PointCloudViewer extends PApplet {

	PointSet points; // input point cloud
	int renderType=0; // choice of type of rendering
	int renderModes=3; // number of rendering modes
	DrawPointSet drawPointSet;
	
	Rotation_3 rotation=new Rotation_3(); // identity rotation by default
	Translation_3 translation=new Translation_3(new Vector_3(0., 0., 0.)); // no translation
	public static String filename;// input point clouds
	
	public void setup() {
		  size(1000,800,P3D);
		  ArcBall arcball = new ArcBall(this);

		  this.points=new PointSet(filename);
		  
		  this.drawPointSet=new DrawPointSet(this, this.points, null);
		  //this.icp=new IterativeClosestPoint();
	}
		 
		public void draw() {
		  background(10);
		  //this.lights();
		  directionalLight(101, 204, 255, -1, 0, 0);
		  directionalLight(51, 102, 126, 0, -1, 0);
		  directionalLight(51, 102, 126, 0, 0, -1);
		  directionalLight(102, 50, 126, 1, 0, 0);
		  directionalLight(51, 50, 102, 0, 1, 0);
		  directionalLight(51, 50, 102, 0, 0, 1);
		 
		  translate(width/2.f,height/2.f,-1*height/2.f);
		  this.strokeWeight(1);
		  stroke(150,150,150);
		  
		  this.drawPointSet.draw(renderType);
		  this.drawOptions();
		}
		
		public void keyPressed(){
			  switch(key) {
			    case('z'):case('Z'): this.zoomIn(); break;
			    case('x'):case('X'): this.zoomOut(); break;
			    case('q'):case('Q'): this.scaleNormal(2.0);; break;
			    case('w'):case('W'): this.scaleNormal(0.5);; break;
			    case('n'):case('N'): this.computeNormals(); break;
			    case('o'):case('O'): this.computeOutliers(); break;
			    case('p'):case('P'): this.points.permute(); this.drawPointSet=new DrawPointSet(this, this.points, null); break;
			    case('r'):this.renderType=(this.renderType+1)%this.renderModes; break;
			    case('s'):case('S'): this.savePointCloud(); break;
			    case('t'):case('T'): this.transformPointCloud(); this.drawPointSet=new DrawPointSet(this, this.points, null); break;
			  }
		}
		
		public void drawOptions() {
			int hF=12;
			fill(255);
			this.textMode(this.SCREEN);
			this.text("'n' for normals estimation", 10, hF);
			this.text("'o' for outliers computation", 10, hF*2);
			this.text("'z' or 'x' for zooming", 10, hF*3);
			this.text("'q' or 'w' scaling normal vectors", 10, hF*4);
		}
		
		public void computeNormals() {
			int k=10;
			//double R=2*NormalEstimator.estimateAverageDistance(points); // distance parameter: for computing closest neighbors (within distance epsilon)
			//double[][] normals=NormalEstimator.computeNormals(this.points, R*R);
			double[][] normals=NormalEstimator.computeNormals(this.points, k);
			this.drawPointSet.normals=normals;
			this.drawPointSet.showNormals=true;
		}

		public void computeOutliers() {
			double epsilon=0.9;
			int k=10;
			//double R=0.5; // to set to the good value
			boolean[] outliers=OutliersComputation.computeOutliers(this.points, R, epsilon);
			//boolean[] outliers=OutliersComputation.computeOutliers(this.points, k, epsilon);
			this.drawPointSet.outliers=outliers;
			this.drawPointSet.showOutliers=true;
		}

		public void zoomIn() {
			this.drawPointSet.zoom=this.drawPointSet.zoom*1.5;
		}

		public void zoomOut() {
			this.drawPointSet.zoom=this.drawPointSet.zoom*0.75;
		}

		public void scaleNormal(double scale) {
			this.drawPointSet.normalScale*=scale;
		}

		public void savePointCloud() {
			if(points!=null)
				points.toFile("output.off");
		}

		public void transformPointCloud() {
			points = points.transformPoints(rotation, translation);
		}

		public static void setInputFile(String input) {
			if(input==null) { 
				System.out.println("Error: wrong input file "+input);
				System.exit(0);
			}
			File file = new File(input);
			if(file.exists()==false) { 
				System.out.println("Wrong input file: "+input+" not found");
				System.exit(0);
			}
			if(input.endsWith(".off")==false) { 
				System.out.println("Error: wrong input format "+input+" (.off required)");
				System.exit(0);
			}
			filename=input;
		}

		/**
		 * For running the PApplet as Java application
		 */
		public static void main(String args[]) {
			if(args.length!=1) {
				System.out.println("Wrong number of input parameters: required one file .off");
				System.exit(0);
			}
			PointCloudViewer.setInputFile(args[0]);
			
			PApplet.main(new String[] { "PointCloudViewer" });
		}

}
