package edu.ohiou.mfgresearch.labimp.gtk3d;

import java.awt.geom.Rectangle2D;
import edu.ohiou.mfgresearch.labimp.draw.ImpObject;
import javax.vecmath.Matrix4d;
import edu.ohiou.mfgresearch.labimp.draw.DrawWFPanel;
import javax.vecmath.Point2d;
import java.util.*;
import java.awt.geom.*;
import javax.swing.JList;
import javax.vecmath.*;
import edu.ohiou.mfgresearch.labimp.basis.*;
import edu.ohiou.mfgresearch.labimp.gtk2d.*;
import edu.ohiou.mfgresearch.labimp.draw.*;

public class Prof2d extends ImpObject {
	private List curves;
	private boolean isClosed;

	public static final int INNER_SIDE = -1;
	public static final int OUTER_SIDE = 1;
	public static final boolean CLOSED_PROFILE = true;
	public static final boolean OPEN_PROFILE = false;

	/*
	 *
	 *    TODO: implement method to orient the curves in the profile
	 *   
	 */

	public Prof2d() {
		this(new Curve2d[] {});
	}

	public Prof2d(Curve2d[] profileCurves) {
		curves = new LinkedList();
		for (int i = 0; i < profileCurves.length; i++) {
			addCurve(profileCurves[i]);
		}
	}

	public Prof2d(Rectangle2D rectangle) {
		double x = rectangle.getX(), y = rectangle.getY(), w = rectangle
			.getWidth(), h = rectangle.getHeight();
		Point2d vertex1 = new Point2d(x, y);
		Point2d vertex2 = new Point2d(x + w, y);
		Point2d vertex3 = new Point2d(x + w, y + h);
		Point2d vertex4 = new Point2d(x, y + h);
		try {
			addCurve(new Line2d(vertex1, vertex2));
			addCurve(new Line2d(vertex2, vertex3));
			addCurve(new Line2d(vertex3, vertex4));
			addCurve(new Line2d(vertex4, vertex1));
			isClosed = CLOSED_PROFILE;
		} catch (InvalidLineException ex) {
			//never happens
		}
	}

	public List getCurves() {
		return curves;
	}

	public int getNumberOfCurves() {
		return curves.size();
	}

	public void addCurve(Curve2d curve) {
		curves.add(curve);
	}

	public Curve2d getCurve(int index) {
		return (Curve2d) curves.get(index);
	}

	public boolean removeCurve(Curve2d c) {
		boolean removedCurve = false;
		for (int i = 0; i < getNumberOfCurves(); i++) {
			Curve2d curve = (Curve2d) curves.get(i);
			if (curve instanceof Line2d && c instanceof Line2d) {
				if (((Line2d) c).epsilonEquals(
					(Line2d) curve,
					GeometryConstants.EPSILON)) {
					curves.remove(i);
					removedCurve = true;
					break;
				}
			} else if (curve instanceof Arc2d && c instanceof Arc2d) {
				if (((Arc2d) c).epsilonEquals(
					(Arc2d) curve,
					GeometryConstants.EPSILON)) {
					curves.remove(i);
					removedCurve = true;
					break;
				}
			}
		}
		return removedCurve;
	}

	public void replaceCurve(int index, Curve2d c) {
		curves.remove(index);
		curves.add(index, c);
	}

	public Iterator getIterator() {
		return curves.iterator();
	}

	public boolean isClosedProfile() {
		int curvesNo = getNumberOfCurves();
		if (((Curve2d) curves.get(curvesNo - 1))
			.haveCommonPoint((Curve2d) curves.get(0))) {
			return CLOSED_PROFILE;
		}
		return OPEN_PROFILE;
	}

	public ListIterator getListIterator() {
		return curves.listIterator();
	}

	//Gets the list of traversal points .. if its open profile, the list has
	// of curves should start with starting point of the first curve
	protected List getPoints() {
		List points = new LinkedList();
		int curvesNo = getNumberOfCurves();
		Point2d traversalPoint;
		try {
			traversalPoint = ((Curve2d) curves.get(curvesNo - 1))
				.findCommonPoint((Curve2d) curves.get(0));
		} catch (InvalidIntersectionException ex) {
			traversalPoint = ((Curve2d) curves.get(0)).getStartPoint();
		}
		for (Iterator iter = getIterator(); iter.hasNext();) {
			Curve2d c = (Curve2d) iter.next();
			//      System.out.println("c --->"+c);
			//      System.out.println("traversalpoint --->"+traversalPoint);
			if (traversalPoint.epsilonEquals(
				c.getEndPoint(),
				GeometryConstants.EPSILON)) {
				points.add(c.getEndPoint());
				traversalPoint = c.getStartPoint();
			} else {
				points.add(c.getStartPoint());
				traversalPoint = c.getEndPoint();
			}
			//      System.out.println("points --->"+points);
		}
		return points;
	}

