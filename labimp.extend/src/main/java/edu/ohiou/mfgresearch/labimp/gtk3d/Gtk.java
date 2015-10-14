package edu.ohiou.mfgresearch.labimp.gtk3d;

import java.awt.Dimension;

import javax.vecmath.*;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
/**
 *
 *  this class provide utility methods for geometric computations
 *  that are not available in Java3d package
 *  All methods in a class are static.
 * 
 */

public class Gtk {

	/**
	 * Test if input vector is zero vector and returns boolean for that
	 * test
	 */
	public static boolean isZeroVector(Tuple3d inVector) {
		return isZeroVector(inVector, GeometryConstants.EPSILON);
	}

	public static boolean isZeroVector(Tuple3d inVector, double epsilon) {
		return inVector.epsilonEquals(GeometryConstants.ZERO_VECTOR, epsilon);
	}

	public static boolean epsilonEquals(
		double number1,
		double number2,
		double epsilon) {
		double difference = Math.abs(number1 - number2);
		return difference <= epsilon;
	}

	public static boolean epsilonEquals(double number1, double number2) {
		return epsilonEquals(number1, number2, GeometryConstants.EPSILON);
	}
  
  public static double round (double input) {
    return round (input, GeometryConstants.ROUND_PRECISION);
  }
  
  public static double round (double input, int precision) {
    double temp = input;

    for (int i =0; i<precision; i++) {
      temp *= 10.;
    }
    temp = Math.round(temp);
    for (int i =0; i<precision; i++) {
      temp /= 10.;
    }
    return temp;
  }

	public static double truncateDouble(double inDouble, int numDigits) {
		double tempDouble = inDouble;
		System.out.println("\ntempDouble before " + tempDouble);
		for (int i = 0; i < numDigits; i++)
			tempDouble *= 10.0;
		System.out.println("\ntempDouble after " + tempDouble);
		//    int tempInt =  Math.floor(tempDouble);
		//    System.out.println("\ntempInt " + tempInt);
		double newDouble = Math.floor(tempDouble);
		System.out.println("\nnewDouble before" + newDouble);
		for (int i = 0; i < numDigits; i++)
			newDouble /= 10.0;
		System.out.println("\nnewDouble after" + newDouble);
		return newDouble;
	}

	public static double truncateDouble(double inDouble) {
		return truncateDouble(inDouble, 3);
	}
	/**
	 * Adds two Dimension objects to create new object
	 *
	 * Metod accepts tow Dimension objects and returns new Dimension objects with
	 * added values - Dimension (w,h) way of computaiton is defined by mode:
	 * HEIGHT : w = max (w1 + w2), h = h1 + h2
	 * WIDTH :  w = w1 + w2, h = max (h1, h2)
	 * WIDTH_HEIGHT : w = w1 + w2, h = h1 + h2
	 *
	 * @param mamber1 - first element to be added
	 * @param member2 second element to be added
	 * @param mode - defines how to add members
	 * @return added Dimension (w,h)
	 */
	public static Dimension add(Dimension member1, Dimension member2, int mode) {
		if (mode == GeometryConstants.HEIGHT)
			return new Dimension(
				Math.max(member1.width, member2.width),
				member1.height + member2.height);
		if (mode == GeometryConstants.WIDTH)
			return new Dimension(member1.width + member2.width, Math.max(
				member1.height,
				member2.height));
		if (mode == GeometryConstants.WIDTH_HEIGHT)
			return new Dimension(member1.width + member2.width, member1.height
				+ member2.height);
		return null;
	}

	/** Method to compute transformation matrix for a given vector
	 *  Supplied vector is new Z axis, new X axis or orthogonal to old Y axis
	 *
	 * @param ptVec - Vector3d on which to perfrom transformation, this vector is new z axis
	 * @return Matrix4d - Matrix4d that represent transformatino matrix
	 */
	public static Matrix4d computeTransformMatrix(Vector3d ptVec) {
		Vector3d vp = new Vector3d(ptVec);
		Matrix4d vtMatrix = new Matrix4d();
		double epsilonValue = 0.001;

		// create a 3d vector y = (0,1,0) and x = (1,0,0)
		//    Vector3d y = new Vector3d(0, 1, 0);
		//    Vector3d x = new Vector3d(1, 0, 0);

		// zv = normalize(vp)
		Vector3d zv = new Vector3d();
		zv.normalize(vp);
		//System.out.println("zv: " + zv);

		// yzv is a vector which represents y*zv
		Vector3d yzv = new Vector3d();
		yzv.cross(GeometryConstants.Y_VECTOR, zv);
		//    System.out.println("y*zv: " + yzv);

		if (Gtk.isZeroVector(yzv, epsilonValue)) {
			// yzv = -x
			yzv.negate(GeometryConstants.X_VECTOR);
			//      System.out.println("Now: y*zv = -x");
			//      System.out.println("epsilonValue = "+ epsilonValue);
		}

		// xv = normalize(y*zv)
		Vector3d xv = new Vector3d();
		xv.normalize(yzv);
		//    System.out.println("xv: " + xv);

		// yv = zv*xv
		Vector3d yv = new Vector3d();
		yv.cross(zv, xv);
		//    System.out.println("yv: " + yv);

		vtMatrix = new Matrix4d(
			xv.x,
			yv.x,
			zv.x,
			0,
			xv.y,
			yv.y,
			zv.y,
			0,
			xv.z,
			yv.z,
			zv.z,
			0,
			0,
			0,
			0,
			1);
		//System.out.println("vtMatrix:\n"+vtMatrix);
		return vtMatrix;
	}
	/**
	 * added by chandu 11/13/2006 to translate to origin after transform
	 * @param vec
	 * @param pt
	 */
	public static Matrix4d ccomputeTransformMatrix(Vector3d vec, Point3d pt)
	{
		Matrix4d zMat = computeTransformMatrix(vec);
		Matrix4d mat = new Matrix4d(
				zMat.m00,
				zMat.m01,
				zMat.m02,
				-pt.x,				
				zMat.m10,
				zMat.m11,
				zMat.m12,
				-pt.y,				
				zMat.m20,
				zMat.m21,
				zMat.m22,
				-pt.z,				
				zMat.m30,
				zMat.m31,
				zMat.m32,
				zMat.m33);				
		return mat;
	}

	/**
	 * Method to carry out triangulation and related methods.
	 */
	public static GeometryInfo doTriangulation(GeometryInfo pInfo) {
		pInfo.compact();
		pInfo.recomputeIndices();
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(pInfo);
		pInfo.compact();
		pInfo.recomputeIndices();
		return pInfo;
	}

	public Vector3d computeOrthoVector(Vector3d v1, Vector3d v2) {
		Vector3d out = new Vector3d();
		out.cross(v1, v2);
		out.normalize();
		return out;
	}
} // end of GTK class definition

