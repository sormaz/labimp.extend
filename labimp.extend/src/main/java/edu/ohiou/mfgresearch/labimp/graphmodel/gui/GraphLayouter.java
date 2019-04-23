/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.graphmodel.gui;

import java.awt.geom.Point2D;
import java.util.*;
import edu.ohiou.mfgresearch.labimp.graphmodel.GraphNode;

/**
 * @author Ganduri
 *
 */
public interface GraphLayouter {
	
	public Point2D getVertexLocation(GraphVertex aVertex);	
	
	public void makeLayout();
	
	public void setRenderer (GraphRenderer renderer);

}