	public double getBoundingArea() {
		Rectangle2D rectangle = getBoundingBox();
		return rectangle.getWidth() * rectangle.getHeight();
	}

	public Rectangle2D getBoundingBox() {
		List traversalPoints = getPoints();
		double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
		for (Iterator itr = traversalPoints.iterator(); itr.hasNext();) {
			Point2d p = (Point2d) itr.next();
			maxX = p.x > maxX ? p.x : maxX;
			maxY = p.y > maxY ? p.y : maxY;
			minX = p.x < minX ? p.x : minX;
			minY = p.y < minY ? p.y : minY;
		}
		return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
	}

	private Prof2d computeOffsetCurves(double offsetDistance, int offsetSide)
		throws UnsupportedOperationException {
//		this.setApplet(new Draw2DApplet (this));
//		this.display();
		Prof2d offsetProfile = new Prof2d();
		if (!areProfileLinesTangent()) {
			throw new UnsupportedOperationException(
				"Computation of offset for non-tangent profiles"
					+ " is not supported yet");
		} else {
			List traversalPoints = getPoints();
			//    System.out.println("traversalpoints -->"+traversalPoints);
			Iterator ptsIter = traversalPoints.iterator();
			for (Iterator iter = curves.iterator(); iter.hasNext();) {
				//      System.out.println("offsetprofile-->"+offsetProfile);
				Curve2d c = (Curve2d) iter.next();
				//      System.out.println("c -->"+c);
				Point2d traversalPoint = (Point2d) ptsIter.next();
				//      System.out.println("traversal pt -->"+traversalPoint);
				try {
					if (traversalPoint.epsilonEquals(
						c.getEndPoint(),
						GeometryConstants.EPSILON))
						offsetProfile.addCurve(c.offset(offsetDistance, Curve2d
							.oppositeSide(offsetSide)));
					else
						offsetProfile.addCurve(c.offset(
							offsetDistance,
							offsetSide));
				} catch (InvalidCurveException ex) {
					continue;
				}
			}
		}
		return offsetProfile;
	}

	/**
	 * Returns Prof2d which is offset at a distance of 'offset' either
	 * on the inner side or outer side of this. 'offsetSide' being defined
	 * using two class variables INNER_SIDE and OUTER_SIDE.
	 *
	 * @param offset
	 * @return an offset Prof2d
	 */
	public Prof2d offset(double offsetDistance, int offsetSide) {
		Prof2d offsetProfile = new Prof2d();
		switch (offsetSide) {
			case Prof2d.INNER_SIDE :
				offsetProfile = computeOffsetCurves(
					offsetDistance,
					Curve2d.LEFT_SIDE);
				break;
			case Prof2d.OUTER_SIDE :
				offsetProfile = computeOffsetCurves(
					offsetDistance,
					Curve2d.RIGHT_SIDE);
				break;
		}
		int offsetCurvesNo = offsetProfile.getNumberOfCurves();
		isClosed = isClosedProfile();
		if (!isClosed)
			offsetCurvesNo = offsetProfile.getNumberOfCurves() - 1;
		for (int i = 0; i < offsetCurvesNo; i++) {
			Curve2d offsetCurve = (Curve2d) offsetProfile.getCurves().get(i);
			Curve2d nextOffsetCurve = (Curve2d) offsetProfile.getCurves().get(
				(i + 1) % offsetCurvesNo);
			if (!isClosed)
				nextOffsetCurve = (Curve2d) offsetProfile.getCurves()
					.get(i + 1);
			//      System.out.println("size -->" + ((i + 1) % offsetCurvesNo));
			//      System.out.println("offsetcurve-->" + offsetCurve);
			//      System.out.println("nextoffset curve-->" + nextOffsetCurve);
			if (!offsetCurve.haveCommonPoint(nextOffsetCurve)) {
				try {
					Point2d intersectionPt = offsetCurve
						.intersect(nextOffsetCurve);
					//          System.out.println("intsnpt -->" + intersectionPt);
					offsetCurve.setEndPoint(intersectionPt);
					nextOffsetCurve.setStartPoint(intersectionPt);
				} catch (InvalidIntersectionException ex) {
					ex.printStackTrace();
				}
			}
		}
		return offsetProfile;
	}

