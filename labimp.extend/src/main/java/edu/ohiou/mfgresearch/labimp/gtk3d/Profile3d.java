/**
 * Title:         Profile Class (abstract)<p>
 * Description:   Class defining parameters of profiles. <p>
 * Copyright:     Copyright (c) Jaikumar Arumugam<p>
 * Company:       Ohio University<p>
 * @author        Jaikumar Arumugam
 * @version       1.0
 */
package edu.ohiou.mfgresearch.labimp.gtk3d;

import edu.ohiou.mfgresearch.labimp.draw.ImpObject;
import edu.ohiou.mfgresearch.labimp.draw.DrawWFPanel;
import java.awt.Dimension;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Matrix4d;

import edu.ohiou.mfgresearch.labimp.draw.DrawWFApplet;
import edu.ohiou.mfgresearch.labimp.gtk2d.*;

public class Profile3d extends ImpObject {

	// Class parameter
	private LinkedList shapes = new LinkedList();

	private  boolean isPlanar = false;
	private Plane plane;
	private Point3d origin = new Point3d(); //added by Chandu 10/12
	private HashMap curveMap;
	Matrix4d transformMatrix = null;
	private Point3d position;
	private Vector3d[] dirr_array;
	private double pocket_length,pocket_width, corner_radius;
	

	public Vector3d[] gettDirr_array() {
		return dirr_array;
	}
	/*public double[][] getDirr_array() {
		double[][] ret_arr=new double[this.dirr_array.length][3];
		for(int i =0; i< this.dirr_array.length; i++){
			double [] ret = {dirr_array[i].x,dirr_array[i].y, dirr_array[i].z};
			ret_arr[i]=ret; 
		}
		
		return ret_arr;
	}*/
	public void settDirr_array(Vector3d[] dirr_array){
		this.dirr_array = dirr_array;
	}
	/*public void setDirr_array(double[][] dirr_array) {
		Vector3d[] dirr_arr = new Vector3d[dirr_array.length];
		for(int i =0; i< dirr_array.length; i++){
			dirr_arr[i]= new Vector3d(dirr_array[i][0],dirr_array[i][1],dirr_array[i][2]);
		}
		this.dirr_array = dirr_arr;
	}*/
	
	public double[] getOrigin() {
		double[] origin = new double[]{this.origin.x,this.origin.y, this.origin.z };
		return origin ;
	}
	public void setOrigin(double[] origin) {
		this.origin = new Point3d(origin[0],origin[1],origin[2]);
	}
	public void setShapes(LinkedList shapes) {
		this.shapes = shapes;
	}
	public void setPlanar(boolean isPlanar) {
		this.isPlanar = isPlanar;
	}
	public boolean getPlanar() {
		return this.isPlanar ;
	}
	public void setPlane(Plane plane) {
		this.plane = plane;
	}
	

	public double getPocket_length() {
		return pocket_length;
	}

	public void setPocket_length(double pocket_length) {
		this.pocket_length = pocket_length;
	}

	public double getPocket_width() {
		return pocket_width;
	}

	public void setPocket_width(double pocket_width) {
		this.pocket_width = pocket_width;
	}

	public double getCorner_radius() {
		return corner_radius;
	}

	public void setCorner_radius(double corner_radius) {
		this.corner_radius = corner_radius;
	}

	public Point3d getttPosition() {
		return position;
	}
	/*public double[] getPosition() {
		double [] ret = {position.x, position.y, position.z};
		return ret;
	}*/

	public void settPosition(Point3d position) {
		this.position = position;
	}
	/*
	public void setPosition(double [] v ) {
		this.position = new Point3d(v[0], v[1], v[2]);
	}*/

	public LinkedList loops = new LinkedList();

	// CONSTRUCTORS

	/**
	 * Default constructor.
	 * 
	 */
	public Profile3d() {
	}

	/**
	 * Constructor of Profile3d taking a linked list of shapes.
	 * 
	 */
	public Profile3d(LinkedList shapesList) {
		shapes = new LinkedList(shapesList);
	}

	/**
	 * Copy constructor of Profile3d.
	 * 
	 */
	public Profile3d(Profile3d inProfile) {
		for (ListIterator itr = inProfile.shapes.listIterator(); itr.hasNext();) {
			Object shape = itr.next();
			if (shape instanceof LineSegment) {
				LineSegment ls = (LineSegment) shape;
				shapes
						.add(new LineSegment(ls.gettStartPoint(), ls
								.gettEndPoint()));
			} else if (shape instanceof Arc) {
				Arc inArc = (Arc) shape;
				shapes.add(new Arc(inArc));
			}
		}
	}

	// MODIFIERS

