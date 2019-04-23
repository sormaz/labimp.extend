/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.graphmodel;

import java.util.EventListener;

/**
 * Provides the specification for directed arc 
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Ohio University</p>
 * @author Ganduri
 * @version 1.0
 */
public interface DirectedArc extends Arc{
	
		
	/**
	 * Returns the parent graph node in the arc
	 * @return parent Graph node
	 */
	public GraphNode getParent();
	
	/**
	 * Returns the child graph node in the arc
	 * @return parent Graph node
	 */
	public GraphNode getChild();
	
	/**
	 * Sets the parent graph node in the arc
	 * @param parent Graph node
	 */
	public void setParent(GraphNode parent);
	
	/**
	 * Sets the parent graph node in the arc
	 * @param child Graph node
	 */
	public void setChild(GraphNode child);
	
		
	

}
