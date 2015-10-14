package edu.ohiou.mfgresearch.labimp.gtk3d;

import java.util.Comparator;
import javax.vecmath.Point3d;

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

public class Point3dEqualComparator implements Comparator {

	public Point3dEqualComparator() {
	}

	public int compare(Object o1, Object o2) {
		if (((Point3d) o1).equals((Point3d) o2))
			return 0;
		return 1;
	}
}