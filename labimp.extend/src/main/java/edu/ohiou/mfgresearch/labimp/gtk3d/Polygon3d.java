package edu.ohiou.mfgresearch.labimp.gtk3d;

import java.util.LinkedList;
import edu.ohiou.mfgresearch.labimp.basis.ViewObject.ViewPanel;
import edu.ohiou.mfgresearch.labimp.draw.ImpObject;
import javax.vecmath.Point3d;
import java.awt.Graphics2D;
import edu.ohiou.mfgresearch.labimp.draw.DrawWFPanel;
import java.util.*;
import java.awt.geom.Line2D;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.vecmath.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.*;

import edu.ohiou.mfgresearch.labimp.draw.*;
import edu.ohiou.mfgresearch.labimp.basis.*;
import edu.ohiou.mfgresearch.labimp.gtk2d.*;

/**
 *
 *  Class to represent bounded surfaces of a geometrical entity.
 *  For example: stock faces, cutting surface generated while machining
 * 
 */

public class Polygon3d extends ImpObject {
	private LinkedList pointSet;

	//  static Polygon3dPanel staticPanel;
	//private static Polygon3d guiPolygon;

	public Polygon3d() {
		pointSet = new LinkedList();
		/** @todo dnsormaz commented out 10/28/03 to allow deepak to work */
		//    color = Color.red;
	}

	public Polygon3d(int mode) {
		pointSet = new LinkedList();
		createPolygon();
	}

	public Polygon3d(LinkedList inPointSet) {
		pointSet = inPointSet;
	}

	/**
	 * Copy Constructor
	 */
	public Polygon3d(Polygon3d inPolygon3d) {
		pointSet = new LinkedList(inPolygon3d.getPointSet());
	}

	public void createPolygon() {
		Polygon3dPanel panel = new Polygon3dPanel(this);
		panel.settApplet(new GUIApplet(panel));
		panel.display("Polygon3d Creation Panel", new Dimension(600, 400));
		while (!isValid()) {
			// ImpObject.doNothing(staticPanel,"polygon is null");
		}
		panel.geettApplet().appletFrame.dispose();
		panel.geettApplet().destroy();
		//    return panel.polygon;
	}

	public static Polygon3d staticCreatePolygon() {
		Polygon3dPanel staticPanel = new Polygon3dPanel();
		staticPanel.settApplet(new GUIApplet(staticPanel));
		staticPanel.display("Polygon3d Creation Panel from static method");
		while ((guiObject == null) || !((Polygon3d) guiObject).isValid()) {
			// ImpObject.doNothing(staticPanel,"polygon is null");
		}
		staticPanel.geettApplet().appletFrame.dispose();
		staticPanel.geettApplet().destroy();
		return (Polygon3d) guiObject;
	}

	private boolean isValid() {
		if (this.pointSet.size() > 2)
			return true;
		return false;
	}

	//Modifier methods
	/**
	 * Method to add point to Polygon3d's point set.
	 */
	public Polygon3d addPoint(Point3d inPoint) {
		pointSet.add(inPoint);
		return this;
	}

	//Accessor methods
	/**
	 * Method to return point set of a Polygon3d.
	 */
	public LinkedList getPointSet() {
		return pointSet;
	}

	/**
	 * Method to return an indexed point from the Polygon3d.
	 */
	public Point3d getPoint(int index) {
		return (Point3d) this.pointSet.get(index);
	}

