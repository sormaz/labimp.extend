package edu.ohiou.mfgresearch.labimp.gtk3d;

/**
 * Title:        Line3d Class <p>
 * Description:  Class defining a 3D line. <p>
 * Copyright:    Copyright (c) 2001 <p>
 * Company:      Ohio University <p>
 * @author  Jaikumar Arumugam + Dusan N. Sormaz
 * @version 1.0
 */

import javax.vecmath.Point3d;
import edu.ohiou.mfgresearch.labimp.draw.ImpObject;
import javax.vecmath.Vector3d;

import edu.ohiou.mfgresearch.labimp.draw.*;
import edu.ohiou.mfgresearch.labimp.gtk2d.*;
import javax.vecmath.*;
import java.util.LinkedList;

/**
 *
 *  Class that represents infinite 3d line (line is 3d euclidean space
 *  The class is represented by its point and direction vector
 * 
 */

public class Line3d extends ImpObject {
	private Point3d linePoint;
	private Vector3d lineDirection = new Vector3d();

	// CONSRTUCTORS
	/** Constructor taking point and vector.
	 *
	 */
	public Line3d(Point3d point, Vector3d vector) throws InvalidLineException {
		if (Gtk.isZeroVector(vector))
			throw new InvalidLineException("Input vector is zero vector");
		linePoint = point;
		lineDirection.normalize(vector);
	}

	/** Constructor taking two points.
	 *
	 */
	public Line3d(Point3d p1, Point3d p2) throws InvalidLineException {
		if (p1.epsilonEquals(p2, GeometryConstants.EPSILON))
			throw new InvalidLineException("Points on the line are the same");
		linePoint = p1;
		lineDirection.sub(p2, p1);
		lineDirection.normalize();
	}

	/** Constructor taking line segment.
	 *
	 */
	public Line3d(LineSegment inSegment) throws InvalidLineException {
		this(inSegment.startPoint, inSegment.endPoint);
	}

	// SELECTORS
	/** Returns point of line.
	 *
	 */
	public Point3d getPoint() {
		return linePoint;
	}

	/**
	 * Returns direction of line.
	 *   
	 *   
	 */

	public Vector3d getDirection() {
		return lineDirection;
	}

	// GEOMETRY Computation methods
	/** Find a point on line given a factor.
	 *
	 *  @param factor - a double value of the paramater in parametric equation
	 *  x = xo + p v
	 *  @return - Point3d that corresponds to the factor
	 */
	public Point3d pointOnLine(double factor) {
		Point3d newPoint = new Point3d();
		Point3d scaledPoint = new Point3d(lineDirection);
		scaledPoint.scale(factor);
		newPoint.add(linePoint, scaledPoint);
		return newPoint;
	}

	/** Returns the point of intersection between "this" line and plane.
	 *  This method calls its counterpart in Plane class
	 *
	 * @param inPlane - plane whose interscetion point with line is sought
	 * @return  Point3d of the intersection or null if intersectino does not exist
	 * i.e. line and plane are parallel
	 *
	 * @seealso Plane
	 *
	 */
	public Point3d intersectLinePlane(Plane inPlane)
		throws InvalidIntersectionException,
		NotUniqueValueException {
		return inPlane.intersectLinePlane(this);
	}

	/**
	 * Computes point of intersection of two lines, if lines are coplanar
	 *
	 * @param inLine - the other line
	 */
	public Point3d intersectLineLine(Line3d inLine)
		throws InvalidIntersectionException,
		NotUniqueValueException {
		Point3d tempPoint = closestPointToLine(inLine);
		if (Gtk.epsilonEquals(distancePointLine(tempPoint), 0.0))
			return tempPoint;
		else
			throw new InvalidIntersectionException("Line"
				+ this
				+ " does not intersect line "
				+ inLine);
	}

