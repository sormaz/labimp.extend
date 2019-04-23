package edu.ohiou.mfgresearch.labimp.gtk3d;

import edu.ohiou.mfgresearch.labimp.draw.ImpObject;
import java.awt.Graphics2D;
import edu.ohiou.mfgresearch.labimp.draw.DrawWFPanel;
import javax.vecmath.*;
import java.awt.*;
import javax.swing.*;
import java.util.LinkedList;
import edu.ohiou.mfgresearch.labimp.draw.*;
//import javax.swing.JFrame;

/**
 * The class that represnet World coordinate system.
 * It consists of three line ssegments aligned with three axis
 * with user definable lengths
 */
public class WorldCS extends ImpObject {
	LineSegment xAxis;
	LineSegment yAxis;
	LineSegment zAxis;

	// CONSTRUCTORS.
	/** Default constructor.
	 * Constructs world coordinate system with axis lengths of 1, 2, 3,
	 * in x, y, and z axes respectively.
	 *
	 */
	public WorldCS() {
		this(1, 2, 3);
	}

	/** Constructor taking lengths of axes.
	 *  Constructs world coordinate system with user supplied axis lengths
	 *
	 * @param x the lengths of x axis segment
	 * @param y the length of y axis segment
	 * @param z the length of z axis segment
	 */
	public WorldCS(double x, double y, double z) {
		xAxis = new LineSegment(GeometryConstants.ORIGIN, new Point3d(x, 0, 0));
		yAxis = new LineSegment(GeometryConstants.ORIGIN, new Point3d(0, y, 0));
		zAxis = new LineSegment(GeometryConstants.ORIGIN, new Point3d(0, 0, z));
	}

	// SELECTORS.
	/** init method.
	 *
	 */
	public void init() {
		try {
			//      ((DrawableWF) canvas).init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//    this.getApplet().getContentPane().add (canvas);
		panel = new JPanel();
		panel.add(new JLabel("Showing WCS:"));
		panel.add(new JLabel("X axis: " + xAxis));
		panel.add(new JLabel("Y axis: " + yAxis));
		panel.add(new JLabel("Z axis: " + zAxis));
		panel.setLayout(new GridLayout(4, 1));
		panel.setPreferredSize(new Dimension(600, 100));
	}

	/** Returns 2D shape list for WCS.
	 *
	 * @param canvas canvas on which to draw
	 * @return the list of objects that implement Shape interface to be drawn
	 */
	public LinkedList geetShapeList(DrawWFPanel canvas) {
		LinkedList ll = new LinkedList();
		ll.addAll(xAxis.geetShapeList(canvas));
		ll.addAll(yAxis.geetShapeList(canvas));
		ll.addAll(zAxis.geetShapeList(canvas));
		return ll;
	}

  public void makeShapeSets (DrawWFPanel canvas) {
     canvas.addDrawShape(Color.red , xAxis.createDisplayLine(canvas));
     canvas.addDrawShape(Color.blue , yAxis.createDisplayLine(canvas));
     canvas.addDrawShape(Color.green , zAxis.createDisplayLine(canvas));

   }

	public void paintComponent(Graphics2D g, DrawWFPanel canvas) {
		g.setColor(Color.red);
		g.draw(xAxis.createDisplayLine(canvas));
		g.setColor(Color.blue);
		g.draw(yAxis.createDisplayLine(canvas));
		g.setColor(Color.green);
		g.draw(zAxis.createDisplayLine(canvas));
	}
	/** Main method.
	 * @param args array of command line arguments
	 */
	public static void main(String[] args) {
		WorldCS worldCS = new WorldCS();
		worldCS.settApplet(new DrawWFApplet(worldCS));
		worldCS.display("wcs", new Dimension(600, 900), JFrame.EXIT_ON_CLOSE);
	}
} // END OF CLASS DEFINITION.
