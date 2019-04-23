package edu.ohiou.mfgresearch.labimp.gtk3d;

import javax.media.j3d.IndexedLineArray;
import javax.vecmath.Color3f;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Axis extends Shape3D {
	private float val;
	private IndexedLineArray xAxis;
	private IndexedLineArray yAxis;
	private IndexedLineArray zAxis;

	Color3f[] red = new Color3f[6];
	Color3f[] green = new Color3f[6];
	Color3f[] blue = new Color3f[6];

	public Axis(int scale) {
		val = scale * 1.0f;
		xAxis = new IndexedLineArray(6, GeometryArray.COLOR_3
			| GeometryArray.COORDINATES, 10);
		yAxis = new IndexedLineArray(6, GeometryArray.COLOR_3
			| GeometryArray.COORDINATES, 10);
		zAxis = new IndexedLineArray(6, GeometryArray.COLOR_3
			| GeometryArray.COORDINATES, 10);
		createAxes();
		setAxisColors();
		setGeometry(xAxis);
		addGeometry(yAxis);
		addGeometry(zAxis);
	}

	private void createAxes() {
		// create line for X axis
		float val_a = val - 0.1f;
		xAxis.setCoordinate(0, new Point3f(-val, 0.0f, 0.0f));
		xAxis.setCoordinate(1, new Point3f(val, 0.0f, 0.0f));
		xAxis.setCoordinate(2, new Point3f(val_a, 0.1f, 0.1f));
		xAxis.setCoordinate(3, new Point3f(val_a, -0.1f, 0.1f));
		xAxis.setCoordinate(4, new Point3f(val_a, 0.1f, -0.1f));
		xAxis.setCoordinate(5, new Point3f(val_a, -0.1f, -0.1f));
		setAxisCoordinateAxis(xAxis);
		// create line for Y axis
		yAxis.setCoordinate(0, new Point3f(0.0f, -val, 0.0f));
		yAxis.setCoordinate(1, new Point3f(0.0f, val, 0.0f));
		yAxis.setCoordinate(2, new Point3f(0.1f, val_a, 0.1f));
		yAxis.setCoordinate(3, new Point3f(-0.1f, val_a, 0.1f));
		yAxis.setCoordinate(4, new Point3f(0.1f, val_a, -0.1f));
		yAxis.setCoordinate(5, new Point3f(-0.1f, val_a, -0.1f));
		setAxisCoordinateAxis(yAxis);
		// create line for Z axis
		zAxis.setCoordinate(0, new Point3f(0.0f, 0.0f, -val));
		zAxis.setCoordinate(1, new Point3f(0.0f, 0.0f, val));
		zAxis.setCoordinate(2, new Point3f(0.1f, 0.1f, val_a));
		zAxis.setCoordinate(3, new Point3f(-0.1f, 0.1f, val_a));
		zAxis.setCoordinate(4, new Point3f(0.1f, -0.1f, val_a));
		zAxis.setCoordinate(5, new Point3f(-0.1f, -0.1f, val_a));
		setAxisCoordinateAxis(zAxis);
	} // end of Axis createGeometry()

	private void setAxisColors() {
		for (int i = 0; i < red.length; i++) {
			red[i] = new Color3f(0.8f, 0, 0);
			green[i] = new Color3f(0.0f, 0.5f, 0);
			blue[i] = new Color3f(0, 0, 0.8f);
		}
		xAxis.setColors(0, red);
		yAxis.setColors(0, green);
		zAxis.setColors(0, blue);
	}

	private void setAxisCoordinateAxis(IndexedLineArray axis) {
		axis.setCoordinateIndex(0, 0);
		axis.setCoordinateIndex(1, 1);
		axis.setCoordinateIndex(2, 2);
		axis.setCoordinateIndex(3, 1);
		axis.setCoordinateIndex(4, 3);
		axis.setCoordinateIndex(5, 1);
		axis.setCoordinateIndex(6, 4);
		axis.setCoordinateIndex(7, 1);
		axis.setCoordinateIndex(8, 5);
		axis.setCoordinateIndex(9, 1);
	}
} // end of class Axis
