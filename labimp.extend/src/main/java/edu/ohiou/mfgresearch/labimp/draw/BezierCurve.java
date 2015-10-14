package edu.ohiou.mfgresearch.labimp.draw;

import edu.ohiou.mfgresearch.labimp.basis.*;
import edu.ohiou.mfgresearch.labimp.draw.*;
import edu.ohiou.mfgresearch.labimp.gtk3d.LineSegment;
import javax.vecmath.*;
import java.util.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * <p>Title: ise 589 project for messge center, spring 02/03</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ohio University</p>
 * @author Dusan N. Sormaz
 * @version 1.0
 */

public class BezierCurve extends ImpObject {
  List points = new LinkedList();
  int nsteps = 20;
  Color controlColor = Color.green;

  public BezierCurve(List points) {
    this.points = new LinkedList (points);
    color = Color.blue;
  }
  public static void main(String[] args) {
    Point3d p1 = new Point3d(0,0,0);
    Point3d p2 = new Point3d(1,1,0);
    Point3d p3 = new Point3d(2,1,0);
    Point3d p4 = new Point3d(3,0,0);
    Point3d p5 = new Point3d(5,1,0);
    Point3d p6= new Point3d(8,0,0);
    LinkedList list = new LinkedList();
    list.add(p1);
    list.add(p2);
    list.add(p3);
    list.add(p4);
    list.add(p5);
    list.add(p6);
    BezierCurve bc = new BezierCurve(list);
    bc.settApplet(new DrawWFApplet (bc));
    bc.display();
    
  }

  public LinkedList getShapeContour (DrawWFPanel canvas) {
    LinkedList list = new LinkedList();
    for (int i = 0; i < nsteps; i++) {
      double u1 = (double) i / (double) nsteps;
      double u2 = (double) (i+1) / (double) nsteps;
      LineSegment ls = new LineSegment (calculatePoint(u1), calculatePoint(u2));
      list.addAll(ls.getShapeList(canvas));
    }
    return list;
  }

  public LinkedList getShapeList (DrawWFPanel canvas) {
    LinkedList list = getShapeContour (canvas);
    for (int i = 0; i < points.size(); i++) {
      LineSegment line = new LineSegment ((Point3d) points.get(i), (Point3d)points.get((i+1 )% points.size()));
      list.addAll(line.getShapeList (canvas));
    }
    return list;
  }

  public void paintComponent (Graphics2D g, DrawWFPanel canvas)
  {
    LinkedList list = getShapeContour (canvas);
    g.setColor(color);
    for (Iterator itr = list.iterator(); itr.hasNext();){
      g.draw ((Shape) itr.next() );
    }
    g.setColor(controlColor);
    int size = points.size();
    for (int i=0;i < size; i++)
    {
      LineSegment line = new LineSegment((Point3d)points.get(i),
      (Point3d)points.get((i+1)%size));
      g.draw(line.createDisplayLine(canvas));
    }
  }


  public LinkedList getPointSet () {
    return (LinkedList) points;
  }

  public Point3d calculatePoint (double u) {
    Point3d oldPoints [] = new Point3d [points.size()];
    for (int i=0; i < points.size(); i++) {
      oldPoints [i] = (Point3d) points.get(i);
    }
    Point3d oldStart, oldEnd;
    while (true) {
//      System.out.println("oldLength" + oldPoints.length);
      int newLength = oldPoints.length -1;
//      System.out.println("newLength" + newLength);
      Point3d newPoints [] = new Point3d [newLength];
      for (int i = 0; i < newLength; i++) {
        oldStart = new Point3d (oldPoints [i]);
        oldStart.scale(1-u);
        oldEnd = new Point3d (oldPoints [i+1]);
        oldEnd.scale(u);
        oldStart.add(oldEnd);
        newPoints [i] = new Point3d (oldStart);
      }
      if (newPoints.length == 1)
        return newPoints[0];
      else
        oldPoints = newPoints;
    }
  }
}