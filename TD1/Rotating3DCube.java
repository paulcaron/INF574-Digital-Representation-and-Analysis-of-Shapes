import java.awt.Color;

import processing.core.*;

/**
 * A simple 3d viewer for visualizing cubes in 3D space
 * 
 * @author Luca Castelli Aleardi (INF574, 2018)
 * Student Paul Caron
 *
 */
public class Rotating3DCube extends PApplet {

	Draw3DCube renderer;
	
	/** the skeleton of a 3D cube to transform and animate */
	Cube cube;
	
	Cube[] rubik=null; // a set of 27 cubes, defining a Rubik's cube

	
	public void setup() {
		  size(800,600,P3D);
		  this.renderer=new Draw3DCube(this);
		  this.cube=new Cube(20);
	}
	
		public void draw() {
		  background(0);
		  this.lights();
		  
		  // rotate and translate the camera
		  translate(width/2.f,height/2.f,-1*height/2.f);
		  this.rotateX((float)(PI/3.0));
		  //this.rotateY((float)(PI/3.0));
		  this.rotateZ((float)(PI/3.0));
		  this.strokeWeight(1);
		  stroke(150,150,150);
		  
		  this.renderer.draw(this.cube);
		  
		  if(this.rubik!=null)
			  this.renderer.draw(this.rubik);
		}
		
		public void keyPressed(){
			  switch(key) {
			    case('i'):case('I'): this.scale(2.0); break;
			    case('o'):case('O'): this.scale(0.5); break;
			    case('x'):case('X'): this.rotateX(PI/6.0); break;
			    case('y'):case('Y'): this.rotateY(PI/6.0); break;
			    case('z'):case('Z'): this.rotateZ(PI/6.0); break;
			    case('r'):case('R'): this.rotateAroundDiagonal(PI/6.0); break;
			    case('u'):case('U'): this.moveZ(2.0); break;
			    case('d'):case('D'): this.moveZ(-2.0); break;
			    case('a'):case('A'): this.rotateAroundDiagonal(PI/6.0); break;
			  }
		}
		
		/**
		 * Zoom in
		 */
		public void scale(double zoomFactor) {
			Transformation_3 scaling = Transformation_3.scaling(zoomFactor);
			this.cube.transformVertices(scaling);
			System.out.println("Scaling by a factor "+zoomFactor);
		}

		/**
		 * Move up (vertically)
		 */
		public void moveZ(double t) {
			Vector_3 v = new Vector_3(0,0,t);
			Transformation_3 movingZ = Transformation_3.translation(v);
			this.cube.transformVertices(movingZ);
			System.out.println("Moving on z axis of "+t);
		}

		/**
		 * rotate around X-axis
		 */
		public void rotateX(double angle) {
			Point_d barycenter = new Point_d(4);
			barycenter.barycenter(this.cube.vertices);
			Vector_3 v = new Vector_3(barycenter.coordinates);
			Transformation_3 translation1 = Transformation_3.translation(v.opposite());
			Transformation_3 rotating = Transformation_3.rotationAxisX(angle);
			Transformation_3 translation2 = Transformation_3.translation(v);
			this.cube.transformVertices(translation1);
			this.cube.transformVertices(rotating);
			this.cube.transformVertices(translation2);
			System.out.println("Rotating on x axis of angle "+angle);
		}

		/**
		 * rotate around Y-axis
		 */
		public void rotateY(double angle) {
			Point_d barycenter = new Point_d(4);
			barycenter.barycenter(this.cube.vertices);
			Vector_3 v = new Vector_3(barycenter.coordinates);
			Transformation_3 translation1 = Transformation_3.translation(v.opposite());
			Transformation_3 rotating = Transformation_3.rotationAxisY(angle);
			Transformation_3 translation2 = Transformation_3.translation(v);
			this.cube.transformVertices(translation1);
			this.cube.transformVertices(rotating);
			this.cube.transformVertices(translation2);
			System.out.println("Rotating on y axis of angle "+angle);
		}

		/**
		 * rotate around Z-axis
		 */
		public void rotateZ(double angle) {
			Point_d barycenter = new Point_d(4);
			barycenter.barycenter(this.cube.vertices);
			Vector_3 v = new Vector_3(barycenter.coordinates);
			Transformation_3 translation1 = Transformation_3.translation(v.opposite());
			Transformation_3 rotating = Transformation_3.rotationAxisZ(angle);
			Transformation_3 translation2 = Transformation_3.translation(v);
			this.cube.transformVertices(translation1);
			this.cube.transformVertices(rotating);
			this.cube.transformVertices(translation2);
			System.out.println("Rotating on z axis of angle "+angle);
		}

		/**
		 * rotate the cube around its diagonal
		 */
		public void rotateAroundDiagonal(double angle) {
			Point_d barycenter = new Point_d(4);
			barycenter.barycenter(this.cube.vertices);
			Vector_3 v = new Vector_3(barycenter.coordinates);
			Transformation_3 translation1 = Transformation_3.translation(v.opposite());
			Transformation_3 rotating = Transformation_3.rotationAxisDiagonal(this.cube,angle);
			Transformation_3 translation2 = Transformation_3.translation(v);
			this.cube.transformVertices(translation1);
			this.cube.transformVertices(rotating);
			this.cube.transformVertices(translation2);
			System.out.println("Rotating on diagonal axis of angle "+angle);
		}
		
		/**
		 * For running the PApplet as Java application
		 */
		public static void main(String args[]) {
			PApplet.main(new String[] { "Rotating3DCube" });
		}
		
}
