package edu.ohiou.mfgresearch.labimp.graphmodel;



import java.util.*;

import javax.swing.table.AbstractTableModel;

/**
 * This interface specifies the APIs for a graph model
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Ohio University</p>
 * @author Chandrasekhar Ganduri
 * @version 1.0
 */
public interface GraphModel {
	
	/**
	 * Adds a node to the graph
	 * @param node Graph node
	 */
	public void addNode(GraphNode node);
	
	/**
	 * Removes a node from the graph
	 * @param node Graph node
	 */
	public void removeNode(GraphNode node);
	
	/**
	 * Adds an undirected arc to the graph
	 * @param node1 graphnode
	 * @param node2 graphnode
	 */
	public void addBiArc(GraphNode node1, GraphNode node2);
	
	/**
	 * Adds an directed arc to the graph
	 * @param source graph node
	 * @param sink graph node
	 */
	public void addDiArc(GraphNode source, GraphNode sink);
	
	/**
	 * Removes an undirected arc from the graph
	 * @param arc undirected arc
	 */
	public void removeArc(UndirectedArc arc);
	
	/**
	 * Removes a directed arc from the graph
	 * @param arc directed arc
	 */
	public void removeArc(DirectedArc arc);
	
	/**
	 * Tests whether a graph node exists in the graph
	 * @param node graph node
	 * @return true if node exists
	 */
	public boolean nodeExists(GraphNode node);
	
	/**
	 * Tests whether an arc exists between two nodes in the graph
	 * @param node1 graph node
	 * @param node2 graph node
	 * @return true if arc exists
	 */
	public boolean arcExists(GraphNode node1, GraphNode node2);
	
	/**
	 * Tests whether a graph node is connected to other nodes by undirected or directed arcs
	 * @param node graph node
	 * @return true if node exists
	 */
	public boolean nodeHasArcs(GraphNode node);
	
	public GraphNode getNode(Object userObject);
	
	/**
	 * Tests whether a graph node is connected to other nodes by directed arcs
	 * @param node graph node
	 * @return true if node exists
	 */
	public boolean nodeHasDirectedArcs(GraphNode node);
	
	/**
	 * Returns an iterator over the nodes connected to the input node
	 * @param node graph nodes
	 * @return an iterator
	 */
	public Iterator<GraphNode> getConnectedNodes(GraphNode node);
	
	/**
	 * Returns an iterator over the children nodes to the input node
	 * @param node graph nodes
	 * @return an iterator
	 */
	public Iterator<GraphNode> getChildNodes(GraphNode node);
	
	/**
	 * Returns an iterator over the parent nodes to the input node
	 * @param node graph nodes
	 * @return an iterator
	 */
	public Iterator<GraphNode> getParentNodes(GraphNode node);
	
	/**
	 * Returns an iterator over all the nodes in the graph model
	 * @param node graph nodes
	 * @return an iterator
	 */
	public Iterator<GraphNode> getNodes();
	
	
	/**
	 * Returns a subGraph based on the specified list of nodes
	 * @param nodes graph nodes for the subgraph
	 */
	public GraphModel subGraph(List<GraphNode> nodes);
	
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
	
	/**
	 * Returns the graph as an AbstractTableModel type
	 * @return AbstractTableModel
	 */
	public AbstractTableModel getTableModel();
	
	/**
	 * Returns an iterator over the set of  DirectedArcs present in the graph model
	 * @return list of directed arcs
	 */
	public Iterator<DirectedArc> getDirectedArcs();
	
	/**
	 * Returns an iterator over the set of UndirectedArcs present in the graph model
	 * @return list of undirected arcs
	 */
	public Iterator<UndirectedArc> getUndirectedArcs();
	
}
