package edu.ohiou.mfgresearch.labimp.gtk3d;

import java.util.Comparator;
import javax.vecmath.Point2d;

/**
 *
 *  <p>Title:       Point2dComparator class</p>
 *  <p>Description: </p>
 *  <p>Copyright:   Dr.D.N.Sormaz, Ohio University Copyright (c) 2003</p>
 *  <p>Company:     IMSE Dept, Ohio University</p>
 *  @author         Deepak Pisipati + Dr.Dusan N. Sormaz
 *  @version 1.0
 * 
 */

public class Pt2dComparator implements Comparator {
	Point2d comparingPoint;
	double epsilon;

	public Pt2dComparator(Point2d point) {
		this(point, GeometryConstants.EPSILON);
	}

	public Pt2dComparator(Point2d point, double epsilon) {
		comparingPoint = point;
		this.epsilon = epsilon;
	}

	public int compare(Object o1, Object o2) {
		double d11 = comparingPoint.distance((Point2d) o1);
		double d22 = comparingPoint.distance((Point2d) o2);
		Double d1 = new Double(d11);
		Double d2 = new Double(d22);
		return Gtk.epsilonEquals(d11, d22, epsilon) ? 0 : (d1.compareTo(d2));
	}
}
