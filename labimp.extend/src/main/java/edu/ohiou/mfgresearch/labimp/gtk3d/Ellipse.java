package edu.ohiou.mfgresearch.labimp.gtk3d;

/**
 * Title:        Features
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Ohio University
 * @author Jaikumar Arumugam
 * @version 1.0
 */

import edu.ohiou.mfgresearch.labimp.draw.ImpObject;
import edu.ohiou.mfgresearch.labimp.draw.DrawWFPanel;
import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import edu.ohiou.mfgresearch.labimp.draw.DrawWFApplet;
import edu.ohiou.mfgresearch.labimp.draw.DrawWFPanel;
import edu.ohiou.mfgresearch.labimp.draw.ImpObject;

public class Ellipse extends ImpObject {

	// Class parameters.
	Point3d center = new Point3d(); // Default: ORIGIN (0,0,0)
	Vector3d xAxis = new Vector3d(1, 0, 0); // Default: X-Axis
	Vector3d yAxis = new Vector3d(0, 1, 0); // Default: Y-Axis
	Vector3d normal = new Vector3d(0, 0, 1); // Default: Z-Axis
	double minor = 1.0;
	double major = 2.0;
	double startAngle = 0; // in radians
	double endAngle = 2 * Math.PI;
	int segments = 50;

	// CONSTRUCTORS
	/** Default Constructor.
	 *  Creates an ellipse with minor axis of unit length and major axis of length
	 *  twice that of minor axis and normal along Z-axis.
	 */
	public Ellipse() {
	}

	/** Constructor taking in parameters for creating complete ellipse.
	 *
	 */
	public Ellipse(
			double minorRadius,
			double majorRadius,
			Point3d centerPt,
			Vector3d xMajorAxis,
			Vector3d yMinorAxis) {
		minor = minorRadius;
		major = majorRadius;
		center = new Point3d(centerPt);
		xAxis = new Vector3d(xMajorAxis);
		xAxis.normalize();
		yAxis = new Vector3d(yMinorAxis);
		yAxis.normalize();
		normal = getNormal();
	}

	/** Constructor taking all parameters for an ellipse.
	 *
	 */
	public Ellipse(
			double min,
			double maj,
			Point3d cen,
			Vector3d xMajorAxis,
			Vector3d yMinorAxis,
			double start,
			double end) {
		this(min, maj, cen, xMajorAxis, yMinorAxis); // normal computed here!
		startAngle = start;
		endAngle = end;
	}

	/** Constructor taking all parameters for an ellipse.
	 * (takes start point and end point instead of start angle and end angle)
	 */
	public Ellipse(
			double min,
			double maj,
			Point3d cen,
			Vector3d xMajorAxis,
			Vector3d yMinorAxis,
			Point3d startPt,
			Point3d endPt) {
		this(min, maj, cen, xMajorAxis, yMinorAxis); // normal computed here!
		Matrix4d inverseMatrix = Ellipse.getInverseMatrix(cen,xMajorAxis,yMinorAxis,normal);
		Point3d startPoint = new Point3d(startPt);
		inverseMatrix.transform(startPoint);
		Point3d endPoint = new Point3d(endPt);
		inverseMatrix.transform(endPoint);
		System.out.println("In 2D: start pt = "
				+ startPoint
				+ ", end point = "
				+ endPoint);
		Vector3d startVec = new Vector3d(startPoint);
		startAngle = GeometryConstants.X_VECTOR.angle(startVec);
		if (!Ellipse.isPositive(startVec)) {
			//      startAngle = 2*Math.PI - startAngle;
			startAngle = startAngle * (-1);
		}
		Vector3d endVec = new Vector3d(endPoint);
		endAngle = GeometryConstants.X_VECTOR.angle(endVec);
		if (!Ellipse.isPositive(endVec)) {
			//      endAngle = 2*Math.PI - endAngle;
			endAngle = endAngle * (-1);
		}
		// Temp test
		if(startAngle > endAngle) {
			endAngle = endAngle + 2*Math.PI;
		}
	}

	/**
	 * Method that returns start point of ellipse.
	 *   
	 *   
	 */

	public Point3d getStartPoint() {
		return this.getPointForAngle(startAngle);
	}

	/**
	 * Method that returns end point of ellipse.
	 *   
	 *   
	 */

	public Point3d getEndPoint() {
		return this.getPointForAngle(endAngle);
	}

	/** Method to get point on ellipse based on angle.
	 *
	 */
	public Point3d getPointForAngle(double angle) {
		double arcAngle = angle % (Math.PI * 2);
		if (arcAngle < 0)
			arcAngle += Math.PI * 2;
		Matrix4d transformMatrix = Ellipse.getTransformMatrix(center,xAxis,yAxis,normal);
		Point3d arcPoint = new Point3d(major * Math.cos(arcAngle), minor
				* Math.sin(arcAngle), 0);
		transformMatrix.transform(arcPoint);
		return arcPoint;
	}

	/**
	 *
	 *    Method to return tree representation of ellipse object.
	 *    @return
	 *   
	 */

	public DefaultMutableTreeNode geetTree() {
		return new DefaultMutableTreeNode(this);
	}

	/**
	 * String representation of an ellipse.
	 *   
	 *   
	 */

