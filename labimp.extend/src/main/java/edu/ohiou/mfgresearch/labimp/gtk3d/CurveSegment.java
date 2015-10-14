package edu.ohiou.mfgresearch.labimp.gtk3d;

/**
 * <p>Title: Features</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: Ohio University</p>
 * @author unascribed
 * @version 1.0
 */
import javax.vecmath.Point3d;
import edu.ohiou.mfgresearch.labimp.draw.ImpObject;
import edu.ohiou.mfgresearch.labimp.draw.*;

import javax.vecmath.*;

public abstract class CurveSegment extends ImpObject {
	
	

//	protected Point3d startPoint;//starting point
//
//	protected Point3d endPoint;//end point

	// SELECTORS + MODIFIERS

	/** Returns start point of line segment.
	 *
	 */
	public abstract Point3d gettStartPoint();

//	public Point3d getStartPt() {
//		return startPoint;
//	}

	/**
	 * Returns end point of line segment.
	 *   
	 *   
	 */

	public abstract Point3d gettEndPoint();

//	public Point3d getEndPt() {
//		return endPoint;
//	}

	/**
	 * Returns new curve segment with ends (direction) swapped.
	 *  
	 *  
	 */

	public abstract CurveSegment swap();
	
	/**
	 * methods to set and get status of swapped
	 *
	 */
	
	
	
	/** Method to set the start point of line segment.
	 *
	 * @param inPoint - point to be set as start point
	 */
//	public void setStPoint(Point3d inPoint) {
//		startPoint = inPoint;
//	}

	/** Method to set the end point of line segment.
	 *
	 * @param inPoint - point to be set as end point
	 */
//	public void setEndPoint(Point3d inPoint) {
//		endPoint = inPoint;
//	}

	abstract public Vector3d getTangent(Point3d point);

}