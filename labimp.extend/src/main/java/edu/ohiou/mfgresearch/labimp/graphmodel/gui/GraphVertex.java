/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.graphmodel.gui;


import java.awt.geom.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.JPanel;
import edu.ohiou.mfgresearch.labimp.basis.*;
import edu.ohiou.mfgresearch.labimp.basis.GraphicsConfiguration;
import edu.ohiou.mfgresearch.labimp.graphmodel.*;


/**
 * Provides the default functionality for displaying a graph node
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Ohio University</p>
 * @author Chandrasekhar Ganduri
 * @version 1.0
 */
public class GraphVertex implements Drawable2D {

	private String vLabel;
	private GraphRenderer renderer;
	private GraphNode node;
	private Shape nodeShape;
	private Draw2DPanel canvas;
	private Color color = Color.RED;
	private Color fillColor = Color.YELLOW;
	private Point2D.Double nodeLocation;
	private boolean isDefaultShape = true;
	private boolean isShapeSet = false;
	private AffineTransform transform;
	protected GraphicsConfiguration graphics = null;

	private Point2D.Double labelLocation;
	
	
	// Constructors
	public GraphVertex(GraphNode aNode, GraphRenderer renderer)
    {
		this.renderer = renderer;
		setNode(aNode);		
    }    
    
    // My methods	
	public void setLabel(String label)
	{
	        this.vLabel = label;
	}
	 
    public String getLabel()
	{
		 return vLabel;
	}
    
    public void setShape(Shape aShape)
    {
    	nodeShape = aShape;
    	isDefaultShape = true;
    	isShapeSet = true;
    }
    
    public Shape getShape(Shape aShape)
    {
    	return nodeShape;
    }
    
    public Point2D.Double getNodeLocation() {
		return nodeLocation;
	}

	public void setNodeLocation(Point2D.Double nodeLocation) {
		this.nodeLocation = nodeLocation;
	}
	
	public void setNodeLocation(double x, double y)
	{
		setNodeLocation(new Point2D.Double(x, y));		
	}	
	 
	public void setNode(GraphNode node)
	{
		 this.node = node;
		 if(node.getUserObject() instanceof Drawable2D && !renderer.isDefault_behavior())
		 {
			 vLabel = ((Drawable2D)node.getUserObject()).toString();
			isDefaultShape = false;			
		 }
		 else
		 {
			 isDefaultShape = true;
			 vLabel = node.toString();
		 }		
	}
	
	public GraphNode getNode()
	{
		return node;
	}
	 
	public void setColor(Color color)
	{
		 this.color = color;
	}
	 
	public Color getColor(Color getColor)
	{
		 return color;
	}
	 
	public Color getFillColor() 
	{
			return fillColor;
	}

	public void setFillColor(Color fillColor) 
	{
			this.fillColor = fillColor;
	}
	
	// Drawable 2D methods
	public void settCanvas(Draw2DPanel arg0) {
		this.canvas = arg0;
	}

	public JPanel gettCanvas() {
		return canvas;
	}
	
	  public GraphicsConfiguration getGraphicsConfig () {
		  if (graphics == null) {
			  graphics = new GraphicsConfiguration();
			  graphics.configure(this);
		  }
		  return graphics;
		  
	  }


	public void paintComponent(Graphics arg0) {
		System.out.println("In paintComponent 1");
	}

	public void paintComponent(Graphics2D arg0) {
		System.out.println("In paintComponent 2");		
	}

	public void makeDrawSets() {
//		System.out.println("In makeDrawSets of Graph Vertex "+toString());
		if(isDefaultShape && !isShapeSet)
		{
			makeDefaultShape();
			//		System.out.println("After makeDefaultShape: "+ ((Ellipse2D)nodeShape).getCenterX()+", "+ ((Ellipse2D)nodeShape).getCenterY());
		}
		canvas.addDrawShapes(color, getDrawList());
		canvas.addFillShapes(fillColor, getFillList());
	}

	@SuppressWarnings("unchecked")
	public LinkedList<Shape> getDrawList() {
		LinkedList<Shape> toReturn = new LinkedList<Shape>();
		if(isDefaultShape)
		{
			toReturn.add(nodeShape);
		}
		else
		{
//			System.out.println("In getDraw List with userObject");
			Drawable2D toDraw = (Drawable2D)node.getUserObject();
			toReturn.addAll(getTransformedList(toDraw.getDrawList().iterator()));					
		}				
		return toReturn;
	}

