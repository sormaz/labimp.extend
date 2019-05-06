package edu.ohiou.mfgresearch.labimp.draw;

/**
 * <p>Title:       ImpObject Class</p>
 * <p>Description: ImpObject class is a root class for all process planning
 *                 related objects in IMPLAN development. It provides for easy
 *                 implementation of applets on any object in IMPLAN development
 *                 It provides basic functionality using which any
 *                 subclass can be displayed in AnimApplet</p>
 * <p>Copyright:   Dr.D.N.Sormaz, Ohio University Copyright (c) 2002</p>
 * <p>Company:     IMSE Dept, Ohio University</p>
 * @author         Dr.Dusan N.Sormaz
 * @version 1.0
 */

import java.io.OutputStream;
import java.io.File;
import java.io.Serializable;



import java.awt.Color;
import java.awt.Graphics2D;

import java.io.*;
import java.lang.reflect.Constructor;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.geom.*;
import java.util.LinkedList;
import javax.swing.tree.DefaultMutableTreeNode;

//Importing classes required for Java 3D methods
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;

import cv97.j3d.loader.VRML97Loader;

//Importing classes from Implanner
import edu.ohiou.mfgresearch.labimp.basis.ImpXmlWriter;
import edu.ohiou.mfgresearch.labimp.basis.ViewObject;
import edu.ohiou.mfgresearch.labimp.basis.GUIApplet;
import edu.ohiou.mfgresearch.labimp.gtk3d.WorldCS;

