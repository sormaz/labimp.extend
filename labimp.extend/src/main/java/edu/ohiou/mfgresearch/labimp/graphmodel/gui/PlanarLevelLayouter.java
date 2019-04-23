package edu.ohiou.mfgresearch.labimp.graphmodel.gui;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Provides a planar layout of the graph
  * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Ohio University</p>
 * @author Chandrasekhar Ganduri
 * @version 1.0
 */
public class PlanarLevelLayouter extends AbstractLayouter {

	public static final int DOWNWARD = 0;
	public static final int UPWARD = 1;
	private Map<GraphVertex, Point2D> vertexLocMap;
	private Map<GraphVertex, Integer> vertexLevelMap;
	private GraphRenderer renderer;
	GraphVertex startVertex;
	GraphVertex endVertex;
	
		
	
	public PlanarLevelLayouter(GraphRenderer renderer)
	{
		this.renderer = renderer;
		vertexLocMap = new HashMap<GraphVertex, Point2D>();
		makeLevels();
	
	}
	
	private void makeLevels()
	{
		// Identify vertices without children
		List<GraphVertex> parentLess = new ArrayList<GraphVertex>();
		Iterator<GraphVertex> itr = renderer.getVertexIterator();
		GraphVertex aVertex;
		
		while(itr.hasNext())
		{
			aVertex = itr.next();
			if(aVertex.getNode().isRoot())
			{
				parentLess.add(aVertex);
				vertexLevelMap.put(aVertex, 1);
			}
		}
		// For each in childless, set levels
		itr = parentLess.iterator();
		GraphVertex child;
		int counter = 2;
		Point2D.Double verLoc;
		while(itr.hasNext())
		{
			aVertex = itr.next();
			vertexLevelMap.put(aVertex, counter++);
			while(aVertex.getNode().isParent())
			{
				child = renderer.getGraphVertex(aVertex.getNode().getChildren().get(0));
//				verLoc = new Point2D.Double(aVertex.getNodeLocation().getX()+GraphConstants.NODE_SEPARATION, aVertex.getNodeLocation().getY());
//				child.setNodeLocation(verLoc);
//				vertexLocMap.put(aVertex, verLoc);
//				aVertex = child;				
			}			
		}
	}
	
	
	public Point2D getVertexLocation(GraphVertex aVertex) {
		// TODO Auto-generated method stub
		return null;
	}

	public void makeLayout() {
		// TODO Auto-generated method stub

	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
