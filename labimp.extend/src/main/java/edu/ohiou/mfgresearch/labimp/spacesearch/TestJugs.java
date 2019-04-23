package edu.ohiou.mfgresearch.labimp.spacesearch;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import edu.ohiou.mfgresearch.labimp.spacesearch.WaterJugs;
import edu.ohiou.mfgresearch.labimp.spacesearch.WaterJugs;
import edu.ohiou.mfgresearch.labimp.basis.*;

import java.util.LinkedList;
import java.awt.BorderLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.swing.tree.DefaultMutableTreeNode;

public class TestJugs extends WaterJugs {

	int bigState = 4;
	int smallState = 3;
	int bigCapacity = 8;
	int smallCapacity = 6;

	public TestJugs(int big, int small) {
		super(big, small);
		node = new DefaultMutableTreeNode(this);
	}

	public LinkedList getDrawList() {
		LinkedList shapes = new LinkedList();
		shapes.add(new Line2D.Double(
			new Point2D.Double(0, bigCapacity),
			new Point2D.Double(0, 0)));
		shapes.add(new Line2D.Double(
			new Point2D.Double(0, 0),
			new Point2D.Double(2, 0)));
		shapes.add(new Line2D.Double(
			new Point2D.Double(2, 0),
			new Point2D.Double(2, bigCapacity)));
		shapes.add(new Line2D.Double(
			new Point2D.Double(0, bigState),
			new Point2D.Double(2, bigState)));

		shapes.add(new Line2D.Double(
			new Point2D.Double(4, smallCapacity),
			new Point2D.Double(4, 0)));
		shapes.add(new Line2D.Double(
			new Point2D.Double(4, 0),
			new Point2D.Double(6, 0)));
		shapes.add(new Line2D.Double(
			new Point2D.Double(6, 0),
			new Point2D.Double(6, smallCapacity)));
		shapes.add(new Line2D.Double(
			new Point2D.Double(4, smallState),
			new Point2D.Double(6, smallState)));
		return shapes;
	}

	public void init() {
		super.init();
		System.out.println("in init of testJugs");
		panel.setLayout(new BorderLayout());
		Draw2DPanel twoDPanel = new Draw2DPanel(this);
		twoDPanel.init();
		panel.add(twoDPanel, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		TestJugs testJugs1 = new TestJugs(5, 3);

		testJugs1.settApplet(new Draw2DApplet(testJugs1));
		testJugs1.display();
	}
}