/*
 * Created on Mar 17, 2006
 * This class is written for hint generation purposes in extrude sketch-based
 * feature recognition.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.ohiou.mfgresearch.labimp.gtk3d;

import java.util.*;
import java.awt.geom.*;

import javax.vecmath.*;

import edu.ohiou.mfgresearch.labimp.basis.*;
import edu.ohiou.mfgresearch.labimp.gtk2d.*;

import jess.*;

/**
 * @author Chandu
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Loop2d extends Prof2d {

	public Loop2d() {

	}
	
	//returns 1 if the arc is "convex", i.e. it adds to the area of the loop
	//       -1 if it is "concave" i.e. cuts into the loop area
	//        0 if it does both

	private boolean showArrows = false;
	
	public void setShowArrows(boolean value)
	{
		showArrows = value;
	}
	
	public int isArcConvex(Arc2d arc) {
		if (Gtk.epsilonEquals(
				(arc.getEndAngle() - arc.getBeginAngle()),2*Math.PI)) 
			return 1;
		int n = getPoints().size();
		double[] xp = new double[n];
		double[] yp = new double[n];
		Point2d extend0 = arc.getPoint(arc.getBeginAngle() - 0.001);
		Point2d extend1 = arc.getPoint(arc.getEndAngle() + 0.001);
		for(int i = 0; i < n; i++)
		{
			Point2d point = (Point2d) getPoints().get(i);
			xp[i] = point.x;
			yp[i] = point.y;
		}		
		if (Polygon2d.isPointInside(xp, yp, extend0.x, extend0.y) 
				&& Polygon2d.isPointInside(xp, yp, extend1.x, extend1.y))
			return 1;
		if (!Polygon2d.isPointInside(xp, yp, extend0.x, extend0.y)
				&& !Polygon2d.isPointInside(xp, yp, extend1.x, extend1.y))
			return -1;
		else return 0;
	}
	
	protected List getPointss() {
		List points = new LinkedList();
		int curvesNo = getNumberOfCurves();
		Point2d traversalPoint;
		try {
			traversalPoint = ((Curve2d) this.getCurve(curvesNo - 1))
				.findCommonPoint((Curve2d) this.getCurve(0));
		} catch (InvalidIntersectionException ex) {
			traversalPoint = ((Curve2d) this.getCurve(0)).getStartPoint();
		}
//		 System.out.println(">>>>>>>>>> in getPoints() printing curve\n");
//	      System.out.println("c --->"+this.getCurves());
		for (Iterator iter = getIterator(); iter.hasNext();) {
			Curve2d c = (Curve2d) iter.next();
//			      System.out.println("c --->"+c);
			//      System.out.println("traversalpoint --->"+traversalPoint);
			if (traversalPoint.epsilonEquals(
				c.getEndPoint(),
				GeometryConstants.EPSILON)) {
				points.add(c.getEndPoint());
				traversalPoint = c.getStartPoint();
			} else {
				points.add(c.getStartPoint());
				traversalPoint = c.getEndPoint();
			}
			
		}
//      System.out.println("in getPoints: points --->"+points);
		return points;
	}
	
	public Vector2d getCurveNormal(Curve2d curve)
	{
		if (curve instanceof Line2d)
		{
			return getLineNormal((Line2d) curve);
		}
		if (curve instanceof Arc2d)
		{
			return getArcLoopNormal((Arc2d) curve);
		}
		return new Vector2d();
	}
	//returns a vector whose direction is perpendicular to the given line and
	//facing outward w.r.t. this loop.
	
	public Vector2d getLineNormal(Line2d line)
	{
		Draw2DApplet da = new Draw2DApplet(this);
		this.display();
		Vector2d dir = new Vector2d(line.getDirection());
		Vector2d nDir = new Vector2d(-dir.y, dir.x);
		Vector2d negDir = new Vector2d();
		negDir.negate(nDir);
		Vector2d sc = new Vector2d(nDir);
		sc.scale(20. * GeometryConstants.EPSILON);
		final Point2d mp = line.pointOnLine(line.getLength()/2);
		mp.add(sc);
		List points = getPoints();
		final int n = points.size();
		final double[] xp = new double[n];
		final double[] yp = new double[n];
//		System.out.println(">>>>>>>>>> mpoint is: " + mp);
		for(int i = 0; i < n; i++) 
		{
			Point2d point = (Point2d) points.get(i);
			xp[i] = point.x;
			yp[i] = point.y;
//			System.out.println("-------------- point" + i + " is: " + xp[i] + ", " + yp[i]);
		}
/*		ViewObject vo = new ViewObject () {
			public LinkedList getDrawList () {
				LinkedList list = new LinkedList(); 
				for (int i = 0; i <n-1; i++) {
					list.add(new Line2D.Double (xp[i], yp[i], xp[i+1], yp[i+1]));
				}
				list.add (new Ellipse2D.Double(mp.x, mp.y, mp.x+0.2, mp.y+0.2));
				return list;
			}
		};
		vo.setApplet (new Draw2DApplet(vo));
		vo.display();*/
		 return Polygon2d.isPointInside(xp, yp, mp.x, mp.y)? negDir: nDir; 
	}
	
	public Vector2d getArcLoopNormal(Arc2d arc)
	{
		Vector2d vec = new Vector2d();
		try
		{
			Line2d line = new Line2d(arc.getStartPoint(), arc.getEndPoint());
			vec = getLineNormal(line);
		}
		catch(Exception ile)
		{
			vec = new Vector2d(1.0, 0.0);
//			ile.printStackTrace();
			//shouldn't happen unless arc has zero length or method is 
			//called on a circle
		}
		return vec;
	}
  
  public boolean areLinesFacing (Line2d line, Line2d otherLine) {
    return areLinesFacing (line, otherLine, 
        getLineNormal(line), getLineNormal(otherLine));
  }
  
	//calculates whether the normals of two lines face toward or away from each other
	//check method
	public boolean areLinesFacing(Line2d line, Line2d otherLine, 
      Vector2d vector, Vector2d otherVector) {
	
//		Vector2d sc1 = new Vector2d();
//		Vector2d sc2 = new Vector2d();
		vector.scale(GeometryConstants.EPSILON);
		otherVector.scale(GeometryConstants.EPSILON);
		Point2d mp1 = line.pointOnLine(line.getLength()/2);
		Point2d mp2 = otherLine.pointOnLine(line.getLength()/2);		
		double dmp = mp1.distance(mp2);
		mp1.add(vector);
		mp2.add(otherVector);
		double dop = mp1.distance(mp2);
		return (dmp > dop)? true: false;
	}
	
	//looks for "overlap" in two parallel lines
	//if lines are perpendicular, returns false.
	//if lines are not parallel, calculates overlap between projection 
	//of first in the other's direction, and the other line.
	public boolean doLinesOverlap(Line2d line, Line2d otherLine)
	{
		double angle = (line.angle(otherLine));
		if(Gtk.epsilonEquals(angle, Math.PI/2)) return false;
		if (!Gtk.epsilonEquals(angle, Math.PI) 
				|| !Gtk.epsilonEquals(angle, 0.0))		
		{
			Line2d pLine = new Line2d (otherLine.getDirection(), line.getStartPoint());
			Point2d pp = pLine.projectPoint(line.getEndPoint());
			pLine.setEndPoint(pp);
			line = pLine;
		}
		try
		{
			Line2d line1 = 
				new Line2d(line.pointOnLine(0.001), 
						line.pointOnLine(line.factorForPoint(line.getEndPoint()) - 0.001));
			//the above line of code ensures that two lines with exactly one
			//end point (and no other overlap) are not returned, e.g.
			//Line1[(0.0, 0.0), (1.0, 0.0)] and Line2[(1.0, 1.0),(2.0, 1.0)]
			Point2d lspp = otherLine.projectPoint(line1.getStartPoint());
			Point2d lepp = otherLine.projectPoint(line1.getEndPoint());
			Point2d olspp = line1.projectPoint(otherLine.getStartPoint());
			Point2d olepp = line1.projectPoint(otherLine.getEndPoint());
			
			return ((line1.isPointOnCurve(olspp)) 
					|| line1.isPointOnCurve(olepp))	|| 
					((otherLine.isPointOnCurve(lspp)) 
							|| otherLine.isPointOnCurve(lepp));
		}
		catch (GeometryException ile)
		{
			ile.printStackTrace();
		}		
		return false;
	}
	
	

	public void makeFact(Rete rete, String prof, int count, boolean outer) {

		// enter code that will make Jes fact from this object
		try {
			Fact f = new Fact("Loop", rete);
			f.setSlotValue("ID", new Value(this));
			f.setSlotValue("profileID", new Value(prof, RU.STRING));
			f.setSlotValue("outer", new Value(outer));
			ValueVector vv = new ValueVector();
			for(Iterator itr = getCurves().iterator(); itr.hasNext();)
			{
				vv.add(new Value(itr.next()));
			}
			f.setSlotValue("curves", new Value(vv, RU.LIST));
			rete.assertFact(f);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String toString()
	{
		return new String("Loop2d");
	}
	
	public String toToolTipString()
	{
		return ((Prof2d) this).toString();
	}
	
	public LinkedList getDrawList()
	{
		if (showArrows == false) return super.getDrawList();
		
		LinkedList lines = super.getDrawList();
		List curves = getCurves();
//		LinkedList arrows = new LinkedList();
		Point2d cp = new Point2d();
		Point2d cp1 = new Point2d();
		Vector2d dir1 = new Vector2d();
		for (Iterator itr = curves.iterator(); itr.hasNext();)
		{
			Curve2d currCurve = (Curve2d) itr.next();
			if (currCurve instanceof Line2d)
			{
				Line2d line = (Line2d) currCurve;
				cp = line.pointOnLine(line.getLength()/2);
				cp1 = line.pointOnLine(line.getLength()/2 + 0.2);
				Vector2d dir = line.getDirection();
				dir1 = new Vector2d(-dir.y, dir.x);				
			}
			if (currCurve instanceof Arc2d)
			{
				Arc2d arc = (Arc2d) currCurve;
				cp = arc.getPoint((arc.getBeginAngle() + arc.getEndAngle())/2);
				cp1 = arc.getPoint((arc.getBeginAngle() + arc.getEndAngle())/2 + 0.4);
				Point2d center = arc.getCenter();
				dir1 = new Vector2d (center.x - cp.x, center.y - cp.y);				
			}
			dir1.normalize();
			dir1.scale(0.1);
			Point2d sp1 = new Point2d(cp), sp2 = new Point2d(cp);
			sp1.add(dir1);
			dir1.negate();
			sp2.add(dir1);
			lines.add(new Line2D.Double(cp1.x, cp1.y, sp1.x, sp1.y));
			lines.add(new Line2D.Double(cp1.x, cp1.y, sp2.x, sp2.y));			
		}		
		return lines;
	}

	public static void main(String[] args) {
	}
}
