package edu.ohiou.mfgresearch.labimp.gtk3d;

import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.JTable;
import javax.vecmath.Tuple3d;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.*;
import java.awt.*;
import javax.swing.*;
import javax.vecmath.*;
import java.text.NumberFormat;

/**
 *
 *  <p>Title: Generic classes for manufacturing planning</p>
 *  <p>Description: Thsi project implements general classes for intelligent manufacturing planning. These are:
 *  <p>Copyright: Copyright (c) 2001</p>
 *  <p>Company: Ohio University</p>
 *  @author Dusan Sormaz
 *  @version 1.0
 * 
 */

public class Tuple3dRenderer extends JLabel
	implements
		TableCellRenderer,
		TreeCellRenderer,
		ListCellRenderer {
	private Tuple3d tuple;
	static public final int NUM_FRACTION_DIGITS = 3;
	int numFracDigits;

	public Tuple3dRenderer(Tuple3d tuple) {
		this(tuple, NUM_FRACTION_DIGITS);
	}

	public Tuple3dRenderer(Tuple3d tuple, int numFracDigits) {
		this.tuple = tuple;
		this.numFracDigits = numFracDigits;
	}

	public static void main(String[] args) {
		Tuple3dRenderer tr = new Tuple3dRenderer(new Point3d(
			Math.PI,
			Math.E,
			Math.sqrt(2.)), 5);
		System.out.println("point : " + tr.format());
	}

	public Component getTableCellRendererComponent(
		JTable table,
		Object value,
		boolean isSelected,
		boolean hasFocus,
		int row,
		int column) {
		tuple = (Tuple3d) value;
		this.setText(format());
		return this;
	}

	public Component getTreeCellRendererComponent(
		JTree tree,
		Object value,
		boolean selected,
		boolean expanded,
		boolean leaf,
		int row,
		boolean hasFocus) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		//  DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node.getUserObject();
		System.out.println("user object " + node.getUserObject().toString());
		Object object = node.getUserObject();
		if (object instanceof Tuple3d) {
			tuple = (Tuple3d) node.getUserObject();
			this.setText(format());
		} else {
			this.setText(object.toString());
		}
		return this;

	}

	public Component getListCellRendererComponent(
		JList list,
		Object value,
		int index,
		boolean isSelected,
		boolean cellHasFocus) {
		tuple = (Tuple3d) value;
		this.setText(format());
		return this;
	}

	public String format() {
		return format(tuple, numFracDigits);
	}

	static public String format(Tuple3d tuple) {
		return format(tuple, NUM_FRACTION_DIGITS);
	}

	static public String format(Tuple3d tuple, int numFracDigits) {

		NumberFormat formatter = NumberFormat.getInstance();
		String className = tuple.getClass().getName();
		String name = className.substring(
			className.lastIndexOf(".") + 1,
			className.length());
		formatter.setMaximumFractionDigits(numFracDigits);
		return name
			+ ": ["
			+ formatter.format(tuple.x)
			+ ", "
			+ formatter.format(tuple.y)
			+ ", "
			+ formatter.format(tuple.z)
			+ "]";
	}
	static public String formatXML(Tuple3d tuple) {
		return formatXML(tuple, NUM_FRACTION_DIGITS);
	}

	static public String formatXML(Tuple3d tuple, int numFracDigits) {

		NumberFormat formatter = NumberFormat.getInstance();
		String className = tuple.getClass().getName();
		String name = className.substring(
			className.lastIndexOf(".") + 1,
			className.length());
		formatter.setMaximumFractionDigits(numFracDigits);
		return formatter.format(tuple.x)
			+ " "
			+ formatter.format(tuple.y)
			+ " "
			+ formatter.format(tuple.z);
	}
}
