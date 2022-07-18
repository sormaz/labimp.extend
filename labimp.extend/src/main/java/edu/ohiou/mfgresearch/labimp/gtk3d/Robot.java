package edu.ohiou.mfgresearch.labimp.gtk3d;

import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Point3d;

import edu.ohiou.mfgresearch.labimp.basis.ViewObject.ViewPanel;
import edu.ohiou.mfgresearch.labimp.draw.DrawWFApplet;
import edu.ohiou.mfgresearch.labimp.draw.DrawWFPanel;
import edu.ohiou.mfgresearch.labimp.draw.ImpObject;

public class Robot extends ImpObject {
	
	private double firstSeg;
	private double secondSeg;
	private double base = 1;
	double alpha;
	double beta;
	double gamma;

	public Robot(double f, double s, double a, double b, double g) {
		firstSeg = f;
		secondSeg = s;
		alpha = a;
		beta = b;
		gamma = g;
	}
	
	private double dToR (double d) {
		return d * Math.PI/180.;
	}
	
	public LinkedList geetShapeList(DrawWFPanel canvas) {
		LinkedList shapeList = new LinkedList();
		double z1 = firstSeg * Math.sin(dToR(beta));
		double x1 = firstSeg * Math.cos(dToR(beta)) * Math.cos(dToR(alpha));
		double y1 = firstSeg * Math.cos(dToR(beta)) * Math.sin(dToR(alpha));
		
		double z2 = firstSeg * Math.sin(dToR(beta)) + secondSeg * Math.sin(dToR(beta + gamma));
		double x2 = (firstSeg * Math.cos(dToR(beta)) +
				 secondSeg * Math.cos(dToR(beta + gamma)))* Math.cos(dToR(alpha));
		double y2 = (firstSeg * Math.cos(dToR(beta)) +
				 secondSeg * Math.cos(dToR(beta + gamma)))* Math.sin(dToR(alpha));		
		Point3d baseP = new Point3d (0,0,0);
		Point3d firstP = new Point3d (x1, y1, z1);
		Point3d secP = new Point3d (x2, y2, z2);
//		System.out.println("first point" + firstP);
//		System.out.println("second point" + secP);
		LineSegment ls = new LineSegment(baseP, firstP);
		shapeList.addAll(ls.geetShapeList(canvas));
		ls = new LineSegment(firstP, secP);
		shapeList.addAll(ls.geetShapeList(canvas));
		double ortAlpha = alpha + 90;
		ls = new LineSegment(new Point3d(base * Math.cos(dToR(ortAlpha)), 
										base * Math.sin(dToR(ortAlpha)), 0), 
							new Point3d(-base * Math.cos(dToR(ortAlpha)), 
												-base * Math.sin(dToR(ortAlpha)), 0));
		shapeList.addAll(ls.geetShapeList(canvas));

		return shapeList;
	}
	
	public void init () {
		panel = new RobotPanel(this);
	}

	public static void main(String[] args) {
		Robot r = new Robot(10,5,0,0,0);
		DrawWFApplet dwfa = new DrawWFApplet(r);
		r.display();

	}
}

class RobotPanel extends ViewPanel {
	
	Robot r;
	
	RobotPanel (Robot r) {
		this.r = r;
		init();
	}
	
	public void init() {
		
		JSlider alphaSlider = new JSlider(JSlider.HORIZONTAL,0, 360, 0);
		alphaSlider.addChangeListener(new ChangeListener () {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
			    JSlider source = (JSlider)e.getSource();
			    int fps = (int)source.getValue();
			    r.alpha = fps;
		    	r.repaint();
			}
			}  );
		add(alphaSlider);
//Turn on labels at major tick marks.
		alphaSlider.setMajorTickSpacing(60);
		alphaSlider.setMinorTickSpacing(10);
		alphaSlider.setPaintTicks(true);
		alphaSlider.setPaintLabels(true);
		alphaSlider.setName("alpha");
		alphaSlider.setToolTipText("Slide to change alpha - base angle");
		
		JSlider betaSlider = new JSlider(JSlider.HORIZONTAL,0, 360, 0);
		betaSlider.addChangeListener(new ChangeListener () {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
			    JSlider source = (JSlider)e.getSource();
			    int fps = (int)source.getValue();
			    r.beta = fps;
		    	r.repaint();
			}
			}  );
		add(betaSlider);
//Turn on labels at major tick marks.
		betaSlider.setMajorTickSpacing(60);
		betaSlider.setMinorTickSpacing(10);
		betaSlider.setPaintTicks(true);
		betaSlider.setPaintLabels(true);
		betaSlider.setName("beta");
		betaSlider.setToolTipText("Slide to change beta - first angle");
		
		JSlider gammaSlider = new JSlider(JSlider.HORIZONTAL,0, 360, 0);
		gammaSlider.addChangeListener(new ChangeListener () {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
			    JSlider source = (JSlider)e.getSource();
			    int fps = (int)source.getValue();
			    r.gamma = fps;
		    	r.repaint();
			}
			}  );
		add(gammaSlider);
//Turn on labels at major tick marks.
		gammaSlider.setMajorTickSpacing(60);
		gammaSlider.setMinorTickSpacing(10);
		gammaSlider.setPaintTicks(true);
		gammaSlider.setPaintLabels(true);
		gammaSlider.setName("gamma");
		gammaSlider.setToolTipText("Slide to change gamma - second angle");
		
	}
	
}
