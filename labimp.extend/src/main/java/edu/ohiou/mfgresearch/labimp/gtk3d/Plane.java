package edu.ohiou.mfgresearch.labimp.gtk3d;
/**
 * Title:        Plane Class <p>
 * Description:  Class defining a plane. <p>
 * Copyright:    Copyright (c) 2001 <p>
 * Company:      Ohio University <p>
 * @author  Jaikumar Arumugam + Dusan N. Sormaz
 * @version 1.0
 */

import javax.vecmath.Vector3d;
import edu.ohiou.mfgresearch.labimp.draw.ImpObject;
import edu.ohiou.mfgresearch.labimp.draw.DrawWFPanel;
import javax.vecmath.Point3d;
import edu.ohiou.mfgresearch.labimp.gtk2d.*;

import edu.ohiou.mfgresearch.labimp.draw.*;
import edu.ohiou.mfgresearch.labimp.basis.*;
import javax.vecmath.*;
import java.util.LinkedList;
import javax.swing.*;
import java.awt.Dimension;
import java.text.DecimalFormat;

public class Plane extends ImpObject {
	Point3d planePoint = new Point3d();
	Vector3d planeNormal = new Vector3d(0, 0, 1);

	public double[] getPlanePoint() {
		double[] ret = new double[]{this.planePoint.x,this.planePoint.y,this.planePoint.z};
		return ret;
	}

	public void setPlanePoint(double[] planePoint) {
		this.planePoint = new Point3d(planePoint[0],planePoint[1],planePoint[2]);
	}

	public double[] getPlaneNormal() {
		double[] ret = new double[]{this.planeNormal.x,this.planeNormal.y,this.planeNormal.z};
		return ret;
	}

	public void setPlaneNormal(double[] planeNormal) {
		this.planeNormal = new Vector3d(planeNormal[0],planeNormal[1],planeNormal[2]);;
	}

	// CONSTRUCTORS
	/** Constructor taking four double constants defining plane.
	 *
	 */
	public Plane(double A, double B, double C, double D)
		throws InvalidPlaneException {
		Vector3d normal = new Vector3d(A, B, C);
		if (Gtk.isZeroVector(normal))
			throw new InvalidPlaneException(
				"parameters A, B, and C make zero vector");
		planeNormal.normalize(normal);
		Vector3d pointVector = new Vector3d(planeNormal);
		pointVector.scale(-D / normal.length());
		planePoint = new Point3d(pointVector);
	}

	/** Constructor taking three double constants defining plane.
	 *
	 */
	public Plane(double a, double b, double c) throws InvalidPlaneException {
		this(1 / a, 1 / b, 1 / c, -1.0);
	}

	/** Constructor taking point and vector.
	 *
	 */
	public Plane(Point3d point, Vector3d vector) throws InvalidPlaneException {
		if (Gtk.isZeroVector(vector))
			throw new InvalidPlaneException("supplied vector is zero vector");
		planePoint = point;
		planeNormal.normalize(vector);
	}

	/** Constructor taking three points defining plane.
	 *
	 */
	public Plane(Point3d p1, Point3d p2, Point3d p3)
		throws InvalidPlaneException {
		Vector3d p2_p1 = new Vector3d(), p3_p1 = new Vector3d(), normal = new Vector3d();
		p2_p1.sub(p2, p1);
		p3_p1.sub(p3, p1);
		normal.cross(p2_p1, p3_p1);
		if (Gtk.isZeroVector(normal))
			throw new InvalidPlaneException("supplied points are colinear");
		planePoint = p1;
		planeNormal.normalize(normal);
	}

	// SELECTORS
	/** init method.
	 *
	 */
	public void init() {
		panel = new JPanel();
		panel.add(new JLabel(this.toString()));
	}

	/**
	 * Method to return plane normal.
	 *   
	 *   
	 */

	public Vector3d getNormal() {
		return this.planeNormal;
	}

	/**
	 * Method to return point defining plane.
	 *   
	 *   
	 */

	public Point3d getPoint() {
		return this.planePoint;
	}

	/** Method to find whether point lies on plane or not.
	 *
	 */
	public boolean contains(Point3d testPoint) {
		return Gtk.epsilonEquals(distancePointPlane(testPoint), 0.0);
	}

	public boolean contains(Line3d testLine) {
		if (parallel(testLine)) {
			Vector3d pointDiff = new Vector3d();
			pointDiff.sub(testLine.getPoint(), planePoint);
			return Math.abs(planeNormal.dot(pointDiff)) == 0;
		}
		return false;
	}

	public boolean parallel(Line3d testLine) {
		double dotNormalDir = planeNormal.dot(testLine.getDirection());
		return Math.abs(dotNormalDir) == 0;
	}

