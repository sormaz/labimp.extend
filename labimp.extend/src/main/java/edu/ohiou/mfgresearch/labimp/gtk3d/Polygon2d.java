package edu.ohiou.mfgresearch.labimp.gtk3d;

import edu.ohiou.mfgresearch.labimp.basis.*;
import edu.ohiou.mfgresearch.labimp.gtk2d.*;

import java.util.List;

public class Polygon2d extends ViewObject {
	private List pointSet;

	public Polygon2d() {
	}

	public Polygon2d(List inPointSet) {
		pointSet = inPointSet;
	}

	/**
	 * This method will return TRUE if the point (x,y) is inside the
	 * polygon, or FALSE if it is not. If the point (x,y) is exactly on
	 * the edge of the polygon, then the "modified" method returns TRUE.
	 * (Source: Determining Whether A Point Is Inside A Complex Polygon
	 *  Author: Darel R. Finley
	 *  URL: http://www.alienryderflex.com/polygon/
	 *
	 * NOTE: Division by zero is avoided because the division is
	 *       protected by the "if" clause which surrounds it.
	 *
	 * The method "pointOnBoundary" is used to determine if point (x,y) lies on
	 * the boundary of polygon.
	 *
	 * @param xp[]        Horizontal coordinates of vertices of polygon.
	 * @param yp[]        Vertical coordinates of vertices of polygon.
	 * @param x           Horizontal coordinate of test point.
	 * @param y           Vertical coordinate of test point.
	 * @return TRUE/FALSE Test point inside/outside of polygon.
	 */
	public static boolean isPointInside(
		double xp[],
		double yp[],
		double x,
		double y) {
		// Variable declarations.
		int polySides = xp.length; // polySides = number of vertices of polygon.
		boolean inside = false;

		if (polySides < 3)
			return false; // Testing for polygon. (atleast 3 sides)
		if (isPointOnBoundary(xp, yp, x, y))
			return true;//Point on polygon "boundary".
		for (int i = 0, j = 0; i < polySides; i++) { // Point NOT on polygon "boundary".
			j++;
			if (j == polySides)
				j = 0;
			if ((yp[i] < y && yp[j] >= y) || (yp[j] < y && yp[i] >= y)) {
				if (x > (xp[i] + (y - yp[i])
					/ (yp[j] - yp[i])
					* (xp[j] - xp[i])))
					inside = !inside;
			}
		}
		return inside;
	}

	/**
	 * Method to find if point (x,y) is on boundary of polygon.
	 *
	 */
	public static boolean isPointOnBoundary(
		double xp[],
		double yp[],
		double x,
		double y) {
		int errorIndex = 0;
		try {
			// Variable declarations.
			int polySides = xp.length; // polySides = number of vertices of polygon.
			Line2d polyEdge;
			

			// Create Line2D.Double for all edges of polygon and check if test point
			// lies on the edge of polygon.
			for (int i = 0; i < polySides - 1; i++) {
				errorIndex = i;
				polyEdge = new Line2d(xp[i], yp[i], xp[i + 1], yp[i + 1]);
				if (polyEdge.getLineSegment2D().relativeCCW(x, y) == 0)
					return true;
			}
			errorIndex = polySides - 1;
			// Line joining first and last vertex of polygon.
			polyEdge = new Line2d(
				xp[0],
				yp[0],
				xp[polySides - 1],
				yp[polySides - 1]);
			return (polyEdge.getLineSegment2D().relativeCCW(x, y) == 0);
		} catch (InvalidLineException ex) {
//			ex.printStackTrace();
			throw new IllegalArgumentException("Invalid line at point " + errorIndex, ex);
		}
//		return false;
	}

	public static void main(String[] args) {
		Polygon2d polygon2d1 = new Polygon2d();
	}

}