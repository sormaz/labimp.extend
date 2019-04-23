package edu.ohiou.mfgresearch.labimp.graphmodel.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JPanel;

import edu.ohiou.mfgresearch.labimp.basis.Draw2DPanel;
import edu.ohiou.mfgresearch.labimp.basis.DrawString;
import edu.ohiou.mfgresearch.labimp.basis.Drawable2D;
import edu.ohiou.mfgresearch.labimp.basis.GraphicsConfiguration;
import edu.ohiou.mfgresearch.labimp.graphmodel.*;

public abstract class DefaultGraphEdge implements GraphEdge{
	// TODO: DSormaz why this does not implement Drawable2D interface??

	protected Draw2DPanel canvas;
	protected Color color = Color.RED;
	protected Color fillColor = Color.RED;
	protected Shape arcShape;
	protected Shape arrowShape;
	protected boolean isDefaultShape = true;
	protected GraphRenderer renderer;
	protected String arcLabel;
	protected Arc arc;	
	protected Point2D sourceLoc;
	protected Point2D sinkLoc;
	protected GraphVertex sourceVertex;
	protected GraphVertex sinkVertex;
	protected double trimConstant;
	protected GraphicsConfiguration graphics = null;

	public DefaultGraphEdge() {
		super();
		trimConstant = GraphConstants.RADIUS;
	}

	public void settCanvas(Draw2DPanel arg0) {
		canvas = arg0;
	}

	public JPanel geettCanvas() {
		return canvas;
	}

	

	public Shape getArcShape() {
		return arcShape;
	}

	public void setArcShape(Shape arcShape) {
		this.arcShape = arcShape;
		isDefaultShape = true;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}
	
	  public GraphicsConfiguration geetGraphicsConfig () {
		  if (graphics == null) {
			  graphics = new GraphicsConfiguration();
			  graphics.configure(this);
		  }
		  return graphics;
		  
	  }


	public void paintComponent(Graphics arg0) {
		
	}

	public void paintComponent(Graphics2D arg0) {
	
	}

	public void makeDrawSets() {
	//		System.out.println("In Make DrawSets");
			if(isDefaultShape)
			{
			makeDefaultShape();
			}
			
			canvas.addDrawShapes(color, geetDrawList());
			canvas.addFillShapes(fillColor, geetFillList());
		}

	public LinkedList geetDrawList() {
			LinkedList toReturn = new LinkedList();
			if(isDefaultShape)
			{
				toReturn.add(arcShape);
				if(arrowShape != null)
				{
				toReturn.add(arrowShape);
				}
			}
			else
			{
				toReturn.addAll(((Drawable2D)arc.getUserObject()).geetDrawList());
			}
			return toReturn;
		}

	public LinkedList geetFillList() {
		LinkedList toReturn = new LinkedList();
		if(isDefaultShape)
		{
			toReturn.add(arcShape);
			if(arrowShape != null)
			{
			toReturn.add(arrowShape);
			}
		}
		else
		{
			toReturn.addAll(((Drawable2D)arc.getUserObject()).geetFillList());
		}
		return toReturn;
	}

	public LinkedList geetStringList() {
	
		LinkedList toReturn = new LinkedList();
		Line2D.Double line = (Line2D.Double)arcShape;
		if(!isDefaultShape)
		{
			toReturn.addAll(((Drawable2D)arc.getUserObject()).geetStringList());
		}
		else
		{
			//toReturn.add(new DrawString(arcLabel, new java.lang.Float(sourceLoc.getX()), new java.lang.Float(sourceLoc.getY())));
			
			//<<< Change by Danyu,
			//Logic: To put label of arc between source and sink rather than on source
			// node to avoid overlap of the arc label and the source label.
			toReturn.add(new DrawString(arcLabel, new java.lang.Float(sourceLoc
					.getX() - (sourceLoc.getX() - sinkLoc.getX()) * 0.3),
					new java.lang.Float(sourceLoc.getY()
							- (sourceLoc.getY() - sinkLoc.getY()) * 0.3)));
			// >>>
		}
		return toReturn;
	}
	
	
	public void addButtonOptions(int options) {
		
		canvas.addButtonOptions (options);
	}

	
	public void removeButtonOptions(int options) {
		
		canvas.removeButtonOptions (options);
	}


	
	protected void clipArc(Line2D line) {
//			Line2D.Double line = (Line2D.Double)arcShape;
			Point2D p1 = line.getP1();
			Point2D p2 = line.getP2();
			double length = p1.distance(p2);
			double xDiff = p2.getX() - p1.getX();
			double yDiff = p2.getY() - p1.getY();
			double lengthBreak = length - trimConstant;
			double xLoc = xDiff * lengthBreak / length;
			double yLoc = yDiff * lengthBreak / length;
			line.setLine(p1.getX(), p1.getY(), xLoc + p1.getX(), yLoc + p1.getY());
	}
	
	protected void trimArc()
	{
		for (int i = 0; i < 2; i++) {
			Line2D.Double line = (Line2D.Double)arcShape;
			clipArc(line);
			line.setLine(line.getP2(), line.getP1());
		}
	}
			
			
	

	public void setArc(Arc arc) {}

	public Point2D geettPosition() {
		Point2D.Double toReturn = new Point2D.Double();
		toReturn.setLocation((sourceLoc.getX()+sinkLoc.getX())/2, (sourceLoc.getY()+sinkLoc.getY())/2);
		return toReturn;
	}

	public void settPosition(Point2D point) {
		  System.out.println("In setPosition of directedGraphEdge");
		  System.out.println("Point "+point.toString());  
	  }

	public void generateImageList() {
	  }

	public Collection giveSelectables() {
		Collection toReturn = new ArrayList();
		toReturn.add(this);		
		return toReturn;
	}


	public String getArcLabel() {
		return arcLabel;
	}

	public void setArcLabel(String arcLabel) {
		this.arcLabel = arcLabel;
	}

	
}