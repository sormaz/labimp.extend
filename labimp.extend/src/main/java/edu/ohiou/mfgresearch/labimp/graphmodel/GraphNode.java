package edu.ohiou.mfgresearch.labimp.graphmodel;


import java.util.EventListener;
import java.util.List;


/**
 * Interface for the node in a graph 
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Ohio University</p>
 * @author Ganduri
 * @version 1.0
 */
public interface GraphNode {
	
	/**
	 * Adds a node to this node connected by an undirected arc
	 * @param aNode the node to connect to this node 
	 */
	public void addNode(GraphNode aNode);
	
	/**
	 * Removes the relation between the parameter and this node
	 * @param aNode the node whose relation is removed
	 */
	public void removeNode(GraphNode aNode);
	
	/**
	 * Adds a child node connected by a directed arc with this object as parent
	 * @param child child node to this node
	 */
	public void addChild(GraphNode child);
	
	/**
	 * Removes the child node and the arc associated with it
	 * @param child child node to this node
	 * @throws IllegalArgumentException if input node is not a child node
	 */
	public void removeChild(GraphNode child);
	
	/**
	 * Adds a parent node connected by a directed arc with this object as parent
	 * @param parent parent node to this node
	 */
	public void addParent(GraphNode parent);
	
	/**
	 * Removes the parent node and the arc associated with it
	 * @param parent parent node to this node
	 * @throws IllegalArgumentException if input node is not a parent node
	 */
	public void removeParent(GraphNode parent);
	
	/**
	 * Returns a list of graph nodes connected to this node by undirected arcs
	 * @return List of connected GraphNodes
	 */
	public List<GraphNode> getNodes();
	
	/**
	 * Returns a list of parent nodes connected to this node by directed arcs
	 * @return List of parent GraphNodes
	 */
	public List<GraphNode> getParents();
	
	/**
	 * Returns a list of child nodes connected to this node by directed arcs
	 * @return List of child GraphNodes
	 */
	public List<GraphNode> getChildren();
	
	/**
	 * Returns true if this node is root node having no parent nodes
	 * @return true if root node
	 */
	public boolean isRoot();
	
	/**
	 * Returns true if this node is leaf node having no child nodes
	 * @return true if child node
	 */
	public boolean isLeaf();
	
	/**
	 * Returns true if this node has atleast one parent node
	 * @return true if child
	 */
	public boolean isChild();
	
	/**
	 * Returns true if this node is child to the input node
	 * @return true if child
	 */
	public boolean isChild(GraphNode parent);
	
	/**
	 * Returns true if this node has atleast one child node
	 * @return true if parent
	 */
	public boolean isParent();
	
	/**
	 * Returns true if this node is parent to the input node
	 * @param child graph node
	 * @return true if parent
	 */
	public boolean isParent(GraphNode child);
	
	/**
	 * Returns true if this node is connected to atleast one node by an undirected arc
	 * @return true if connected
	 */
	public boolean isConnected();
	
	/**
	 * Returns true if the input node is connected to this node
	 * @param aNode
	 * @return true if input node is connected
	 */
	public boolean isConnected(GraphNode aNode);
	
	/**
	 * Adds the directed arc to this node with this node as child
	 * @param arc the DirectedArc to be added
	 */
	public void addInArc(DirectedArc arc);
	
	/**
	 * Removes the directed arc to this node
	 * @param arc the DirectedArc to be removed
	 */
	public void removeInArc(DirectedArc arc);
	
	/**
	 * Adds the directed arc to this node with this node as parent
	 * @param arc the DirectedArc to be added
	 */
	public void addOutArc(DirectedArc arc);
	
	/**
	 * Removes the directed arc to this node
	 * @param arc the DirectedArc to be removed
	 */
	public void removeOutArc(DirectedArc arc);
	
	/**
	 * Adds the undirected arc to this node
	 * @param arc the UndirectedArc to be added
	 */
	public void addArc(UndirectedArc arc);
	
	/**
	 * Removes the undirected arc to this node
	 * @param arc the UndirectedArc to be removed
	 */
	public void removeArc(Arc arc);
	
	/**
	 * Returns the undirected arc between the input node and this node
	 * @param aNode graphnode
	 * @return the undirected arc between the nodes
	 * @throws IllegalArgumentException if no arc exists
	 */	
	public Arc getArc(GraphNode aNode);
	
	/**
	 * Returns the directed arc between the input node and this node
	 * @param parent graphnode
	 * @return the directed arc between the nodes
	 * @throws IllegalArgumentException if no arc exists
	 */	
	public DirectedArc getInArc(GraphNode parent);
	
	/**
	 * Returns the directed arc between the input node and this node
	 * @param child graphnode
	 * @return the undirected arc between the nodes
	 * @throws IllegalArgumentException if no arc exists
	 */	
	public DirectedArc getOutArc(GraphNode child);
	
	/**
	 * Returns a list of directed arcs to nodes having this node as child node
	 * @return list of directed arcs
	 */	
	public List<DirectedArc> getInArcs();

	/**
	 * Returns a list of directed arcs to nodes having this node as parent node
	 * @return list of directed arcs
	 */	
	public List<DirectedArc> getOutArcs();
	
	/**
	 * Returns a list of undirected arcs to nodes connected to this node
	 * @return list of directed arcs
	 */	
	public List<UndirectedArc> getArcs();
	
	/**
	 * Clears the relations (arcs) between this node and other connected nodes
	 * and removes references to this node in other nodes
	 */
	public void clearArcs();
	
	/**
	 * Sets the user object associated with the node
	 * @param o the user object 
	 * @throws IllegalArgumentException if o is null
	 */
	public void setUserObject(Object o);
	
	/**
	 * Gets the user object associated with the arc
	 * @return user object 
	 */
	public Object getUserObject();
	
	/**
	 * Adds a listener to the graph
	 * @param l an EventListener
	 */
	public void addListener(EventListener l);
	
	/**
	 * Removes the listener to the graph
	 * @param l an EventListener
	 */
	public void removeListener(EventListener l);
	
	
	
}
