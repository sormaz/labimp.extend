/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.graphmodel.gui;


import java.awt.geom.*;

import javax.vecmath.Vector2d;

import edu.ohiou.mfgresearch.labimp.basis.Drawable2D;
import edu.ohiou.mfgresearch.labimp.basis.Viewable;
import edu.ohiou.mfgresearch.labimp.graphmodel.Arc;
import edu.ohiou.mfgresearch.labimp.graphmodel.DirectedArc;
//import edu.ohiou.mfgresearch.labimp.graphmodel.GraphConstants;

/**
 * Provides the default functionality for displaying a graph arc
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Ohio University</p>
 * @author Chandrasekhar Ganduri
 * @version 1.0
 */
public class DirectedGraphEdge extends DefaultGraphEdge implements Drawable2D, GraphEdge {

	/**
	 * Constructor for Directed Graph Edge
	 * @param arc
	 * @param renderer
	 */
	public DirectedGraphEdge(DirectedArc arc, GraphRenderer renderer)
	{
		this.renderer = renderer;
		setArc(arc);
		//sourceVertex = renderer.getGraphVertex(arc.getParent() 
	}
		
	public void setArc(DirectedArc arc) {
		this.arc = arc;
		if(arc.getUserObject() instanceof Drawable2D  &&!renderer.isDefault_behavior())
		{
			isDefaultShape = false;
		}
		GraphVertex temp = renderer.getGraphVertex(arc.getParent());
		if(temp != null)
		{
			sourceLoc = temp.getNodeLocation();
		}
		
		else
		{
			throw new IllegalArgumentException("Vertex does not exist");
		}
		temp = renderer.getGraphVertex(arc.getChild());
		if(temp != null)
		{
			sinkLoc = temp.getNodeLocation();
		}
		else
		{
			throw new IllegalArgumentException("Vertex does not exist");
		}
		//arcLabel = arc.toString();
		arcLabel = "";
	}

	public void setNeedUpdate(boolean arg0) {
		if(arg0 == true)
		{
		//	System.out.println("In need update of Graph Edge");
			sourceLoc = renderer.getGraphVertex(((DirectedArc)arc).getParent()).getNodeLocation();
			sinkLoc = renderer.getGraphVertex(((DirectedArc)arc).getChild()).getNodeLocation();
			makeDefaultShape();
			trimArc();
		}
	}
  
  public String toToolTipString () {
    if (arc.getUserObject() instanceof Viewable) {
      return ((Viewable) arc.getUserObject()).toToolTipString();
    }
    if (arc.getUserObject() instanceof Drawable2D) {
        return ((Drawable2D) arc.getUserObject()).toToolTipString();
    }
    return arc.getUserObject().toString();
  }

	
	public DirectedArc getArc() {
		return (DirectedArc)arc;
	}
	
	public void makeDefaultShape()
	{
			arcShape = new Line2D.Double(sourceLoc, sinkLoc);
			makeArrow();
			trimArc();		
	}
	
	private void makeArrow()
	{
		Vector2d p = new Vector2d(sourceLoc.getX() - sinkLoc.getX(), sourceLoc.getY() - sinkLoc.getY());
		p.scale(GraphConstants. ARROW_LENGTH / p.length());
		Point2D.Double p2D = new Point2D.Double(p.x, p.y );
		Point2D.Double posPoint = new Point2D.Double(), negPoint = new Point2D.Double();
		AffineTransform posXForm = AffineTransform.getRotateInstance(GraphConstants. ARROW_ANGLE );
		posXForm.transform(p2D, posPoint);
		AffineTransform negXForm = AffineTransform.getRotateInstance(-GraphConstants. ARROW_ANGLE );
		negXForm.transform(p2D, negPoint);
		Point2D.Double endPoint = new Point2D.Double((sourceLoc.getX()+sinkLoc.getX())/2,(sourceLoc.getY()+sinkLoc.getY())/2); 
		posPoint = new Point2D.Double(endPoint. x + posPoint.x , endPoint.y + posPoint.y);
		negPoint = new Point2D.Double(endPoint. x + negPoint.x , endPoint.y + negPoint.y);
		GeneralPath arrow = new GeneralPath();
		arrow.moveTo((float)posPoint.getX(), (float)posPoint.getY());
		arrow.lineTo((float)endPoint.getX(), (float)endPoint.getY());
		arrow.lineTo((float)negPoint.getX(), (float)negPoint.getY());
		arrow.closePath();
		arrowShape = arrow;
	}
	
	
	public void setArc(Arc arc) {}

}
