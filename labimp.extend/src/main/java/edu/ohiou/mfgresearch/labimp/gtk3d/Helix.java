/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.gtk3d;

import java.util.LinkedList;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import edu.ohiou.mfgresearch.labimp.draw.*;

/**
 * @author Dusan Sormaz
 *
 */
public class Helix extends CurveSegment {
	
	private Vector3d normal;
	private double radius;
	private double pitch;
	double height;
	private Point3d center;
	double startAngle;
	int segments = 50;

	/**
	 * 
	 */
	public Helix() {
		this (new Point3d (0,0,0), new Vector3d (0,0,1), 1.0, 1.0, 5.0, .7850);
	}
	
	public Helix (Point3d point, Vector3d vector, double radius, double pitch, double height, double startAngle) {
		center = point;
		normal = vector;
		this.radius = radius;
		this.pitch = pitch;
		this.height = height;
		this.startAngle = startAngle;
		double segs = height/pitch * segments;
		segments = Math.round( (float) segs);

	}

	/* (non-Javadoc)
	 * @see edu.ohiou.mfgresearch.labimp.gtk3d.CurveSegment#getEndPoint()
	 */
	public Point3d gettEndPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ohiou.mfgresearch.labimp.gtk3d.CurveSegment#getStartPoint()
	 */
	public Point3d gettStartPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ohiou.mfgresearch.labimp.gtk3d.CurveSegment#getTangent(javax.vecmath.Point3d)
	 */
	public Vector3d getTangent(Point3d point) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// DrawableWF interface
	
	public LinkedList getShapeList (DrawWFPanel canvas){
		LinkedList shapeList = new LinkedList();
		
			if (segments < 3)
				segments = 3;
			normal.normalize();
			double endAngle;
			endAngle = startAngle + Math.PI * 2 * height/pitch;
//			System.out
//				.println("- - - - - - - - - - - - - - - - - - - - - - - - - -");
//			System.out
//				.println("... center = " + center + ", normal =" + normal);
//			System.out.println("--> startAngle="
//				+ startAngle
//				+ ", endAngle="
//				+ endAngle);
//			boolean addedTwoPi = false;
			double arcStart = startAngle % (Math.PI * 2);
			//      if(arcStart < 0) {
			//        arcStart += Math.PI*2;
			//        addedTwoPi = true;
			//      }
			double arcEnd = endAngle;
			//      if(addedTwoPi)
			//        arcEnd += Math.PI*2;
			//      if(arcEnd <= 0)
			//        arcEnd += Math.PI*2;
			double totalAngle = arcEnd - arcStart;
//			System.out.println("==> start="
//				+ arcStart
//				+ ", end="
//				+ arcEnd
//				+ ", total="
//				+ totalAngle);
			double angle = totalAngle / segments;
			double zIncrement = height/segments;
			Matrix4d transformMatrix = new Matrix4d();
			transformMatrix.setIdentity();
			for (int i = 0; i < segments; i++) {
				Point3d start = new Point3d(
						radius * Math.cos(angle * i + arcStart), 
						radius * Math.sin(angle * i + arcStart), 
						zIncrement * i);
				transformMatrix.transform(start);
				Point3d end = new Point3d();				
				end = new Point3d(
							radius * Math.cos(angle * (i + 1) + arcStart), 
							radius * Math.sin(angle * (i + 1) + arcStart), 
							zIncrement * (i + 1));
				transformMatrix.transform(end);
				LineSegment ls = new LineSegment(start, end);
				shapeList.addAll(ls.getShapeList(canvas));
			}
		
		return shapeList;
		
	}

	/* (non-Javadoc)
	 * @see edu.ohiou.mfgresearch.labimp.gtk3d.CurveSegment#swap()
	 */
	public CurveSegment swap() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Helix h = new Helix();
		h.settApplet(new DrawWFApplet (h));
		h.display();
		// TODO Auto-generated method stub

	}

}