	/** Returns the point of intersection of "this" plane and given line.
	 *
	 */
	public Point3d intersectLinePlane(Line3d inLine)
		throws InvalidIntersectionException,
		NotUniqueValueException {
		Vector3d pointDiff = new Vector3d();
		pointDiff.sub(inLine.getPoint(), planePoint);
		double dotNormalDir = planeNormal.dot(inLine.getDirection());
		if (Math.abs(dotNormalDir) == 0) {
			if (Math.abs(planeNormal.dot(pointDiff)) == 0) {
				throw new NotUniqueValueException(inLine.toString()
					+ " lies in "
					+ this.toString());
			} else {
				throw new InvalidIntersectionException(inLine.toString()
					+ " is parallel to "
					+ this.toString());
			}
		} // first if
		Point3d resultPoint = inLine.pointOnLine(-planeNormal.dot(pointDiff)
			/ dotNormalDir);
		return resultPoint;
	}

	/** Returns the line resulting from intersection of "this" and given plane.
	 *
	 */
	public Line3d intersectPlanePlane(Plane otherPlane)
		throws InvalidIntersectionException {
		Vector3d crossNormals = new Vector3d();
		Vector3d orthoLine = new Vector3d();
		Point3d newPoint = new Point3d();
		crossNormals.cross(this.planeNormal, otherPlane.planeNormal);
		if (crossNormals.epsilonEquals(
			GeometryConstants.ORIGIN,
			GeometryConstants.EPSILON)) {
			throw new InvalidIntersectionException(this.toString()
				+ " is parallel to "
				+ otherPlane.toString());
			//return null;
		} else {
			crossNormals.normalize();
			orthoLine.cross(this.planeNormal, crossNormals);
			orthoLine.normalize();
			try {
				newPoint = otherPlane.intersectLinePlane(new Line3d(
					this.planePoint,
					orthoLine));
				Line3d resultLine = new Line3d(newPoint, crossNormals);
				return resultLine;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public double distancePointPlane(Point3d inPoint) {
		Vector3d vec = new Vector3d();
		vec.sub(inPoint, planePoint);
		double dotProduct = vec.dot(planeNormal);
		return dotProduct;
	}

	public Point3d projectPoint(Point3d inPoint) throws InvalidPointException {
		return projectPoint(inPoint, planeNormal);
	}

	public Point3d projectPoint(Point3d inPoint, Vector3d projectionVector)
		throws InvalidPointException {
		try {
			return intersectLinePlane(new Line3d(inPoint, projectionVector));
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidPointException(
				"projection vector does not intersect plane");
		}

	}

	public Vector3d projectVector(Vector3d inVector) {
		//    Vector3d out = new Vector3d();
		Vector3d out = normalizeVectorProjection(inVector);
		out.scale(out.dot(inVector));
		return out;
	}

	public Vector3d normalizeVectorProjection(Vector3d inVector) {
		Vector3d crossProduct = new Vector3d();
		crossProduct.cross(getNormal(), inVector);
		try {
			Plane tempPlane = new Plane(getPoint(), crossProduct);
			Vector3d out = new Vector3d();
			out.cross(crossProduct, this.getNormal());
			return out;
		} catch (InvalidPlaneException e) {
			e.getMessage();
			return GeometryConstants.ZERO_VECTOR;
		}
	}

	public Point3d projectPoint(Point3d inPoint, Line3d projectionLine)
		throws InvalidPointException {
		try {
			return intersectLinePlane(new Line3d(inPoint, projectionLine
				.getDirection()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidPointException(
				"projection line does not intersect plane");
		}
	}

	/** Returns linked list of 2D shapes representing a plane. (with WCS)
	 *
	 */
	public LinkedList getShapeList(DrawWFPanel canvas) {
		Point3d xPoint = null, yPoint = null, zPoint = null;
		try {
			xPoint = this.intersectLinePlane(GeometryConstants.X_AXIS);
		} catch (Exception e) {
		}
		try {
			yPoint = this.intersectLinePlane(GeometryConstants.Y_AXIS);
		} catch (Exception e) {
		}
		try {
			zPoint = this.intersectLinePlane(GeometryConstants.Z_AXIS);
		} catch (Exception e) {
		}
		LineSegment line1;
		LineSegment line2;
		LineSegment line3;

		if (xPoint == null) {
			if (yPoint == null) {
				line1 = new LineSegment(zPoint, new Point3d(
					0,
					GeometryConstants.BIG_NUMBER,
					zPoint.z));
				line2 = new LineSegment(zPoint, new Point3d(
					GeometryConstants.BIG_NUMBER,
					0,
					zPoint.z));
				line3 = new LineSegment(new Point3d(
					0,
					GeometryConstants.BIG_NUMBER,
					zPoint.z), new Point3d(
					GeometryConstants.BIG_NUMBER,
					0,
					zPoint.z));
			} // if ypoint
			else {
				if (zPoint == null) {
					line1 = new LineSegment(yPoint, new Point3d(
						0,
						yPoint.y,
						GeometryConstants.BIG_NUMBER));
					line2 = new LineSegment(yPoint, new Point3d(
						GeometryConstants.BIG_NUMBER,
						yPoint.y,
						0));
					line3 = new LineSegment(new Point3d(
						GeometryConstants.BIG_NUMBER,
						yPoint.y,
						0), new Point3d(
						0,
						yPoint.y,
						GeometryConstants.BIG_NUMBER));
				} // if zpoint
				else {
					line1 = new LineSegment(zPoint, new Point3d(
						GeometryConstants.BIG_NUMBER,
						0,
						zPoint.z));
					line2 = new LineSegment(zPoint, yPoint);
					line3 = new LineSegment(yPoint, new Point3d(
						GeometryConstants.BIG_NUMBER,
						yPoint.y,
						0));
				}
			} // else ypoint
		} // if xpoint
		else {
			if (yPoint == null) {
				if (zPoint == null) {
					line1 = new LineSegment(xPoint, new Point3d(
						xPoint.x,
						0,
						GeometryConstants.BIG_NUMBER));
					line2 = new LineSegment(xPoint, new Point3d(
						xPoint.x,
						GeometryConstants.BIG_NUMBER,
						0));
					line3 = new LineSegment(new Point3d(
						xPoint.x,
						0,
						GeometryConstants.BIG_NUMBER), new Point3d(
						xPoint.x,
						GeometryConstants.BIG_NUMBER,
						0));
				} //if zpoint
				else {
					line1 = new LineSegment(zPoint, new Point3d(
						0,
						GeometryConstants.BIG_NUMBER,
						zPoint.z));
					line2 = new LineSegment(zPoint, xPoint);
					line3 = new LineSegment(xPoint, new Point3d(
						xPoint.x,
						GeometryConstants.BIG_NUMBER,
						0));
				} // else zpoint
			} // if ypoint
			else {
				if (zPoint == null) {
					line1 = new LineSegment(yPoint, new Point3d(
						0,
						yPoint.y,
						GeometryConstants.BIG_NUMBER));
					line2 = new LineSegment(yPoint, xPoint);
					line3 = new LineSegment(xPoint, new Point3d(
						xPoint.x,
						0,
						GeometryConstants.BIG_NUMBER));
				} else {
					line1 = new LineSegment(zPoint, yPoint);
					line2 = new LineSegment(zPoint, xPoint);
					line3 = new LineSegment(xPoint, yPoint);
				} // else zpoint
			} // else ypoint
		} // else xpoint

		LinkedList list = new LinkedList();
		list.addAll(line1.getShapeList(canvas));
		list.addAll(line2.getShapeList(canvas));
		list.addAll(line3.getShapeList(canvas));
//		WorldCS cs = new WorldCS();
//		list.addAll(cs.getShapeList(canvas));
		return list;
	}

	/**
	 * Returns string representation of plane.
	 *   
	 *   
	 */

	public String toString() {
		return new String("Plane: <p "
			+ Tuple3dRenderer.format(planePoint)
			+ ">, <n "
			+ Tuple3dRenderer.format(planeNormal)
			+ ">");
	}

	/** Main method.
	 *
	 */
	static public void main(String[] args) {
		try {
			Plane pl = new Plane(new Point3d(1, 1, 1), new Vector3d(1, 1, 1));
			Plane p2 = new Plane(new Point3d(), new Vector3d(1, 1, 1));
			Plane p3 = new Plane(1, 1, 0, -2);
			Plane p4 = new Plane(2, 2, 2);
			Vector3d test = new Vector3d(0, 1, 0);
			ViewObject.doNothing(pl, pl.projectVector(test).toString());
			p3.settApplet(new DrawWFApplet(p3));
			p3
				.display(
					"test pl",
					new Dimension(600, 600),
					JFrame.EXIT_ON_CLOSE);
			Line3d line = GeometryConstants.X_AXIS;
			System.out.println(p3.toString()
				+ p3.distancePointPlane(new Point3d(2, 2, 2)));
			System.out.println(pl.toString() + line.toString());
			System.out.println(p3.toString());
			System.out.println(p4.toString());
			Point3d p = p2.intersectLinePlane(line);
			DecimalFormat f = new DecimalFormat("####.###");
			System.out.println(" sqare root " + f.format(Math.sqrt(2.0)));
			if (p != null) {
				System.out.println("int point " + p.toString());
			}
			System.out.println("new int line"
				+ pl.intersectPlanePlane(p2).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isCoplanar(Plane otherPlane) {
		return this.planeNormal.epsilonEquals(otherPlane.planeNormal, GeometryConstants.EPSILON) &&
				this.contains(otherPlane.planePoint);
	}

} // END OF CLASS DEFINITION.