	private boolean areProfileLinesTangent() {
		System.out
			.println("-------------Prof2d areProfileLinesTangent in here??? ----------------");
		Prof2d prof = new Prof2d();
		for (Iterator profileIter = curves.iterator(); profileIter.hasNext();) {
			Curve2d c = (Curve2d) profileIter.next();
			if (c instanceof Line2d)
				prof.addCurve(c);
		}
		for (int j = 0; j < prof.getNumberOfCurves(); j++) {
			Line2d c1 = (Line2d) prof.getCurve(j);
			Line2d c2 = (Line2d) prof.getCurve((j + 1)
				% prof.getNumberOfCurves());
			System.out.println("angle betweens " + j + "  -->>>>" + c1.angle(c2));
			System.out.println("-->>>>, c1: " + c1.toString());
			System.out.println("-->>>>, c2: " + c2.toString());
			// dsormaz added code to cover pi-half and 3pi-half
			if (!Gtk.epsilonEquals(c1.angle(c2),Math.PI / 2) || 
					!Gtk.epsilonEquals(c1.angle(c2),Math.PI / 2))
				return false;
		}
		return true;
	}

	public boolean isPointOnProfile(Point2d pt) {
		for (Iterator profileIter = curves.iterator(); profileIter.hasNext();) {
			Curve2d c = (Curve2d) profileIter.next();
			if (c.isPointOnCurve(pt)) {
				return true;
			}
		}
		return false;
	}

	public Profile3d getProfile3d(Matrix4d transformationMatrix) {
		Profile3d prof3d = new Profile3d();
		for (Iterator profileIter = curves.iterator(); profileIter.hasNext();) {
			Curve2d c = (Curve2d) profileIter.next();
			prof3d.addShape((CurveSegment) c
				.getCurveSegment(transformationMatrix));
		}
		return prof3d;
	}

	/**
	 * @todo followProfile is for generating a list of curves along the profile
	 * which lie inbetween the pts .. this is for zigzagpath .. incomplete method
	 * @param startPt
	 * @param endPt
	 * @return
	 * @throws java.lang.IllegalArgumentException
	 */
	public List followProfile(Point2d startPt, Point2d endPt)
		throws IllegalArgumentException {
		if (!isPointOnProfile(startPt)) {
			throw new IllegalArgumentException(startPt + " is not on Profile");
		}
		if (!isPointOnProfile(endPt)) {
			throw new IllegalArgumentException(endPt + " is not on Profile");
		}
		List curvesList = new ArrayList();
		for (Iterator profileIter = curves.iterator(); profileIter.hasNext();) {
			Curve2d c = (Curve2d) profileIter.next();
			//      if(c.isPointOnCurve(pt))

		}
		return null;
	}

	//intersection pts arranged w.r.t comparing point
	public Set intersect(Line2d line, Point2d comparingPoint) {
		Set intsnPtsSet = new TreeSet(new Pt2dComparator(
			comparingPoint,
			GeometryConstants.EPSILON));
		//    System.out.println("------------");
		//    System.out.println("line-->"+line);
		for (Iterator profileIter = curves.iterator(); profileIter.hasNext();) {
			Curve2d c = (Curve2d) profileIter.next();
			//      System.out.println("curve-->"+c);
			if (line.intersectsCurveSegment(c)) {
				try {
					intsnPtsSet.add(line.intersectCurveSegment(c));
					//          System.out.println("intsnPtsSet--->"+intsnPtsSet);
				} catch (InvalidIntersectionException ex) {
					//should never happen because of if condition
					ex.printStackTrace();
				}
			}
		}
		return intsnPtsSet;
	}

	//@todo Create Origin2d in geometry constants and replace 1st line with that
	public Set intersect(Line2d line) {
		return intersect(line, new Point2d(0, 0));
	}

	//Returns the point with the least y-coordinate in the profile
	public Point2d lowestPoint() {
		List traversalPoints = getPoints();
		Point2d lowestPoint = (Point2d) traversalPoints.get(0);
		for (Iterator it = traversalPoints.iterator(); it.hasNext();) {
			Point2d pt = (Point2d) it.next();
			if (pt.y < lowestPoint.y) {
				lowestPoint = pt;
			}
		}
		return lowestPoint;
	}

