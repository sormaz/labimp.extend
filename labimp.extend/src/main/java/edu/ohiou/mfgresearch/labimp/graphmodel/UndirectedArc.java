package edu.ohiou.mfgresearch.labimp.graphmodel;

import java.util.List;




/**
 * Provides the specification for an undirected or bidirectional arc 
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Ohio University</p>
 * @author Ganduri
 * @version 1.0
 */
public interface UndirectedArc extends Arc {
	
	/**
	 * Returns the other node associated with the arc given one node
	 * @param aNode graph node in the arc
	 * @throws IllegalArgumentException if node does not belong to arc	 
	 */
	public GraphNode getOtherNode(GraphNode aNode);
	
	public List<GraphNode> getNodes();

}
