package edu.ohiou.mfgresearch.labimp.gtk2d;




/**
 * <p>Title: Generic classes for manufacturing planning</p>
 * <p>Description: Thsi project implements general classes for intelligent manufacturing planning. These are:
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: Ohio University</p>
 * @author Dusan Sormaz
 * @version 1.0
 */

import java.util.*;
import java.awt.Color;
import java.awt.geom.*;

import edu.ohiou.mfgresearch.labimp.basis.Draw2DApplet;
import edu.ohiou.mfgresearch.labimp.basis.ViewObject;

public class BezierCurve2D extends ViewObject {



	/**
	 * <p>Title: ise 589 project for messge center, spring 02/03</p>
	 * <p>Description: </p>
	 * <p>Copyright: Copyright (c) 2003</p>
	 * <p>Company: Ohio University</p>
	 * @author Dusan N. Sormaz
	 * @version 1.0
	 */

		List points = new LinkedList();
		int nsteps = 100;
		Color controlColor = Color.green;

		public BezierCurve2D (List points) {
			this.points = new LinkedList (points);
			color = Color.blue;
		}
		public static void main(String[] args) {
			Point2D.Double p1 = new Point2D.Double(0,0);
			Point2D.Double p2 = new Point2D.Double(1,1);
			Point2D.Double p3 = new Point2D.Double(2,1);
			Point2D.Double p4 = new Point2D.Double(3,0);
			Point2D.Double p5 = new Point2D.Double(5,1);
			Point2D.Double p6= new Point2D.Double(8,0);
			LinkedList list = new LinkedList();
			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
			list.add(p6);
			BezierCurve2D bc = new BezierCurve2D(list);
			bc.settApplet(new Draw2DApplet (bc));
			bc.display();
		}

//		public LinkedList getShapeContour (DrawWFPanel canvas) {
//			LinkedList list = new LinkedList();
//			for (int i = 0; i < nsteps; i++) {
//				double u1 = (double) i / (double) nsteps;
//				double u2 = (double) (i+1) / (double) nsteps;
//				Point2D.Double start = calculatePoint(u1);
//				Point2D.Double end = calculatePoint(u2);
//				LineSegment ls = new LineSegment (new Point3d (start.x, start.y, 0), new Point3d (end.x, end.y, 0));
//				list.addAll(ls.getShapeList(canvas));
//			}
//			return list;
//		}
//
//		public LinkedList getShapeList (DrawWFPanel canvas) {
//			LinkedList list = getShapeContour (canvas);
//			for (int i = 0; i < points.size(); i++) {
//				LineSegment line = new LineSegment ((Point3d) points.get(i), (Point3d)points.get((i+1 )% points.size()));
//				list.addAll(line.getShapeList (canvas));
//			}
//			return list;
//		}

		public LinkedList geetDrawList () {
			LinkedList list = new LinkedList();
			for (int i = 0; i < nsteps; i++) {
				double u1 = (double) i / (double) nsteps;
				double u2 = (double) (i+1) / (double) nsteps;
				Point2D.Double start = calculatePoint(u1);
				Point2D.Double end = calculatePoint(u2);
				Line2D.Double ls = new Line2D.Double (start, end);
				list.add(ls);
			}
			return list;
		}

//		public void paintComponent (Graphics2D g, DrawWFPanel canvas)
//		{
//			LinkedList list = getShapeContour (canvas);
//			g.setColor(color);
//			for (Iterator itr = list.iterator(); itr.hasNext();){
//				g.draw ((Shape) itr.next() );
//			}
//			g.setColor(controlColor);
//			int size = points.size();
//			for (int i=0;i < size; i++)
//			{
//				LineSegment line = new LineSegment((Point3d)points.get(i),
//				(Point3d)points.get((i+1)%size));
//				g.draw(line.createDisplayLine(canvas));
//			}
//		}

		public Point2D.Double calculatePoint (double u) {
			Point2D.Double oldPoints [] = new Point2D.Double [points.size()];
			for (int i=0; i < points.size(); i++) {
				oldPoints [i] = (Point2D.Double) points.get(i);
			}
			Point2D.Double oldStart, oldEnd;
			while (true) {
//      System.out.println("oldLength" + oldPoints.length);
				int newLength = oldPoints.length -1;
//      System.out.println("newLength" + newLength);
				Point2D.Double newPoints [] = new Point2D.Double [newLength];
				for (int i = 0; i < newLength; i++) {
					oldStart = new Point2D.Double (oldPoints [i].x, oldPoints [i].y );
					AffineTransform at = new AffineTransform();
					at.setToScale(1-u, 1-u);
					at.transform(oldStart, oldStart);

					oldEnd = new Point2D.Double (oldPoints [i+1].x, oldPoints [i+1].y);
					at.setToScale(u, u);
					at.transform(oldEnd, oldEnd);
					oldStart.x = oldStart.x + oldEnd.x;
					oldStart.y = oldStart.y + oldEnd.y;
					newPoints [i] = new Point2D.Double (oldStart.x, oldStart.y);
				}
				if (newPoints.length == 1)
					return newPoints[0];
				else
					oldPoints = newPoints;
			}
		}
}