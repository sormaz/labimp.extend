/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.draw;

import java.util.LinkedList;

import javax.vecmath.Point3d;

import edu.ohiou.mfgresearch.labimp.gtk3d.LineSegment;

/**
 * @author sormaz
 *
 */
public class Icosahedron extends ImpObject {
	
	double phi = (1 +Math.sqrt(5))/2;

	Point3d opp = new Point3d (0, 1, phi);
	Point3d opn = new Point3d (0, 1, -phi);
	Point3d onp = new Point3d (0, -1, phi);
	Point3d onn = new Point3d (0, -1, -phi);
	
	Point3d ppo = new Point3d (1, phi, 0);
	Point3d pno = new Point3d (1, -phi, 0);
	Point3d npo = new Point3d (-1, phi, 0);
	Point3d nno = new Point3d (-1, -phi, 0);

	Point3d pop = new Point3d (phi, 0, 1);	
	Point3d pon = new Point3d (phi, 0, -1);
	Point3d nop = new Point3d (-phi, 0, 1);
	Point3d non = new Point3d (-phi, 0, -1);
	
	
	public Icosahedron() {
		// TODO Auto-generated constructor stub
	}
	
	public LinkedList geetShapeList (DrawWFPanel dwp) {
		 LinkedList list = new LinkedList();
	     LineSegment line1 = new LineSegment (opn, onn);
	     LineSegment line2 = new LineSegment (opn, npo);
	     LineSegment line3 = new LineSegment (opn, ppo);
	     LineSegment line4 = new LineSegment (opn, pon);
	     LineSegment line5 = new LineSegment (opn, non);

	     LineSegment line6 = new LineSegment (onn, pon);
	     LineSegment line7 = new LineSegment (pon, ppo);
	     LineSegment line8 = new LineSegment (ppo, npo);
	     LineSegment line9 = new LineSegment (npo, non);
	     LineSegment line10 = new LineSegment (non, onn);

	     
	     list.addAll(line1.geetShapeList (dwp));	     
	     list.addAll(line2.geetShapeList (dwp));
	     list.addAll(line3.geetShapeList (dwp));	     
	     list.addAll(line4.geetShapeList (dwp));
	     list.addAll(line5.geetShapeList (dwp));	     
	     list.addAll(line6.geetShapeList (dwp));	     
	     list.addAll(line7.geetShapeList (dwp));
	     list.addAll(line8.geetShapeList (dwp));	     
	     list.addAll(line9.geetShapeList (dwp));
	     list.addAll(line10.geetShapeList (dwp));	
	     
	     LineSegment line11 = new LineSegment (onp, opp);
	     LineSegment line12 = new LineSegment (onp, pno);
	     LineSegment line13 = new LineSegment (onp, nno);
	     LineSegment line14 = new LineSegment (onp, nop);
	     LineSegment line15 = new LineSegment (onp, pop);

	     LineSegment line16 = new LineSegment (opp, nop);
	     LineSegment line17 = new LineSegment (nop, nno);
	     LineSegment line18 = new LineSegment (nno, pno);
	     LineSegment line19 = new LineSegment (pno, pop);
	     LineSegment line20 = new LineSegment (pop, opp);
	     
	     list.addAll(line11.geetShapeList (dwp));	     
	     list.addAll(line12.geetShapeList (dwp));
	     list.addAll(line13.geetShapeList (dwp));	     
	     list.addAll(line14.geetShapeList (dwp));
	     list.addAll(line15.geetShapeList (dwp));	     
	     list.addAll(line16.geetShapeList (dwp));	     
	     list.addAll(line17.geetShapeList (dwp));
	     list.addAll(line18.geetShapeList (dwp));	     
	     list.addAll(line19.geetShapeList (dwp));
	     list.addAll(line20.geetShapeList (dwp));	
	     
	     LineSegment line21 = new LineSegment (npo, opp);
	     LineSegment line22 = new LineSegment (opp, ppo);
	     LineSegment line23 = new LineSegment (ppo, pop);
	     LineSegment line24 = new LineSegment (pop, pon);
	     LineSegment line25 = new LineSegment (pon, pno);

	     LineSegment line26 = new LineSegment (pno, onn);
	     LineSegment line27 = new LineSegment (onn, nno);
	     LineSegment line28 = new LineSegment (nno, non);
	     LineSegment line29 = new LineSegment (non, nop);
	     LineSegment line30 = new LineSegment (nop, npo);
	     
	     list.addAll(line21.geetShapeList (dwp));	     
	     list.addAll(line22.geetShapeList (dwp));
	     list.addAll(line23.geetShapeList (dwp));	     
	     list.addAll(line24.geetShapeList (dwp));
	     list.addAll(line25.geetShapeList (dwp));	     
	     list.addAll(line26.geetShapeList (dwp));	     
	     list.addAll(line27.geetShapeList (dwp));
	     list.addAll(line28.geetShapeList (dwp));	     
	     list.addAll(line29.geetShapeList (dwp));
	     list.addAll(line30.geetShapeList (dwp));	

	     

		 return list;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Icosahedron ic = new Icosahedron();
	    ic.settApplet(new DrawWFApplet (ic));
	    ic.display();
	}

}
