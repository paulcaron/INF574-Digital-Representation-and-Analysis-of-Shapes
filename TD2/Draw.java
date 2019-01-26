import processing.core.*;

import java.util.*;

/**
 * A simple 2D editor for handling curves and mouse events
 *
 * @author Luca Castelli Aleardi (INF555, 2014)
 * Student Paul Caron
 */
public class Draw extends PApplet {
	
	Interpolation scheme; // interpolation scheme
	Transformation_2 transformation; // geometric transformation (rotation, translation, scaling, ...)
	LinkedList<Point_2> points; // the input points to interpolate
	LinkedList<Vector_2> slopeList;
	final static double epsilon=10.;
	int curveMode=0;
	final static int curveTypes=5; // there are 5 types of curve interpolation
	boolean click;
	boolean leftClick;
	
	public Point_2 selectedPoint=null; // point selected with mouse events

	  public void setup() {
		  size(400,400); // size of the window
	      this.points=new LinkedList<Point_2>(); // input points
	      this.slopeList=new LinkedList<Vector_2>();
	      setInterpolationMethod();
	      this.transformation=new Transformation_2(); // identity transformation
	      this.click = false;
	      this.leftClick = false;
	  }
	  
	  public void setInterpolationMethod() {		  
	      if(curveMode%curveTypes==0) {
	    	  this.scheme=new NoInterpolation(this);
	    	  System.out.println("No interpolation");
	      }
	      else if(curveMode%curveTypes==1) {
	    	  this.scheme=new LinearInterpolation(this);
	    	  System.out.println("Linear interpolation");
	      }
	      else if(curveMode%curveTypes==2) {
	    	  this.scheme=new LagrangeInterpolation(this);
	    	  System.out.println("Lagrange interpolation");
	      }
	      else if(curveMode%curveTypes==3) {
	    	  this.scheme=new CubicSplineInterpolation(this);
	    	  System.out.println("Cubic spline interpolation");
	      }
	      else if(curveMode%curveTypes==4) {
	    	  this.scheme=new HermiteSplineInterpolation(this);
	    	  System.out.println("Hermite Spline interpolation");
	      }
	      else {
	    	  this.scheme=new NoInterpolation(this);
	    	  System.out.println("No interpolation");
	      }
	      this.stroke(0, 0, 0);
	  }

	  public void applyTransformation(int i) {		  
	      if(i==0) {
	    	  this.transformation=new Transformation_2(new Vector_2(2., 0.));
	    	  System.out.println("translation");
	      }
	      else if(i==1) {
	    	  double angle=Math.PI/4;
			  Point_2 barycenter=Point_2.barycenter(points);
			  this.transformation=new Transformation_2(angle, barycenter);
			  //this.transformation=new Transformation_2(angle);
			  System.out.println("rotation");
	      }
	      else if(i==2) {
	    	  this.transformation=new Transformation_2(1.2, 1.2);
	    	  System.out.println("scaling in");
	      }
	      else if(i==3) {
	    	  this.transformation=new Transformation_2(0.8, 0.8);
	    	  System.out.println("scaling in");
	      }
	      else {
	    	  this.transformation=new Transformation_2();
	      }
	      
	      for(Point_2 p: this.points) { // iterate over all points
	    	  Point_2 q; // the transformed point
	    	  q=this.transformation.transform(p);
	    	  p.setX(q.getX());
	    	  p.setY(q.getY());
	      }
	  }

	  public void draw() {
	    background(200);
	    
	    if(points.isEmpty()) return; // no points to interpolate
	    scheme.interpolate();
	  }

	  public void removePoint(int x, int y) {
		  int index=findPoint(x, y);
		  if(index>=0 && index<this.points.size())
			  this.points.remove(index);
	  }
	  
	  public int findPoint(int x, int y) {
		  Point_2 p=new Point_2(x, y);
		  
		  int index=0;
		  boolean found=false;
		  for(Point_2 q:this.points) {
			  if(q.squareDistance(p)<epsilon) {
				  found=true;
				  break;
			  }
			  index++;
		  }
		  if(found==true)
			  return index;
		  else
			  return -1;
	  }

	  public Point_2 selectPoint(int x, int y) {
		  Point_2 p=new Point_2(x, y);
		  
		  int index=0;
		  boolean found=false;
		  for(Point_2 q:this.points) {
			  if(q.squareDistance(p)<epsilon) {
				  found=true;
				  break;
			  }
			  index++;
		  }
		  if(found==true)
			  return this.points.get(index);
		  else
			  return null;
	  }

	  public void mouseClicked() {
		  Point_2 p=new Point_2(mouseX, mouseY);
		  		  
		  if(mouseButton==LEFT && this.selectedPoint==null) {
			    this.points.add(p);
		  		Random randomGenerator = new Random();
		  		this.slopeList.add(new Vector_2(100*randomGenerator.nextInt(10)-500,100*randomGenerator.nextInt(10)-500));
		  }
		  else if(mouseButton==RIGHT)
			  removePoint(mouseX, mouseY);
		  
		  this.scheme.updateInputPoints();
		  this.scheme.updateInputSlopes();
	  }

	  public void mousePressed() {
		  this.selectedPoint=selectPoint(mouseX, mouseY);
		  if(mouseButton==LEFT) this.leftClick = true;
		  this.click = true;
	  }
	  
	  public void mouseReleased() {
		  
		  if(this.selectedPoint!=null && this.leftClick) {
			  this.selectedPoint.setX(mouseX);
			  this.selectedPoint.setY(mouseY);
		  }
		  this.click = false;
		  this.leftClick = false;
		  this.scheme.updateInputSlopes();
		  setInterpolationMethod();
	  }
	  
	  public void mouseDragged() {
		  if(this.click && !this.leftClick && this.selectedPoint!=null) {
			  double x = mouseX-selectedPoint.x;
			  double y = mouseY-selectedPoint.y;
			  double len = Math.sqrt(x*x + y*y);
			  x = 350*x/len;
			  y = 350*y/len;
			  int i = 0;
			  while(!this.points.get(i).equals(this.selectedPoint)) i++;
			  this.slopeList.set(i, new Vector_2(x,y));
		  }
	  }
	  
	  public void keyPressed(){
		  switch(key) {
		  	case('s'):case('S'): { curveMode++; setInterpolationMethod(); } break;
		  	case('d'):case('D'): { removeLastPoint(); } break;
		  	case('t'):case('T'): { applyTransformation(0); } break;
		  	case('r'):case('R'): { applyTransformation(1); } break;
		  	case('i'):case('I'): { applyTransformation(2); } break;
		  	case('o'):case('O'): { applyTransformation(3); } break;
		  }
	  }

	  public void removeLastPoint() {
		  if(this.points==null || this.points.size()==0)
			  return;
		  this.points.removeLast();
		  this.setInterpolationMethod();
	  }

}