	@SuppressWarnings("unchecked")
	public LinkedList<Shape> getFillList() {
		LinkedList<Shape> toReturn = new LinkedList<Shape>();
		if(isDefaultShape)
		{
		  toReturn.add(nodeShape);
		}
		else
		{
			Drawable2D toDraw = (Drawable2D)node.getUserObject();
			toReturn.addAll(getTransformedList(toDraw.getFillList().iterator()));
		}				
		return toReturn;
	}
	
	@SuppressWarnings("unchecked")
	private LinkedList getTransformedList(Iterator drawList)
	{
		LinkedList toReturn = new LinkedList();
		Object userObjectShape;
		Drawable2D userObject =(Drawable2D) node.getUserObject();
		Shape toAdd = null;
		DrawString toAddString;
		transform = AffineTransform.getTranslateInstance(nodeLocation.getX()-userObject.gettPosition().getX(),
				nodeLocation.getY()-userObject.gettPosition().getY());
		while(drawList.hasNext())
		{
			userObjectShape = drawList.next();
			if(userObjectShape instanceof Shape)
			{
				toAdd = (Shape)userObjectShape;
				toAdd = transform.createTransformedShape(toAdd);
				toReturn.add(toAdd);
			}	
			else if(userObjectShape instanceof DrawString)
			{
				toAddString = (DrawString)userObjectShape;
				toAddString.transform(transform);
				toReturn.add(toAddString);
			}			
		}					
	return toReturn;
	}

	@SuppressWarnings("unchecked")
	public LinkedList<DrawString> getStringList() 
	{
		LinkedList<DrawString> toReturn = new LinkedList<DrawString>();
		if(node.getUserObject() instanceof Drawable2D && !renderer.isDefault_behavior())
		{
			Drawable2D toDraw = (Drawable2D)node.getUserObject();
			toReturn.addAll(getTransformedList(toDraw.getStringList().iterator()));
		}
		else
		{
			toReturn.add(new DrawString(vLabel, new java.lang.Float(getLabelLocation().getX()), new java.lang.Float(getLabelLocation().getY())));
		}
		return toReturn;
	}
	
	
	public void addButtonOptions(int options) {
		
		canvas.addButtonOptions (options);
	}

	
	public void removeButtonOptions(int options) {
		
		canvas.removeButtonOptions (options);
	}

	
	public void makeDefaultShape()
	{
			nodeShape = new Ellipse2D.Double(nodeLocation.getX() - GraphConstants.RADIUS ,
			nodeLocation.getY() - GraphConstants.RADIUS, 2*GraphConstants.RADIUS, 2*GraphConstants.RADIUS);	
	}
	
	public void setNeedUpdate(boolean arg0) {}

	public Point2D gettPosition() {
		return nodeLocation;
	}

  public void settPosition(Point2D point) {
	  nodeLocation = (Point2D.Double) point;
	  List<Arc> arcList = new ArrayList<Arc>();
	  arcList.addAll(node.getArcs());
	  arcList.addAll(node.getInArcs());
	  arcList.addAll(node.getOutArcs());
	  Iterator<Map.Entry<Arc, GraphEdge>> itr = renderer.getArcEdgeMapIterator();
	  Map.Entry<Arc, GraphEdge> me;
	  while(itr.hasNext())
	  {
		  me = itr.next();
		  if(arcList.contains(me.getKey()))
		  {
			 me.getValue().setNeedUpdate(true);
		  }
	  }
	  
  }
  
  public void setLabelLocation(double x, double y)
  {
	  this.labelLocation = new Point2D.Double(x,y);
  }
  
  public void setLabelLocation(Point2D.Double labelLoc)
  {
	  this.labelLocation = labelLoc;
  }
  
  public Point2D getLabelLocation()
  {
	  if(labelLocation == null)
		  return nodeLocation;
	  else return labelLocation;
  }
  
  public void generateImageList () {
  }


	@SuppressWarnings("unchecked")
	public Collection giveSelectables() {
		Collection toReturn = new ArrayList();
		toReturn.add(this);		
		return toReturn;
	}		
  
  public String toToolTipString () {
    if (node.getUserObject() instanceof Viewable) {
      return ((Viewable) node.getUserObject()).toToolTipString();
    }
    if (node.getUserObject() instanceof Drawable2D) {
        return ((Drawable2D) node.getUserObject()).toToolTipString();
    }
    return node.getUserObject().toString();
  }
}