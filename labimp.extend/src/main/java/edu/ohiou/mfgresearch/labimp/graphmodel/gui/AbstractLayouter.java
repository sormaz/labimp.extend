/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.graphmodel.gui;

import java.awt.geom.Point2D;
import java.util.Map;

/**
 * @author sormaz
 *
 */
public abstract class AbstractLayouter implements GraphLayouter {
	
	protected Map<GraphVertex, Point2D> vertexLocMap;
	protected GraphRenderer renderer;
	protected int limit = 50;

	/**
	 * 
	 */
	public AbstractLayouter() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see edu.ohiou.mfgresearch.labimp.graphmodel.gui.GraphLayouter#getVertexLocation(edu.ohiou.mfgresearch.labimp.graphmodel.gui.GraphVertex)
	 */
	
	public Point2D getVertexLocation(GraphVertex vertex) {
		// TODO Auto-generated method stub
		return null;
	}


	public void setRenderer (GraphRenderer renderer)  {
		this.renderer = renderer;
		while(renderer.getGraphModel().getTableModel().getColumnCount() > (Math.pow(limit, 2)/GraphConstants.NODE_SEPARATION))
		{
			limit += limit;
		}
	}

}
