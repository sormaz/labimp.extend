package edu.ohiou.mfgresearch.labimp.gtk3d;

import javax.vecmath.Point2d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import edu.ohiou.mfgresearch.labimp.draw.DrawWFPanel;
import java.util.LinkedList;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.Dimension;

//Importing classes from vecmath package for geometry calculations
import javax.vecmath.*;

//Importing swing classes for gui
import javax.swing.JLabel;

//Importing classes from IMPLAN
import edu.ohiou.mfgresearch.labimp.draw.DrawWFApplet;
import edu.ohiou.mfgresearch.labimp.draw.DrawWFPanel;
import edu.ohiou.mfgresearch.labimp.gtk2d.*;

public class Line2d extends Curve2d {
	//End points of the Line2d
	private Point2d startPoint;
	private Point2d endPoint;

	/**
	 *
	 *    Default constructor creating a Line2d from origin to Point2d(0,1)
	 *   
	 */

	public Line2d() {
		startPoint = new Point2d(0, 0);
		endPoint = new Point2d(1, 0);
	}
	
	/**
	 * Constructors taking vector and point and creating default line segment of length 1
	 * added by chandu 9/18 
	 * @param direction
	 * @param point
	 */
	public Line2d(Vector2d direction, Point2d point)
	{
		// D Sormaz changed code to make new copy of points.
		this(direction, point, 1.0);
	}
	
	public Line2d(Vector2d direction, Point2d point, double length)
	{
//		 D Sormaz changed code to make new copy of points.
		endPoint = new Point2d (point);
		startPoint = new Point2d (point);
		direction.scale(length);
		endPoint.add(direction);
	}
	/**
	 * Creates a Line2d with specified double values
	 *
	 * @param stPtX x-coordinate of startPoint
	 * @param stPtY y-coordinate of startPoint
	 * @param endPtX x-coordinate of endPoint
	 * @param endPtY y-coordinate of endPoint
	 * @throws InvalidLineException if the startPoint is same as endPoint
	 */
	public Line2d(double stPtX, double stPtY, double endPtX, double endPtY)
		throws InvalidLineException {
		this(new Point2d(stPtX, stPtY), new Point2d(endPtX, endPtY));
	}

	/**
	 * Creates a Line2d with specified startPoint and endPoint
	 *
	 * @param start startPoint of Line2d
	 * @param end endPoint of Line2d
	 * @throws InvalidLineException if the startPoint is same as endPoint
	 */
	public Line2d(Point2d start, Point2d end) throws InvalidLineException {
		if (start.epsilonEquals(end, GeometryConstants.EPSILON))
			throw new InvalidLineException
			("start " + start.toString() +  " and end " + 
					end.toString() + " points are same");
		startPoint = start;
		endPoint = end;
	}

	/**
	 *
	 *    Gets the start Point of this
	 *    @return startPoint
	 *   
	 */

	public Point2d getStartPoint() {
		return startPoint;
	}

	/**
	 *
	 *    Gets the end Point of this
	 *   
	 *    @return endPoint
	 *   
	 */

	public Point2d getEndPoint() {
		return endPoint;
	}

	/**
	 * Sets the start Point of this
	 *
	 * @param startPt startPoint of this
	 */
	public void setStartPoint(Point2d startPt) {
		startPoint = startPt;
	}

	/**
	 * Sets the end Point of Line2d
	 *
	 * @param endPt endPoint of this
	 */
	public void setEndPoint(Point2d endPt) {
		endPoint = endPt;
	}
	/*
	 *
	 *    Method to return new line object with end points reversed
	 *   
	 */