	public String toString() {
		StringBuffer buf = new StringBuffer(getClass().getName());
		buf.append(" <Minor " + minor + ">").append(" <Major " + major + ">")
		.append(" <Center " + center + ">").append(" <xAxis " + xAxis + ">")
		.append(" <yAxis " + yAxis + ">").append(" <Normal " + normal + ">")
		.append(" <Start " + startAngle + ">").append(" <End " + endAngle + ">");
		return buf.toString();
	}

	/** get normal of ellipse.
	 *
	 */
	public Vector3d getNormal() {
		normal = new Vector3d();
		normal.cross(xAxis,yAxis);
		normal.normalize();
		return new Vector3d(normal);
	}

	/**
	 * init method.
	 *   
	 *   
	 */

	public void init() {
		panel = new JPanel();
		JLabel ellipseLabel = new JLabel(this.toString());
		ellipseLabel.setFont(new java.awt.Font("SansSerif", 0, 11));
		this.panel.add(ellipseLabel);
		//    this.panel.setSize(new Dimension(650,10));
	}

	/**
	 * Returns the size of applet for display
	 *
	 public Dimension getAppletSize () {
	 Dimension panelSize = this.panel.getPreferredSize();
	 return new Dimension(panelSize.width + 5, panelSize.height + 5);
	 }

	 /** getShapeList() method.
	 *  STEPS:
	 *  (1) A 2D Circle is constructed in X-Y plane and Z-Axis as normal with angles
	 *  measured from +ve X-Axis.
	 *  (2) This circle is transformed ito 3D space by: translating to "center"
	 *  and aligning "normal" as new Z-axis.
	 */
	public LinkedList getShapeList(DrawWFPanel canvas) {
		LinkedList shapeList = new LinkedList();
		if (startAngle != endAngle) {
			if (segments < 3)
				segments = 3;

			double arcStart = startAngle % (Math.PI * 2);
			double arcEnd = endAngle % (Math.PI * 2);
			double totalAngle = arcEnd - arcStart;
			if (arcStart >= arcEnd) {
				totalAngle += Math.PI * 2;
				arcEnd += Math.PI * 2;
			}

			double angle = totalAngle / segments;
			Matrix4d transformMatrix = Ellipse.getTransformMatrix(center,xAxis,yAxis,normal);
//			Matrix4d transformMatrix = Ellipse.getInverseMatrix(center,xAxis,yAxis,normal);
			for (int i = 0; i < segments; i++) {
				Point3d start = new Point3d(major
						* Math.cos(angle * i + arcStart), minor
						* Math.sin(angle * i + arcStart), 0);
				transformMatrix.transform(start);
				Point3d end = new Point3d();
				if ((i + 1) == segments) {
					end = new Point3d(major * Math.cos(arcEnd), minor
							* Math.sin(arcEnd), 0);
				} else {
					end = new Point3d(major
							* Math.cos(angle * (i + 1) + arcStart), minor
							* Math.sin(angle * (i + 1) + arcStart), 0);
				}
				transformMatrix.transform(end);
				LineSegment ls = new LineSegment(start, end);
				shapeList.addAll(ls.getShapeList(canvas));
			}
		}
		return shapeList;
	}

	/** Method to return transformation matrix.
	 *
	 */
	protected static Matrix4d getTransformMatrix(Point3d center, Vector3d xAxis, Vector3d yAxis, Vector3d normal) {
		xAxis.normalize();
		yAxis.normalize();
		normal.normalize();
		Matrix4d transformMatrix = new Matrix4d(
				xAxis.x, yAxis.x, normal.x, center.x,
				xAxis.y, yAxis.y, normal.y, center.y,
				xAxis.z, yAxis.z, normal.z, center.z,
				0,0,0,1);
		return transformMatrix;
	}

	/** Method to return inverse transformation matrix.
	 *
	 */
	protected static Matrix4d getInverseMatrix(Point3d center, Vector3d xAxis, Vector3d yAxis, Vector3d normal) {
		Matrix4d inverseMatrix = new Matrix4d();
		inverseMatrix.invert(Ellipse.getTransformMatrix(center, xAxis, yAxis, normal));
		return inverseMatrix;
	}

	/** Method to return if angle is positive or negative.
	 *  +ve angle => if crossProduct is in SAME direction as Z vector. (angle = 0)
	 */
	protected static boolean isPositive(Vector3d testVec) {
		Vector3d crossProduct = new Vector3d();
		crossProduct.cross(GeometryConstants.X_VECTOR, testVec);
		//    System.out.println(" X-Axis X testVec = "+crossProduct);
		return Gtk.epsilonEquals(
				GeometryConstants.Z_VECTOR.angle(crossProduct),
				0);
	}

	/** Main method.
	 *
	 */
	public static void main(String[] args) {
		Ellipse c = new Ellipse(2, 2, new Point3d(0, 0, 0), new Vector3d(0,1,0),new Vector3d(0,0,1), Math.PI / 3, 3 * Math.PI / 2);
		//    System.out.println("original c = "+c.toString());
		c.settApplet(new DrawWFApplet(c, new Point3d(0, 0, 30), 20));
		c.display("Ellipse original",new Dimension(650, 700),JFrame.EXIT_ON_CLOSE);
	}
}