	/**
	 * This method computes shortest distance between two lines
	 * Computation is based in throughing a plane through one line that
	 * contains intersection point.
	 *
	 * @param inLine -
	 */
	public double distanceLineLine(Line3d inLine) {
		try {
			return distancePointLine(closestPointOnLine(inLine));
		} catch (NotUniqueValueException e) {
			return distancePointLine(inLine.getPoint());
		}
	}
	public Line3d translate(Vector3d v) throws InvalidLineException {
		Point3d p2 = new Point3d();
		Point3d p1 = new Point3d();
		p1.add(this.linePoint, v);
		return new Line3d(p1, lineDirection);
	}

	public double distancePointLine(Point3d inPoint) {
		try {
			return inPoint.distance(intersectLinePlane(new Plane(inPoint, this
				.getDirection())));
		}
		// this should never happen because line is valid
		catch (Exception e) {
			e.printStackTrace();
			return Double.NaN;
		}
	}
	/**
	 * Return the point on inLine that is closest to this line.
	 *
	 * This methods work the following way:
	 * 1. find cross product of directions
	 * 2.
	 *
	 * @param inLine - Line3d on which to find the point
	 * @return Point3d closest to this line
	 */
	public Point3d closestPointOnLine(Line3d inLine)
		throws NotUniqueValueException {
		Vector3d crossProduct = new Vector3d();
		crossProduct.cross(this.getDirection(), inLine.getDirection());

		if (Gtk.isZeroVector(crossProduct)) {
			throw new NotUniqueValueException("line "
				+ inLine
				+ "is parallel to "
				+ this);
		} else {
			Vector3d newNormal = new Vector3d();
			crossProduct.normalize();
			newNormal.cross(crossProduct, this.getDirection());
			try {
				return inLine.intersectLinePlane(new Plane(
					this.getPoint(),
					newNormal));
			}
			//should never happen only for code compatibility
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public Point3d closestPointToLine(Line3d inLine)
		throws NotUniqueValueException {
		return inLine.closestPointOnLine(this);
	}

	/**
	 * Project given Point3d onto this line
	 * This method constructs a plane orthoganal to the line thruogh given point
	 * and finds intersection between that plane and this line
	 */

	public Point3d projectPoint(Point3d inPoint) throws GeometryException {
		return this.intersectLinePlane(new Plane(inPoint, this.getDirection()));
	}

	/** Return the factor for a given point with reference to "this" line.
	 *
	 */
	public double factorForPoint(Point3d inPoint) {
		if (isPointOnLine(inPoint)) {
			Vector3d tempVec = new Vector3d();
			tempVec.sub(inPoint, linePoint);
			return tempVec.dot(lineDirection);
		}
		return Double.NaN;
	}

	/** Returns whether point is on line or not.
	 *
	 */
	public boolean isPointOnLine(Point3d inPoint) {
		return Gtk.epsilonEquals(distancePointLine(inPoint), 0.0);
	}

	/**
	 * Returns string representation of Line3d.
	 *   
	 *   
	 */

	public String toString() {
		return new String("Line3d: <p "
			+ linePoint.toString()
			+ ">, <v "
			+ lineDirection.toString()
			+ ">");
	}

	public LinkedList geetShapeList(DrawWFPanel canvas) {
		LineSegment ls = new LineSegment(pointOnLine(-1), pointOnLine(1));
		LinkedList ll = ls.geetShapeList(canvas);
		ll.addAll(new WorldCS().geetShapeList(canvas));
		return ll;
	}

	/** Main method.
	 *
	 */
	public static void main(String[] args) {
		Line3d line3d1 = null;

		Line3d l2 = null, l3 = null, l4 = null;
		try {
			line3d1 = new Line3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1));
			l2 = new Line3d(new Point3d(1, 0, 0), new Vector3d(1, 0, 0));
			l3 = new Line3d(new Point3d(0, 1, 0), new Vector3d(1, 0, 1));
			l4 = new Line3d(new Point3d(1, 0, 0), new Point3d(1, 0, 0));
		} catch (InvalidLineException e) {
			e.printStackTrace();
		}
		System.out.println(line3d1.toString()
			+ line3d1.factorForPoint(new Point3d(-2, -2, -2)));
		l3.settApplet(new DrawWFApplet(l3));
		l3.display("line display");
	}
} // END OF CLASS DEFINITION.