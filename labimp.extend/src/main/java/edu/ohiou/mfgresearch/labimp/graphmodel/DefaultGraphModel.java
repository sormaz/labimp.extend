/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.graphmodel;


import java.util.*;

import javax.swing.table.AbstractTableModel;

import edu.ohiou.mfgresearch.labimp.graphmodel.gui.GraphRenderer;
import edu.ohiou.mfgresearch.labimp.table.SquareTableModel;
import edu.ohiou.mfgresearch.labimp.table.TableCellGenerator;


/**
 * Default implementation for the GraphModel Interface
  * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Ohio University</p>
 * @author Chandrasekhar Ganduri
 * @version 1.0
 */
public class DefaultGraphModel implements GraphModel {

	private List<GraphNode> nodes = new ArrayList<GraphNode>();
	private List<EventListener> listeners = new ArrayList<EventListener>();
	private SquareTableModel tableModel;
	private Map<Object, GraphNode> tableNodeMap = new HashMap<Object, GraphNode>();
	private boolean tableModelExists = false;
	private boolean makeStartNode = false;
	private boolean makeEndNode = false;
	
	
	/**
	 * Constructor for initialization with object array, table cell generator 
	 * specifying relationships and specification for start and end nodes
	 * @param objs Object array
	 * @param TableCellGenerator arcGenerator
	 * @param makeStartNode boolean for having root node
	 * @param makeEndNode boolean for having end Node
	 */
	public DefaultGraphModel(Object[] objs, TableCellGenerator arcGenerator,
			boolean makeStartNode,
			boolean makeEndNode) {
			this(
				new SquareTableModel(objs, arcGenerator),
				makeStartNode,
				makeEndNode);
	}
	
	/**
	 * Constructor for initialization with object array, table cell generator 
	 * specifying relationships and specification for start and end nodes
	 * @param objs List of objects that make the graph
	 * @param TableCellGenerator arcGenerator
	 * @param makeStartNode boolean for having root node
	 * @param makeEndNode boolean for having end Node
	 */
	public DefaultGraphModel(List objs, TableCellGenerator arcGenerator,
			boolean makeStartNode,
			boolean makeEndNode) {
			this(objs.toArray(), arcGenerator,			
				makeStartNode,
				makeEndNode);
	}
	
	
	/**
	 * Constructor for initialization with a square table model
	 * @param tablemodel the square table model
	 */
	public DefaultGraphModel(SquareTableModel tableModel)
	{
		this (tableModel, false, false);
	}
	

	
	
	/**
	 * Constructor for initialization with a square table model and specification
	 * for end nodes
	 * @param tablemodel the square table model
	 * @param makeStartNode boolean for having root node
	 * @param makeEndNode boolean for having end Node
	 */
	public DefaultGraphModel(SquareTableModel tableModel,
			boolean makeStartNode,
			boolean makeEndNode)
	{
		this.tableModel = tableModel;
//		tableModel.display();
		tableModelExists = true;
		createNodesFromList(tableModel.getColumnObjects());
		createArcsFromTable();
//		tableModel.display();
		this.makeStartNode = makeStartNode;
		this.makeEndNode = makeEndNode;
	}	
	
	public AbstractTableModel getTableModel()
	{
		return tableModel;
	}
	
	/**
	 * Creates nodes from the inputList and add nodes to graph and tableNodeMap.
	 * @param objs List of objects 
	 */
	
	public void createNodesFromList(List objs)
	{
		if(!tableModelExists)
			tableModel = new SquareTableModel(objs.toArray());
		Iterator itr = objs.iterator();
		while(itr.hasNext())
		{
			Object userObject = itr.next();
			GraphNode node = new DefaultGraphNode(userObject);
			nodes.add(node);
			tableNodeMap.put(userObject, node);
			
		}
	}
	
	/**
	 * Creates Arcs between created nodes based on the table model
	 * @param objs List of objects 
	 */
	public void createArcsFromTable()
	{
		
		for(int i = 0; i < tableModel.getRowCount(); i++)
		{
			GraphNode n1 = tableNodeMap.get(tableModel.getColumnObjects().get(i));
			for(int j = 0; j < tableModel.getColumnCount() - 1; j++)
			{
				
				GraphNode n2 = tableNodeMap.get(tableModel.getColumnObjects().get(j));
				Object o = tableModel.getValueAt(i, j+1); // j+1 because squaretable model is out of sync
				if(o instanceof Boolean)
				{
					if(((Boolean)o).booleanValue() == true)
					{
			            new DefaultDirectedArc(n1, n2); //addDiArc(n1, n2);
					}
				}
				else if(o instanceof Integer)
				{
					if(((Integer)o).intValue() == 1)
					{	
			            new DefaultDirectedArc(n1, n2); //addDiArc(n1, n2);
			        }
					else if(((Integer)o).intValue() == 2)
					{
		            new DefaultUndirectedArc(n1, n2);				//		addBiArc(n1, n2);
           			}					
				}
				else if(o != null)
				{
     				DirectedArc temp = new DefaultDirectedArc(n1, n2);
					temp.setUserObject(o);	
				}				
			}
		}
	}
	
	
	public boolean makeStartNode()
	{
		return makeStartNode;
	}
	
