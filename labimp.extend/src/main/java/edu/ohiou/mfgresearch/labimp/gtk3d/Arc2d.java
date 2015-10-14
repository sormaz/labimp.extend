package edu.ohiou.mfgresearch.labimp.gtk3d;

import edu.ohiou.mfgresearch.labimp.draw.DrawWFPanel;
import edu.ohiou.mfgresearch.labimp.basis.*;
import edu.ohiou.mfgresearch.labimp.draw.*;
import edu.ohiou.mfgresearch.labimp.gtk2d.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.util.*;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

public class Arc2d extends Curve2d {
	private Point2d center;
	private double radius;
	private double beginAngle;
	private double endAngle;
	int noOfPoints = 50;

	public Arc2d() throws InvalidArcException {
		this(new Point2d(0, 0), 1, 0, Math.PI * 2);
	}

	public Arc2d(Arc2d newArc) throws InvalidArcException {
		this(newArc.center, newArc.radius, newArc.beginAngle, newArc.endAngle);
	}

	public Arc2d(Point2d center, double radius) throws InvalidArcException {
		this(center, radius, 0, Math.PI * 2);
	}

	public Arc2d(
		Point2d center,
		double radius,
		double beginAngle,
		double endAngle) throws InvalidArcException {
		if (radius <= 0)
			throw new InvalidArcException("radius should be > 0.0");
		if (endAngle <= beginAngle)
			throw new InvalidArcException("startAngle should be < endAngle");
		this.center = center;
		this.radius = radius;
		this.beginAngle = beginAngle;
		this.endAngle = endAngle;
	}

