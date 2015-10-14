/**
 * Title:        LineSegment Class <p>
 * Description:  Class defining line segment used for drawing lines. <p>
 * Copyright:    Copyright (c) 2001 <p>
 * Company:      Ohio University <p>
 * @author  Jaikumar Arumugam + Dusan N. Sormaz
 * @version 1.0
 */

package edu.ohiou.mfgresearch.labimp.gtk3d;

import edu.ohiou.mfgresearch.labimp.gtk2d.*;

import javax.vecmath.*;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import edu.ohiou.mfgresearch.labimp.draw.*;
import edu.ohiou.mfgresearch.labimp.basis.*;
import java.util.*;

public class LineSegment extends CurveSegment  {
	
	protected Point3d startPoint;//starting point
	

	protected Point3d endPoint;//end point

// CONSTRUCTORS
  /** Default Constructor.
   *
   */
  public LineSegment() {
    startPoint = new Point3d(0,0,0);
    endPoint = new Point3d (1,1,0);
  }

  /** Constructor taking two 3D Points as input.
   *
   */
  public LineSegment (Point3d start, Point3d end) {
    startPoint = start;
    endPoint = end;
  }

  /** Constructor taking x,y,z values for end points.
   *
   */
  public LineSegment (double x1, double y1, double z1, double x2, double y2, double z2) {
    startPoint = new Point3d(x1,y1,z1);
    endPoint = new Point3d(x2,y2,z2);
  }


  /**
   *
   */
  public boolean isPointOnLineSegment (Point3d point) {
    try {
      Line3d line = new Line3d (this);
      double endFactor = line.factorForPoint(gettEndPoint());
      double pointFactor = line.factorForPoint(point);
      return 0.0 <= pointFactor ? pointFactor <= endFactor : false;
    }
    catch (InvalidLineException e) {
      return false;
    }
  }
  
  /** method that returns true if the point is inside line segment, 
   * excludes endpoints
   *
   */
 public boolean isPointInLineSegment (Point3d point) {
   try {
     Line3d line = new Line3d (this);
     double endFactor = line.factorForPoint(gettEndPoint());
     double pointFactor = line.factorForPoint(point);
     return 0.0 < pointFactor ? pointFactor < endFactor : false;
   }
   catch (InvalidLineException e) {
     return false;
   }
 }


  public Point3d intersectSegmentPlane (Plane plane)
	    throws GeometryException {
      Line3d line = new Line3d (this);
      Point3d point = line.intersectLinePlane(plane);
      if (isPointOnLineSegment (point) )
	return point;
      else
	throw new InvalidPointException
	      ("Line segment " + this + " does not intersect plane" + plane);
  }

  /** To transform LineSegment based on given transformation matrix.
   *  (modifies original LineSegment)
   *
   * @param transformMAtrix - 4x4 transformation matrix
   * @return this Linesegment
   */
  public LineSegment transformSelf (Matrix4d transformMatrix) {
    transformMatrix.transform(this.startPoint);
    transformMatrix.transform(this.endPoint);
    return this;
  }

  /** To get new LineSegment from given LineSegment based on given transformation matrix.
   *  (does NOT modify original LineSegment)
   *
   * @param transformMAtrix - 4x4 transformation matrix
   * @return new transformed LineSegment
   */
  public LineSegment transform (Matrix4d transformMatrix) {
    Point3d newStart = new Point3d();
    Point3d newEnd = new Point3d();
    transformMatrix.transform(this.startPoint, newStart);
    transformMatrix.transform(this.endPoint, newEnd);
    return new LineSegment(newStart, newEnd);
  }

  /** To translate LineSegment based on given translation vector.
   *  (modifies original LineSegment)
   */
  public LineSegment translateSelf (Vector3d translationVector) {
    startPoint.add(translationVector);
    endPoint.add(translationVector);
    return this;
  }

  /** To get new LineSegment from given LineSegment based on given translation vector.
   *  (does NOT modify original LineSegment)
   */
  public LineSegment translate (Vector3d translationVector) {
    Point3d newStart = new Point3d(startPoint);
    Point3d newEnd = new Point3d(endPoint);
    newStart.add(translationVector);
    newEnd.add(translationVector);
    return new LineSegment(newStart, newEnd);
  }


  /** Method to get length of LineSegment.
   *
   */
   public double getLength() {
     return startPoint.distance(endPoint);
   }

   public Vector3d getTangent (Point3d point) {
     Vector3d direction = new Vector3d (endPoint);
     direction.sub(startPoint);
     return direction ;
   }
	/** Returns start point of line segment.
	 *
	 */
	public Point3d gettStartPoint() {
		return startPoint;
	}
	
