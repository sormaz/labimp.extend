package edu.ohiou.mfgresearch.labimp.gtk3d;

import javax.vecmath.*;

import java.util.Comparator;

public class PointDistanceComparator implements Comparator {

	private Line2d baseLine;
	
	public PointDistanceComparator(Vector2d vector, Point2d point)
	{
		baseLine = new Line2d(vector, point);
	}
	
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		Curve2d curve0 = (Curve2d) arg0;
		Curve2d curve1 = (Curve2d) arg1;
		return Double.compare(getFactor(getClosestPoint(curve0)), getFactor(getClosestPoint(curve1)));	
	}

	public Point2d getClosestPoint(Curve2d curve)
	{
		if (curve instanceof Line2d)
		{
			Line2d line = (Line2d) curve;
			return (getFactor(line.getStartPoint()) <= getFactor(line.getEndPoint()))
						? line.getStartPoint(): line.getEndPoint(); 
		}
		if (curve instanceof Arc2d)
		{
			Arc2d arc = (Arc2d) curve;
			Vector2d radVec = baseLine.getDirection();
			radVec.negate();
			radVec.scale(arc.getRadius());
			Point2d point = new Point2d(arc.getCenter());
			point.add(radVec);
			if (arc.classify(point) != GeometryConstants.OUTSIDE) 
				{
					return point;
				}
			else
			{
				return (getFactor(arc.getStartPoint()) < getFactor(arc.getEndPoint()))
				? arc.getStartPoint(): arc.getEndPoint();
			}
		}
		return new Point2d();
	}
	
	public double getFactor(Point2d point)
	{
		Point2d ppoint = baseLine.projectPoint(point);
		return baseLine.factorForPoint(ppoint);
	}
	
	public Line2d getBaseLine()
	{
		return this.baseLine;
	}
}
