/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.graphmodel;

import java.util.*;

/**
 * This class provides default functionality for the graph node and implements the GraphNode interface.
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Ohio University</p>
 * @author Chandrasekhar Ganduri
 * @version 1.0
 */

public class DefaultGraphNode implements GraphNode {

	private List<UndirectedArc> biArcs = new ArrayList<UndirectedArc>();
	private List<DirectedArc> dirInArcs = new ArrayList<DirectedArc>();
	private List<DirectedArc> dirOutArcs = new ArrayList<DirectedArc>();
	private List<EventListener> listeners = new ArrayList<EventListener>();
	private Object userObject;
	
	/**
	 * Empty Constructor for the graph node
	 */
	public DefaultGraphNode(String nodeLabel)
	{
		userObject = nodeLabel;
	}
	
	/**
	 * Constructor for the graph node with userObject
	 * @param userObject object represented by the node
	 */
	public DefaultGraphNode(Object userObject)
	{
		this.userObject = userObject;
	}
	
	
	public void addNode(GraphNode aNode) {
		new DefaultUndirectedArc(this, aNode);		
	}
		
	public void removeNode(GraphNode aNode) {
		getArc(aNode).clear();		
	}
	
	public void addChild(GraphNode child) {		
		new DefaultDirectedArc(this, child);
	}

	public void removeChild(GraphNode child) {
		getInArc(child).clear();		
	}

	public void addParent(GraphNode parent) {
		new DefaultDirectedArc(parent, this);		
	}

	public void removeParent(GraphNode parent) {
		getOutArc(parent).clear();		
	}
	
	public List<GraphNode> getNodes() {
		List<GraphNode> toReturn = new ArrayList<GraphNode>();
		Iterator<UndirectedArc> itr = biArcs.iterator();
		while(itr.hasNext())
		{
			toReturn.add(itr.next().getOtherNode(this));
		}
		return toReturn;
	}

	public List<GraphNode> getParents() {
		List<GraphNode> toReturn = new ArrayList<GraphNode>();
		Iterator<DirectedArc> itr = dirInArcs.iterator();
		while(itr.hasNext())
		{
			toReturn.add(itr.next().getParent());
		}
		return toReturn;
	}

	public List<GraphNode> getChildren() {
		List<GraphNode> toReturn = new ArrayList<GraphNode>();
		Iterator<DirectedArc> itr = dirOutArcs.iterator();
		while(itr.hasNext())
		{
			toReturn.add(itr.next().getChild());
		}
		return toReturn;
	}

	public boolean isRoot() {
		return dirInArcs.isEmpty();
	}
	
	public boolean isLeaf()
	{
		return dirOutArcs.isEmpty();
	}

	public boolean isChild() {
		return !dirInArcs.isEmpty();
	}
	
	public boolean isChild(GraphNode parent) {
		Iterator<DirectedArc> itr = dirInArcs.iterator();
		while(itr.hasNext())
		{
			if(itr.next().getParent() == parent)
			{
				return true;
			}
		}
		return false;
	}
		
	public boolean isParent()
	{
		return !dirOutArcs.isEmpty();
	}
		
	public boolean isParent(GraphNode child) {
		Iterator<DirectedArc> itr = dirOutArcs.iterator();
		while(itr.hasNext())
		{
			if(itr.next().getChild() == child)
			{
				return true;
			}
		}
		return false;		
	}
	
	public boolean isConnected()
	{
		return !biArcs.isEmpty();
	}

	public void setUserObject(Object o) {
		userObject = o;		
	}

	public Object getUserObject() {
		return userObject;		
	}	

	public void addInArc(DirectedArc arc) {
		dirInArcs.add(arc);		
	}

	public void removeInArc(DirectedArc arc) {
		dirInArcs.remove(arc);
	}

	public void addOutArc(DirectedArc arc) {
		dirOutArcs.add(arc);		
	}

	public void removeOutArc(DirectedArc arc) {
		dirOutArcs.remove(arc);		
	}

	public boolean isConnected(GraphNode aNode)
	{
		boolean toReturn = false;
		if(!isConnected())
			return toReturn;
		
		Iterator<UndirectedArc> itr = biArcs.iterator();
		
		while(itr.hasNext())
		{
			if(itr.next().getOtherNode(this) == aNode)
			{
				return toReturn = true;
			}
		}
		return toReturn;
	}
	
	public void addArc(UndirectedArc arc) {
		biArcs.add(arc);		
	}
	
	public void removeArc(Arc arc) {
		biArcs.remove(arc);
	}
	
	public DirectedArc getInArc(GraphNode parent)
	{
		DirectedArc toReturn = null;
		if(isParent(parent))
		{
			Iterator<DirectedArc> itr = dirInArcs.iterator();
			while(itr.hasNext())
			{
				toReturn = itr.next();
				if(toReturn.getParent() == parent)
				{
					return toReturn;
				}
			}
		}
		else
		{
			throw new IllegalArgumentException("Input node is not a child");
		}
		return toReturn;
	}
		
	public List<DirectedArc> getInArcs() {
		return dirInArcs;
	}

	public DirectedArc getOutArc(GraphNode child)
	{
		DirectedArc toReturn = null;
		if(isChild(child))
		{
			Iterator<DirectedArc> itr = dirOutArcs.iterator();
			while(itr.hasNext())
			{
				toReturn = itr.next();
				if(toReturn.getChild() == child)
				{
					return toReturn;
				}
			}
		}
		else
		{
			throw new IllegalArgumentException("Input node is not a child");
		}
		return toReturn;
	}
	
	public List<DirectedArc> getOutArcs() {		
		return dirOutArcs;
	}
	
	public Arc getArc(GraphNode aNode)
	{
		UndirectedArc toReturn = null;
		if(isConnected(aNode))
		{
		Iterator<UndirectedArc> itr = biArcs.iterator();
		while(itr.hasNext())
		{
			toReturn = itr.next();
			if(toReturn.getOtherNode(this) == aNode)
			{
				return toReturn;
			}
		}		
		}
		else
		{
			throw new IllegalArgumentException("Input node is not connected to this node");
		}
		return toReturn;
	}
	
	public List<UndirectedArc> getArcs() {
		return biArcs;
	}
	
	public void clearArcs()
	{
		Iterator<UndirectedArc> itr = biArcs.iterator();
		while(itr.hasNext())
		{
			itr.next().clear();
		}
		Iterator<DirectedArc> itrd = dirInArcs.iterator();
		while(itrd.hasNext())
		{
			itrd.next().clear();
		}
		itrd = dirOutArcs.iterator();
		while(itrd.hasNext())
		{
			itrd.next().clear();
		}
	}
	
	public void addListener(EventListener l)
	{
		listeners.add(l);
	}
	
	public void removeListener(EventListener l)
	{
		listeners.remove(l);
	}
	
	
	/**
	 * Returns a description of the node suitable for display purposes
	 * @return string description of the node
	 */
	public String toString()
	{
		return userObject.toString();
	}
	

}

