/**
 * Title:        Features
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Ohio University
 * @author Jaikumar Arumugam
 * @version 1.0
 */

package edu.ohiou.mfgresearch.labimp.gtk3d;

import edu.ohiou.mfgresearch.labimp.draw.DrawWFPanel;
import java.awt.Dimension;
import java.util.LinkedList;
import java.text.NumberFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import edu.ohiou.mfgresearch.labimp.draw.DrawWFApplet;
import edu.ohiou.mfgresearch.labimp.draw.DrawWFPanel;
import edu.ohiou.mfgresearch.labimp.gtk2d.InvalidPlaneException;

public class Arc extends CurveSegment {

	// Class parameters.
	Point3d center = new Point3d(); // Default center = ORIGIN (0,0,0)
	Vector3d normal = null; 
	Vector3d xAxis = null; 
	

	Vector3d yAxis = null; 
	
	double radius = 1.0; // Default radius = 1

	// Default arc = Full circle (Angles are in radians)
	double startAngle = 0; 
	double endAngle = 2 * Math.PI;

	// Number of line segments used to draw the arc.
	int segments = 50;

	// CONSTRUCTORS
	/** Default Constructor.
	 *  Constructs a new arc, initialized to location (0,0,0) with unit radius. 
	 *  The X and Y axis are (1,0,0) and (0,1,0). Normal is along Z (0,0,1).
	 *  The angular extents are (start = 0, end = 2*PI).
	 */
	public Arc() {
		this(1.0,new Point3d(),new Vector3d(1, 0, 0),new Vector3d(0, 1, 0),0,2*Math.PI);
	}

	/** Constructs a new arc, initialized to the specified location, size, and normal. 
	 *  The angular extents are (start = 0, end = 2*PI).
	 */
	public Arc(double arcRadius, Point3d centerPt, Vector3d normalVec) {
		this(arcRadius,centerPt,normalVec,0,2*Math.PI);
	}

	/** Constructs a new arc, initialized to the specified location, size, local X and Y axis.
	 *  The angular extents are (start = 0, end = 2*PI).
	 */
	public Arc(double arcRadius, Point3d centerPt, Vector3d xAxisVec, Vector3d yAxisVec) {
		this(arcRadius,centerPt,xAxisVec,yAxisVec,0,2*Math.PI);
	}

	/** Constructs a new arc, initialized to the specified location, size, 
	 *  local X and Y axis, and angular extents.
	 *
	 */
	public Arc(double arcRadius, Point3d centerPt, 
			Vector3d xAxisVec, Vector3d yAxisVec, 
			double startAngle, double endAngle) {

		radius = arcRadius;
		center = new Point3d(centerPt);
		xAxis = new Vector3d(xAxisVec);
		xAxis.normalize();
		yAxis = new Vector3d(yAxisVec);
		yAxis.normalize();
		normal = geettNormal();
		this.startAngle = startAngle;
		this.endAngle = endAngle;
	}

	/** Constructs a new arc, initialized to the specified location, size, 
	 *  normal and angular extents.
	 *
	 */
	public Arc(double arcRadius, Point3d centerPt, Vector3d normalVec, 
			double startAngle, double endAngle) {

		radius = arcRadius;
		center = new Point3d(centerPt);
		normal = new Vector3d(normalVec);
		normal.normalize();
		this.startAngle = startAngle;
		this.endAngle = endAngle;
	}

	/** Constructs a new arc, initialized to the specified location, size, 
	 *  normal, start and end points.
	 *
	 */
	public Arc(double rad, Point3d cen, Vector3d vec,
			Point3d startPt, Point3d endPt) {

		this(rad, cen, vec);

		// Create inverse matrix to transform Arc to 2D and 
		// to calculate start and end angles from start and end point.
		Matrix4d inverseMatrix = getInverseMatrix();

		Point3d startPoint = new Point3d(startPt);
		inverseMatrix.transform(startPoint);
		Point3d endPoint = new Point3d(endPt);
		inverseMatrix.transform(endPoint);
		System.out.println("In 2D: start pt = "+ startPoint+ ", end point = "+ endPoint);

		// Calculate start and end angles.
		Vector3d startVec = new Vector3d(startPoint);
		startAngle = GeometryConstants.X_VECTOR.angle(startVec);
		if (!Ellipse.isPositive(startVec)) {
			startAngle = startAngle * (-1);
		}

		Vector3d endVec = new Vector3d(endPoint);
		endAngle = GeometryConstants.X_VECTOR.angle(endVec);
		if (!Ellipse.isPositive(endVec)) {
			endAngle = endAngle * (-1);
		}

		// Ensure start angle is greater than end angle for compatibility with UG.
		if(startAngle > endAngle) {
			endAngle = endAngle + 2*Math.PI;
		}
	}

