package edu.ohiou.mfgresearch.labimp.gtk3d;

import java.util.LinkedList;
import edu.ohiou.mfgresearch.labimp.draw.ImpObject;
import javax.vecmath.Point3d;
import edu.ohiou.mfgresearch.labimp.draw.*;
import edu.ohiou.mfgresearch.labimp.gtk2d.InvalidPlaneException;
//import processes.VisualGeometry.Polygon3d;

import java.util.*;
import java.awt.Color;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.*;

/**
 *
 *  Title:        Generic classes for manufacturing planning
 *  Description:  Thsi project implements general classes for intelligent manufacturing planning. These are:
 *  ImpObject - umbrella class fro all objects
 *  MrgPartModel - general part object data
 *  Viewable - interface to display objects in applet
 *  GUIApplet - applet that utilizes viewable interface
 *  Copyright:    Copyright (c) 2001
 *  Company:      Ohio University
 *  @author Dusan Sormaz
 *  @version 1.0
 * 
 */

public class Polyhedron extends ImpObject {
	LinkedList polygons;

	public Polyhedron() {
		this(new LinkedList());
	}

	public Polyhedron(LinkedList inPolygons) {
		polygons = inPolygons;
		//    color = Color.blue;
	}

	public Polyhedron(Point3d[] points) {
		//    if (points.length %2 == 0) {
		//      for (int i == 0; i< points.length/2; i++) {

	}

	public void createTetrahedron(Point3d[] points) {
		if (points.length == 4) {
			Polygon3d p = new Polygon3d().addPoint(points[0]).addPoint(
				points[1]).addPoint(points[2]);
			p.settColor(Color.blue);
			polygons.add(p);
			p = new Polygon3d().addPoint(points[1]).addPoint(points[3])
				.addPoint(points[2]);
			p.settColor(Color.red);
			polygons.add(p);
			p = new Polygon3d().addPoint(points[0]).addPoint(points[2])
				.addPoint(points[3]);
			p.settColor(Color.green);
			polygons.add(p);
			p = new Polygon3d().addPoint(points[0]).addPoint(points[3])
				.addPoint(points[1]);
			p.settColor(Color.cyan);
			polygons.add(p);
		} else {
			System.out.println("incorrect number of points");
		}
	}

	public LinkedList getShapeList(DrawWFPanel canvas) {
		LinkedList lines = new LinkedList();
		for (ListIterator itr = polygons.listIterator(); itr.hasNext();) {
			lines.addAll(((Polygon3d) itr.next()).getShapeList(canvas));
		}
		return lines;
	}

	//Accessor Methods
	/**
	 *
	 */
	public Polygon3d[] getPolygon() {
		Polygon3d[] pArray = new Polygon3d[polygons.size()];
		polygons.toArray(pArray);
		return pArray;
	}

	public LinkedList getPointSet() {
		TreeSet points = new TreeSet(new Point3dEqualComparator());
		for (ListIterator itr = polygons.listIterator(); itr.hasNext();) {
			points.addAll(((Polygon3d) itr.next()).getPointSet());
		}
		return new LinkedList(points);
	}
	
	/**
	 * Tests the location of given point w.r.t. polyhedron.
	 * NOTE: This code is valid for ONLY convex polyhedra
	 *       and for small neighborhood in others
	 * Returns: 0 - point on boundary of polyhedron
	 *          1 - point inside polyhedron
	 *         -1 - point outside polyhedron
	 * @param point
	 * @return
	 */
	private int testPointLocation (Point3d point) {
		double distance = Double.NaN;
		for (Iterator itr = polygons.iterator(); itr.hasNext(); ) {
			Polygon3d polygon = (Polygon3d) itr.next();
			try {
				Plane plane = polygon.getPlane();
				distance = plane.distancePointPlane(point);
			} catch (InvalidPlaneException e) {
				System.out.println("In Polyhedron > testPointLocation: Polygon plane created is invalid.");
				e.printStackTrace();
			}			
		}
		if (distance == 0)
			return 0; // on boundary
		if (distance < 0)
			return 1; // inside
		else
			return -1; // outside
	}

	/**
	 * NOTE: This code is valid for ONLY convex polyhedra
	 *       and for small neighborhood in others
	 * @param point
	 * @return
	 */
	public boolean isPointOnBoundary (Point3d point){
		return (testPointLocation(point)==0); // ? true : false;
	}

	/**
	 * NOTE: This code is valid for ONLY convex polyhedra
	 *       and for small neighborhood in others
	 * @param point
	 * @return
	 */
	public boolean isPointInside (Point3d point) {
		return (testPointLocation(point)>0); // ? true : false;
	}

	/**
	 * NOTE: This code is valid for ONLY convex polyhedra
	 *       and for small neighborhood in others
	 * @param line
	 * @return
	 */
	public boolean isCurveInside (LineSegment line) {
		return isPointInside (line.gettStartPoint()) && 
		isPointInside (line.gettEndPoint());
	}

	/**
	 * NOTE: This code is valid for ONLY convex polyhedra
	 *       and for small neighborhood in others
	 * @param arc
	 * @return
	 */
	public boolean isCurveInside (Arc arc) {
		return isPointInside (arc.gettStartPoint()) && 
		isPointInside (arc.gettEndPoint());
	}
	
	//Modifier Methods
	/**
	 * Static method called for generating the geometry array for supplied set of
	 * polygons. It combines all the supplied polygons and generates triangualtion
	 * for them combinedly.
	 */
	public static GeometryArray createGraphicsPolygon(Polygon3d[] inFaces) {
		GeometryInfo polyInfo = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
		polyInfo.setCoordinates(createPolygonCoordinates(inFaces));
		polyInfo.setStripCounts(getStripCounts(inFaces));
		//    GeometryArray ga = doTriangulation(polyInfo).getIndexedGeometryArray(true);
		//    ga = doTriangulation(polyInfo).getIndexedGeometryArray(true);
		return Gtk.doTriangulation(polyInfo).getIndexedGeometryArray(true);
	}

	/**
	 *
	 *    Method to return 3d Polyhedron for displaying in 3d canvas.
	 *   
	 */

	public GeometryArray createGraphicsPolyhedron() {
		return createGraphicsPolygon(getPolygon());
	}

	/**
	 * Method to return array of coordinates for the supplied set of polygons.
	 */
	private static Point3d[] createPolygonCoordinates(Polygon3d[] inFaces) {
		ArrayList tempList = new ArrayList();
		for (int i = 0; i < inFaces.length; i++)
			tempList.addAll(inFaces[i].getPointSet());
		Point3d[] pa = new Point3d[tempList.size()];
		tempList.toArray(pa);
		return pa;
	}

	/**
	 * Method to return strip array for supplied set of polygon.
	 */
	private static int[] getStripCounts(Polygon3d[] inFaces) {
		int[] strip = new int[inFaces.length];
		for (int i = 0; i < inFaces.length; i++)
			strip[i] = inFaces[i].getPointSet().size();
		return strip;
	}

	public static void main(String[] args) {
		Polyhedron polyhedron = new Polyhedron();
		Point3d p1 = new Point3d(0, 0, 0);
		Point3d p2 = new Point3d(2, 0, 0);
		Point3d p3 = new Point3d(1, 1, 2);
		Point3d p4 = new Point3d(1, 2, 0);
		Point3d[] points = { p1, p2, p3, p4 };
		polyhedron.createTetrahedron(points);
		polyhedron.settApplet(new DrawWFApplet(polyhedron));
		polyhedron.display("test");
	}
} // EndOfClass Polyhedron