	/**
	 * Method to return index of a point in the given Polygon3d
	 */
	public int getIndexOf(Point3d testPoint) {
		int i = 0;
		for (; i < this.getPointSet().size(); i++) {
			if (getPoint(i).equals(testPoint))
				break;
		}
		return i;
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
	private boolean pointInPolygon2D(
		double xp[],
		double yp[],
		double x,
		double y) {
		// Variable declarations.
		int polySides = xp.length; // polySides = number of vertices of polygon.
		boolean inside = false;

		if (polySides < 3)
			return false; // Testing for polygon. (atleast 3 sides)
		if (pointOnBoundary2D(xp, yp, x, y))
			return true; //Point on polygon "boundary".
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
	 * Private method to determine if point is within range of points of polygon.
	 * @param point
	 * @param polygon
	 * @return
	 */
	private boolean isInRange(Point3d point) {
		Point3d polyPoint;
		TreeSet x = new TreeSet();
		TreeSet y = new TreeSet();
		TreeSet z = new TreeSet();
		boolean positive, negative;
		for (Iterator i = this.getPointSet().iterator(); i.hasNext();) {
			polyPoint = (Point3d) i.next();
			x.add(new Double(polyPoint.x));
			y.add(new Double(polyPoint.y));
			z.add(new Double(polyPoint.z));
		}
		positive = (point.x > ((Double) x.last()).doubleValue()
			|| point.y > ((Double) y.last()).doubleValue() || point.z > ((Double) z
			.last()).doubleValue());
		negative = (point.x < ((Double) x.first()).doubleValue()
			|| point.y < ((Double) y.first()).doubleValue() || point.z < ((Double) z
			.first()).doubleValue());
		return !(positive || negative);
	}

	/**
	 * Method to find if point (x,y) is on boundary of polygon.
	 *
	 */
	private boolean pointOnBoundary2D(
		double xp[],
		double yp[],
		double x,
		double y) {
		// Variable declarations.
		int polySides = xp.length; // polySides = number of vertices of polygon.
		Line2D.Double polyEdge;

		// Create Line2D.Double for all edges of polygon and check if test point
		// lies on the edge of polygon.
		for (int i = 0; i < polySides - 1; i++) {
			polyEdge = new Line2D.Double(xp[i], yp[i], xp[i + 1], yp[i + 1]);
			if (polyEdge.relativeCCW(x, y) == 0)
				return true;
		}
		// Line joining first and last vertex of polygon.
		polyEdge = new Line2D.Double(
			xp[0],
			yp[0],
			xp[polySides - 1],
			yp[polySides - 1]);
		return (polyEdge.relativeCCW(x, y) == 0);
	}

	/**
	 * Method to check a point lies inside or outside given Polygon3d.
	 * Returns true if the point lies inside the given Polygon3d and false if the
	 * point lies outside the given Polygon3d.
	 */
	public boolean isPointInsidePolygon3d(Point3d testPoint) {
		if (!isInRange(testPoint))
			return false;
		try {
			Plane polygonPlane = getPlane();
			boolean xz = false, yz = false;
			Vector3d testParallel = new Vector3d();
			// Parallel to XZ-plane
			testParallel.cross(
				polygonPlane.getNormal(),
				GeometryConstants.Y_VECTOR);
			if (testParallel.epsilonEquals(
				new Vector3d(0, 0, 0),
				GeometryConstants.EPSILON)) {
				//System.out.println("Polygon lies parallel to XZ-plane (normal~~Y axis)");
				xz = true;
			}
			// Parallel to YZ-plane
			testParallel.cross(
				polygonPlane.getNormal(),
				GeometryConstants.X_VECTOR);
			if (testParallel.epsilonEquals(
				new Vector3d(0, 0, 0),
				GeometryConstants.EPSILON)) {
				//System.out.println("Polygon lies parallel to YZ-plane (normal~~X axis)");
				yz = true;
			}
			// Creating data for a Polygon2D equivalent to Polygon3d.
			int nPoints = this.pointSet.size();
			double[] xPoints = new double[nPoints];
			double[] yPoints = new double[nPoints];
			int i = 0;
			// Populating arrays with x and y coordinates of points.
			for (ListIterator itr = pointSet.listIterator(); itr.hasNext();) {
				Point3d currentPoint = (Point3d) itr.next();
				if (xz == false && yz == false) {
					xPoints[i] = currentPoint.x;
					yPoints[i] = currentPoint.y;
				} else if (xz == true) {
					xPoints[i] = currentPoint.x;
					yPoints[i] = currentPoint.z;
				} else if (yz == true) {
					xPoints[i] = currentPoint.y;
					yPoints[i] = currentPoint.z;
				}
				i++;
			}

			if (polygonPlane.contains(testPoint)) { // Point is in plane.
				if (xz == false && yz == false) {
					if (pointInPolygon2D(
						xPoints,
						yPoints,
						testPoint.x,
						testPoint.y))
						return true;
				}
				// XZ-plane.
				else if (xz == true) {
					if (pointInPolygon2D(
						xPoints,
						yPoints,
						testPoint.x,
						testPoint.z))
						return true;
				}
				// YZ-plane.
				else if (yz == true) {
					if (pointInPolygon2D(
						xPoints,
						yPoints,
						testPoint.y,
						testPoint.z))
						return true;
				}
				return false;
			} else
				return false; // Point is NOT in plane.
		} catch (Exception e) {
			e.printStackTrace();
			/**
			 * @todo Introduce InvalidPlaneException
			 */
			// temporary code until we figure out what to do if poligon colapses to line
			return false;
		}
	}

	/**
	 *
	 *    @todo implement isPolygon3dInsidePolygon3d()
	 *    Method to check a Polygon3d lies inside or outside given Polygon3d completely.
	 *   
	 */

	public boolean isPolygon3dInsidePolygon3d() {

		return true;
	}

	public void init() {
		super.init();
		panel = new Polygon3dPanel(this);
		((ViewPanel) panel).settApplet(this.geettApplet());
		guiObject = this;
		((Polygon3dPanel) panel).init();
	}

	/**
	 * Method to display Polygon3d on drawWFCanvas.
	 */
	public LinkedList geetShapeList(DrawWFPanel wfCanvas) {
		LinkedList polyList = new LinkedList();
		int size = pointSet.size();
		for (int i = 0; i < size; i++)
			polyList.addAll(new LineSegment(
				(Point3d) pointSet.get(i % size),
				(Point3d) pointSet.get((i + 1) % size)).geetShapeList(wfCanvas));
		return polyList;
	}

	//  public void paintComponent (DrawWFPanel canvas) {
	//  this.doNothing( " canvas  color is " + color);
	//    LinkedList lineList = new LinkedList ();
	//    int size = pointSet.size();
	//    for (int i=0;i < size; i++) {
	//      LineSegment line = new LineSegment((Point3d)pointSet.get(i%size),
	//                     (Point3d)pointSet.get((i+1)%size));
	//      line.setColor (Color.blue);
	//      line.paintComponent(canvas);
	//    }
	//  }

	public void paintComponent(Graphics2D g, DrawWFPanel canvas) {
		int size = pointSet.size();
		for (int i = 0; i < size; i++) {
			LineSegment line = new LineSegment(
				(Point3d) pointSet.get(i),
				(Point3d) pointSet.get((i + 1) % size));
			g.setColor(color);
			g.draw(line.createDisplayLine(canvas));
		}
	}

	/**
	 *
	 *    Method to compute lines connecting all the points of Polygon3d.
	 *   
	 */

	public LinkedList getLine3dList() {
		LinkedList polyList = new LinkedList();
		int listSize = pointSet.size();

		for (int i = 0; i < listSize; i++) {
			try {
				polyList.add(new Line3d(
					(Point3d) pointSet.get(i),
					(Point3d) pointSet.get((i + 1) % listSize)));
			} catch (Exception e) {
			} // ignore nonexistant line3d
		}
		//    polyList.add(new Line3d((Point3d)pointSet.get(i),(Point3d)pointSet.get(0)));
		return polyList;
	}

	/**
	 *
	 *    Method to return plane of a Polygon3d.
	 *   
	 */

	public Plane getPlane() throws InvalidPlaneException {
		for (int i = 2; i < pointSet.size(); i++)
			try {
				return new Plane(getPoint(0), getPoint(1), getPoint(i));
			} catch (Exception e) {
			}
		throw new InvalidPlaneException(
			"Can not create plane for given polygon3d");
	}

	/**
	 *
	 *    Method to return normal of a Polygon3d.
	 *   
	 */

	public Vector3d getNormal() {
		Vector3d vc1 = new Vector3d((Point3d) this.getPoint(0)), vc2 = new Vector3d(
			(Point3d) this.getPoint(1)), vc3 = new Vector3d((Point3d) this
			.getPoint(2)), vc2_1 = new Vector3d(), vc3_1 = new Vector3d(), resultVC = new Vector3d();
		vc2_1.sub(vc2, vc1);
		vc3_1.sub(vc3, vc1);
		resultVC.cross(vc2_1, vc3_1);
		resultVC.normalize();
		return resultVC;
	}

	//******************************************************************************
	/**
	 * Method implementation of Drawable3D interface
	 * To display a polygon in AnimApplet.
	 * This method returns a BranchGoup with a shape object having polygon.
	 */
	public BranchGroup createSceneGraph() {
		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		Shape3D pShape = new Shape3D();
		//    WorkPieceGeometry wp = new WorkPieceGeometry();
		pShape.setGeometry(createGraphicsPolygon());
		pShape.setAppearance(new Appearance());
		bg.addChild(pShape);
		return bg;
	}

	/**
	 *
	 *     Method to generate work piece based on the calculated geometry
	 *     using toolpath.
	 *   
	 */

	public GeometryArray createGraphicsPolygon() {
		GeometryInfo polyInfo = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
		polyInfo.setCoordinates(createPolygonCoordinates());
		polyInfo.setStripCounts(getStripCounts());
		GeometryArray ga = Gtk.doTriangulation(polyInfo)
			.getIndexedGeometryArray(true);
		return ga;
	}

	/**
	 *
	 *    Static method called for generating the geometry array for supplied set of
	 *    polygons. It combines all the supplied polygons and generates triangualtion
	 *    for them combinedly.
	 *   
	 */

	/*  public static GeometryArray createGraphicsPolygon(Polygon3d[] inFaces) {
	 GeometryInfo polyInfo = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
	 polyInfo.setCoordinates(createPolygonCoordinates(inFaces));
	 polyInfo.setStripCounts(getStripCounts(inFaces));
	 GeometryArray ga = doTriangulation(polyInfo).getIndexedGeometryArray(true);
	 ga = doTriangulation(polyInfo).getIndexedGeometryArray(true);
	 return doTriangulation(polyInfo).getIndexedGeometryArray(true);
	 }*/

	/**
	 *
	 */
	public GeometryArray createCompactPlane() {
		int[] b = { 4, 3 };
		int count = pointSet.size();
		IndexedTriangleArray triangleArray = new IndexedTriangleArray(
			4,
			GeometryArray.COORDINATES | GeometryArray.NORMALS,
			6);
		Point3d[] p3d = new Point3d[count];
		pointSet.toArray(p3d);
		int[] indices = { 0, 1, 2, 0, 2, 3 };
		triangleArray.setCoordinates(0, p3d);
		triangleArray.setCoordinateIndices(0, indices);
		return triangleArray;
	}

	/**
	 *
	 *    Method to return array of coordinates for the supplied set of polygons.
	 *   
	 */

	private Point3d[] createPolygonCoordinates() {
		Point3d[] pa = new Point3d[pointSet.size()];
		pointSet.toArray(pa);
		return pa;
	}

	/**
	 *
	 *    Method to return array of coordinates for the supplied set of polygons.
	 *   
	 */

	/*  private static Point3d[] createPolygonCoordinates(Polygon3d[] inFaces) {
	 ArrayList tempList = new ArrayList();
	 for (int i = 0; i < inFaces.length; i++)
	 tempList.addAll(inFaces[i].getPointSet());
	 Point3d[] pa = new Point3d[tempList.size()];
	 tempList.toArray(pa);
	 return pa;
	 }*/

	private int[] getStripCounts() {
		return new int[] { getPointSet().size() };
	}

	/**
	 * Method to return strip array for supplied set of polygon. 
	 */

	/*  private static int[] getStripCounts(Polygon3d[] inFaces) {
	 int[] strip = new int[inFaces.length];
	 for (int i = 0; i < inFaces.length; i++)
	 strip[i] = inFaces[i].getPointSet().size();
	 return strip;
	 }*/

	//******************************************************************************
	/**
	 * Method to print Polygon3d object.
	 */
	public String toString() {
		String polyString = "Polygon3d : ";
		for (int i = 0; i < this.pointSet.size(); i++) {
			polyString += ((Point3d) this.pointSet.get(i)).toString() + " ";
		}
		return polyString;
	}

	/**
	 * Main method.
	 */
	public static void main(String[] args) {

		Polygon3d p = new Polygon3d();
		DrawWFApplet da = new DrawWFApplet(p);
		p.settApplet(da);
		//    p.display("displaying applet, polygon");

		Polygon3d pol2 = new Polygon3d();
		DrawWFApplet da2 = new DrawWFApplet(pol2);
		pol2.settApplet(da2);
		//    pol2.display("displaying applet, polygon2");

		//    p.display("display polygon after creation");
		//    Polygon3d p3 = staticCreatePolygon ();
		Polygon3dPanel staticPanel = new Polygon3dPanel();
		staticPanel.settApplet(new GUIApplet(staticPanel));
		//    staticPanel.display("Polygon3d Creation Panel from static method");

		//    p2.setApplet(new DrawWFApplet (p2) );
		//    p2.display("display polygon after creation");

		/*    Polygon3dPanel panel = new Polygon3dPanel ();
		 panel.setApplet(new GUIApplet (panel) );
		 panel.display ("local panel");
		 Polygon3d poly1 = new Polygon3d();

		 Polygon3dPanel panel2 = new Polygon3dPanel (poly1);
		 panel2.setApplet(new GUIApplet (panel2) );
		 panel2.display ("local panel 2"); */
		Point3d p1 = new Point3d(0, 0, 0);
		Point3d p2 = new Point3d(1, 0, 0);
		Point3d p3 = new Point3d(1, 1, 0);
		Point3d p4 = new Point3d(0, 1, 0);
		Point3d p5 = new Point3d(0, 0, 1);
		Point3d p6 = new Point3d(1, 0, 1);
		Point3d p7 = new Point3d(1, 1, 1);
		Point3d p8 = new Point3d(0, 1, 1);
		//    Polygon3d poly1 = new Polygon3d();
		Polygon3d poly2 = new Polygon3d();
		//    Polygon3d poly3 = new Polygon3d();
		//    Polygon3d poly4 = new Polygon3d();
		//    Polygon3d poly5 = new Polygon3d();
		//    Polygon3d poly6 = new Polygon3d();
		//    poly1.addPoint(p1).addPoint(p2).addPoint(p3).addPoint(p4);
		poly2.addPoint(p1).addPoint(p5).addPoint(p6).addPoint(p2);
		//    poly3.addPoint(p5).addPoint(p6).addPoint(p7).addPoint(p8);
		//    poly4.addPoint(p4).addPoint(p8).addPoint(p7).addPoint(p3);
		//    poly5.addPoint(p1).addPoint(p4).addPoint(p8).addPoint(p5);
		//    poly6.addPoint(p2).addPoint(p3).addPoint(p7).addPoint(p6);
		//    Point3d tp = new Point3d(0,1,1);
		//    System.out.println(poly3+" contains "+tp+"? "+poly3.isPointInsidePolygon3d(tp));
		poly2.settApplet(new DrawWFApplet(poly2));
		poly2.display(poly2.toString());

	}

	static class Polygon3dPanel extends ViewPanel {
		//    GUIApplet applet;
		//    Polygon3d polygon;

		public Polygon3dPanel() {

			//   init();
		}

		public Polygon3dPanel(Polygon3d inPolygon) {
			super(inPolygon);
			inPolygon.settPanel(this);
		}

		public void init() {
			final ImpObject object = (ImpObject) this.object;
			this.setLayout(new BorderLayout());
			JPanel topPanel = new JPanel();

			topPanel.add(new JLabel("I am Polygon3dPanel"));
			JButton addPointButton = new JButton("AddPoint");

			addPointButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ViewObject.doNothing(object, "in addpoint callback");
					if (applet.getTarget() instanceof ViewObject)
						guiObject = applet.getTarget();
					else {
						Polygon3d np = new Polygon3d();
						guiObject = np;
						applet.setTarget(np);
					}
					//                  ViewObject.doNothing(this, "before if DrawWFPanel" + object.getCanvas() );
					Polygon3d guiPolygon = (Polygon3d) guiObject;
					guiPolygon.pointSet.add(new Point3d(1, 2, 3));
					guiPolygon.repaint();
				}
			});
			topPanel.add(addPointButton);
			JTree pointTree = new JTree();
			;
			if (object != null) {
				pointTree = new JTree(((Polygon3d) object).pointSet.toArray());
				//     DefaultTreeModel treeMOdel = new DefaultTreeModel ( ( (Polygon3d) object).pointSet.toArray());
			}
			pointTree.setCellRenderer(new Tuple3dRenderer(new Point3d()));
			pointTree.setCellEditor(new Tuple3dEditor(new Point3d()));
			//      topPanel.add(viewlist);
			this.add(topPanel, BorderLayout.NORTH);
			JScrollPane pointPanel = new JScrollPane(pointTree);
			this.add(pointPanel, BorderLayout.CENTER);
			JButton colorButton = new JButton("Choose color");
			colorButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					guiObject.settColor(JColorChooser.showDialog(
						Polygon3dPanel.this,
						"Select a color",
						Color.black));
					((DrawableWF) guiObject).repaint();
				}
			});
			topPanel.add(colorButton);
			if (object == null) {
				JButton showPolygonButton = new JButton("Show");
				showPolygonButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (object != null) {
							//      polygon.pointSet.add (new Point3d() );
							Polygon3dPanel.this.geettApplet().appletFrame
								.dispose();
							Polygon3dPanel.this.geettApplet().destroy();
							object.settApplet(new DrawWFApplet(object));
							object.display(object.toString());
						}
					}
				});
				this.add(showPolygonButton);
			}
		}

	}
}
