package edu.ohiou.mfgresearch.labimp.gtk3d;

import java.util.*;
import javax.vecmath.*;

public class Tuple3dStringParser {

	private static double[] get3dValues(String numbers) {
		double[] values = new double[3];
		StringTokenizer tokens = new StringTokenizer(numbers);
		int size = tokens.countTokens();
		for (int i = 0; i < size; i++) {
			values[i] = (new Double(tokens.nextToken()).doubleValue());
		}
		return values;
	}
	public static Point3d getPoint3d(String numbers) {
		double[] points = get3dValues(numbers);
		Point3d point = new Point3d(points[0], points[1], points[2]);
		return point;
	}
	public static Vector3d getVector3d(String numbers) {
		double[] points = get3dValues(numbers);
		Vector3d vector = new Vector3d(points[0], points[1], points[2]);
		return vector;
	}
}