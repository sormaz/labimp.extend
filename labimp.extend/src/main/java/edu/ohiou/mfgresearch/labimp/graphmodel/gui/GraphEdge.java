package edu.ohiou.mfgresearch.labimp.graphmodel.gui;

import java.awt.Color;
import java.awt.Shape;

import edu.ohiou.mfgresearch.labimp.graphmodel.*;
import edu.ohiou.mfgresearch.labimp.basis.Drawable2D;

public interface GraphEdge extends Drawable2D {

//	public void setArc(UndirectedArc arc);

	public void setArc(Arc arc);
	
	public DirectedArc getArc();

	public Shape getArcShape();

	public void setArcShape(Shape arcShape);

	public Color getColor();

	public void setColor(Color color);

	public Color getFillColor();

	public void setFillColor(Color fillColor);

	public void makeDefaultShape();

}