/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.graphmodel.gui;

import java.awt.geom.Point2D;
import java.util.*;
import edu.ohiou.mfgresearch.labimp.graphmodel.*;

/**
 * @author Ganduri
 *
 */
public class RandomLayouter extends AbstractLayouter {

	private Random randGen = new Random();
	
//	private RandomLayouter () {
//		
//	}
	
	public RandomLayouter(GraphRenderer renderer)
	{
//		this ();
		if (renderer == null) 
			throw new IllegalArgumentException ("Can not pass null as renderer argument!");
		setRenderer (renderer);
		vertexLocMap = new HashMap<GraphVertex, Point2D>();
	}
	
	
	
	public void makeLayout()
	{
		Iterator<GraphVertex> itr = renderer.getVertexIterator();
		Iterator<GraphVertex> itr1 = vertexLocMap.keySet().iterator();
		GraphVertex temp;
		GraphVertex tempPositioned;
		Point2D.Double tempLoc;
		while(itr.hasNext())
		{
			temp = itr.next();
			tempLoc = new Point2D.Double(randGen.nextInt(limit), randGen.nextInt(limit));
			while(itr1.hasNext())
			{
				tempPositioned = itr1.next();
				while(tempLoc.distance(vertexLocMap.get(tempPositioned)) < GraphConstants.NODE_SEPARATION)
				{
					tempLoc = new Point2D.Double(randGen.nextInt(100), randGen.nextInt(100));
				}
			}
			
			temp.setNodeLocation(tempLoc);
			vertexLocMap.put(temp, tempLoc);
		}
	}
	
	public Point2D getVertexLocation(GraphVertex aVertex) {
		return vertexLocMap.get(aVertex);
	}
}
