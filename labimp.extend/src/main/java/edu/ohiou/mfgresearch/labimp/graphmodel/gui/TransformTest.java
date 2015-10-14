package edu.ohiou.mfgresearch.labimp.graphmodel.gui;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class TransformTest extends JPanel{
	
	    final static Color bg = Color.white;
	    final static Color fg = Color.black;
	    final static Color red = Color.red;
	    final static Color white = Color.white;
	    public Ellipse2D.Double circle1;
	    public Ellipse2D.Double circle2;
	    public Ellipse2D.Double circle3;
	    public Ellipse2D.Double circle4;
	    public AffineTransform transform;
	    public Point2D cirLoc1 = new Point2D.Double(100,100);
	    public Point2D cirLoc2 = new Point2D.Double(300,100);
	    public Point2D cirLoc3 = new Point2D.Double(300,300);
	    
	
	    public void init() {
	        //Initialize drawing colors
	        setBackground(bg);
	        setForeground(fg);
	    }

	   
	    public void paint(Graphics g) {
	        Graphics2D g2 = (Graphics2D) g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	       // draw Ellipse2D.Double
	        double x = 0;
	        double y = 0;
	        double rectWidth = 20;
	        double rectHeight = 20;
	        double gridWidth = 50;
	        Shape s;
	        circle1 = new Ellipse2D.Double(x, y, rectWidth, rectHeight);
	        g2.draw(circle1.getBounds2D());
	        transform = AffineTransform.getTranslateInstance(cirLoc1.getX()-circle1.getBounds2D().getWidth()/2, 
	        		cirLoc1.getX()-circle1.getBounds2D().getHeight()/2);
	        s = transform.createTransformedShape(circle1);
	        g2.draw(s);
	        g2.drawString("Circle 1", (float)cirLoc1.getX(),
	        		(float)cirLoc1.getY());
	        g2.draw(s.getBounds2D());
	        
	        
//	        x += rectWidth;
//	        y += rectHeight;
	        circle2 = new Ellipse2D.Double(x, y, rectWidth, rectHeight);
	        transform = AffineTransform.getTranslateInstance(cirLoc2.getX(), cirLoc2.getY());
	        s = transform.createTransformedShape(circle2);
	        g2.draw(s);
	        g2.drawString("Circle 2", (float)cirLoc2.getX(), (float)cirLoc2.getY());
	        
//	        x += rectWidth;
//	        y += rectHeight;
	        circle3 = new Ellipse2D.Double(x, y, rectWidth, rectHeight);
	        transform = AffineTransform.getTranslateInstance(cirLoc3.getX(), cirLoc3.getX());
	        s = transform.createTransformedShape(circle3);
	        g2.draw(s);
	        g2.drawString("Circle 3", (float)cirLoc3.getX(), (float)cirLoc3.getY());
	       
	        
	        
	    }

	    public static void main(String s[]) {
	        JFrame f = new JFrame("ShapesDemo2D");
	        f.addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent e) {System.exit(0);}
	        });
	        JPanel panel = new TransformTest();
	        f.getContentPane().add("Center", panel);
	        ((TransformTest)panel).init();
	        f.setSize(new Dimension(1000,1000));
	        f.pack();
	        f.setVisible(true);
	    }

	}


