package edu.ohiou.mfgresearch.labimp.bezier;

/**
 * <p>Title: Generic classes for manufacturing planning</p>
 * <p>Description: Thsi project implements general classes for intelligent manufacturing planning. These are:
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: Ohio University</p>
 * @author Dusan Sormaz
 * @version 1.0
 */

import java.util.*;
import java.util.stream.Collectors;

import javax.vecmath.GMatrix;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import java.awt.Color;
import java.awt.geom.*;

import edu.ohiou.mfgresearch.labimp.basis.Draw2DApplet;
import edu.ohiou.mfgresearch.labimp.basis.ViewObject;

public class BezierCurve extends ViewObject {

	List<Point2d> points = new LinkedList<Point2d>();
	// List<Vector2d> vecPoints = new LinkedList<Vector2d>();
	int order;
	int nsteps = 100;
	int pascalTriangle[][] = null;

	public BezierCurve(List<Point2d> points, int granularity, Color c) {
		this.points = new LinkedList<Point2d>(points);
		// this.vecPoints = points.stream()
		// .map(p->createVector2DfromPoint2d(p))
		// .collect(Collectors.toCollection(()->{
		// return new LinkedList<Vector2d>();
		// }));
		this.nsteps = granularity;
		this.order = points.size() - 1;
		color = c;
	}

	private Vector2d createVector2DfromPoint2d(Point2d p) {
		return new Vector2d(p.x, p.y);
	}

	@SuppressWarnings("unchecked")
	@Override
	public LinkedList getDrawList() {
		LinkedList list = new LinkedList();
		Point2d lastPoint = null;
		// calculate nSteps points on curve
		for (int i = 0; i < nsteps; i++) {
			// normalize t where 0 <= t <= 1
			double t = (double) i / (double) nsteps;
			// get the point on curve for t
			Point2d point = getPoint(t);
			System.out.println("(" + point.x + "," + point.y + ")");
			if (i == 0) {
				lastPoint = point;
			} else {
				// join the points successively to form the curve
				list.add(new Line2D.Double(lastPoint.x, lastPoint.y, point.x, point.y));
				lastPoint = point;
			}
		}
		for (Point2d p : points) {
			list.add(new Ellipse2D.Double(p.x, p.y, 0.1, 0.1));
		}
		return list;
	}

	@Override
	public LinkedList getFillList() {
		LinkedList list = new LinkedList();
		for (Point2d p : points) {
			list.add(new Ellipse2D.Double(p.x, p.y, 0.1, 0.1));
		}
		return list;
	}

	@Override
	public Collection giveSelectables() {
		// TODO Auto-generated method stub
		return points.stream().map(s -> {
			return new Point2D.Double(s.x, s.y);
		}).collect(Collectors.toList());
	}

	/**
	 * calculate the point on the curve for a given t
	 * 
	 * @param t
	 * @return point on he curve as Point2d
	 */
	public Point2d getPoint(double t) {

		double[] controls = new double[points.size()];

		for (int j = 0; j < points.size(); j++) {
			controls[j] = points.get(j).x;
		}
		 double x = getBezierByDeCasteljeau(order, controls, t);
		//double x = getBezierByMatrix(order, controls, t);
		for (int j = 0; j < points.size(); j++) {
			controls[j] = points.get(j).y;
		}
		 double y = getBezierByDeCasteljeau(order, controls, t);
//		double y = getBezierByMatrix(order, controls, t);
		return new Point2d(x, y);
	}

	/**
	 * De Caasteljeau algorithm by recursive function to calculate bezier value
	 * of order n, with n+1 control points and given t
	 * 
	 * @param order
	 *            order of Bernstein polynomial
	 * @param controls
	 *            set of control points of length order + 1
	 * @param t
	 *            given variable
	 * @return
	 */
	public double getBezierByDeCasteljeau(int order, double[] controls, double t) {
		if (order == 1) {
			return (1 - t) * controls[0] + t * controls[1];
		} else {
			int numControls = order;
			double[] controls1 = new double[numControls], controls2 = new double[numControls];
			System.arraycopy(controls, 0, controls1, 0, numControls);
			System.arraycopy(controls, 1, controls2, 0, numControls);
			return (1 - t) * getBezierByDeCasteljeau(order - 1, controls1, t)
					+ t * getBezierByDeCasteljeau(order - 1, controls2, t);
		}
	}

