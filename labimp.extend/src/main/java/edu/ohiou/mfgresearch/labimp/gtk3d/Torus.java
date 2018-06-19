package edu.ohiou.mfgresearch.labimp.gtk3d;

import java.awt.Shape;
import java.util.LinkedList;
import static java.lang.Math.*;

import javax.vecmath.Point3d;

import edu.ohiou.mfgresearch.labimp.draw.DrawWFApplet;
import edu.ohiou.mfgresearch.labimp.draw.DrawWFPanel;
import edu.ohiou.mfgresearch.labimp.draw.ImpObject;

public class Torus extends ImpObject {
	double R;
	double r;
	
	public Torus (double R, double r) {
		this.R = R;
		this.r = r;
	}
	
	public LinkedList<Shape> geetShapeList (DrawWFPanel canvas) {
		LinkedList<Shape> list = new  LinkedList<Shape>();
		Point3d  oldPoint, newPoint;
		oldPoint = new Point3d(R+r,0,0);
		double deltaTheta = 10, deltaPhi = 1;
		double theta = 0.0, phi = 0.0;
		for (int j =0; j <= 360/deltaPhi; j++) {
			phi += deltaPhi;
			theta = 0.0;
			for (int i = 0; i <= 360/deltaTheta; i++) {
				double x,y,z;
				theta += deltaTheta;
				x = (R + r * cos (toRadians(theta))) * cos (toRadians(phi));
				y = (R + r * cos (toRadians(theta))) * sin (toRadians(phi));
				z = r * sin (toRadians(theta));
				newPoint = new Point3d (x,y,z);
				LineSegment ls = new LineSegment(oldPoint, newPoint);
				list.addAll(ls.geetShapeList(canvas));
				oldPoint = newPoint;
			}
		}

		return list;
		
	}
	
	public static void main (String [] args) {
		Torus t = new Torus (20,1);
		
		DrawWFApplet da = new DrawWFApplet(t);
		da.display();
	}

}
