package edu.ohiou.mfgresearch.labimp.gtk3d;

/**
 * Title:        GeometryConstants Class <p>
 * Description:  Class defining global geometry constants. <p>
 * Copyright:    Copyright (c) 2001 <p>
 * Company:      Ohio University <p>
 * @author  Jaikumar Arumugam + Dusan N. Sormaz
 * @version 1.0
 *
 * */
import edu.ohiou.mfgresearch.labimp.gtk2d.GtkConstants;
import javax.vecmath.*;
import edu.ohiou.mfgresearch.labimp.gtk2d.*;

public class GeometryConstants extends GtkConstants {
	public static final Point3d ORIGIN = new Point3d(0, 0, 0);
	public static final Vector3d ZERO_VECTOR = new Vector3d(0, 0, 0);
	public static final Vector3d X_VECTOR = new Vector3d(1, 0, 0);
	public static final Vector3d Y_VECTOR = new Vector3d(0, 1, 0);
	public static final Vector3d Z_VECTOR = new Vector3d(0, 0, 1);

	public static Line3d X_AXIS = null;
	public static Line3d Y_AXIS = null;
	public static Line3d Z_AXIS = null;

	static {
		try {
			X_AXIS = new Line3d(
				GeometryConstants.ORIGIN,
				GeometryConstants.X_VECTOR);
			Y_AXIS = new Line3d(
				GeometryConstants.ORIGIN,
				GeometryConstants.Y_VECTOR);
			Z_AXIS = new Line3d(
				GeometryConstants.ORIGIN,
				GeometryConstants.Z_VECTOR);
		} catch (InvalidLineException e) {
			e.printStackTrace();
		}
	}

	public static final int INSIDE = 1;
	public static final int ON_BOUNDARY = 0;
	public static final int OUTSIDE = -1;
	public static double EPSILON = 0.00001;
  public static int ROUND_PRECISION = 6;
	public static double BIG_NUMBER = 100;
	public static final int HEIGHT = 1;
	public static final int WIDTH = 2;
	public static final int WIDTH_HEIGHT = 3;

}