	/**
	 * get point on the Bezier curve for the specified values of t , control
	 * points and order this function calculates the point based on blending
	 * function matrix
	 * 
	 * @param order
	 * @param controls
	 * @param t
	 * @return
	 */
	public double getBezierByMatrix(double[] controls, double t) {

		// create the blending function for Bernstein polynomial
		Matrix4d blendFunc = null;
		try {
			blendFunc = getBlendingMatrix(order);
		} catch (Exception e) {
			// implement the blending function matrix for the desired order
			e.printStackTrace();
		}

		// create vector t = [1 t t^2 t^3 ... t^order]
		GMatrix tVector = new GMatrix(1, order + 1);
		for (int i = 0; i < order + 1; i++) {
			tVector.setElement(0, i, Math.pow(t, i));
		}

		// create matrix for control points (will be populated by x and y part
		// separately by the calling method)
		GMatrix vecControls = new GMatrix(order + 1, 1);
		vecControls.set(controls);

		// multiply to get the point on curve
		System.out.print(tVector + " * " + blendFunc + " * " + vecControls + " = ");
		//tVector.mul(blendFunc);
		System.out.print(tVector + " * " + vecControls + " = ");
		GMatrix bmat = new GMatrix(1, order + 1);
		bmat.set(tVector);
		bmat.mul(vecControls); // vecmath throws dimension mismatch error while
								// multiplying 1X4 and 4X1 matrices
		return tVector.getElement(0, 0);
	}

	/**
	 * returns a precoded triangular matrix as blending function for the given
	 * order if the matrix is not available then exception is thrown.
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public static Matrix4d getBlendingMatrix(int order) throws Exception {
		Matrix4d bernsteinPoly = new Matrix4d();
		switch (order) {
		case 2:
			bernsteinPoly.set(new Matrix3d(1, 0, 0, -2, 2, 0, 1, -2, 1));
			break;
		case 3:
			bernsteinPoly.set(new Matrix4d(1, 0, 0, 0, -3, 3, 0, 0, 3, -6, 3, 0, -1, 3, -3, 1));
			break;
		default:
			throw new Exception("Blending Matrix for " + order
					+ " is not implemented. Please implemnet it first in getBlendingMatrix method of Class BezierCurve");
		}
		return bernsteinPoly;
	}

	/**
	 * returns value of nCk (binomial) it creates a pascal triagle if not
	 * already created
	 * 
	 * @param n
	 * @param k
	 * @return
	 */
	// public double[] getBinomial(int n) {
	// // initialize pascal triangle
	// if (pascalTriangle == null) {
	// pascalTriangle = new int[n][];
	// }
	// // fill up the pascal triangle upto the order needed
	// if (pascalTriangle.length < n - 1) {
	// for (int i = 0; i < n; i++) {
	// int row[] = new int[i + 3];
	// if (i == 0) {
	// row[1] = 1; // rest are 0 by constructor
	// } else {
	// for (int j = 1; j <= i + 1; j++) {
	// row[j] = pascalTriangle[i - 1][j - 1] + pascalTriangle[i - 1][j];
	// }
	// }
	// pascalTriangle[i] = row;
	// }
	// return Arrays.stream(pascalTriangle[n]).asDoubleStream().toArray();
	// } else
	// return Arrays.stream(pascalTriangle[n]).asDoubleStream().toArray();
	// }

	public static void main(String[] args) {
		Point2d p1 = new Point2d(0, 0);
		Point2d p2 = new Point2d(1, 1);
		Point2d p3 = new Point2d(2, 1);
		Point2d p4 = new Point2d(3, 0);
		// Point2d p5 = new Point2d(5, 1);
		// Point2d p6 = new Point2d(8, 0);
		LinkedList<Point2d> list = new LinkedList<Point2d>();
		list.add(p1);
		list.add(p2);
		list.add(p3);
		list.add(p4);
		// list.add(p5);
		// list.add(p6);
		BezierCurve bc = new BezierCurve(list, 100, Color.MAGENTA);
		bc.settApplet(new Draw2DApplet(bc));
		bc.display();
	}

}