	/**
	 * Add Line Segment to linked list of shapes.
	 * 
	 */
	public Profile3d addShape(CurveSegment cs) {
		shapes.add(cs);
		return this;
	}

	// /** Add Arc to linked list of shapes.
	// *
	// */
	// public Profile3d addShape(Arc arc) {
	// shapes.add(arc);
	// return this;
	// }

	// SELECTORS

	/**
	 * Method to represent Profile3d as a string.
	 * 
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer("");
		buf.append(getClass().getName()).append(" shapes:");
		for (ListIterator itr = shapes.listIterator(); itr.hasNext();) {
			buf.append("\n -> " + itr.next());
		}
		return buf.toString();
	}

	public String toToolTipString() {
		StringBuffer buf = new StringBuffer();
		buf.append(getClass().getName()).append("<br>Shapes:");
		for (ListIterator itr = shapes.listIterator(); itr.hasNext();) {
			buf.append("<br>" + itr.next());
		}
		return buf.toString();
	}

	/**
	 * Method that returns shapes linked list.
	 * 
	 * 
	 */

	public LinkedList getShapes() {
		return shapes;
	}

	// /**
	// * method that makes loops from profiles
	// * @todo dnsormaz chandu added this method 11/11/2005
	// */
	// public void makeLoops() {
	// Loop currentLoop = new Loop();
	// CurveSegment first = (CurveSegment) getShapes().getLast();
	// currentLoop.addCurve(first);
	// for (int i = 0; i < getShapes().size(); i++) {
	// CurveSegment next = (CurveSegment) getShapes().get(i);
	// if (!currentLoop.addCurve(next)) {
	// loops.add(currentLoop);
	// currentLoop = new Loop();
	// currentLoop.addCurve(next);
	// }
	// }
	// loops.add(currentLoop);
	// System.out.println(loops.size() + " loops found.");
	// }

	public LinkedList getLoops() {
		return loops;
	}

	/**
	 * 
	 * Method to return tree representation of profile3d object.
	 * 
	 * @return
	 * 
	 */

	public DefaultMutableTreeNode getTree() {
		return new DefaultMutableTreeNode(this);
	}

	/**
	 * Method that returns arcs of profile as a linked list.
	 * 
	 * 
	 */

	public LinkedList getArcs() {
		LinkedList arcs = new LinkedList();
		for (ListIterator itr = shapes.listIterator(); itr.hasNext();) {
			Object shape = itr.next();
			if (shape instanceof Arc)
				arcs.add(shape);
		}
		return arcs;
	}

	/**
	 * Method that returns line segments of profile as a linked list.
	 * 
	 * 
	 */

	public LinkedList getLineSegments() {
		LinkedList lines = new LinkedList();
		for (ListIterator itr = shapes.listIterator(); itr.hasNext();) {
			Object shape = itr.next();
			if (shape instanceof LineSegment)
				lines.add(shape);
		}
		return lines;
	}

	/**
	 * Method to return defining points of the profile. (both arc and line
	 * segment points)
	 * 
	 */

	public HashSet getProfilePoints() {
		HashSet pointSet = new HashSet();
		for (ListIterator itr = shapes.listIterator(); itr.hasNext();) {
			Object shape = itr.next();
			if (shape instanceof LineSegment) {
				pointSet.add(((LineSegment) shape).gettStartPoint());
				pointSet.add(((LineSegment) shape).gettEndPoint());
			}
			if (shape instanceof Arc)
				pointSet.add(((Arc) shape).getCenter());
		}
		return pointSet;
	}
	
	/**
	 * Method to return line segement points of the profile.
	 * 
	 * 
	 */

	public HashSet getLinePoints() {
		HashSet pointSet = new HashSet();
		for (ListIterator itr = shapes.listIterator(); itr.hasNext();) {
			Object shape = itr.next();
			if (shape instanceof LineSegment) {
				pointSet.add(((LineSegment) shape).gettStartPoint());
				pointSet.add(((LineSegment) shape).gettEndPoint());
			}
		}
		return pointSet;
	}

	/**
	 * Method to return arc points of the profile.
	 * 
	 * 
	 */

	public HashSet getArcPoints() {
		HashSet pointSet = new HashSet();
		for (ListIterator itr = shapes.listIterator(); itr.hasNext();) {
			Object shape = itr.next();
			if (shape instanceof Arc)
				pointSet.add(((Arc) shape).gettCenter());
		}
		return pointSet;
	}

	public LinkedList getPointSetAsLinkedList() {
		LinkedList pointList = new LinkedList();
		Object[] pointArray = getProfilePoints().toArray();
		for (int i = 0; i < pointArray.length; i++) {
			pointList.add(pointArray[i]);
		}
		return pointList;

	}
	