	//Returns the point with the least x-coordinate in the profile
	public Point2d leftMostPoint() {
		List traversalPoints = getPoints();
		Point2d leftMostPoint = (Point2d) traversalPoints.get(0);
		for (Iterator it = traversalPoints.iterator(); it.hasNext();) {
			Point2d pt = (Point2d) it.next();
			if (pt.x < leftMostPoint.x) {
				leftMostPoint = pt;
			}
		}
		return leftMostPoint;
	}

	public double[] getX() {
		List pointSet = getPoints();
		int nPoints = pointSet.size();
		double[] xPoints = new double[nPoints];
		int i = 0;
		// Populating arrays with x and y coordinates of points.
		for (ListIterator itr = pointSet.listIterator(); itr.hasNext();) {
			Point2d currentPoint = (Point2d) itr.next();
			xPoints[i] = currentPoint.x;
			i++;
		}
		return xPoints;
	}

	public double[] getY() {
		List pointSet = getPoints();
		int nPoints = pointSet.size();
		double[] yPoints = new double[nPoints];
		int i = 0;
		// Populating arrays with x and y coordinates of points.
		for (ListIterator itr = pointSet.listIterator(); itr.hasNext();) {
			Point2d currentPoint = (Point2d) itr.next();
			yPoints[i] = currentPoint.y;
			i++;
		}
		return yPoints;
	}

	//Returns the point with the highest x-coordinate in the profile
	public Point2d rightMostPoint() {
		List traversalPoints = getPoints();
		Point2d rightMostPoint = (Point2d) traversalPoints.get(0);
		for (Iterator it = traversalPoints.iterator(); it.hasNext();) {
			Point2d pt = (Point2d) it.next();
			if (pt.x > rightMostPoint.x) {
				rightMostPoint = pt;
			}
		}
		return rightMostPoint;
	}

	//Returns the point with the highest y-coordinate in the profile
	public Point2d highestPoint() {
		List traversalPoints = getPoints();
		Point2d highestPoint = (Point2d) traversalPoints.get(0);
		for (Iterator it = traversalPoints.iterator(); it.hasNext();) {
			Point2d pt = (Point2d) it.next();
			if (pt.y > highestPoint.y) {
				highestPoint = pt;
			}
		}
		return highestPoint;
	}

	public void init() {
		panel = new ViewPanel();
		panel.add(new JList(curves.toArray()));
	}

	public boolean isPointAVertex(Point2d pt) {
		List pointSet = getPoints();
		for (ListIterator itr = pointSet.listIterator(); itr.hasNext();) {
			Point2d currentPoint = (Point2d) itr.next();
			if (currentPoint.epsilonEquals(pt, GeometryConstants.EPSILON)) {
				return true;
			}
		}
		return false;
	}

	public LinkedList getDrawList() {
		LinkedList shapeList = new LinkedList();
		for (int i = 0; i < curves.size(); i++) {
			LinkedList shapes = ((Curve2d) curves.get(i)).getDrawList();
			shapeList.addAll(shapes);
		}
		return shapeList;
	}

