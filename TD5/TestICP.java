import processing.core.*;

import java.io.File;

import Jcg.geometry.*;

/**
 * A simple 3d viewer for dealing with ICP computation and point clouds
 * 
 * @author Luca Castelli Aleardi (INF574, 2018)
 *
 */
public class TestICP extends PApplet {

	PointSet points1, points2; // input point clouds
	RigidTransformation_3 icp;
	int renderType=0; // choice of type of rendering
	int renderModes=3; // number of rendering modes
	DrawPointSet drawPointSet;
	
	Rotation_3 rotation=new Rotation_3(); // identity rotation by default
	Translation_3 translation=new Translation_3(new Vector_3(0., 0., 0.)); // no translation
	public static String filename1, filename2;// input point clouds
	
	public void setup() {
		  size(800,600,P3D);
		  ArcBall arcball = new ArcBall(this);
		  this.points1=new PointSet(filename1);
		  this.points2=new PointSet(filename2);
		  
		  this.drawPointSet=new DrawPointSet(this, this.points1, this.points2);
		  this.icp=new IterativeClosestPoint();
		  //this.icp=new QuaternionsICP();
	}
		 
		public void draw() {
		  background(255);
		  this.lights();
		  /*directionalLight(101, 204, 255, -1, 0, 0);
		  directionalLight(151, 102, 126, 0, -1, 0);
		  directionalLight(151, 102, 126, 0, 0, -1);
		  directionalLight(102, 150, 126, 1, 0, 0);
		  directionalLight(150, 150, 102, 0, 1, 0);
		  directionalLight(150, 150, 102, 0, 0, 1);*/
		 
		  translate(width/2.f,height/2.f,-1*height/2.f);
		  this.strokeWeight(1);
		  stroke(150,150,150);
		  
		  this.drawPointSet.draw(renderType);
		  this.drawOptions();
		}
		
		public void keyPressed(){
			  switch(key) {
			    case('i'):case('I'): this.computeICP(); this.drawPointSet=new DrawPointSet(this, this.points1, this.points2); break;
			    case('r'):this.renderType=(this.renderType+1)%this.renderModes; break;
			    case('z'):this.zoomIn(); break;
			    case('x'):this.zoomOut(); break;
			    case('l'):this.drawPointSet.pointSize*=0.75f; break;
			    case('s'):this.drawPointSet.pointSize*=1.25f; break;
			  }
		}

		public void drawOptions() {
			int hF=12;
			fill(50);
			this.textMode(this.SCREEN);
			this.text("'i' for running ICP computation", 10, hF);
			this.text("'l' or 's' for increasing/decreasing point size", 10, hF*2);
			this.text("'z' or 'x' for zooming", 10, hF*3);
		}

		public void computeICP() {
			this.rotation=this.icp.getRotation(points1, points2);
			this.translation=this.icp.getTranslation(points1, points2);
			points2 = points2.transformPoints(rotation, translation);
		}
		
		public void zoomIn() {
			this.drawPointSet.zoom=this.drawPointSet.zoom*1.5;
		}

		public void zoomOut() {
			this.drawPointSet.zoom=this.drawPointSet.zoom*0.75;
		}

		public static void setInputFile(String input1, String input2) {
			if(input1==null || input2==null) { 
				System.out.println("Error: wrong input files "+input1+", "+input2);
				System.exit(0);
			}
			File file1 = new File(input1);
			File file2 = new File(input2);
			if(file1.exists()==false || file2.exists()==false) { 
				System.out.println("Wrong input file: "+input1+","+input2+" not found");
				System.exit(0);
			}
			if(input1.endsWith(".off")==false || input2.endsWith(".off")==false) { 
				System.out.println("Error: wrong input format (.off required)");
				System.exit(0);
			}
			filename1=input1;
			filename2=input2;
		}

		/**
		 * For running the PApplet as Java application
		 */
		public static void main(String args[]) {
			if(args.length!=2) {
				System.out.println("Wrong number of input parameters: required two files .off");
				System.exit(0);
			}
			TestICP.setInputFile(args[0], args[1]);
			
			PApplet.main(new String[] { "TestICP" });
		}

}