	public double[] getStartPoint() {
		double[] ret = new double[]{this.startPoint.x, this.startPoint.y,this.startPoint.z};
		return ret;
	}

	/**
	 * Returns end point of line segment.
	 *   
	 *   
	 */
	public Point3d gettEndPoint() {
		return endPoint;
	}
	
	public double[] getEndPoint() {
		double[] ret = new double[]{this.endPoint.x, this.endPoint.y,this.endPoint.z};
		return ret;
	}
	
	public void setStartPoint(double[] startPoint) {
		this.startPoint = new Point3d(startPoint[0],startPoint[1],startPoint[2] );
	}

	public void setEndPoint(double[] endPoint) {
		this.endPoint = new Point3d(endPoint[0],endPoint[1],endPoint[2] );
	}

   public Line2d getLine2d () {
     return getLine2d(GeometryConstants.Z_VECTOR);
   }

   public Line2d getLine2d(Vector3d projectionVector)
 {
   Line2d line2d = new Line2d();
   try
   {
     Matrix4d rotationYMatrix = Gtk.computeTransformMatrix(projectionVector);
     LineSegment xyLineSeg = this.transform(rotationYMatrix);
     Point2d stPoint2d = new Point2d(xyLineSeg.startPoint.x,xyLineSeg.startPoint.y);
     Point2d endPoint2d = new Point2d(xyLineSeg.endPoint.x,xyLineSeg.endPoint.y);
     line2d = new Line2d(stPoint2d,endPoint2d);
   }
   catch(Exception ex)
   {
     //never happens
   }
   return line2d;
  }
   
   public CurveSegment swap()
   {
   	LineSegment line =  new LineSegment(this.gettEndPoint(), this.gettStartPoint());   	
   	return line;
   }

  /** init() method.
   *  (called by DrawWFApplet init)
   */
  public void init () {
    // displays label with toString of LineSegment.
    super.init();
    panel.add (new JLabel (this.toString()));
  }

  /**
   * Returns the size of applet for display
   */
  public Dimension getAppletSize () {
    Dimension finalPanelSize = this.panel.getPreferredSize();
    return new Dimension(finalPanelSize.width + 10, finalPanelSize.height + 10);
  }

  /** get shapes list - a linked list of 2D shapes representing line segment.
   *  (called by DrawWFCanvas getShapeList)
   */
  public LinkedList getShapeList(DrawWFPanel canvas) {
    LinkedList shapeList = new LinkedList();
    // get 2D shape for tranformed line segment and add to shapeList.
    shapeList.add(this.createDisplayLine(canvas));
    return shapeList;
  } 

  /** get display line.
   *  (returns a 2D shape for line segment suitable for display based on view point)
   */
   public Line2D.Double createDisplayLine(DrawWFPanel canvas) {
//	   System.out.println("start point:" + getStartPoint());
//	   System.out.println("end point:" + getEndPoint());
    return new Line2D.Double (canvas.createDisplayPoint(gettStartPoint ()),
				canvas.createDisplayPoint(gettEndPoint ()) );
   }


  /** paintComponent (Graphics g)
   *  (calls graphics2D paintcomponent)
   */
  public void paintComponent (Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    paintComponent (g2d);
  }

  /** paintComponent (Graphics2D g)
   *  (calls drawWFCanvas paintcomponent)
   */
  public void paintComponent (Graphics2D g) {
    ((DrawWFPanel) this.gettCanvas()).paintComponent(g);
  }

  public void paintComponent (Graphics2D g, DrawWFPanel canvas) {
    ViewObject.doNothing(this, "color is " + gettColor());
      g.draw (this.createDisplayLine(canvas));
  }

  /** repaint()
   *  (calls drawWFCanvas repaint)
   */
     public void repaint() {
    ((DrawWFPanel) this.gettCanvas()).repaint();
  }

  /** toString() to represent LineSegment.
   *
   */
  public String toString () {
    return this.getClass().getName() + ": " + Tuple3dRenderer.format(startPoint) + 
    " -- " + Tuple3dRenderer.format(endPoint);
  }

  /** main method.
   *
   */
  public static void main (String args []) {
    LineSegment ls = new LineSegment(2,0,0,5,5,0);
    //ls.setApplet();
    ls.settApplet(new DrawWFApplet (ls, new Point3d(1,1,1), 50));
    ls.display("Line segment", new Dimension(600,600), JFrame.EXIT_ON_CLOSE);
  }
}  // END OF CASS DEFINITION.