	public Line2d flip() {
		Line2d newLine = new Line2d();
		try {
			newLine = new Line2d(this.getEndPoint(), this.getStartPoint());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return newLine;
	}
	
	/**
	 * Clips start of line (this) to limitPoint.
	 * Does nothing if limitPoint lies outside the line.
	 * @author Chandu
	 * @date 9/16/2006
	 * @param limitPoint
	 * @return
	 * @throws InvalidLineException
	 */
	private void clipStart(Point2d limitPoint)
	{
//		if (!isPointOnLine(limitPoint)) 
//			throw new InvalidLineException
//			("The given point does not lie on the line!");
		if (isPointOnCurve(limitPoint))
		{
			this.setStartPoint(limitPoint);
		}
	}
	
	/**
	 * Clips end of line (this) to limitPoint.
	 * Does nothing if limitPoint lies outside the line.
	 * @author Chandu
	 * @date 9/16/2006
	 * @param limitPoint
	 * @return
	 * @throws InvalidLineException
	 */
	private void clipEnd(Point2d limitPoint)
	{
//		if (!isPointOnLine(limitPoint)) 
//			throw new InvalidLineException
//			("The given point does not lie on the line!");
		if (isPointOnCurve(limitPoint))
		{
			this.setEndPoint(limitPoint);
		}
	}
	
	/**
	 * Returns a new line clipped to the limit points.
	 * Does nothing if points lie outside start and end of this line.
	 * assumes that parameters are on the infinite line of this line2d
	 * @author Chandu
	 * @date 9/16/2006
	 * @param limitPoint1
	 * @param limitPoint2
	 * @return new clipped line
	 * @throws InvalidLineException
	 */
	private Line2d clip(Point2d limitPoint1, Point2d limitPoint2) 
						throws InvalidLineException
	{
		if (limitPoint1.epsilonEquals(limitPoint2, GeometryConstants.EPSILON))
			throw new InvalidLineException("Start and end points are the same");
		Line2d line = new Line2d(this.getStartPoint(), this.getEndPoint());
		if (factorForPoint(limitPoint1) < factorForPoint(limitPoint2))
		{
			line.clipStart(limitPoint1);
			line.clipEnd(limitPoint2);
		}
		else
		{
			line.clipStart(limitPoint2);
			line.clipEnd(limitPoint1);
		}
		return line;
	}
	
	/**
	 * comment
	 */
	public Line2d clip(Line2d limitLine1, Line2d limitLine2) 
	throws InvalidLineException, InvalidIntersectionException
	{
		Point2d point1 = this.intersectLine(limitLine1);
		Point2d point2 = this.intersectLine(limitLine2);
		
		return clip(point1, point2);		
	}
	/**
	 *
	 *    Gets the direction of this beginning at startPoint to endPoint
	 *   
	 *    @return direction of this
	 *   
	 */
	public Vector2d getDirection() {
		Vector2d lineDirection = new Vector2d();
		lineDirection.sub(endPoint, startPoint);
		lineDirection.normalize();
		return lineDirection;
	}

	/**
	 *
	 *    Initialize the panel with the gui components
	 *   
	 */

	public void init() {
		super.init();
		panel.add(new JLabel(this.toString()));
	}

	/**
	 *
	 *    Returns the list of tool path segments which can be drawn along with the
	 *    feature's profile
	 *   
	 *    @return LinkedList of toolPathSegments and feature's 2d profile
	 *   
	 */

	public LinkedList getDrawList() {
		LinkedList shapesList = new LinkedList();
		Line2D.Double ln = getLineSegment2D();
		shapesList.add(ln);
		return shapesList;
	}

	/**
	 * Returns Line2d which is offset at a distance of 'offset' either
	 * on left side or right side of this. Left side / Right side being defined
	 * while moving in the direction from start point to end point.
	 * Use class variables LEFT_SIDE or RIGHT_SIDE defined in Curve2d to define
	 * offsetSide
	 *
	 * @param offsetDistance distance to which this needs to be offsetted
	 * @param offsetSide side on which this needs to be offsetted
	 * @return an offset of this
	 */
	public Curve2d offset(double offsetDistance, int offsetSide) {
		Vector2d direction = getDirection();
		double angle = 0;
		switch (offsetSide) {
			case Curve2d.LEFT_SIDE :
				angle = 90;
				break;
			case Curve2d.RIGHT_SIDE :
				angle = -90;
				break;
		}
		double a = angle * (Math.PI / 180);
		Vector2d normal = new Vector2d(direction.x
			* Math.cos(a)
			- direction.y
			* Math.sin(a), direction.x
			* Math.sin(a)
			+ direction.y
			* Math.cos(a));
		normal.scale(offsetDistance);
		Point2d stPt = new Point2d(startPoint);
		Point2d endPt = new Point2d(endPoint);
		stPt.add(normal);
		endPt.add(normal);
		Line2d offset = new Line2d();
		try {
			offset = new Line2d(stPt, endPt);
		} catch (InvalidLineException ex) {
			//never happens
		}
		return offset;
	}

	/**
	 *
	 *    Gets the 2D version of this
	 *   
	 *    @return Line2D.Double of this
	 *   
	 */

	public Line2D.Double getLineSegment2D() {
		return new Line2D.Double(
			new Point2D.Double(startPoint.x, startPoint.y),
			new Point2D.Double(endPoint.x, endPoint.y));
	}

	/**
	 *
	 *    Gets the 3d Line segment version of this
	 *   
	 *    @return Linesegment of this
	 *   
	 */

	public LineSegment geetLineSegment() {
		Point3d stPoint3d = new Point3d(startPoint.x, startPoint.y, 0.0);
		Point3d endPoint3d = new Point3d(endPoint.x, endPoint.y, 0.0);
		return new LineSegment(stPoint3d, endPoint3d);
	}

	/**
	 *
	 *    Reverses Line2d's end points
	 *   
	 */

	public void reverseEndPoints() {
		Point2d temp = startPoint;
		startPoint = endPoint;
		endPoint = temp;
	}

	/**
	 * Intersects Line2d (this) with Curve2d.
	 * This is when curve is Line2d and not Line2d segment. When curve is Arc2d,
	 * it does not matter.
	 *
	 * @param c a Curve2d
	 * @return intersection point
	 * @throws InvalidIntersectionException if there is no intersection or if this
	 * is parallel to c
	 */
	public Point2d intersect(Curve2d c) throws InvalidIntersectionException {
		if (c instanceof Line2d)
			return intersectLine((Line2d) c);
		else
			return ((Arc2d) c).intersect(this);
	}

	/**
	 * Intersects Line2d (this) with Curve2d. This is when curve is Line2d
	 * segment and not Line2d. When curve is Arc2d, it does not matter.
	 *
	 * @param c a Curve2d
	 * @return intersection point
	 * @throws InvalidIntersectionException if there is no intersection or if this
	 * is parallel to c
	 */
	public Point2d intersectCurveSegment(Curve2d otherCurve)
		throws InvalidIntersectionException {
		if (otherCurve instanceof Line2d)
			return intersectLineseg((Line2d) otherCurve);
		else
			return ((Arc2d) otherCurve).intersect(this);
	}

	/**
	 * Intersects Line2d segment(this) with another Line2d segment
	 *
	 * @param otherLineseg Line2d segment with which the intersection needs to be
	 * made
	 * @return intersection point
	 * @throws InvalidIntersectionException if there is no intersection or if this
	 * is parallel to otherLineseg
	 */
	public Point2d intersect(Line2d otherLineseg)
		throws InvalidIntersectionException {
		if (intersects(otherLineseg))
			return intersectLine(otherLineseg);
		else
			throw new InvalidIntersectionException(otherLineseg
				+ " does not intersect "
				+ this);
	}

	/**
	 * Intersects Line2d (this) with Line2d segment
	 *
	 * @param lineseg Line2d segment with which the intersection needs to be made
	 * @return intersection point
	 * @throws InvalidIntersectionException if there is no intersection or if this
	 * is parallel to lineseg
	 */
	public Point2d intersectLineseg(Line2d lineseg)
		throws InvalidIntersectionException {
		Point2d intsnPt = intersectLine(lineseg);
		//    System.out.println("intsnPt-->"+intsnPt);
		if (lineseg.isPointOnCurve(intsnPt))
			return intsnPt;
		else
			throw new InvalidIntersectionException(this
				+ " does not intersect segment "
				+ lineseg);
	}

	/**
	 * Intersects Line2d (this) with another Line2d.
	 * 
	 * extra comment line
	 *
	 * @param otherLine Line2d with which the intersection needs to be made
	 * @return intersection point
	 * @throws InvalidIntersectionException if this is parallel to otherLine
	 */
	public Point2d intersectLine(Line2d otherLine)
		throws InvalidIntersectionException {
		if (isLineParallel(otherLine))
			throw new InvalidIntersectionException(otherLine
				+ "is parallel to "
				+ this);
		double angle = angle(otherLine);
		double dist = otherLine.ptLineDist(startPoint);
		double factor = dist / (Math.sin(angle));
		Point2d positivePt = pointOnLine(factor); //pt in the direction of vector
		//    System.out.println("positivePt -->"+positivePt);
		Point2d negativePt = pointOnLine(-factor); //pt in opp direction of vector
		//    System.out.println("negativePt -->"+negativePt);
		return otherLine.isPointOnLine(positivePt) ? positivePt : negativePt;
	}

	/**
	 * Returns boolean indicating whether Line2d (this) intersects with Curve2d.
	 * This is when curve is Line2d and not Line2d segment. When curve is Arc2d,
	 * it does not matter.
	 *
	 * @param otherCurve a Curve2d
	 * @return true if Line2d intersects with Curve2d false otherwise
	 */
	public boolean intersects(Curve2d otherCurve) {
		try {
			Point2d p = intersect(otherCurve);
			return true;
		} catch (InvalidIntersectionException ex) {
			return false;
		}
	}

	/**
	 * Returns boolean indicating whether Line2d (this) intersects with Curve2d.
	 * This is when curve is Line2d segment and not Line2d. When curve is Arc2d,
	 * it does not matter.
	 *
	 * @param otherCurve a Curve2d
	 * @return true if Line2d intersects with Curve2d false otherwise
	 */
	public boolean intersectsCurveSegment(Curve2d otherCurve) {
		try {
			Point2d p = intersectCurveSegment(otherCurve);
			return true;
		} catch (InvalidIntersectionException ex) {
			return false;
		}
	}

	/**
	 * Returns boolean indicating whether Line2d segment (this) intersects with
	 * another Line2d segment.
	 *
	 * @param otherLineseg Line2d with which the intersection check needs to be made
	 * @return true if Line2d segment (this) intersects with otherLineseg false
	 * otherwise
	 */
	public boolean intersects(Line2d otherLineseg) {
		Line2D.Double lnseg2D = getLineSegment2D();
		Line2D.Double otherLineseg2D = otherLineseg.getLineSegment2D();
		return lnseg2D.intersectsLine(otherLineseg2D);
	}

	/**
	 * Returns boolean indicating whether Line2d (this) intersects with
	 * another Line2d segment.
	 *
	 * @param lineseg Line2d segment with which the intersection check needs to be
	 * made
	 * @return true if Line2d (this) intersects with otherLineseg false otherwise
	 */
	public boolean intersectsLineseg(Line2d lineseg) {
		try {
			Point2d p = intersectLineseg(lineseg);
			return true;
		} catch (InvalidIntersectionException ex) {
			return false;
		}
	}

	/**
	 * Returns boolean indicating whether Line2d (this) intersects with
	 * another Line2d.
	 *
	 * @param otherLine Line2d with which the intersection check needs to be
	 * made
	 * @return true if Line2d (this) intersects with otherLine false otherwise
	 */
	public boolean intersectsLine(Line2d otherLine) {
		return !isLineParallel(otherLine);
	}

	/**
	 * Returns boolean indicating whether pt lies on Line2d (this)
	 *
	 * @param pt Point2d with which the collinearity needs to be checked
	 * @return true if pt lies on Line2d (this) false otherwise
	 */
	public boolean isPointOnLine(Point2d pt) {
		//    System.out.println("pt-->"+pt);
		//    System.out.println("ptLineDist-->"+ptLineDist(pt));
		//    System.out.println("espilonequals-->"+Gtk.epsilonEquals(ptLineDist(pt),0));
		return Gtk
			.epsilonEquals(ptLineDist(pt), 0.0, GeometryConstants.EPSILON)
			? true
			: false;
	}

	/**
	 * Returns boolean indicating whether pt lies on Line2d segment (this)
	 *
	 * @param pt Point2d with which the collinearity needs to be checked
	 * @return true if pt lies on Line2d segment(this) false otherwise
	 */
	public boolean isPointOnCurve(Point2d pt) {
		return (isPointOnLine(pt) && isPointInBetween(pt)) ? true : false;
	}

	/**
	 * Returns boolean indicating whether Line2d/Line2d segment (this)
	 * is parallel to another Line2d/Line2d segment
	 *
	 * @param otherLine Line2d with which the check for parallelism needs to be made
	 * @return true if this is parallel to otherLine false otherwise
	 */
	public boolean isLineParallel(Line2d otherLine) {
		double angle = angle(otherLine);
		return Gtk.epsilonEquals(angle, 0.0, GeometryConstants.EPSILON)
			? true
			: false;
	}

	/**
	 * Returns boolean indicating whether Line2d/Line2d segment (this)
	 * is collinear to another Line2d/Line2d segment
	 *
	 * @param otherLine Line2d with which the check for collinearity needs to be made
	 * @return true if this is collinear to otherLine false otherwise
	 */
	public boolean isLineCollinear(Line2d otherLine) {
		return (isPointOnLine(otherLine.getStartPoint()) && isPointOnLine(otherLine
			.getEndPoint()));
	}

	/**
	 * Gets the perpencular distance of pt from an infinitely extended this
	 *
	 * @param pt Point2d from which the distance  needs to be calculated
	 * @return distance of pt from this
	 */
	public double ptLineDist(Point2d pt) {
		if (isPtOneOfEndPoints(pt))
			return 0.0;
		double angle = 0.0;
		try {
			Line2d hyp = new Line2d(pt, getStartPoint());
			if (hyp.isLineParallel(this))
				return 0.0;
			angle = hyp.angle(this);
		} catch (InvalidLineException ex) {
			//never happens
			ex.printStackTrace();
		}
		return pt.distance(getStartPoint()) * (Math.sin(angle));
		/**
		 * Couldn't use the following code bcoz it returns NaN for some cases
		 * For example : Line2D(start(1,13),end(1,3)) and Pt(1,12.74) returns NaN
		 * instead of zero
		 *
		 * Line2D.Double ln2D = getLineSegment2D();
		 * return ln2D.ptLineDist(new Point2D.Double(pt.x,pt.y));
		 */
	}

	public Point2d projectPoint(Point2d inPoint)
	{
		Vector2d inDir = new Vector2d(-this.getDirection().y, this.getDirection().x);
		try
		{
			Line2d inLine = new Line2d(inDir, inPoint);
			return this.intersectLine(inLine);			
		}
		catch (InvalidIntersectionException iie)
		{
			iie.printStackTrace();
			return null;
		}		
	}
	/**
	 * Computes the angle between this and otherLine based on their vectors
	 *
	 * @param otherLine Line2d with which angle needs to be calculated
	 * @return angle between this and otherLine
	 */
	public double angle(Line2d otherLine) {
		Vector2d v1 = getDirection();
		Vector2d v2 = otherLine.getDirection();
		return (v1.angle(v2));
	}

	//
	/**
	 * Returns the distance between Line2d (this) and another Line2d. Returns 0.0 if they
	 * intersect
	 * @param otherLine Line2d from which distance needs to be calculated
	 * @return
	 */
	public double distanceLine(Line2d otherLine) {
		return isLineParallel(otherLine)
			? ptLineDist(otherLine.getStartPoint())
			: 0;
	}

	/**
	 * Finds a point on Line2d (this) given a factor.
	 *
	 *  @param factor - a double value of the paramater in parametric equation
	 *  x = xo + p v
	 *  @return - Point2d that corresponds to the factor
	 */
	public Point2d pointOnLine(double factor) {
		Point2d newPoint = new Point2d();
		Point2d scaledPoint = new Point2d(getDirection());
		scaledPoint.scale(factor);
		newPoint.add(startPoint, scaledPoint);
		return newPoint;
	}
	
	/**
	 * added by chandu 8/17/2006
	 * to retrieve factor for any point on line2d using
	 * parametric form x = x0 + pv
	 */
	public double factorForPoint(Point2d inPoint) {
		if (isPointOnLine(inPoint)) {
			Vector2d tempVec = new Vector2d();
			tempVec.sub(inPoint, this.getStartPoint());
			return tempVec.dot(this.getDirection());
		}
		return Double.NaN;
	}

	/**
	 * Returns a boolean indicating whether pt is inbetween the end points of this
	 * or not. pt need not be lying on this.
	 * Note - Algorithm adopted from Joseph O'Rourke's "Computational geometry in C"
	 *
	 * @param pt Point2d for which the inbetweeness needs to be checked
	 * @return true if pt lies in between the end points of this false otherwise.
	 */
	public boolean isPointInBetween(Point2d pt) {
		//    System.out.println("vertical ??--->"+Gtk.epsilonEquals((startPoint.x-endPoint.x),GeometryConstants.EPSILON));
		if (pt.epsilonEquals(startPoint, GeometryConstants.EPSILON)
			|| pt.epsilonEquals(endPoint, GeometryConstants.EPSILON))
			return true;
		//If this is not vertical, checks betweenness on x; else on y
		if (!Gtk.epsilonEquals(
			startPoint.x,
			endPoint.x,
			GeometryConstants.EPSILON)) {
			return ((pt.x > startPoint.x && pt.x < endPoint.x) || (pt.x > endPoint.x && pt.x < startPoint.x));
		} else {
			return ((pt.y > startPoint.y && pt.y < endPoint.y) || (pt.y > endPoint.y && pt.y < startPoint.y));
		}
	}

	/**
	 * Returns a boolean indicating whether this is equal to another Line2d based
	 * on the supplied epsilon.
	 *
	 * @param otherLine Line2d for which the equality needs to be checked
	 * @param epsilon double value based on which the comparison is done
	 * @return true if this is equal to otherLine false otherwise.
	 */
	public boolean epsilonEquals(Line2d otherLine, double epsilon) {
		return (otherLine.getStartPoint().epsilonEquals(
			startPoint,
			GeometryConstants.EPSILON) && otherLine.getEndPoint()
			.epsilonEquals(endPoint, GeometryConstants.EPSILON));
	}

	/**
	 *
	 *    Returns String representation of this
	 *   
	 *    @return Line2d with start and end point
	 *   
	 */

	public String toString() {
		return "Line2d: Start " + Tuple2dRenderer.format(startPoint) + ", End " + Tuple2dRenderer.format(endPoint);
	}

	public LinkedList getShapeList(DrawWFPanel canvas) {
		return geetLineSegment().geetShapeList(canvas);
	}

	public Line2d transform(Matrix3d transformationMatrix) {
		Point3d stPt = new Point3d();
		Point3d endPt = new Point3d();
		Line2d tranformedLine = new Line2d();
		transformationMatrix.transform(new Point3d(
			startPoint.x,
			startPoint.y,
			1), stPt);
		transformationMatrix.transform(
			new Point3d(endPoint.x, endPoint.y, 1),
			endPt);
		try {
			tranformedLine = new Line2d(stPt.x, stPt.y, endPt.x, endPt.y);
		} catch (InvalidLineException ex) {
			ex.printStackTrace();
		}
		return tranformedLine;
	}

	public CurveSegment getCurveSegment(Matrix4d transformationMatrix) {
		return geetLineSegment().transform(transformationMatrix);
	}

	public double getLength() {
		return startPoint.distance(endPoint);
	}

	public static void main(String[] args) {
		try {
			Line2d line2d = new Line2d(new Point2d(1, 1), new Point2d(2, 1));
			Matrix3d m = new Matrix3d(1, 0, 0, 0, 1, 0, 0, 0, 1);
			Matrix4d mat = new Matrix4d(
				0,
				0,
				1,
				0,
				0,
				1,
				0,
				0,
				-1,
				0,
				0,
				0,
				0,
				0,
				0,
				1);
			CurveSegment transformed = line2d.getCurveSegment(mat);
			transformed.settApplet(new DrawWFApplet(transformed));
			transformed.display("prof3d", new Dimension(600, 600));
			//      System.out.println("transformed-->"+line2d.getCurveSegment(mat));
			//      Line2d otherLine2d = new Line2d(new Point2d(1, 13), new Point2d(1, 3));
			//    Point2d pt2d = new Point2d(7.6160254000345,3.25);
			//      Point2d pt = line2d.intersectCurveSegment(otherLine2d);
			//      System.out.println("pt-->" + line2d.offset(2,Curve2d.RIGHT_SIDE));
			//      double d = otherLine2d.ptLineDist(pt2d);
			//      System.out.println("d-->" + d);
			//      boolean b = otherLine2d.isPointInBetween(new Point2d(1,4));
			//      System.out.println("b-->" + b);
			//      boolean intersects = otherLine2d.intersectsSegment(line2d);
			//      System.out.println("intersects-->"+intersects);
			//      boolean online = line2d.isPointOnCurve(new Point2d(1,12.74));
			//      System.out.println("online-->"+online);
			//      Line2D.Double ln2D = new Line2D.Double(new Point2D.Double(1,13),new Point2D.Double(1,3));
			//      System.out.println("ptLineDistance2D-->"+ln2D.ptLineDist(new Point2D.Double(1,12.742)));
			//      System.out.println("ptLineDistance2d-->"+otherLine2d.ptLineDist(new Point2d(1,13)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