	public Plane gettPlane () throws InvalidPlaneException {
		if (!issPlanar()) {
			throw new InvalidPlaneException("Profile " + toString() + "is not planar");
		}
		return plane;
	}
	
	public Plane getPlane(){
		return this.plane;
	}


	/**
	 * dnsormaz needs explanation !!?!?!!?
	 * not foolproof code
	 * covers only is profile curves are colinear
	 * 
	 * @return
	 */
	/*public boolean isPlanar() {
		return this.isPlanar;
	}*/
	public boolean issPlanar() {
		if (plane != null) return true;
		LinkedList points = getPointSetAsLinkedList();
		Plane tryPlane = null;
		if (points.size() < 3)
		{
			CurveSegment curve = (CurveSegment) getShapes().getFirst();
			if (curve instanceof LineSegment)
				return false;
			if (curve instanceof Arc)
			{
				Arc arc = (Arc) curve;
				try
				{
					tryPlane = new Plane(arc.gettCenter(), arc.gettNormal());
					plane = tryPlane;
					isPlanar = true;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}				
			}
		}
//		System.out.println("points: " + points);
		else
		{
			Point3d p1 = (Point3d) points.getFirst();
			Point3d p2 = (Point3d) points.get(1);
			int i = 2, j = 2;
			while (i<points.size())
			{
				try
				{
					Point3d p3 = (Point3d) points.get(i);
					tryPlane = new Plane(p1, p2, p3);
					break;
				}
				catch (InvalidPlaneException ipe)
				{
					i++;
				}
			}
			if (tryPlane == null) return false;
			while (j < points.size())
			{
				Point3d testPoint = (Point3d) points.get(j);
				if (!tryPlane.contains(testPoint)) 
				{
					return false;
				}
				j++;
			}
			plane = tryPlane;
			isPlanar = true;
		}
		return isPlanar;
	}
	
	/**
	 * transfrom for its own matrix (Using computeTransformMatrix 
	 * for transformtation)
	 * @return
	 */
	
	public Prof2d transformTo2d () 
		throws InvalidCurveException, InvalidPlaneException {
		
		transformMatrix = computeTransformMatrix();
		return transformTo2d(transformMatrix);
	}

	/**
	 * @author Chandu
	 * transforms this profile to a plane parallel to the Z axis
	 * and creates a Prof2d by dropping the z distance. 
	 */

	public Prof2d transformTo2d(Matrix4d matrix)
			throws InvalidCurveException, InvalidPlaneException {
		Vector3d normal = gettPlane().getNormal();
		Prof2d prof2d = new Prof2d();
		curveMap = new HashMap();
		if (issPlanar()) {
			
			for (Iterator shapeItr = getShapes().iterator(); shapeItr.hasNext();) {
				Object shape = shapeItr.next();
				if (shape instanceof LineSegment) {
					LineSegment newLine = (LineSegment) shape;
					newLine = newLine.transform(matrix);
					
					try {
						Line2d line = new Line2d(newLine.startPoint.x,
								newLine.startPoint.y, newLine.endPoint.x,
								newLine.endPoint.y);
						prof2d.addCurve(line);
						curveMap.put(line, shape);
						
					} catch (Exception e) {
						System.out
								.println("Exception in transformTo2D (line) :"
										+ e.getMessage());
					}
				}// if
				if (shape instanceof Arc) {
					Arc tempArc = (Arc) shape;
					System.out.println("3d Arc angles:"
							+ tempArc.getStartAngle() + " and "
							+ tempArc.getEndAngle());
					if (!tempArc.gettNormal().epsilonEquals(normal,
							GtkConstants.EPSILON))
						tempArc = (Arc) tempArc.swap();
					tempArc = (tempArc).transform(matrix);
					
					Vector3d arcX = tempArc.gettXAxis();
					double angleX = arcX.angle(GeometryConstants.X_VECTOR);
//					System.out.println("Angle between X axes: " + angleX);
					Point2d center = new Point2d(tempArc.center.x,
							tempArc.center.y);
					try {
						Arc2d arc = new Arc2d(center, tempArc.radius, tempArc
								.getStartAngle()
								+ angleX
								, tempArc.getEndAngle() 
								+ angleX
								);
						prof2d.addCurve(arc);
						curveMap.put(arc, shape);
//						System.out.println("Added arc: " + arc);
					} catch (Exception e) {
						System.out.println("Exception in transformTo2D (Arc) :"
								+ e.getMessage());
					}
				}// if
			}// for - curves
		}// if - planar
		return prof2d;
	}
	