	/** Constructs a new arc, initialized to the specified location, size, 
	 *  local X and Y axis, start and end points.
	 *
	 */
	public Arc(double rad, Point3d cen,
			Vector3d xAxisVec, Vector3d yAxisVec,
			Point3d startPt, Point3d endPt) {

		this(rad, cen, xAxisVec, yAxisVec);

		// Create inverse matrix to transform Arc to 2D and 
		// to calculate start and end angles from start and end point.
		Matrix4d inverseMatrix = Ellipse.getInverseMatrix(cen, xAxisVec, yAxisVec, geettNormal());

		Point3d startPoint = new Point3d(startPt);
		inverseMatrix.transform(startPoint);
		Point3d endPoint = new Point3d(endPt);
		inverseMatrix.transform(endPoint);
		System.out.println("In 2D: start pt = "+startPoint+ ", end point = "+ endPoint);

		// Calculate start and end angles.
		Vector3d startVec = new Vector3d(startPoint);
		startAngle = GeometryConstants.X_VECTOR.angle(startVec);
		if (!Ellipse.isPositive(startVec)) {
			startAngle = startAngle * (-1);
		}
		Vector3d endVec = new Vector3d(endPoint);
		endAngle = GeometryConstants.X_VECTOR.angle(endVec);
		if (!Ellipse.isPositive(endVec)) {
			endAngle = endAngle * (-1);
		}

		// Ensure start angle is greater than end angle for compatibility with UG.
		if(startAngle > endAngle) {
			endAngle = endAngle + 2*Math.PI;
		}
	}

	/** Copy Constructor for an arc.
	 *
	 */
	public Arc(Arc inArc) {
		this(inArc.getRadius(), inArc.geettCenter(), inArc.geettNormal(), 
				inArc.getStartAngle(), inArc.getEndAngle());
		if(inArc.hasAxes()) {
			this.xAxis = inArc.geettXAxis();
			this.yAxis = inArc.geettYAxis();
		}
	}

	/** Method to create translation matrix for given Point3d.
	 *
	 */
	private Matrix4d createTranslationMatrix() {
		Matrix4d tMatrix = new Matrix4d();
		//    System.out.println("translate:\n"+tMatrix);
		tMatrix.set(new Vector3d (center));
		return tMatrix;
	}

	/** Method to return transformation matrix.
	 *
	 */
	private Matrix4d getTransformMatrix() {
		Matrix4d transformMatrix = new Matrix4d();
		transformMatrix.mul(createTranslationMatrix(), Gtk.computeTransformMatrix(normal));
		return transformMatrix;
	}

	/** Method to return inverse transformation matrix.
	 *
	 */
	private Matrix4d getInverseMatrix() {
		Matrix4d inverseMatrix = new Matrix4d();
		inverseMatrix.invert(getTransformMatrix());
		return inverseMatrix;
	}

	/** Method that sets normal direction of arc.
	 *
	 */
	public void settNormal(Vector3d arcNormal) {
		normal = new Vector3d(arcNormal);
	}
	
	public void setNormal(double[] arcNormal) {
		normal = new Vector3d(arcNormal[0], arcNormal[1], arcNormal[2]);
	}

	/** Method that sets center of arc.
	 *
	 */
	
	public int getSegments() {
		return segments;
	}

