/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.graphmodel;

import java.util.*;

/**
 * Provides the default functionality for directed arc and implements the DirectedArc interface.
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Ohio University</p>
 * @author Chandrasekhar Ganduri
 * @version 1.0
 */
public class DefaultDirectedArc implements DirectedArc {
	
	GraphNode source;
	GraphNode sink;
	Object userObject;
	List<EventListener> listeners = new ArrayList<EventListener>();
	
	
	/**
	 * Constructor for the directed arc going from source to sink
	 * @param source first node
	 * @param sink second node
	 */
	public DefaultDirectedArc(GraphNode source, GraphNode sink) {
		this.source = source;
		this.sink = sink;
		source.addOutArc(this);
		sink.addInArc(this);
	}
	
	public void clear()
	{
		source.removeOutArc(this);
		source.removeInArc(this);
		source = null;
		sink = null;
	}

	public GraphNode getParent() {
		return source;
	}

	public GraphNode getChild() {		
		return sink;
	}

	public void setParent(GraphNode parent) {
		source = parent;
		source.addOutArc(this);
	}

	public void setChild(GraphNode child) {
		sink = child;
		sink.addInArc(this);
	}

	public boolean nodeExists(GraphNode aNode) {
		if(source == aNode || sink == aNode)
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