	public Point2d getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
	}

	public double getBeginAngle() {
		return beginAngle;
	}

	public double getEndAngle() {
		return endAngle;
	}

	public void setCenter(Point2d newCenter) {
		center = newCenter;
	}

	public void setRadius(double newRadius) {
		radius = newRadius;
	}

	public void setBeginAngle(double newBeginAngle) {
		beginAngle = newBeginAngle;
	}

	public void setEndAngle(double newEndAngle) {
		endAngle = newEndAngle;
	}

	public Point2d getStartPoint() {
		return new Point2d(center.x + radius * Math.cos(beginAngle), center.y
			+ radius
			* Math.sin(beginAngle));
	}

	public Point2d getEndPoint() {
		return new Point2d(center.x + radius * Math.cos(endAngle), center.y
			+ radius
			* Math.sin(endAngle));
	}

	public void setStartPoint(Point2d startPt) {
		//yet to be implemented. Implemented for Profile2d's offset method
	}

	public void setEndPoint(Point2d endPt) {
		//yet to be implemented. Implemented for Profile2d's offset method
	}

	/**
	 * Returns a boolean indicating whether this is equal to another Arc2d based
	 * on the supplied epsilon.
	 *
	 * @param otherArc Arc2d for which the equality needs to be checked
	 * @param epsilon double value based on which the comparison is done
	 * @return true if this is equal to otherArc false otherwise.
	 */
	public boolean epsilonEquals(Arc2d otherArc, double epsilon) {
		return (center.epsilonEquals(otherArc.getCenter(), epsilon)
			&& Gtk.epsilonEquals(radius, otherArc.getRadius(), epsilon)
			&& Gtk.epsilonEquals(beginAngle, otherArc.getBeginAngle(), epsilon) && Gtk
			.epsilonEquals(endAngle, otherArc.getEndAngle(), epsilon));
	}

	/**
	 *
	 *  Creates and returns a string representation of a circle. The returned
	 *  string is in the format "C (x, y), R r".
	 *  @return The string representation of the circle.
	 * 
	 */

	public String toString() {
		java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		StringBuffer sb = new StringBuffer();
		sb.append("Arc:> Start ");
		sb.append(getStartPoint().toString());
		sb.append(",End ");
		sb.append(getEndPoint().toString());
		sb.append(",Center ");
		sb.append(center.toString());
		sb.append(", R ");
		sb.append(nf.format(radius));
		sb.append(", from " + nf.format(beginAngle * 360 / (2 * Math.PI)));
		sb.append(", to " + nf.format(endAngle * 360 / (2 * Math.PI)));
		return new String(sb);
	}

	public Point2d getPoint(double angle) {
		return new Point2d(center.x + radius * Math.cos(angle), center.y
			+ radius
			* Math.sin(angle));
	}

	public double getAngle(Point2d point) {
		Vector2d xAxis = new Vector2d(1, 0);
		double angle = xAxis.angle(new Vector2d(point.x - center.x, point.y
			- center.y));
		if (point.y - center.y < 0)
			angle = -angle;
		if (angle > getEndAngle()) {
			angle -= Math.PI * 2;
		}
		if (angle < getBeginAngle()) {
			angle += Math.PI * 2;
		}
		return angle;
	}

	public Vector2d getTangentVectorAt(Point2d point) {
		//  	if (classify(point) != GeometryConstants.ON_BOUNDARY) return null;
		Vector3d radVector = new Vector3d(center.x - point.x, center.y
			- point.y, 0);
		Vector3d tV = new Vector3d();
		tV.cross(radVector, GeometryConstants.Z_VECTOR);
		Vector2d tangent = new Vector2d(tV.x, tV.y);
		System.out.println("tangent :" + tangent);
		return tangent;
	}

	public int classify(Point2d point) {
		double angle = getAngle(point);
		if (Gtk.epsilonEquals(angle, beginAngle)
			|| Gtk.epsilonEquals(angle, endAngle))
			return GeometryConstants.ON_BOUNDARY;
		else
			return angle < beginAngle || angle > endAngle
				? GeometryConstants.OUTSIDE
				: GeometryConstants.INSIDE;
	}

	public boolean isPointOnCurve(Point2d pt) {
		return (classify(pt) == GeometryConstants.ON_BOUNDARY);
	}
	
	/**
	 * Clips start of arc (this) to limitPoint.
	 * Does nothing if limitPoint lies outside the arc.
	 * Throws InvalidCurveException if point does not lie on arc trajectory.
	 * @author Chandu
	 * @date 9/16/2006
	 * @param limitPoint
	 * @return
	 * @throws InvalidCurveException
	 */
	public void clipStart(Point2d limitPoint)
	{
//		if (!Gtk.epsilonEquals(limitPoint.distance(center), radius))
//				throw new InvalidCurveException("Point does not lie on arc's circle");
		if (classify(limitPoint) == GeometryConstants.INSIDE)
		{
			this.setBeginAngle(getAngle(limitPoint));
		}
	}
	
	/**
	 * Clips end of arc (this) to limitPoint.
	 * Does nothing if limitPoint lies outside the arc.
	 * Throws InvalidCurveException if point does not lie on arc trajectory.
	 * @author Chandu
	 * @date 9/16/2006
	 * @param limitPoint
	 * @return
	 * @throws InvalidCurveException
	 */
	public void clipEnd(Point2d limitPoint)
	{
//		if (!Gtk.epsilonEquals(limitPoint.distance(center), radius))
//				throw new InvalidCurveException("Point does not lie on arc's circle");
		if (classify(limitPoint) == GeometryConstants.INSIDE)
		{
			this.setEndAngle(getAngle(limitPoint));
		}
	}
	
	/**
	 * Returns a new arc clipped to the limit points.
	 * Does nothing if points lie outside start and end of this arc.
	 * @author Chandu
	 * @date 9/16/2006
	 * @param limitPoint1
	 * @param limitPoint2
	 * @return new clipped arc
	 * @throws InvalidCurveException
	 */
	public Arc2d clip(Point2d limitPoint1, Point2d limitPoint2) 
		throws InvalidCurveException
	{
		Arc2d arc = new Arc2d(this);
		double angle1 = getAngle(limitPoint1);
		double angle2 = getAngle(limitPoint2);
		if (angle1 < angle2)
		{
			arc.clipStart(limitPoint1);
			arc.clipEnd(limitPoint2);
		}
		else
		{
			arc.clipStart(limitPoint2);
			arc.clipEnd(limitPoint1);
		}
		return arc;
	}
	
	/**
	 * Returns a new arc clipped to the limit lines.
	 * Does nothing if lines lie outside start and end of this arc.
	 * @author Chandu
	 * @date 9/16/2006
	 * @param limitLine1
	 * @param limitLine2
	 * @return new clipped arc
	 * @throws InvalidCurveException
	 */
	public Arc2d clip(Line2d limitLine1, Line2d limitLine2) 
	throws InvalidCurveException
	{
		Point2d pt1 = this.getStartPoint();
		Point2d pt2 = this.getEndPoint();
		ArrayList pts1 = this.calculateLineIntersectionPoints(limitLine1);
		ArrayList pts2 = this.calculateLineIntersectionPoints(limitLine2);
		
		switch(pts1.size())
		{
		case 0:
			break;
		case 1:
			pt1 = (Point2d) pts1.get(0);
		default:
			pt1 = (Point2d) pts1.get(1);
		}
		switch(pts2.size())
		{
		case 0:
			break;
		case 1:
			pt2 = (Point2d) pts2.get(0);
		default:
			pt2 = (Point2d) pts2.get(1);
		}
		
		return clip(pt1, pt2);
	}
	
	/** @todo implement this method */
	public Point2d intersect(Arc2d otherArc)
		throws InvalidIntersectionException {
		return findCommonPoint(otherArc);
	}

	public Point2d intersect(Curve2d otherCurve)
		throws InvalidIntersectionException {
		if (otherCurve instanceof Arc2d)
			return intersect((Arc2d) otherCurve);
		else
			return intersect((Line2d) otherCurve);
	}

	//Arc intersecting Lineseg
	public Point2d intersectLineseg(Line2d lnseg)
		throws InvalidIntersectionException {
		LinkedList arcLines2d = computeArcLines();
		for (Iterator iter = arcLines2d.iterator(); iter.hasNext();) {
			Line2d ln2d = (Line2d) iter.next();
			//      System.out.println("ln2d-->"+ln2d);
			if (lnseg.intersects(ln2d))
				return lnseg.intersect(ln2d);
		}
		throw new InvalidIntersectionException(lnseg
			+ "does not intersect "
			+ this);
	}

	//Arc intersecting Line
	public Point2d intersect(Line2d ln) throws InvalidIntersectionException {
		LinkedList arcLines2d = computeArcLines();
		for (Iterator iter = arcLines2d.iterator(); iter.hasNext();) {
			Line2d ln2d = (Line2d) iter.next();
			if (ln.intersectsLineseg(ln2d)) {
				//        System.out.println("ln2d-->"+ln2d);
				return ln.intersectLineseg(ln2d);
			}

		}
		throw new InvalidIntersectionException(ln
			+ "does not intersect "
			+ this);
	}
	
	/**
	 * checks whether point lies on arc
	 * @author Chandu, date 11/28/2006
	 * 
	 * @param point
	 * @return
	 */
	public boolean isPointOnArc(Point2d pt)
	{
		return ((classify(pt) == GeometryConstants.ON_BOUNDARY)||
		(classify(pt) == GeometryConstants.INSIDE));
	}
	
	/**
	 * Returns an ArrayList of intersection points between a line2d segment and this.
	 * ArrayList maximum size = 2. Size 0 implies line does not intersect arc.
	 * Implemented from algorithms listed in:
	 * http://local.wasp.uwa.edu.au/~pbourke/geometry/
	 * @author Chandu
	 * @param line
	 * @return
	 */
	
	public ArrayList calculateLineSegIntersectionPoints(Line2d line)
	{
		ArrayList segPts = new ArrayList();
		ArrayList points = calculateLineIntersectionPoints(line);
		for (int i = 0; i < points.size(); i++)
		{
			Point2d pt = (Point2d) points.get(i);
			if (line.isPointOnCurve(pt)) segPts.add(pt);
		}		
		return segPts;
	}
	
	/**
	 * Returns an ArrayList of intersection points between a (infinite) line2d and this.
	 * ArrayList maximum size = 2. Size 0 implies line does not intersect arc.
	 * Implemented from algorithms listed in:
	 * http://local.wasp.uwa.edu.au/~pbourke/geometry/
	 * @author Chandu
	 * @param line
	 * @return
	 */
	public ArrayList calculateLineIntersectionPoints(Line2d line)
	{
		ArrayList points = new ArrayList();
		//declaring variables
		double x1 = line.getStartPoint().x;
		double x2 = line.getEndPoint().x;
		double y1 = line.getStartPoint().y;
		double y2 = line.getEndPoint().y;
		double x3 = this.getCenter().x;
		double y3 = this.getCenter().y;
		
		double l = line.getLength();
		double r = this.getRadius();
		//calculating b2-4ac
		double a =  (x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1);
		double b = 2*((x2 - x1)*(x1 - x3) + (y2 - y1)*(y1 - y3));
		double c = x3*x3 + y3*y3 + x1*x1 + y1*y1 - 2*(x3*x1 + y3*y1) - r*r;
		
		double del = b*b - 4*a*c;
		if (del < 0) return points;
		if (Gtk.epsilonEquals(del, 0.0))
		{
			points.add(line.pointOnLine(-b*l/(2*a)));
			return points;
		}
		double u1 = (-b + Math.sqrt(del))/(2*a);
		double u2 = (-b - Math.sqrt(del))/(2*a);
		
		Point2d sol1 = line.pointOnLine(u1*l);
		Point2d sol2 = line.pointOnLine(u2*l);
		
		if (this.isPointOnArc(sol1))
			points.add(sol1);
		if (this.isPointOnArc(sol2))
			points.add(sol2);		
		
		return points;
	}
	
	/**
	 * Returns ArrayList of Arc2d segments as cut by (infinite) Line2d.
	 * ArrayList maximum size = 3, implying 2 cutting points.
	 * Arraylist minimum size = 1, returns duplicate of this for no cutting points.
	 * @param line
	 * @return
	 */
	public ArrayList getArcSegments(Line2d line)
	{
		ArrayList points = calculateLineIntersectionPoints(line);
		ArrayList arcSegs = new ArrayList();
		switch (points.size())
		{
		case 0:
			{
				try
				{
					arcSegs.add(new Arc2d(this));
				}
				catch (InvalidArcException iae)
				{
					iae.printStackTrace();
				}
				return arcSegs;
			}
		case 1:
		{
			try{
				arcSegs.add(new Arc2d(this.getCenter(), this.getRadius(),
				this.getBeginAngle(), this.getAngle((Point2d) points.get(0))));
				arcSegs.add(new Arc2d(this.getCenter(), this.getRadius(),
				this.getAngle((Point2d) points.get(0)), this.getEndAngle()));
			}
			catch (InvalidArcException iae)
			{
				iae.printStackTrace();
			}
			return arcSegs;
		}
		default:
		{
			try{
				double a1 = this.getAngle((Point2d) points.get(0));
				double a2 = this.getAngle((Point2d) points.get(1));
				
				if (a1<a2)
				{
					arcSegs.add(new Arc2d(this.getCenter(), this.getRadius(),
							this.getBeginAngle(), 
							this.getAngle((Point2d) points.get(0))));	
					arcSegs.add(new Arc2d(this.getCenter(), this.getRadius(),
							a1, a2));
					arcSegs.add(new Arc2d(this.getCenter(), this.getRadius(),
							this.getAngle((Point2d) points.get(1)), 
							this.getEndAngle()));	
				}
				else
				{
					arcSegs.add(new Arc2d(this.getCenter(), this.getRadius(),
							this.getBeginAngle(), 
							this.getAngle((Point2d) points.get(1))));	
					arcSegs.add(new Arc2d(this.getCenter(), this.getRadius(),
							a2, a1));
					arcSegs.add(new Arc2d(this.getCenter(), this.getRadius(),
							this.getAngle((Point2d) points.get(0)), 
							this.getEndAngle()));
				}
							
			}
			catch (InvalidArcException iae)
			{
				iae.printStackTrace();
			}
			return arcSegs;
		}
		}//switch
	}

	/*public Arc2d intersect (Arc2d arc) {// need to test fro compatibility of center and radius
	 if (this.beginAngle > arc.beginAngle) {
	 return arc.intersect(this);
	 }
	 else if (this.endAngle > arc.beginAngle)
	 return new Arc2d (center, radius, arc.getBeginAngle(), getEndAngle());
	 /** @todo implement this method
	 return null;
	 }*/

	/**
	 * Translates center of this to the supplied point 'p'
	 * @param p point to which center of this has to be translated to
	 * @return Translated Arc2d
	 */
	public Arc2d translate(Point2d p) {
		Point2d duplicateCenter = new Point2d(center);
		duplicateCenter.add(p);
		Arc2d translatedArc = null;
		try {
			translatedArc = new Arc2d(
				duplicateCenter,
				radius,
				beginAngle,
				endAngle);
		} catch (InvalidArcException ex) {
			//should never happen as arc is just being translated
			ex.printStackTrace();
		}
		return translatedArc;
	}

	/**
	 * Returns Arc2d which is offset at a distance of 'offset' either
	 * on left side or right side of this. Left side / Right side being defined
	 * while moving in the direction from start point to end point.
	 * Use class variables LEFT_SIDE or RIGHT_SIDE defined in Curve2d to define
	 * offsetSide.
	 *
	 * @param offset
	 * @return an offset Arc2d
	 */
	public Curve2d offset(double offsetDistance, int offsetSide)
		throws InvalidArcException {
		double offsetRadius = radius;
		switch (offsetSide) {
			case Curve2d.LEFT_SIDE :
				offsetRadius -= offsetDistance;
				break;
			case Curve2d.RIGHT_SIDE :
				offsetRadius += offsetDistance;
				break;
		}
		return new Arc2d(center, offsetRadius, beginAngle, endAngle);
	}

	public void init() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		final JLabel label = new JLabel();
		panel.add(label, BorderLayout.SOUTH);
		panel.addMouseMotionListener(new MouseInputAdapter() {
			public void mouseDragged(MouseEvent e) {
				Vector2d vector = new Vector2d(e.getX() - center.x, e.getY()
					- center.y);
				Vector2d xAxis = new Vector2d(1, 0);
				label.setText("x "
					+ e.getX()
					+ ", y "
					+ e.getY()
					+ ", angle "
					+ getAngle(new Point2d(e.getX(), e.getY())));
			}
		});
	}

	//  private LinkedList computeArcPts()
	//  {
	//    double angle1 = beginAngle;
	//    double increment = (endAngle - beginAngle)/(noOfPoints-1);
	//    LinkedList arcPtsList = new LinkedList();
	//    for (int i = 0; i < noOfPoints; i++)
	//    {
	//      Point2d p1 = new Point2d();
	//      p1.x = center.x + radius * Math.cos(angle1);
	//      p1.y = center.y + radius * Math.sin(angle1);
	//      angle1 += increment;
	//      arcPtsList.add(p1);
	//    }
	//    return arcPtsList;
	//  }

	private LinkedList computeArcLines() {
		double angle1 = beginAngle;
		double increment = (endAngle - beginAngle) / noOfPoints;
		LinkedList lineList = new LinkedList();
		try {
			for (int i = 0; i < noOfPoints; i++) {
				Point2d p1 = new Point2d();
				Point2d p2 = new Point2d();
				p1.x = center.x + radius * Math.cos(angle1);
				p1.y = center.y + radius * Math.sin(angle1);
				angle1 += increment;
				p2.x = center.x + radius * Math.cos(angle1);
				p2.y = center.y + radius * Math.sin(angle1);
				Line2d lineSeg = new Line2d(p1, p2);
				lineList.add(i, lineSeg);
			}
		} catch (InvalidLineException ex) {
			//never happens
			ex.printStackTrace();
		}
		return lineList;
	}

	/**
	 * @todo Implement this method
	 * @param transformationMatrix
	 * @return
	 */
	public CurveSegment getCurveSegment(Matrix4d transformationMatrix) {
		    return getArc().transform(transformationMatrix); 
//		    <-- have to implement trnasform in Arc
//		return null;
	}

	/**
	 *
	 *    Gets the 3d Line segment version of this
	 *   
	 *    @return Linesegment of this
	 *   
	 */

	public Arc getArc() {
		return new Arc(
			radius,
			new Point3d(center.x, center.y, 0),
			new Vector3d(1, 0, 0),
			new Vector3d(0, 1, 0),
			this.getBeginAngle(),
			this.getEndAngle());
	}

	public LinkedList getDrawList() {
		LinkedList arcLines2d = computeArcLines();
		LinkedList lineList = new LinkedList();
		for (Iterator iter = arcLines2d.iterator(); iter.hasNext();) {
			Line2d ln2d = (Line2d) iter.next();
			Point2d p1 = ln2d.getStartPoint();
			Point2d p2 = ln2d.getEndPoint();
			Line2D.Double lineSeg = new Line2D.Double(new Point2D.Double(
				p1.x,
				p1.y), new Point2D.Double(p2.x, p2.y));
			lineList.add(lineSeg);
		}
		return lineList;
	}

	public LinkedList getShapeList(DrawWFPanel canvas) {
		return getArc().getShapeList(canvas);
	}

	public static void main(String[] args) {
		try {
			Arc2d c2 = new Arc2d(
				new Point2d(0, 0),
				2,
				Math.PI/2,
				2*Math.PI);
			Line2d line = new Line2d(new Point2d(-1.0, 2.5), 
					new Point2d(3.0, -2.5));
			Prof2d prof = new Prof2d();
//			prof.addCurve(c2);
			prof.addCurve(line);
			ArrayList points = c2.calculateLineSegIntersectionPoints(line);
			for (int i = 0; i < points.size(); i++)
			{
				Arc2d dot = new Arc2d((Point2d) points.get(i), 0.05, 0.0, 2*Math.PI);
//				dot.setColor(Color.RED);
				prof.addCurve(dot);
//				System.out.println("Intersection point "+ i + ":- "
//						+ points.get(i));
			}
			ArrayList segs = c2.getArcSegments(line);
			for (int i = 0; i < segs.size(); i++)
			{
				Arc2d seg = (Arc2d) segs.get(i);
//				dot.setColor(Color.RED);
				prof.addCurve(seg);
//				System.out.println("Intersection point "+ i + ":- "
//						+ points.get(i));
			}
			prof.settApplet(new Draw2DApplet(prof));
			prof.display();
//			Line2d line = new Line2d(new Point2d(12, 13), new Point2d(13, 13));
//			Arc2d offset = (Arc2d) c2.offset(1, Curve2d.LEFT_SIDE);
//			prof.addCurve(offset);
//			offset.setApplet(new Draw2DApplet(offset));
//			offset.display();
//			      System.out.println("-->" + c2.intersect(line));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

class PointOnArcComparator implements Comparator {
	Arc2d hostArc;

	public PointOnArcComparator(Arc2d arc) {
		hostArc = arc;
	}

	public int compare(Object o1, Object o2) {
		Point2d p1, p2;
		try {
			p1 = (Point2d) o1;
			p2 = (Point2d) o2;
		} catch (Exception ex) {
			throw new RuntimeException(
				"Objects are not compatible with comparator");
		}
		double angle1 = hostArc.getAngle(p1);
		double angle2 = hostArc.getAngle(p2);
		if (angle1 > hostArc.getEndAngle()) {
			angle1 -= Math.PI * 2;
		}
		if (angle1 < hostArc.getBeginAngle()) {
			angle1 += Math.PI * 2;
		}
		if (angle2 > hostArc.getEndAngle()) {
			angle2 -= Math.PI * 2;
		}
		if (angle2 < hostArc.getBeginAngle()) {
			angle2 += Math.PI * 2;
		}
		Double angle1Obj = new Double(angle1);
		Double angle2Obj = new Double(angle2);
		return angle1Obj.compareTo(angle2Obj);
	}
}