	public void setSegments(int segments) {
		this.segments = segments;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public void setStartAngle(double startAngle) {
		this.startAngle = startAngle;
	}

	public void setEndAngle(double endAngle) {
		this.endAngle = endAngle;
	}
	
	
	public void settCenter(Point3d arcCenter) {
		center = new Point3d(arcCenter);
	}

	public void setCenter(double[] arcCenter) {
		center = new Point3d(arcCenter[0],arcCenter[1],arcCenter[2]);
	}
	/**
	 *
	 *    Method to return tree representation of arc object.
	 *    @return
	 *
	 */

	public void setXAxis(double[] xAxis) {
		this.xAxis = new Vector3d(xAxis[0],xAxis[1], xAxis[2]);
	}

	public void setYAxis(double[] yAxis) {
		this.yAxis = new Vector3d(yAxis[0],yAxis[1], yAxis[2]);
	}

	public DefaultMutableTreeNode geetTree() {
		return new DefaultMutableTreeNode(this);
	}

	/**
	 * Method that returns center of arc.
	 *
	 *
	 */

	public Point3d geettCenter() {
		return new Point3d(center);
	}
	
	public double[] getCenter() {
		double[] ret  = new double[]{center.x, center.y, center.z};
		return ret;
	}
	

	/**
	 * Method that returns normal direction of arc.
	 *
	 *
	 */

	public Vector3d geettNormal() {
		if(hasAxes()) {
			normal = new Vector3d();
			normal.cross(xAxis,yAxis);
			normal.normalize();
		}
		return new Vector3d(normal);
	}
	
	public double[] getNormal() {
		double[] ret = new double[]{normal.x, normal.y, normal.z};
		return ret;
	}

	/**
	 * Method that returns x-axis direction of arc.
	 *
	 *
	 */

	public Vector3d geettXAxis() {
		return new Vector3d(xAxis);
	}
	
	public double[] getXAxis() {
		double[] ret = new double[]{xAxis.x, xAxis.y,xAxis.z};
		return ret;
	}

	/**
	 * Method that returns y-axis direction of arc.
	 *
	 *
	 */

	public Vector3d geettYAxis() {
		return new Vector3d(yAxis);
	}
	
	public double[] getYAxis() {
		double[] ret = new double[]{yAxis.x, yAxis.y,yAxis.z};
		return ret;
	}

	/**
	 * Method that returns radius of arc.
	 *
	 *
	 */

	public double getRadius() {
		return radius;
	}

	/**
	 * Method that returns start angle of arc.
	 *
	 *
	 */

	public double getStartAngle() {
		return startAngle;
	}

	/**
	 * Method that returns end angle of arc.
	 *
	 *
	 */

	public double getEndAngle() {
		return endAngle;
	}



	/**
	 * Method that returns start point of arc.
	 *
	 *
	 */

	public Point3d geettStartPoint() {
		return this.getPointForAngle(startAngle);
	}

	/**
	 * Method that returns end point of arc.
	 *
	 *
	 */

	public Point3d geettEndPoint() {
		return this.getPointForAngle(endAngle);
	}

	/** Method to get point on arc based on angle.
	 *
	 */
	public Point3d getPointForAngle(double angle) {
		// If X and Y axis are present
		if (hasAxes())
			return new Ellipse(radius, radius, center, xAxis, yAxis, startAngle, endAngle).getPointForAngle(angle);
		// If only normal is present
		double arcAngle = angle % (Math.PI * 2);
		if (arcAngle < 0)
			arcAngle += Math.PI * 2;
		Matrix4d transformMatrix = getTransformMatrix();
		Point3d arcPoint = new Point3d(
				radius * Math.cos(arcAngle), 
				radius * Math.sin(arcAngle), 
				0);
		transformMatrix.transform(arcPoint);
		return arcPoint;
	}

	/** Method to see existence of x and y axis.
	 * 
	 */
	private boolean hasAxes() {
		return ((xAxis!=null && yAxis!=null) ? true : false);
	}

	/**todo
	 *
	 */
	public Vector3d getTangent(Point3d point) {
		return null;
	}

	/** Method to test whether given point lies within arc limits.
	 *
	 */
	public boolean isPointInLimits(Point3d testPoint) {
		return testPointOnArc(testPoint, false);
	}

	/** Method to test whether given point lies on arc or not.
	 *
	 */
	public boolean isPointOnArc(Point3d testPoint) {
		return testPointOnArc(testPoint, true);
	}

	/** Method to test whether given point lies on arc or not.
	 *
	 */
	private boolean testPointOnArc(Point3d testPoint, boolean type) {
		try {
			// PHASE 1: Check whether point in on arc plane.
			Plane arcPlane = new Plane(center, normal);
			if (!arcPlane.contains(testPoint))
				return false;
			else
				System.out.println(testPoint + " lies on arc plane!");
			if (type) {
				// PHASE 2: Check whether point lies within radius distance.
				double distance = center.distance(testPoint);
				if (Math.abs(distance) > radius)
					return false;
				else
					System.out.println(testPoint + " lies within arc radius!");
			}
			// PHASE 3: Check whether point lies between arc start and end angles.
			Matrix4d inverseMatrix = getInverseMatrix();
			Point3d test = new Point3d(testPoint);
			inverseMatrix.transform(test);
			// if test point transformed to 2D is origin, then it lies on the arc.
			if (Gtk.isZeroVector(test))
				return true;
			Vector3d testVec = new Vector3d(test);
			double angle = GeometryConstants.X_VECTOR.angle(testVec);
			angle = angle % (Math.PI * 2);
			System.out.println("angle between "
					+ testVec
					+ " and "
					+ GeometryConstants.X_VECTOR
					+ " = "
					+ angle);
			if (!Ellipse.isPositive(testVec)) {
				angle *= -1;
				//	angle += Math.PI*2;
			}
			double start = startAngle % (Math.PI * 2);
			double end = endAngle % (Math.PI * 2);
			//      start = start%(Math.PI*2);
			//      end = end%(Math.PI*2);
			if (start > end) {
				//	end += Math.PI*2;
				start = Math.PI * 2 - start;
				start *= -1;
			}
			System.out.println("start = "
					+ start
					+ ", end = "
					+ end
					+ ", angle = "
					+ angle);
			return (angle >= start && angle <= end);
			//      Plane startPlane = new Plane(GeometryConstants.ORIGIN,new Point3d(0,0,1),start);
			//      Plane endPlane = new Plane(new Point3d(0,0,1),GeometryConstants.ORIGIN,end);
			//      return (startPlane.distancePointPlane(test) >= 0 && endPlane.distancePointPlane(test) >= 0);
		} catch (InvalidPlaneException ipe) {
			System.out.println("In Arc/isPointOnArc, Invalid Plane");
		}
		return false;
	}


	/**
	 * @todo dnsormaz chandu added this method 11/11/2005
	 * @param transformMatrix Matrix4d
	 * @return Arc
	 */
	public Arc transform(Matrix4d transformMatrix) {
		Point3d newCenter = new Point3d();
		Vector3d newXAxis = new Vector3d();
		Vector3d newYAxis = new Vector3d();
		transformMatrix.transform(this.center, newCenter);
		transformMatrix.transform(this.geettXAxis(), newXAxis);
		transformMatrix.transform(this.geettYAxis(), newYAxis);
		return new Arc(
				this.radius,
				newCenter,
				newXAxis,
				newYAxis,
				this.startAngle,
				this.endAngle);
	}
	/**
	 * method that swaps (???) start and end points
	 * @todo dnsormaz chandu added this method 11/11/2005
	 * @return CurveSegment
	 */
	public CurveSegment swap() {
		double nStart, nEnd;
		nStart = Math.PI - this.getEndAngle();
		nEnd = Math.PI - this.getStartAngle();
		Vector3d nX = new Vector3d();
		nX.negate(this.geettXAxis());
		Arc newArc = new Arc(radius, center, nX, this.geettYAxis(), nStart, nEnd);
		System.out.println(this + " swapped to " + newArc);		
		return newArc;
	}
	/**
	 * String representation of an arc.
	 *
	 *
	 */

	public String toString() {
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(Tuple3dRenderer.NUM_FRACTION_DIGITS);
		StringBuffer buf = new StringBuffer("Arc: ");//getClass().getName());
		buf.append(": [rad ").append(format.format(radius)).append("]");
		buf.append(", [center ").append(Tuple3dRenderer.format(center)).append("]");
		buf.append(", [norm ").append(Tuple3dRenderer.format(normal)).append("]");

		if(hasAxes()) {
			buf.append(", [x-axis ").append(Tuple3dRenderer.format(xAxis)).append("]");
			buf.append(", [y-axis ").append(Tuple3dRenderer.format(yAxis)).append("]");
		}

		buf.append(", [start ").append(format.format(startAngle)).append("]");
		buf.append(", [end ").append(format.format(endAngle)).append("]");
		return buf.toString();
	}

	/**
	 * init method.
	 *
	 *
	 */

	public void init() {
		//    super.init();
		panel = new JPanel();
		this.panel.add(new JLabel(this.toString()));
		this.panel.setSize(new Dimension(650, 60));
	}

	/** getShapeList() method.
	 *
	 */
	public LinkedList geetShapeList(DrawWFPanel canvas) {
		// if X and Y axis are present
		if (hasAxes())
			return new Ellipse(radius, radius, center, xAxis, yAxis, startAngle, endAngle).geetShapeList(canvas);
		// If only normal is there
		LinkedList shapeList = new LinkedList();
		if (startAngle != endAngle) {
			if (segments < 3) {
				segments = 3;
			}

			this.getNormal();

			double arcStart = startAngle % (Math.PI * 2);
			double arcEnd = endAngle % (Math.PI * 2);

			double totalAngle = arcEnd - arcStart;
			if (arcStart >= arcEnd) {
				totalAngle += Math.PI * 2;
				arcEnd += Math.PI * 2;
			}

			double angle = totalAngle / segments;
			Matrix4d transformMatrix = getTransformMatrix();
			for (int i = 0; i < segments; i++) {
				Point3d start = new Point3d(
						radius * Math.cos(angle * i + arcStart), 
						radius * Math.sin(angle * i + arcStart), 
						0);
				transformMatrix.transform(start);
				Point3d end = new Point3d();
				if ((i + 1) == segments) {
					end = new Point3d(
							radius * Math.cos(arcEnd), 
							radius * Math.sin(arcEnd), 
							0);
				} else {
					end = new Point3d(
							radius * Math.cos(angle * (i + 1) + arcStart), 
							radius * Math.sin(angle * (i + 1) + arcStart), 
							0);
				}
				transformMatrix.transform(end);
				LineSegment ls = new LineSegment(start, end);
				shapeList.addAll(ls.geetShapeList(canvas));
			}
		}
		return shapeList;
	}

	/** Main method.
	 *
	 */
	public static void main(String[] args) {
		//    Arc c = new Arc(0.2, new Point3d(0.5,0.3,0.5), new Vector3d(0,1,0), 0, 2*Math.PI);
		Arc c = new Arc(
				4,
				new Point3d(0, 0, 0),
				new Vector3d(1, 0, 0),
				new Vector3d(0, 0, -1),
				Math.PI/2,
				Math.PI);
		Arc c1 = (Arc) c.swap();
		System.out.println(c1.toString());
		Point3d testPoint = new Point3d(-2, -2, 0);
		System.out.println("is "
				+ testPoint
				+ " on arc? "
				+ c.isPointOnArc(testPoint) + " " + c1.isPointOnArc(testPoint));
		System.out.println("Start and end points of c: "+ c.geettStartPoint() + " " + c.geettEndPoint());
		System.out.println("Start and end points of c1: "+ c1.geettStartPoint() + " " + c1.geettEndPoint());
		c.settApplet(new DrawWFApplet(c, new Point3d(0, 0, 30), 20));
		c.display("Arc", new Dimension(650, 700), JFrame.EXIT_ON_CLOSE);
		c1.settApplet(new DrawWFApplet(c1, new Point3d(0, 0, 30), 20));
		c1.display("Arc", new Dimension(650, 700), JFrame.EXIT_ON_CLOSE);
	}
}
