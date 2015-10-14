/*
 * Created on Mar 17, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.ohiou.mfgresearch.labimp.gtk3d;

import java.awt.Dimension;
import java.util.*;

import javax.swing.JFrame;
import javax.vecmath.*;

import edu.ohiou.mfgresearch.labimp.basis.*;
import edu.ohiou.mfgresearch.labimp.draw.*;


/**
 * @author Chandu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Loop3d extends Profile3d {

//	private LinkedList shapes = new LinkedList();
	
	public Loop3d() {
		getShapes().clear();
	}

	public boolean checkShape(CurveSegment inCurve) {
//		if (getShapes().size() == 0) {
//			getShapes().add(inCurve);
//			return true;
//		}
		CurveSegment last = (CurveSegment) getShapes().getLast();
		if (last.gettEndPoint().epsilonEquals(inCurve.gettEndPoint(), 
				GeometryConstants.EPSILON)
			|| last.gettStartPoint().epsilonEquals(
				inCurve.gettStartPoint(),
				GeometryConstants.EPSILON)) {
			addShape(inCurve.swap());
			return true;
		}
		if (last.gettEndPoint().epsilonEquals(inCurve.gettStartPoint(), 
				GeometryConstants.EPSILON)
			|| last.gettStartPoint().epsilonEquals(
				inCurve.gettEndPoint(),
				GeometryConstants.EPSILON)) {
			addShape(inCurve);
			return true;
		}
		return false;
	}

	public Loop2d transformToLoop2d(Matrix4d matrix)
	{
		Loop2d loop2d = new Loop2d();
		try
		{
			Prof2d prof = this.transformTo2d(matrix);
			for (Iterator itr = prof.getCurves().iterator(); itr.hasNext();)
			{
				loop2d.addCurve((Curve2d) itr.next());
			}
		}
		catch(Exception e)
		{
			System.out.println("Error in Loop3d transformTo2d.");
			e.printStackTrace();
		}
		
//		loop2d.setApplet(new Draw2DApplet(loop2d));
//		loop2d.display();
		return loop2d;
	}

	public LinkedList getShapeList(DrawWFPanel canvas) {
		LinkedList shapeList = new LinkedList();
		for (ListIterator itr = getShapes().listIterator(); itr.hasNext();) {
			Object shape = itr.next();
			if (shape instanceof LineSegment)
				shapeList.addAll(((LineSegment) shape).getShapeList(canvas));
			if (shape instanceof Arc)
				shapeList.addAll(((Arc) shape).getShapeList(canvas));
		}
		return shapeList;
	}

	public static void main(String[] args) {
		Loop3d prof = new Loop3d();
		prof.addShape(new LineSegment(0.2, 0, 0, 0.8, 0, 0)).addShape(
			new Arc(
				0.2,
				new Point3d(0.8, 0.2, 0),
				new Vector3d(0, 0, 1),
				3 * Math.PI / 2,
				2 * Math.PI));

		try {
			Loop2d prof2ds = prof.transformToLoop2d(prof.computeTransformMatrix());
//			System.out.println(prof2ds.size() + " profs.");
//			for (Iterator itr = prof2ds.iterator(); itr.hasNext();) {
//				Prof2d prof2d = (Prof2d) itr.next();
				prof2ds
					.settApplet(new edu.ohiou.mfgresearch.labimp.basis.Draw2DApplet(prof2ds));
				prof2ds.display();
//			}
		} catch (Exception e) {
		}
		prof.settApplet(new DrawWFApplet(prof, new Point3d(30, 30, 30), 200));
		prof.display("Profile", new Dimension(650, 700), JFrame.EXIT_ON_CLOSE);
		System.out.println("" + prof);
	}
}