public abstract class ImpObject extends ViewObject
	implements
		DrawableWF,
		Drawable3D,
		Serializable {
	
	  static {
		  
		  loadProperties (ImpObject.class, "labimp.extend");
		  displayProperties();  	
	  }
	//Root BranchGroup of SceneGraph
	private BranchGroup objRoot;

	public ImpObject() {

	}

	/**
	 *
	 *  Initializes visual components for GUIApplet
	 *
	 */

	public void init() {
		if (needCanvas()) {
		}
		if (needPanel()) {
			panel = new JPanel();
			try {
				if (this.getClass() == Class
					.forName("edu.ohiou.mfgresearch.labimp.draw.ImpObject"))
					panel.add(new JLabel("This is instance of "
						+ this.getClass().toString()));
				else
					panel.add(new JLabel(this.getClass().toString()
						+ " needs to implement init()"));
				panel.setSize(new Dimension(640, 100));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets object's applet to supplied applet
	 *
	 * @param inApplet applet to be assigned
	 */
	public void settApplet(GUIApplet inApplet) {
		applet = inApplet;
	}

	/** @todo dsormaz to wf applet */
	public void setNeedUpdate(boolean needUpdate) {
		if (canvas == null) return;
		if (canvas instanceof DrawableWF) {
			((DrawableWF) canvas).setNeedUpdate(needUpdate);
		} else if (canvas instanceof Drawable3D) {
			//      ( (Drawable3D) canvas).setNeedUpdate(needUpdate);

		} else {
			super.setNeedUpdate(needUpdate);
		}

	}

	// Methods for Drawable3D interface

	/**
	 * Creates a simple rotating color cube as the contentbranch of the default
	 * ImpObject in an Animation Universe
	 *
	 * @return BranchGroup contentBranch of universe
	 */
//	public BranchGroup createSceneGraph() {
//		// Create the root of the branch graph
//		objRoot = new BranchGroup();
//		objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
//		objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
//
//		// Create the transform group node and initialize it to the
//		// identity. Add it to the root of the subgraph.
//		TransformGroup objSpin = new TransformGroup();
//		objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//		objRoot.addChild(objSpin);
//
//		// Create a simple shape leaf node, add it to the scene graph.
//		// ColorCube is a Convenience Utility class
//		objSpin.addChild(new ColorCube(1 * 0.1));
//
//		// Create a new Behavior object that will perform the desired
//		// operation on the specified transform object and add it into
//		// the scene graph.
//		Alpha rotationAlpha = new Alpha(-1, 4000);
//
//		RotationInterpolator rotator = new RotationInterpolator(
//			rotationAlpha,
//			objSpin);
//
//		// a bounding sphere specifies a region a behavior is active
//		// create a sphere centered at the origin with radius of 100
//		BoundingSphere bounds = new BoundingSphere();
//		rotator.setSchedulingBounds(bounds);
//		objSpin.addChild(rotator);
//		return objRoot;
//	}

	
	public BranchGroup createSceneGraph()
	{		
		objRoot = new BranchGroup();
		objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		
//		// Create the transform group node and initialize it to the
//		// identity. Add it to the root of the subgraph.
//		TransformGroup objSpin = new TransformGroup();
//		objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//		objRoot.addChild(objSpin);
		
//		System.out.println("this.getClass().getName() = "+this.getClass().getName());
		String vrmlFileString = properties.getProperty(this.getClass().getName());
		if(vrmlFileString != null)
		{
			File vrmlFile = new File(vrmlFileString);
			VRML97Loader vrmlFileLoader = new VRML97Loader();
			if (vrmlFile != null) 
			{
				try
				{
//					System.out.println("Inside VRML");
					vrmlFileLoader.load(vrmlFile.getCanonicalPath());
					BranchGroup vrmlGroup;
					vrmlGroup = vrmlFileLoader.getBranchGroup();
						
					TransformGroup vrmlTransformGroup = new TransformGroup();
					vrmlTransformGroup.addChild(vrmlGroup);
					objRoot.addChild(vrmlTransformGroup);
				}
				catch (Exception ex) 
				{
					ex.printStackTrace();
				}
			}
		}
		else
		{
			System.err.println(this.getClass().getName()+" node vrml file does not exist");
			Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
			
			Shape3D s3d = new Shape3D();
			String shapeString = properties.getProperty(this.getClass().getName()
					+"."+s3d.getClass().getName());
			if(shapeString != null)
			{
				Class c;
				try 
				{
					c = Class.forName(shapeString);
					Primitive shape = (Primitive) c.newInstance();
					Appearance shapeApp = new Appearance();
					
					if (this.toString().toLowerCase().equals(this.toString())) 
					{
						String sphereColor2 = properties.getProperty(this.getClass().getName()+".shapeColor2."+String.class.getName(), properties.getProperty("defaultShapeColor."+String.class.getName(), ""));
						Color color2 = new Color(Integer.parseInt(sphereColor2, 16));
						shapeApp.setMaterial(new Material(new Color3f(color2), black,
								new Color3f(color2), black, 80.0f));
					}
					else
					{
						String sphereColor = properties.getProperty(this.getClass().getName()+".shapeColor."+String.class.getName(), properties.getProperty("defaultShapeColor."+String.class.getName(), ""));
						Color color = new Color(Integer.parseInt(sphereColor, 16));
						shapeApp.setMaterial(new Material(new Color3f(color), black,
								new Color3f(color), black, 80.0f));
					}
					shape.setAppearance(shapeApp);
					
					TransformGroup shapeTransformGroup = new TransformGroup();
					shapeTransformGroup.addChild(shape);
					objRoot.addChild(shapeTransformGroup);
				}
				catch (Exception e) 
				{
					System.err.print("Class " + shapeString + " can not be loaded");
//					e.printStackTrace();
				}
			}
			else
			{
				System.err.println(this.getClass().getName()+" node shape does not exist");
//				Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
					
				TransformGroup sphereGroup = new TransformGroup();
				Appearance sphereApp = new Appearance();
						
				if (this.toString().toLowerCase().equals(this.toString())) 
				{
					String sphereColor2 = properties.getProperty(this.getClass().getName()+".sphereColor2."+String.class.getName(), properties.getProperty("defaultSphereColor."+String.class.getName(), ""));
					Color color2 = new Color(Integer.parseInt(sphereColor2, 16));
					sphereApp.setMaterial(new Material(new Color3f(color2), black,
							new Color3f(color2), black, 80.0f));
				}
				else
				{
					String sphereColor = properties.getProperty(this.getClass().getName()+".sphereColor."+String.class.getName(), properties.getProperty("defaultSphereColor."+String.class.getName(), ""));
					Color color = new Color(Integer.parseInt(sphereColor, 16));
					sphereApp.setMaterial(new Material(new Color3f(color), black,
							new Color3f(color), black, 80.0f));
				}
				float sphereRadius = Float.parseFloat(properties.getProperty
				(this.getClass().getName()+".sphereRadius."+String.class.getName(), 
				properties.getProperty("defaultSphereRadius."+String.class.getName())));
						
				sphereGroup.addChild (new Sphere(sphereRadius, sphereApp));
				objRoot.addChild(sphereGroup);
			}
		}
		
//		// Create a new Behavior object that will perform the desired
//		// operation on the specified transform object and add it into
//		// the scene graph.
//		Alpha rotationAlpha = new Alpha(-1, 4000);
//
//		RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, objSpin);
//
//		// a bounding sphere specifies a region a behavior is active
//		// create a sphere centered at the origin with radius of 100
//		BoundingSphere bounds = new BoundingSphere();
//		rotator.setSchedulingBounds(bounds);
//		objSpin.addChild(rotator);
		
		return objRoot;
	}
	
	
	//Creates an AnimationGraph
	public BranchGroup createAnimationGraph() {
		return new BranchGroup();
	}

	//Definition to set the universe view point
	public void setUniverseViewPoint(Vector3d inVector) {
	}

	//Definition to start the animation associated with the scene graph
	public void startAnimation() {
	}

	//Definition to stop the animation associated with the scene graph
	public void stopAnimation() {
	}

	//Definition to change the mode of the 3D object to either Wire frame or Solid mode
	public void setAppearance(boolean showWireFrame) {
	}

	/**
	 * Sets the canvas
	 *
	 * @param inCanvas instance of AnimPanel
	 */
	public void settCanvas(AnimPanel inCanvas) {
		canvas = inCanvas;
	}

	public void settCanvas(DrawWFPanel inCanvas) {
		canvas = inCanvas;
	}

	public void paintComponent(Graphics2D g, DrawWFPanel canvas) {
		this.doNothing("in ivieobj paintcomp");

	}

	/**
	 * Returns the node that can be added to some tree for display
	 * @param noLevels number of levels that this object should be expanded
	 *        usually it is reduced by one if it is sent to some members to create
	 *        their own nodes.
	 * @return
	 */
	public DefaultMutableTreeNode geetTree(int noLevels) {
		return new DefaultMutableTreeNode(this);
	}

	public DefaultMutableTreeNode geetTree() {
		return new DefaultMutableTreeNode(this);
	}

	public String makeSchema() {
		return new String();
	}

	public void writeXMLFile(File f) throws IOException {
		FileOutputStream stream = new FileOutputStream(f);
		writeXMLFile(stream);
		stream.close();
		
	}

	public void writeXMLFile(OutputStream stream) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(stream);
		StringBuffer buffer = new StringBuffer();
		writeXMLHeader(buffer);
		writeXML(buffer, "");
		writer.write(buffer.toString());
		writer.close();
	}
	
	public ImpXmlWriter findXMLWriter()  {
		ImpXmlWriter writer = null;
		String writerName = getProperty("XML_WRITER");

		Object[] args = { this };
		if (writerName != null && !writerName.equalsIgnoreCase("")) {
			try {
				Class handlerClass = Class.forName(writerName);
				Constructor c = handlerClass.getConstructor(geettXMLWriterArgTypes());
				writer =(ImpXmlWriter) c.newInstance(args);
//				writer = (ImpXmlWriter) handlerClass.newInstance();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException("XML Writer not specified");
		}

		return writer;
	}
	
	public Class [] geettXMLWriterArgTypes () {
		Class [] argTypes = new Class [] {};
		return argTypes;
	}

  public void makeShapeSets (DrawWFPanel canvas) {
    Color color = this.color;
    if (color == null) {
      String propColor = properties.getProperty(this.getClass().getName() +
                                                ".color", "000000");
      color = new Color(Integer.parseInt(propColor, 16));
    }

    if (canvas instanceof DrawWFPanel) {
      DrawWFPanel drawPanel = (DrawWFPanel) canvas;
      drawPanel.addDrawShapes(color, geetShapeList(drawPanel));

    }

  }

	/**
	 * Generates list of objects to be drawn in WF Canvas.
	 *
	 * @param   canvas   the <code>DrawWFCanvas</code> on which to draw.
	 * @return  the <code>LinkedList</code> of shape object to be drawn
	 *          directly on Canvas
	 * @since   1.2
	 */
	public LinkedList geetShapeList(DrawWFPanel canvas) {
		try {
			if (this.getClass() == Class
				.forName("edu.ohiou.mfgresearch.labimp.draw.ImpObject")) {
				WorldCS cs = new WorldCS();
				return cs.geetShapeList(canvas);
			} else {
				System.out
					.println("Method 'public LinkedList getShapeList(DrawWFPanel canvas)' should be implemented in "
						+ this.getClass().toString());
				if (panel != null && panel.getComponentCount() < 2)
					panel
						.add(new JLabel(
							this.getClass().toString()
								+ " needs to implement method 'public LinkedList getShapeList(DrawWFPanel canvas)'"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new LinkedList();
	}

	public LinkedList geetDrawList() {
		LinkedList list = new LinkedList();
//		list.add(new Line2D.Double(3.2, 4.3, 100.3, 200.5));
    list.add(new Ellipse2D.Double (0,0,1,1));
		return list;
	}

	/**
	 * Main method for testing purposes
	 * This method is implemented in the class to test the class in stand-alone mode
	 *
	 * @param arg array of strings for command line arguments
	 */
	public static void main(String[] arg) {

	}
}