	public Matrix4d computeTransformMatrix()
	{
		if (transformMatrix != null)
			return transformMatrix;
		transformMatrix = new Matrix4d();  
		Matrix4d rotationMatrix= null;
		try
		{
		Vector3d normal = gettPlane().getNormal();
		Point3d pt = ((CurveSegment) getShapes().getFirst()).gettStartPoint();

			double distance = gettPlane().distancePointPlane(GeometryConstants.ORIGIN);
			Vector3d tPoint = new Vector3d(normal);
			tPoint.scale(distance);	
//			tPoint.negate();
		rotationMatrix = Gtk.computeTransformMatrix(normal);

		     Matrix4d translationMatrix = new Matrix4d();
		     Vector3d translateVec = new Vector3d(tPoint);
		     translateVec.negate();

		     translationMatrix.set(translateVec);
		     transformMatrix = new Matrix4d(translationMatrix);
		     transformMatrix.mul(rotationMatrix);
		
		}
		catch (Exception ipe)
		{
			ipe.printStackTrace();
		}
		transformMatrix.invert();
		return transformMatrix;
	}
	
	/** @todo implement properly -- translation et al */
	public Profile3d transform(Vector3d vector) {
		return transform(Gtk.computeTransformMatrix(vector));
	}
	
	public HashMap gettCurveMap()
	{
		return curveMap;
	}
	
	public Profile3d transform(Matrix4d xFormMatrix) {
		Profile3d transformedProfile = new Profile3d();
		for (ListIterator itr = shapes.listIterator(); itr.hasNext();) {
			Object shape = itr.next();
			if (shape instanceof LineSegment)
				transformedProfile.addShape(((LineSegment) shape).transform(xFormMatrix));
			if (shape instanceof Arc)
				transformedProfile.addShape(((Arc) shape).transform(xFormMatrix));
		}
		return transformedProfile;
	}

	/**
	 * Method that defines the display properties of Profile3d.
	 * 
	 */
	public LinkedList getShapeList(DrawWFPanel canvas) {
		LinkedList shapeList = new LinkedList();
		for (ListIterator itr = shapes.listIterator(); itr.hasNext();) {
			Object shape = itr.next();
			if (shape instanceof LineSegment)
				shapeList.addAll(((LineSegment) shape).getShapeList(canvas));
			if (shape instanceof Arc)
				shapeList.addAll(((Arc) shape).getShapeList(canvas));
		}
		return shapeList;
	}

	public void init() {
		// super.init(); //////what to do with init()???
		// canvas.init();
		panel = new JPanel();
		// displays label with toString of box.
		panel.add(new JList(shapes.toArray()));
	}

	/**
	 * Main method.
	 * 
	 */
	public static void main(String[] args) {
		Profile3d prof = new Profile3d();
//		prof.addShape(new LineSegment(0.2, 0, 0, 0.8, 0, 0)).addShape(
//				new Arc(0.2, new Point3d(0.8, 0.2, 0), new Vector3d(1, 1, 0),
//						new Vector3d(-1, 1, 0), 3 * Math.PI / 2, 2 * Math.PI));

//		prof.addShape(new LineSegment(0, 0, 1, 1, 0, 0)).addShape(new LineSegment(1, 0, 0, 0, 1, 0)).
//		addShape(new LineSegment(0, 1, 0, 0, 0, 1));
		
//		prof.addShape(new LineSegment(0,0,2, 2, 0,2)).addShape(new LineSegment(2, 0,2, 0, 3,2)).
//		addShape(new LineSegment(0, 3, 2, 0,0,2));
		
		prof.addShape (new Arc(5.0, new Point3d(0.0, 0.0, 0.0), 
				GeometryConstants.X_VECTOR, GeometryConstants.Y_VECTOR));
		
		try {
			Prof2d prof2ds = prof.transformTo2d();
			prof2ds.settApplet(new edu.ohiou.mfgresearch.labimp.basis.Draw2DApplet(prof2ds));
			prof2ds.display();
			Matrix4d m4d = new Matrix4d(prof.computeTransformMatrix());		
			Profile3d p3d = prof.transform(m4d);
			p3d.settApplet(new DrawWFApplet(p3d, new Point3d(30, 30, 30), 200));
			p3d.display("transform");
			m4d.invert();
			Profile3d p3d1 = prof2ds.getProfile3d(m4d);
			p3d1.settApplet(new DrawWFApplet(p3d1, new Point3d(30, 30, 30), 200));
			p3d1.display("getProfile");
		} catch (Exception e) {
			e.printStackTrace();
		}
		prof.settApplet(new DrawWFApplet(prof, new Point3d(30, 30, 30), 200));
		prof.display("original profile", new Dimension(650, 700), JFrame.EXIT_ON_CLOSE);
		System.out.println("" + prof);
	}
}
