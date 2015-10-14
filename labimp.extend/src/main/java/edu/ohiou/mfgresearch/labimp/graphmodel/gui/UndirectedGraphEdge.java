/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.graphmodel.gui;

import java.awt.Color;
import java.awt.geom.Line2D;

import edu.ohiou.mfgresearch.labimp.basis.Drawable2D;
import edu.ohiou.mfgresearch.labimp.basis.Viewable;
import edu.ohiou.mfgresearch.labimp.graphmodel.Arc;
import edu.ohiou.mfgresearch.labimp.graphmodel.DirectedArc;
import edu.ohiou.mfgresearch.labimp.graphmodel.UndirectedArc;

/**
 * @author Ganduri
 *
 */
public class UndirectedGraphEdge extends DefaultGraphEdge implements Drawable2D, GraphEdge{
	
	
	
	public UndirectedGraphEdge(UndirectedArc arc, GraphRenderer renderer)
	{
		this.renderer = renderer;
		setArc(arc);
		this.color = Color.GREEN;
	}
		
	
	public DirectedArc getArc() {
		return (DirectedArc)arc;
	}

	public void setArc(Arc anArc) {
		this.arc = (UndirectedArc)anArc;
		if(arc.getUserObject() instanceof Drawable2D &&!renderer.isDefault_behavior())
		{
			isDefaultShape = false;
		}
		GraphVertex temp = renderer.getGraphVertex(((UndirectedArc)arc).getNodes().get(0));
		if(temp != null)
		{
			sourceLoc = temp.getNodeLocation();
		}
		else
		{
			throw new IllegalArgumentException("Vertex does not exist");
		}
		temp = renderer.getGraphVertex(((UndirectedArc)arc).getNodes().get(1));
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
			//System.out.println("In need update of Graph Edge");
			sourceLoc = renderer.getGraphVertex(((UndirectedArc)arc).getNodes().get(0)).getNodeLocation();
			sinkLoc = renderer.getGraphVertex(((UndirectedArc)arc).getNodes().get(1)).getNodeLocation();
			arcShape = new Line2D.Double(sourceLoc, sinkLoc);
			trimArc();
		}

	}
	
	
	public void makeDefaultShape()
	{
		if(arcShape == null)
		{
			arcShape = new Line2D.Double(sourceLoc, sinkLoc);
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


	
}