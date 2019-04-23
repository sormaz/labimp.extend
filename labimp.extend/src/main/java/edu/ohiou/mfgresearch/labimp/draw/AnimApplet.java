package edu.ohiou.mfgresearch.labimp.draw;

/**
 * <p>Title:       AnimApplet class</p>
 * <p>Description: Class to display animated or 3D objects </p>
 * <p>Copyright:   Dr.D.N.Sormaz, Ohio University Copyright (c) 2002</p>
 * <p>Company:     IMSE Dept, Ohio University</p>
 * @author         Dr.Dusan N.Sormaz+Prashanth Borse
 * @version 1.0
 */

//Importing classes for gui components

import javax.vecmath.Vector3d;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.*;

//Importing classes from IMPLAN
import edu.ohiou.mfgresearch.labimp.basis.*;

import javax.vecmath.*;

public class AnimApplet extends GUIApplet {

	protected AnimPanel canvas;

	/*
	 * In order to create a link between the AnimApplet and a class
	 *   implementing Drawable3D, following varible, private Drawable3D linkTarget;
	 *   declaration is
	 *   required.
	 *  
	 */

	protected Drawable3D linkTarget;

	/**
	 *
	 *    Default Constructor for AnimApplet.
	 *   
	 */

	public AnimApplet() {
		this(null);
	}

	/**
	 * Constructor for AnimApplet with Drawable3D parameter.
	 */
	public AnimApplet(Drawable3D target) {
		super((Viewable) target);
		this.linkTarget = target;
		canvas = new AnimPanel((Drawable3D) target, this);
	}

	/** Constructor taking DrawableWF target, view point and scale.
	 *
	 */
	public AnimApplet(Drawable3D target, Vector3d viewPoint) {
		super((Viewable) target);
		this.linkTarget = target;
		canvas = new AnimPanel(target, this, viewPoint, AnimPanel.SOLID);
	}

	/**
	 *
	 *    Initializes gui components of this
	 *   
	 */

	public void init() {
		super.init();
		try {
			target.settApplet(this);
			((Drawable3D) target).settCanvas(canvas);
			canvas.setTarget((Drawable3D) target);
			appletFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent event) {
					System.exit(0);
				}
			});
		} catch (NullPointerException e) {
		}
	}

	/**
	 *
	 *    Gets the canvas
	 *   
	 *    @return JPanel canvas of this
	 *   
	 */

	public JPanel getCanvas() {
		return canvas;
	}

	/**
	 * Sets the canvas of this with the supplied AnimPanel
	 *
	 * @param inCanvas instance of AnimPanel
	 */
	public void setCanvas(AnimPanel inCanvas) {
		canvas = inCanvas;
	}

	/**
	 *
	 *    Gets Applet information
	 *   
	 */

	public String getAppletInfo() {
		return "Applet Information";
	}

	/**
	 *
	 *    Gets the parameter information
	 *   
	 */

	public String[][] getParameterInfo() {
		return null;
	}

	/**
	 *
	 *    Gets the size of this
	 *   
	 *    @return Dimension dimension of this
	 *   
	 */

	public Dimension geetAppletSize() {
		Dimension targetSize = (target != null)
			? target.geetAppletSize()
			: this.defaultGUIAppletSize;
		Dimension animPanelSize = this.getContentPane().getSize();
		return new Dimension(
			Math.max(targetSize.width, animPanelSize.width),
			targetSize.height + animPanelSize.height);
	}

	/**
	 *
	 *    Starts this
	 *   
	 */

	public void start() {
		//    this.getContentPane().add (canvas, BorderLayout.CENTER);
		//    this.getContentPane().add (target.getPanel(), BorderLayout.SOUTH);
		JSplitPane contentPane = new JSplitPane();
		contentPane.setDividerLocation(500);
		contentPane.setDividerSize(5);
		if (target != null) {
			//        addPanel();
			contentPane.setOrientation(target.getPanelOrientation());
			contentPane.setRightComponent(target.geettPanel());
		} else {
			contentPane.setRightComponent(new JLabel(
				"This applet does not have target",
				SwingConstants.CENTER));
			contentPane.setOrientation(SwingConstants.HORIZONTAL);
		}
		contentPane.setLeftComponent(canvas);
		setContentPane(contentPane);

	}

	public static void main(String args[]) {
		AnimApplet aa = new AnimApplet();
		aa.display("test");
	}
}
