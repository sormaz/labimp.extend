package edu.ohiou.mfgresearch.labimp.draw;


import javax.swing.*;
import java.awt.*;

import javax.vecmath.Point3d;
import edu.ohiou.mfgresearch.labimp.basis.*;
import edu.ohiou.mfgresearch.labimp.gtk3d.Polygon3d;

/**
 *
 *  Title:        Generic classes for manufacturing planning
 *  Description:  Thsi project implements general classes for imtelligent manufacturing planning. These are:
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

public class TreeApplet extends GUIApplet {

	public TreeApplet(ViewObject inTarget) {
		super(inTarget);
	}

	public void init() {
		super.init();
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		JScrollPane treeView = new JScrollPane(new JTree(((ImpObject) target)
			.geetTree()));
		treeView.setToolTipText(target.toString());

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(treeView, BorderLayout.WEST);

	}

	public void addPanel() {
		JTabbedPane tabPanel = new JTabbedPane(SwingConstants.NORTH);
		tabPanel.addTab("Data", target.geettPanel());
		Draw2DPanel draw2dPanel = new Draw2DPanel((Drawable2D) target, null);
		draw2dPanel.init();
		tabPanel.addTab("2d", draw2dPanel);
		tabPanel.addTab("wf", new DrawWFPanel((DrawableWF) target, null));
		tabPanel.setSelectedComponent(draw2dPanel);

		this.getContentPane().add(tabPanel, BorderLayout.CENTER);
	}

	public static void main(String args[]) {
		Point3d p1 = new Point3d(0, 0, 0);
		Point3d p2 = new Point3d(1, 0, 0);
		Point3d p3 = new Point3d(1, 1, 0);
		Point3d p4 = new Point3d(0, 1, 0);
		Polygon3d poly1 = new Polygon3d();
		poly1.addPoint(p1).addPoint(p2).addPoint(p3).addPoint(p4);

		TreeApplet treeApplet = new TreeApplet(poly1); //new MfgPartModel()
		treeApplet.display(treeApplet.getTarget().toString());
	}

}
