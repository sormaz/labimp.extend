package edu.ohiou.mfgresearch.labimp.graphmodel;

import java.util.EventListener;
import java.util.List;

public interface Arc {

	
	/**
	 * Determines whether a node belongs to the arc
	 * @param aNode graph node 
	 */
	public abstract boolean nodeExists(GraphNode aNode);

	/**
	 * Clears the arc and removes the relations in the nodes 
	 */
	public abstract void clear();

	/**
	 * Sets the user object associated with the arc
	 * @param o the user object 
	 * @throws IllegalArgumentException if o is null
	 */
	public abstract void setUserObject(Object o);

	/**
	 * Gets the user object associated with the arc
	 * @return user object 
	 */
	public abstract Object getUserObject();

	/**
	 * Adds a listener to the graph
	 * @param l an EventListener
	 */
	public abstract void addListener(EventListener l);

	/**
	 * Removes the listener to the graph
	 * @param l an EventListener
	 */
	public abstract void removeListener(EventListener l);

}