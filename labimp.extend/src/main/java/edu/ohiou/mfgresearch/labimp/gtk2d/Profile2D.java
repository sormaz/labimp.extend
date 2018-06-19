package edu.ohiou.mfgresearch.labimp.gtk2d;

/**
 * Title:        Inlet Fan Case Fiper Demo
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      Ohio University
 * @author
 * @version 1.0
 */
import java.util.*;
import java.awt.geom.*;

import javax.swing.JList;

import edu.ohiou.mfgresearch.labimp.basis.Draw2DApplet;
import edu.ohiou.mfgresearch.labimp.basis.Draw2DPanel;
import edu.ohiou.mfgresearch.labimp.basis.DrawString;
import edu.ohiou.mfgresearch.labimp.basis.ViewObject;

public class Profile2D extends ViewObject {
  LinkedList points;
  boolean closed;
//  public static final double EPSILON = 0.00001;

  public Profile2D(Point2D [] inPoints, boolean inClosed) {
    closed = inClosed;
    points = new LinkedList ();
    for (int i = 0; i < inPoints.length; i++)
      points.add(inPoints[i]);
  }

  public Profile2D (double [] x, double [] y, boolean inClosed) throws Exception {
    if (x.length == y.length) {
    points = new LinkedList();
      Point2D.Double point;
      for (int i = 0; i < x.length; i++) {
        points.add(new Point2D.Double (x[i], y[i]));
      }
    }
    else throw new Exception ("Y array is different size from X array");
  }

  public Profile2D(Point2D [] inPoints) {
    this (inPoints, false);
  }

  public Profile2D(LinkedList inPoints) {
    this (inPoints, false);
  }

  public Profile2D (LinkedList inPoints, boolean inClosed) {
    closed = inClosed;
    points = inPoints;
  }

  public Profile2D (Rectangle2D rectangle) {
    this (new LinkedList() );
    double x = rectangle.getX(), y = rectangle.getY(),
            w = rectangle.getWidth(), h = rectangle.getHeight();
    addPoint (new Point2D.Double(x,y) );
    addPoint (new Point2D.Double(x+w,y));
    addPoint (new Point2D.Double(x+w,y+h));
    addPoint (new Point2D.Double(x,y+h));
  }

  public void addPoint (Point2D point) {
    points.add(point);
  }

  public double getBoundingArea () {
    Rectangle2D rectangle = getBoundingBox();
    return rectangle.getWidth() * rectangle.getHeight();
  }

 public LinkedList getPointSet () {
   return points;
 }

  public Rectangle2D getBoundingBox () {
    double maxX=Double.MIN_VALUE, maxY=Double.MIN_VALUE,
          minX=Double.MAX_VALUE, minY=Double.MAX_VALUE;
    for (ListIterator itr = points.listIterator();itr.hasNext();) {
      Point2D.Double p = (Point2D.Double) itr.next();
      maxX = p.x > maxX ? p.x : maxX;
      maxY = p.y > maxY ? p.y : maxY;
      minX = p.x < minX ? p.x : minX;
      minY = p.y < minY ? p.y : minY;
    }
    return new Rectangle2D.Double (minX, minY, maxX-minX, maxY-minY);
  }

  private Profile2D offset (double offset) throws InvalidProfileException {
    LinkedList newPoints = new LinkedList();
//    Line3d oldLine;
//    Vector3d normal = new Vector3d();
    Point2D p1 = (Point2D) points.getLast();
    Point2D p2 = (Point2D) points.getFirst();
    Point2D p3 = (Point2D) points.get(2);
    Point2D.Double np1 =  (Point2D.Double) p1.clone();
    Point2D.Double np2 =  (Point2D.Double) p2.clone();
    Point2D.Double np3 =  (Point2D.Double) p3.clone();
//    p2.sub(p1);
//    p3.sub(p1);
//    normal.cross(new Vector3d (p2), new Vector3d (p3));
//    try {
//      oldLine = new Line3d (p2, p1);
//      oldLine.translate(vector);
//
//    }
//    catch (InvalidLineException ex) {
//      throw new InvalidProfileException ("Original profile is not valid");
//    }
//
//    for (int i = 0; i < points.size();i++) {
//      Point2D point1 = (Point2D) points.get(i);
//      Point2D point2 = (Point2D) points.get((i+1)% points.size());
//      try {
//        Line3d newLine = new Line3d (point2, point1);
//        Point2D newPoint = newLine.intersectLineLine(oldLine);
//      }
//      catch (InvalidLineException ex) {
//        ex.printStackTrace();
//      }
//
//    }
    return new Profile2D(newPoints);
  }




  public static void main (String [] args) {
    Point2D.Double p1 = new Point2D.Double (1,1);
    Point2D.Double p2 = new Point2D.Double (5,1);
//    Point2D.Double p3 = new Point2D.Double (2,2);
    Point2D.Double p4 = new Point2D.Double (1,2);

    Point2D [] pts = {p1, p2, p4};

    Profile2D profile = new Profile2D (pts, true);

    System.out.println("area" + profile.areaTwo());
    profile.settApplet(new Draw2DApplet (profile));
    ((Draw2DPanel)profile.geettCanvas()).setDisplayStrings(true);
    profile.display();
  }

  public void init () {
    panel = new ViewPanel();
    panel.add(new JList(points.toArray() ));
  }


  public LinkedList geetDrawList () {
    LinkedList shapeList = new LinkedList();
    int size = points.size();
    for (int i = 0; i < size; i++)
      shapeList.add(new Line2D.Double ((Point2D)points.get(i),
                                        (Point2D)points.get((i+1)%size)));
    return shapeList;
  }

  public LinkedList geetStringList () {
    LinkedList strings = new LinkedList();
    strings.add(new DrawString (toString(), 0,0));
    return strings;
  }

//  public LinkedList getShapeList (DrawWFPanel canvas) {
//    LinkedList shapes = new LinkedList();
//    int size = points.size();
//    for (int i = 0; i < size; i++) {
//      Point2D p1 =  (Point2D) points.get(i);
//      Point2D p2 = (Point2D) points.get( (i+1)% size);
//      LineSegment line = new LineSegment ( new Point3d (p1.getX(), p1.getY(), 0), new Point3d (p2.getX(), p2.getY(), 0) );
//      shapes.addAll(line.getShapeList(canvas));
//    }
//    return shapes;
//  }

  public String toString() {
    return points.toString();
  }

  public static double areaTwo (Point2D point, Line2D line) {
    return areaTwo(line.getP1(), line.getP2(), point);
  }

  public static double areaTwo (Point2D pointA, Point2D pointB, Point2D pointC) {
    double a0,a1,b0,b1,c0,c1;
    a0 = pointA.getX();
    a1 = pointA.getY();
    b0 = pointB.getX();
    b1 = pointB.getY();
    c0 = pointC.getX();
    c1 = pointC.getY();
    return  a0 * b1 - a1 * b0 +
            a1 * c0 - a0 * c1 +
            b0 * c1 - b1 * c0;
  }

  public double areaTwo () {
    double area = 0.0;
    for (int i = 1; i < points.size()-1; i++) {
      area += areaTwo (
      (Point2D) points.get(0), (Point2D)points.get(i), (Point2D)points.get(i+1));
    }
    return area;
  }

  public boolean left (Point2D point, Line2D line) {
    return (areaTwo (point, line) < -GtkConstants.EPSILON) ? true : false;
  }

  public boolean collinear (Point2D point, Line2D line) {
    return (Math.abs (areaTwo( point, line)) < GtkConstants.EPSILON) ? true : false;
  }

}
