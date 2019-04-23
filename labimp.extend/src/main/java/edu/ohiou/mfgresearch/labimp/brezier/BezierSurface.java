package edu.ohiou.mfgresearch.labimp.brezier;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

import edu.ohiou.mfgresearch.labimp.draw.DrawWFApplet;
import edu.ohiou.mfgresearch.labimp.draw.DrawWFPanel;
import edu.ohiou.mfgresearch.labimp.draw.ImpObject;
import edu.ohiou.mfgresearch.labimp.gtk3d.LineSegment;
import edu.ohiou.mfgresearch.labimp.gtk3d.WorldCS;

public class BezierSurface extends ImpObject {

	Point3d[][] controlPoints;
	// List<Vector2d> vecPoints = new LinkedList<Vector2d>();
	int order;
	int nsteps = 100;
	int pascalTriangle[][] = null;

	public BezierSurface(int order, Point3d[][] points, int granularity, Color c) {

		controlPoints = new Point3d[order + 1][order + 1];
		controlPoints = points;
		this.nsteps = granularity;
		this.order = order;
		color = c;
	}

	public void setControlPoints(Point3d[][] points) {
		this.controlPoints = points;
	}

//	@Override
//	public LinkedList<?> getShapeList(DrawWFPanel canvas) {
//
//		LinkedList<?> shapes = getControlPatch(canvas);
//
//		shapes.addAll(getSurfaceMesh(canvas));
//
//		return shapes;
//	}

	@Override
	public LinkedList<Point3d> getPointSet() {

		LinkedList<Point3d> shapes = new LinkedList<Point3d>();
		for (int i = 0; i < order + 1; i++) {
			for (int j = 0; j < order + 1; j++) {
				shapes.add(controlPoints[i][j]);
			}
		}
		return shapes;
	}
	
	

	@Override
	public void makeShapeSets(DrawWFPanel canvas) {
		canvas.addDrawShapes(Color.red, getSurfaceMesh(canvas));
		canvas.addDrawShapes(Color.lightGray, getControlPatch(canvas));
	}

	/**
	 * draw the 16 point control patch with grid mesh
	 * 
	 * @param canvas
	 * @return
	 */
	private LinkedList getControlPatch(DrawWFPanel canvas) {
		LinkedList shapes = new LinkedList();
		Point3d lp = null;
		// draw the control point patch
		for (int i = 0; i < order + 1; i++) {
			for (int j = 0; j < order + 1; j++) {
				// row wise segments
				if (j > 0) {
					LineSegment l = new LineSegment(lp, controlPoints[i][j]);
					l.settColor(Color.lightGray);
					shapes.addAll(l.geetShapeList(canvas));
				}
				//columnwise segment
				if (i > 0) {
					LineSegment l = new LineSegment(controlPoints[i - 1][j], controlPoints[i][j]);
					l.settColor(Color.lightGray);
					shapes.addAll(l.geetShapeList(canvas));
				}
				lp = controlPoints[i][j];
			}
		}
		return shapes;
	}

	private LinkedList getSurfaceMesh(DrawWFPanel canvas) {
		LinkedList shapes = new LinkedList();
		Point3d lp = null;
		Point3d[][] points = getSurfacePointSet();
		// draw the control point patch
		for (int i = 0; i <= nsteps; i++) {
			for (int j = 0; j <= nsteps; j++) {
				// row wise segments
				if (j > 0) {
					LineSegment l = new LineSegment(lp, points[i][j]);
					l.settColor(Color.BLACK);
					shapes.addAll(l.geetShapeList(canvas));
				}
				//columnwise segment
				if (i > 0) {
					LineSegment l = new LineSegment(points[i - 1][j], points[i][j]);
					l.settColor(Color.BLACK);
					shapes.addAll(l.geetShapeList(canvas));
					//triangulation
					if (j > 0) {
						LineSegment l1 = new LineSegment(points[i - 1][j - 1], points[i][j]);
						l1.settColor(Color.BLACK);
						shapes.addAll(l1.geetShapeList(canvas));
					}
				}
				lp = points[i][j];
			}
		}

		return shapes;
	}

	private Point3d[][] getSurfacePointSet() {

		Point3d[][] points = new Point3d[nsteps+1][nsteps+1];
		double u,v;
		for (int i = 0; i <= nsteps; i++) {
			for (int j = 0; j <= nsteps; j++) {
				u = (double)i/(double)nsteps;
				v = (double)j/(double)nsteps;
				points[i][j] = getBezierPoint(u, v);
			}
		}

		return points;
	}

	public Point3d getBezierPoint(double u, double v) {
		Point3d point = new Point3d();
		double[] xuControls = new double[order + 1];
		double[] yuControls = new double[order + 1];
		double[] zuControls = new double[order + 1];
		double[] xvControls = new double[order + 1];
		double[] yvControls = new double[order + 1];
		double[] zvControls = new double[order + 1];

		for (int i = 0; i < order + 1; i++) {
			for (int j = 0; j < order + 1; j++) {
				xuControls[j] = controlPoints[i][j].x;
				yuControls[j] = controlPoints[i][j].y;
				zuControls[j] = controlPoints[i][j].z;
			}
			xvControls[i] = getBezierByDeCasteljeau(order, xuControls, u);
			yvControls[i] = getBezierByDeCasteljeau(order, yuControls, u);
			zvControls[i] = getBezierByDeCasteljeau(order, zuControls, u);
		}

		point.x = getBezierByDeCasteljeau(order, xvControls, v);
		point.y = getBezierByDeCasteljeau(order, yvControls, v);
		point.z = getBezierByDeCasteljeau(order, zvControls, v);

		return point;
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

	public static void main(String[] args) {

		// control points
		Point3d[][] cPoints = new Point3d[4][4];
		cPoints[0][0] = new Point3d(0, 0, 1);
		cPoints[0][1] = new Point3d(0, 1, 1);
		cPoints[0][2] = new Point3d(0, 2, 1);
		cPoints[0][3] = new Point3d(0, 3, 1);
		cPoints[1][0] = new Point3d(1, 0, 1);
		cPoints[1][1] = new Point3d(1, 1, 2);
		cPoints[1][2] = new Point3d(1, 2, 3);
		cPoints[1][3] = new Point3d(1, 3, 1);
		cPoints[2][0] = new Point3d(2, 0, 1);
		cPoints[2][1] = new Point3d(2, 1, 2);
		cPoints[2][2] = new Point3d(2, 2, 3);
		cPoints[2][3] = new Point3d(2, 3, 1);
		cPoints[3][0] = new Point3d(3, 0, 1);
		cPoints[3][1] = new Point3d(3, 1, 1);
		cPoints[3][2] = new Point3d(3, 2, 1);
		cPoints[3][3] = new Point3d(3, 3, 1);

		BezierSurface surface = new BezierSurface(3, cPoints, 100, Color.GREEN);

		surface.settApplet(new DrawWFApplet(surface));
		surface.display("Bezier Surface");
	}

}