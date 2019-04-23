package edu.ohiou.mfgresearch.labimp.graphmodel;

import java.util.*;



/**
 * Provides the default functionality for an undirected or bidirectional arc and implements the Undirected interface. 
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Ohio University</p>
 * @author Chandrasekhar Ganduri
 * @version 1.0
 */
public class DefaultUndirectedArc implements UndirectedArc {

	GraphNode node1;
	GraphNode node2;
	Object userObject;
	List<EventListener> listeners = new ArrayList<EventListener>();
	
	/**
	 * Constructor for the undirected or bidirectional arc
	 * @param node1 first node
	 * @param node2 second node
	 */
	public DefaultUndirectedArc(GraphNode node1, GraphNode node2)
	{
		this.node1 = node1;
		this.node2 = node2;
		node1.addArc(this);
		node2.addArc(this);		
	}
	
	public void clear()
	{
		node1.removeArc(this);
		node2.removeArc(this);
		node1 = null;
		node2 = null;		
	}
	
	
	public List<GraphNode> getNodes() {
		List<GraphNode> toReturn = new ArrayList<GraphNode>();
		toReturn.add(node1);
		toReturn.add(node2);
		return toReturn;
	}
	
	public GraphNode getOtherNode(GraphNode aNode) {
		
		if(!nodeExists(aNode))
			throw new IllegalArgumentException("Input node does not belong to arc");
		
		if(node1 == aNode)
		{
			return node2;
		}
		else
		{
			return node1;
		}		
	}

	public boolean nodeExists(GraphNode aNode) {
		if(node1 == aNode || node2 == aNode)
		{
			return true;
		}
		return false;
	}

	public void setUserObject(Object o) {
		if(o == null)
			throw new IllegalArgumentException("input parameter is null");
		userObject = o;
	}

	public Object getUserObject() {
		return userObject;
	}
	
	public void addListener(EventListener l)
	{
		listeners.add(l);
	}
	
	public void removeListener(EventListener l)
	{
		listeners.remove(l);
	}
}