	public LinkedList getShapeList(DrawWFPanel canvas) {
		return getProfile3d(
			new Matrix4d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1))
			.getShapeList(canvas);
	}

	/**
	 * implemented by Chandu to show numbering of curves
	 */
	public LinkedList getStringList() {
		LinkedList strings = new LinkedList();
		String curveStr = new String();
		for (int i = 0; i < curves.size(); i++) {
			Curve2d currentCurve = (Curve2d) curves.get(i);
			Float xPos = new Float(
				(currentCurve.getStartPoint().x + currentCurve.getEndPoint().x) / 2.0);
			Float yPos = new Float(
				(currentCurve.getStartPoint().y + currentCurve.getEndPoint().y) / 2.0);
			String content = new String("Curve " + i);
			strings.add(new DrawString(content, xPos.floatValue(), yPos
				.floatValue()));
		}
		return strings;
	}

	//  public LinkedList getShapeList (DrawWFPanel canvas) {
	//    LinkedList shapes = new LinkedList();
	//    int size = curves.size();
	//    for (int i = 0; i < size; i++) {
	//      Point2d p1 =  (Point2d) points.get(i);
	//      Point2d p2 = (Point2d) points.get( (i+1)% size);
	//      LineSegment line = new LineSegment ( new Point3d (p1.getX(), p1.getY(), 0), new Point3d (p2.getX(), p2.getY(), 0) );
	//      shapes.addAll(line.getShapeList(canvas));
	//    }
	//    return shapes;
	//  }

	public String toString() {
		return "Prof2d ->" + curves.toString();
	}

	public static double areaTwo(Point2d point, Line2d line) {
		return areaTwo(line.getStartPoint(), line.getEndPoint(), point);
	}

	public static double areaTwo(Point2d pointA, Point2d pointB, Point2d pointC) {
		double a0, a1, b0, b1, c0, c1;
		a0 = pointA.x;
		a1 = pointA.y;
		b0 = pointB.x;
		b1 = pointB.y;
		c0 = pointC.x;
		c1 = pointC.y;
		return a0 * b1 - a1 * b0 + a1 * c0 - a0 * c1 + b0 * c1 - b1 * c0;
	}

	public double areaTwo() {
		double area = 0.0;
		List traversalPoints = getPoints();
		for (int i = 1; i < curves.size() - 1; i++) {
			area += areaTwo(
				(Point2d) traversalPoints.get(0),
				(Point2d) traversalPoints.get(i),
				(Point2d) traversalPoints.get(i + 1));
		}
		return area;
	}

	public boolean left(Point2d point, Line2d line) {
		return (areaTwo(point, line) < -GeometryConstants.EPSILON)
			? true
			: false;
	}

	public boolean collinear(Point2d point, Line2d line) {
		return (Math.abs(areaTwo(point, line)) < GeometryConstants.EPSILON)
			? true
			: false;
	}

	//Test case for ToolPathGenerator .. variant oriented arcs
	public static Prof2d testCase1() {
		try {
			Curve2d c1 = new Line2d(new Point2d(0, 2), new Point2d(7, 2));
			Curve2d c2 = new Arc2d(
				new Point2d(7, 4),
				2,
				3 * Math.PI / 2,
				2 * Math.PI);
			Curve2d c3 = new Line2d(new Point2d(9, 4), new Point2d(9, 8));
			Curve2d c4 = new Arc2d(new Point2d(11, 8), 2, Math.PI / 2, Math.PI);
			Curve2d c5 = new Line2d(new Point2d(11, 10), new Point2d(13, 10));
			Curve2d c6 = new Line2d(new Point2d(13, 10), new Point2d(13, 14));
			Curve2d c7 = new Line2d(new Point2d(13, 14), new Point2d(0, 14));
			Curve2d c8 = new Line2d(new Point2d(0, 14), new Point2d(0, 2));
			Curve2d[] curves = new Curve2d[8];
			curves[0] = c1;
			curves[1] = c2;
			curves[2] = c3;
			curves[3] = c4;
			curves[4] = c5;
			curves[5] = c6;
			curves[6] = c7;
			curves[7] = c8;
			return new Prof2d(curves);
		} catch (InvalidArcException ex) {
			ex.printStackTrace();
		} catch (InvalidLineException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	//Test case for ToolPathGenerator .. open-pocket
	public static Prof2d testCase2() {
		try {
			Curve2d c1 = new Line2d(new Point2d(0, 2), new Point2d(7, 2));
			Curve2d c2 = new Arc2d(
				new Point2d(7, 4),
				2,
				3 * Math.PI / 2,
				2 * Math.PI);
			Curve2d c3 = new Line2d(new Point2d(9, 4), new Point2d(9, 8));
			Curve2d c4 = new Arc2d(new Point2d(7, 8), 2, 0, Math.PI / 2);
			Curve2d c5 = new Line2d(new Point2d(7, 10), new Point2d(0, 10));
			Curve2d c6 = new Line2d(new Point2d(0, 10), new Point2d(0, 2));
			Curve2d[] curves = new Curve2d[6];
			curves[0] = c1;
			curves[1] = c2;
			curves[2] = c3;
			curves[3] = c4;
			curves[4] = c5;
			curves[5] = c6;
			return new Prof2d(curves);
		} catch (InvalidArcException ex) {
			ex.printStackTrace();
		} catch (InvalidLineException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	//Test case for ToolPathGenerator .. no arcs
	public static Prof2d testCase3() {
		try {
			Curve2d c1 = new Line2d(new Point2d(0, 2), new Point2d(6, 2));
			Curve2d c2 = new Line2d(new Point2d(6, 2), new Point2d(6, 6));
			Curve2d c3 = new Line2d(new Point2d(6, 6), new Point2d(8, 6));
			Curve2d c4 = new Line2d(new Point2d(8, 6), new Point2d(8, 4));
			Curve2d c5 = new Line2d(new Point2d(8, 4), new Point2d(11, 4));
			Curve2d c6 = new Line2d(new Point2d(11, 4), new Point2d(11, 16));
			Curve2d c7 = new Line2d(new Point2d(11, 16), new Point2d(0, 16));
			Curve2d c8 = new Line2d(new Point2d(0, 16), new Point2d(0, 2));
			Curve2d[] curves = new Curve2d[8];
			curves[0] = c1;
			curves[1] = c2;
			curves[2] = c3;
			curves[3] = c4;
			curves[4] = c5;
			curves[5] = c6;
			curves[6] = c7;
			curves[7] = c8;
			return new Prof2d(curves);
		} catch (InvalidLineException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	//Test case for ToolPathGenerator ..no arcs
	public static Prof2d testCase4() {
		try {
			Curve2d c1 = new Line2d(new Point2d(0, 2), new Point2d(8, 2));
			Curve2d c2 = new Line2d(new Point2d(8, 2), new Point2d(8, 8));
			Curve2d c3 = new Line2d(new Point2d(8, 8), new Point2d(0, 8));
			Curve2d c4 = new Line2d(new Point2d(0, 8), new Point2d(0, 2));
			Curve2d[] curves = new Curve2d[4];
			curves[0] = c1;
			curves[1] = c2;
			curves[2] = c3;
			curves[3] = c4;
			return new Prof2d(curves);
		} catch (InvalidLineException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	//Test case for ToolPathGenerator ..no arcs
	public static Prof2d testCase5() {
		try {
			Curve2d c1 = new Line2d(new Point2d(5, 2), new Point2d(8, 5));
			Curve2d c2 = new Line2d(new Point2d(8, 5), new Point2d(5, 8));
			Curve2d c3 = new Line2d(new Point2d(5, 8), new Point2d(2, 5));
			Curve2d c4 = new Line2d(new Point2d(2, 5), new Point2d(5, 2));
			Curve2d[] curves = new Curve2d[4];
			curves[0] = c1;
			curves[1] = c2;
			curves[2] = c3;
			curves[3] = c4;
			return new Prof2d(curves);
		} catch (InvalidLineException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	//Test case in 3d .. transformed to 2d ..no arcs
	public static Prof2d testCase6() {
		try {
			Curve2d c1 = new Line2d(new Point2d(-5.0, -4.0), new Point2d(
				-2.0,
				-4.0));
			Curve2d c2 = new Line2d(new Point2d(-2.0, -4.0), new Point2d(
				-2.0,
				-1.0));
			Curve2d c3 = new Line2d(new Point2d(-2.0, -1.0), new Point2d(
				-5.0,
				-1.0));
			Curve2d[] curves = new Curve2d[3];
			curves[0] = c1;
			curves[1] = c2;
			curves[2] = c3;
			return new Prof2d(curves);
		} catch (InvalidLineException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	//Test case in 3d .. transformed to 2d ..no arcs
	public static Prof2d testCase7() {
		try {
			Curve2d c1 = new Line2d(new Point2d(-5.0, -4.0), new Point2d(
				-3.0,
				-4.0));
			Curve2d c2 = new Line2d(new Point2d(-3.0, -4.0), new Point2d(
				-2.0,
				-1.0));
			Curve2d c3 = new Line2d(new Point2d(-2.0, -1.0), new Point2d(
				-5.0,
				-1.0));
			Curve2d c4 = new Line2d(new Point2d(-5.0, -1.0), new Point2d(
				-5.0,
				-4.0));
			Curve2d[] curves = new Curve2d[4];
			curves[0] = c1;
			curves[1] = c2;
			curves[2] = c3;
			curves[3] = c4;
			return new Prof2d(curves);
		} catch (InvalidLineException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		Prof2d profile = Prof2d.testCase7();
		profile.settApplet(new Draw2DApplet(profile));
		profile.display();
		Prof2d off = profile.offset(2, Prof2d.INNER_SIDE);
		off.settApplet(new Draw2DApplet(off));
		off.display();
		System.out.println("offset--->" + off);
	}
}
