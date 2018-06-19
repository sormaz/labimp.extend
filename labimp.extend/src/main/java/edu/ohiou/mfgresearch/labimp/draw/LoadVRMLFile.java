package edu.ohiou.mfgresearch.labimp.draw;

/**
 * <p>Title:       LoadVRMLFile class</p>
 * <p>Description: This class adds gui components onto target panel of AnimApplet
 *                 capable of displaying a given vrml file into a new universe or
 *                 same universe</p>
 * <p>Copyright:   Dr.D.N.Sormaz, Ohio University Copyright (c) 2003</p>
 * <p>Company:     IMSE Dept, Ohio University</p>
 * @author         Dr.Dusan N.Sormaz + Deepak Pisipati
 * @version 1.0
 */

//importing classes for gui components
import java.awt.event.ActionEvent;

import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.media.j3d.*;

//importing classes for importing VRML files
import cv97.j3d.loader.VRML97Loader;
import cv97.j3d.*;


public class LoadVRMLFile extends ImpObject  {
	//vrml file to show in applet
	private File vrmlFile;
	private JFileChooser vrmlFileChooser;
	private JCheckBox addToNewUniverseCheckBox;
	private BranchGroup objRoot;
	private BranchGroup vrmlGroup;

	/**
	 *
	 *    Default Constructor
	 *   
	 */

	public LoadVRMLFile() {

	}

	/**
	 *
	 *    Initializes gui components of this in AnimApplet
	 *   
	 */

	public void init() {
		panel = new JPanel();
		panel.setBorder(BorderFactory.createEtchedBorder());
		JButton openVRMLFileButton = new JButton("Open VRML File");
		vrmlFileChooser = new JFileChooser();
		BorderLayout mainBorderLayout = new BorderLayout();
		BorderLayout newUniverseBorderLayout = new BorderLayout();
		addToNewUniverseCheckBox = new JCheckBox(
			"Check to add next VRML file to New Universe",
			false);
		addToNewUniverseCheckBox.setSelected(false);
		panel.setLayout(mainBorderLayout);
		panel.add(openVRMLFileButton, BorderLayout.WEST);
		panel.add(addToNewUniverseCheckBox, BorderLayout.EAST);
		openVRMLFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openVRMLFileButtonActionPerformed(e);
			}
		});
		addToNewUniverseCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			}
		});
	}

	/**
	 *
	 *    Creates content branch graph when provided with a vrml file
	 *   
	 */

	public BranchGroup createSceneGraph() {
		objRoot = new BranchGroup();
		objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		VRML97Loader vrmlFileLoader = new VRML97Loader();
		if (vrmlFile != null) {
			try {
				vrmlFileLoader.load(vrmlFile.getCanonicalPath());
				vrmlGroup = vrmlFileLoader.getBranchGroup();
				objRoot.addChild(vrmlGroup);
				objRoot.compile();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return objRoot;
	}

	/**
	 * Calls createSceneGraph() and adds it to the existing universe or new universe
	 * depending on whether the user checks the 'newuniversecheckbox'
	 */
	void openVRMLFileButtonActionPerformed(ActionEvent e) {
		int optionChosen = vrmlFileChooser.showOpenDialog(geettApplet());
		if (optionChosen == JFileChooser.APPROVE_OPTION) {
			vrmlFile = vrmlFileChooser.getSelectedFile();
			if (!addToNewUniverseCheckBox.isSelected())
				((AnimPanel) ((AnimApplet) geettApplet()).getCanvas())
					.addContentBranchGroupToSameUniverse(createSceneGraph());
			else
				((AnimPanel) ((AnimApplet) geettApplet()).getCanvas())
					.addContentBranchGroupToNewUniverse(createSceneGraph());
		}
	}

	/**
	 * Changes the mode of the 3D object to either Wire frame or Solid mode
	 *
	 * @param wireOrSolid boolean value
	 */
	public void setAppearance(boolean isWireFrame) {
		PolygonAttributes pa = new PolygonAttributes();
		Appearance app = new Appearance();
		if (isWireFrame)
			pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		else
			pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
		app.setPolygonAttributes(pa);
		try {
			((ShapeNodeObject) ((GroupNodeObject) vrmlGroup.getChild(0))
				.getChild(0)).setAppearance(app);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *
	 *    Returns string representation of this
	 *   
	 */

	public String toString() {
		return "Vrml File Opened in AnimApplet:" + vrmlFile.toString();
	}

	public static void main(String[] args) {
		LoadVRMLFile loadVRMLExample1 = new LoadVRMLFile();
		loadVRMLExample1.settApplet(new AnimApplet(loadVRMLExample1));
		loadVRMLExample1.display("VRML File Chooser", new Dimension(700, 700));
	}
}