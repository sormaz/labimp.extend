package edu.ohiou.mfgresearch.labimp.draw;
/**
 * Title:        DrawWFApplet Class <p>
 * Description:  Class defining applet used for drawing wireframes. <p>
 * Copyright:    Copyright (c) 2001 <p>
 * Company:      Ohio University <p>
 * @author  Jaikumar Arumugam + Dusan N. Sormaz
 * @version 1.0
 */


import java.awt.Graphics;

import java.awt.Graphics2D;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import javax.vecmath.Point3d;
import edu.ohiou.mfgresearch.labimp.basis.*;

//import edu.ohiou.implanner.parts.MfgPartModel;

/**
 * DrawWFApplet is the class that allows display of its targets in 3D wireframe
 * view
 * It implements DrawableWF interface for interacting with DrawWFPanel and its
 * target
 */
public class DrawWFApplet extends GUIApplet implements DrawableWF {

	protected DrawWFPanel canvas;

	// CONSTRUCTORS

	/** Default Constructor.
	 *  Sets target to null and uses default viewpopint and scale
	 *
	 */
	public DrawWFApplet() {
		this(null);
	}

	/** Constructor taking DrawableWF target.
	 *  Sets the supplied target and uses default viewpoint and scale
	 *
	 */
	public DrawWFApplet(DrawableWF inTarget) {
		this(
			inTarget,
			DrawWFPanel.DEFAULT_VIEW_POINT,
			DrawWFPanel.DEFAULT_SCALE);
	}

	/** Constructor taking DrawableWF target, view point and scale.
	 *
	 */
	public DrawWFApplet(DrawableWF inTarget, Point3d viewPoint, double scale) {
		super((Viewable)inTarget);
		canvas = new DrawWFPanel(inTarget, this, viewPoint, scale);
	}

	// SELECTORS

	/** Method to return target of applet.
	 *
	 */
	public Viewable getTarget() {
		return target;
	}

	/**
	 * Method to return drawWFCanvas of applet.
	 *
	 *
	 */

	public JPanel gettCanvas() {
		return ((DrawableWF) target).gettCanvas();
	}

	/** Method to set canvas of applet.
	 *
	 */
	public void settCanvas(DrawWFPanel inCanvas) {
		canvas = inCanvas;
	}

	public void setNeedUpdate(boolean needUpdate) {
		canvas.setNeedUpdate(needUpdate);
	}

	public void start() {
		JSplitPane contentPane = new JSplitPane();
		contentPane.setDividerLocation(500);
		contentPane.setDividerSize(5);
		if (target != null) {
			//        addPanel();
			contentPane.setOrientation(target.getPanelOrientation());
			contentPane.setRightComponent(target.gettPanel());
		} else {
			contentPane.setRightComponent(new JLabel(
				"This applet does not have target",
				SwingConstants.CENTER));
			contentPane.setOrientation(SwingConstants.HORIZONTAL);
		}
		contentPane.setLeftComponent(canvas);
		setContentPane(contentPane);
	}

	/**
	 * init() method required to initialize applet.
	 *
	 *
	 */

	public void init() {
		super.init();
		//    canvas.init();
		try {
			target.settApplet(this);
			((DrawableWF) target).settCanvas((DrawWFPanel) canvas);
			((DrawWFPanel) canvas).setTarget((DrawableWF) target);
		} catch (NullPointerException e) {
		}
	}

	/**
	 *
	 *    Returns the size of applet for display
	 *
	 */

	public Dimension geetAppletSize() {
		Dimension targetSize;
		try {
			targetSize = target.geetAppletSize();
		} catch (NullPointerException e) {
			targetSize = new Dimension(0, 0);
		}
		Dimension canvasSize = canvas.getAppletSize();//drawWFCanvas.getAppletSize();
		//   ImpObject.doNothing(this, targetSize.toString() + canvasSize.toString());
		return new Dimension(
			Math.max(targetSize.width, canvasSize.width) + 10,
			targetSize.height + canvasSize.height + 10);
	}

	// methods from DrawableWF interface

	/**
	 * returns list of points that are recognized by mouse movement
	 */
	public LinkedList getPointSet() {
		return ((DrawableWF) target).getPointSet();
	}

public void makeShapeSets (DrawWFPanel canvas) {
  canvas.makeShapeSets(canvas);
}
	/**
	 * Returns shape list of panel to be drawn
	 */
	public LinkedList getShapeList(DrawWFPanel canvas) {
		return canvas.getShapeList(canvas);
	}

	/**
	 * Method that paints applet. It calls canvas's paintComponent
	 */
	public void paintComponent(Graphics2D g) {
		canvas.paintComponent(g);
	}

	/**
	 * Method that paints applet. It calls canvas's paintComponent
	 */
	public void paintComponent(Graphics g) {
		canvas.paintComponent(g);
	}

	public void paintComponent(Graphics2D g, DrawWFPanel canvas) {
		this.canvas.paintComponent(g, canvas);

	}

	/** main method to display applet.
	 *
	 */
	static public void main(String args[]) {
		DrawWFApplet da = new DrawWFApplet();
		da.display(
			"I am in draw wf applet",
			new Dimension(450, 800),
			JFrame.EXIT_ON_CLOSE);
	}
}
