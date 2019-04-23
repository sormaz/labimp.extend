package edu.ohiou.mfgresearch.labimp.gtk3d;

import javax.vecmath.Point2d;
import javax.vecmath.Matrix4d;
import edu.ohiou.mfgresearch.labimp.basis.ViewObject;
import edu.ohiou.mfgresearch.labimp.gtk2d.*;
import edu.ohiou.mfgresearch.labimp.gtk3d.GeometryConstants;

import javax.vecmath.*;

public abstract class Curve2d extends ViewObject {
	public final static int LEFT_SIDE = -1;
	public final static int RIGHT_SIDE = 1;

	public Curve2d() {
	}

	public abstract Point2d getStartPoint();

	public abstract Point2d getEndPoint();

	public abstract void setStartPoint(Point2d stPt);

	public abstract void setEndPoint(Point2d endPt);

	public abstract Curve2d offset(double offsetDistance, int offsetSide)
		throws InvalidCurveException;

	public abstract CurveSegment getCurveSegment(Matrix4d transformationMatrix);

	public abstract Curve2d clip(Line2d limitLine1, Line2d limitLine2)
	throws InvalidIntersectionException, InvalidCurveException, InvalidLineException;
	/**
	 * Returns the closest end point of this from pt. Returns endPoint if they
	 * are at the same distance
	 */
	public Point2d closestEndPoint(Point2d pt) //throws NotUniqueValueException
	{
		Point2d stPt = this.getStartPoint();
		Point2d endPt = this.getEndPoint();
		double distanceFromStartPt = stPt.distance(pt);
		double distanceFromEndPt = endPt.distance(pt);
		if (distanceFromStartPt < distanceFromEndPt)
			return stPt;
		else if (distanceFromStartPt > distanceFromEndPt)
			return endPt;
		else
			return null;
	}

	public Point2d findCommonPoint(Curve2d otherCurve)
		throws InvalidIntersectionException {
		Point2d stPt = getStartPoint();
		Point2d endPt = getEndPoint();
		if (otherCurve.isPtOneOfEndPoints(stPt))
			return stPt;
		else if (otherCurve.isPtOneOfEndPoints(endPt))
			return endPt;
		else
			throw new InvalidIntersectionException(this
				+ "does not intersect"
				+ otherCurve);
	}

	public boolean isPtOneOfEndPoints(Point2d pt) {
		return (pt.epsilonEquals(getStartPoint(), GeometryConstants.EPSILON) || pt
			.epsilonEquals(getEndPoint(), GeometryConstants.EPSILON));
	}

	public boolean haveCommonPoint(Curve2d otherCurve) {
		try {
			findCommonPoint(otherCurve);
			return true;
		} catch (InvalidIntersectionException ex) {
			return false;
		}
	}

	public static int oppositeSide(int side) {
		int oppSide = 0;
		switch (side) {
			case Curve2d.LEFT_SIDE :
				oppSide = Curve2d.RIGHT_SIDE;
				break;
			case Curve2d.RIGHT_SIDE :
				oppSide = Curve2d.LEFT_SIDE;
				break;
		}
		return oppSide;
	}
	/*
	 *
	 *    method to return same object with reversed end points
	 *   
	 */

	public void swap() {
		Point2d temp = this.getEndPoint();
		this.setEndPoint(this.getStartPoint());
		this.setStartPoint(temp);
	}

	public abstract Point2d intersect(Curve2d otherCurve)
		throws InvalidIntersectionException;

	public abstract boolean isPointOnCurve(Point2d pt);

}