	public boolean makeEndNode()
	{
		return makeEndNode;
	}
	public void addNode(GraphNode node) {
		nodes.add(node);
		if(!tableModelExists)
			tableModel = new SquareTableModel(null);
		tableModel.addObject(node.getUserObject());
		tableNodeMap.put(node.getUserObject(), node);		
	}

	public GraphNode getNode(Object userObject)
	{
		return tableNodeMap.get(userObject);
	}
	
	public void removeNode(GraphNode node) {
		nodes.remove(node);
		node.clearArcs();
		tableModel.deleteColumn(tableModel.findColumn(node.getUserObject()));
	}

	public void addBiArc(GraphNode node1, GraphNode node2) {
		new DefaultUndirectedArc(node1, node2);
		tableModel.setValueAt(tableModel.findRow(node1.getUserObject()), 
				tableModel.findColumn(node2.getUserObject()), DefaultGraphTableCellGenerator.BIDIRECTED_RELATION);
		tableModel.setValueAt(tableModel.findRow(node2.getUserObject()), 
				tableModel.findColumn(node1.getUserObject()), DefaultGraphTableCellGenerator.BIDIRECTED_RELATION);

	}

	public void addDiArc(GraphNode source, GraphNode sink) {
		new DefaultDirectedArc(source, sink);	
		tableModel.setValueAt(tableModel.findRow(source.getUserObject()), 
				tableModel.findColumn(sink.getUserObject()), DefaultGraphTableCellGenerator.DIRECTED_RELATION);
	}

	public void removeArc(UndirectedArc arc) {
		arc.clear();
		tableModel.setValueAt(DefaultGraphTableCellGenerator.NO_RELATION, 
				tableModel.findRow(arc.getNodes().get(0).getUserObject()),
				tableModel.findColumn(arc.getNodes().get(1).getUserObject()));
		tableModel.setValueAt(DefaultGraphTableCellGenerator.NO_RELATION, 
				tableModel.findRow(arc.getNodes().get(1).getUserObject()),
				tableModel.findColumn(arc.getNodes().get(0).getUserObject()));
	}

	public void removeArc(DirectedArc arc) {
		arc.clear();
		tableModel.setValueAt(DefaultGraphTableCellGenerator.NO_RELATION, 
				tableModel.findRow(arc.getParent().getUserObject()),
				tableModel.findColumn(arc.getChild().getUserObject()));
	}
	
	public Iterator<DirectedArc> getDirectedArcs() {
		Set<DirectedArc> arcSet = new HashSet<DirectedArc>();
		Iterator<GraphNode> itr = nodes.iterator();
		GraphNode temp;
		while(itr.hasNext())
		{
			temp = itr.next();
			arcSet.addAll(temp.getInArcs());
			arcSet.addAll(temp.getOutArcs());
		}
		return arcSet.iterator();
	}
	
	public Iterator<UndirectedArc> getUndirectedArcs() {
		Set<UndirectedArc> arcSet = new HashSet<UndirectedArc>();
		Iterator<GraphNode> itr = nodes.iterator();		
		while(itr.hasNext())
		{
			arcSet.addAll(itr.next().getArcs());			
		}
		return arcSet.iterator();
	}
	

	public GraphModel subGraph(List<GraphNode> nodes) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addListener(EventListener l) {
		listeners.add(l);
	}
	
	public void removeListener(EventListener l) {
		listeners.remove(l);		
	}
	
	public boolean nodeExists(GraphNode node) {
		return nodes.contains(node);
	}

	public boolean arcExists(GraphNode node1, GraphNode node2) {
		return (node1.isConnected(node2) || node1.isChild(node2) || node2.isParent(node1));
	}

	public boolean nodeHasArcs(GraphNode node) {
		return (node.isConnected() || node.isChild() || node.isParent());
	}
	
	public boolean nodeHasDirectedArcs(GraphNode node)
	{
		return node.isChild();
	}

	public Iterator<GraphNode> getConnectedNodes(GraphNode node) {
		if(!nodeExists(node))
			throw new IllegalArgumentException("Graph node does not exist in graph");
		return node.getNodes().iterator();
	}

	public Iterator<GraphNode> getChildNodes(GraphNode node) {
		if(!nodeExists(node))
			throw new IllegalArgumentException("Graph node does not exist in graph");
		return node.getChildren().iterator();
	}

	public Iterator<GraphNode> getParentNodes(GraphNode node) {
		if(!nodeExists(node))
			throw new IllegalArgumentException("Graph node does not exist in graph model");
		return node.getParents().iterator();
	}
	
	public Iterator<GraphNode> getNodes()
	{
		return nodes.iterator();
	}

/**
 * 
 * @deprecated in new graph model use listeners to achieve this
 *
 */
	public void display () {
		display(toString());
	}

	
	/**
	 * 
	 * @deprecated in new graph model use listeners to achieve this
	 *
	 */
		public void display (String title) {
			GraphRenderer renderer = new GraphRenderer(this, false);		
			renderer.display(title);
		}

	

}
