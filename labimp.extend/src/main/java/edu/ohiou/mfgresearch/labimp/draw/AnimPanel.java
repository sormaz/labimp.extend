package edu.ohiou.mfgresearch.labimp.draw;

/**
 * <p>Title:       AnimPanel class</p>
 * <p>Description: Class to display animated or 3D objects </p>
 * <p>Copyright:   Dr.D.N.Sormaz, Ohio University Copyright (c) 2003</p>
 * <p>Company:     IMSE Dept, Ohio University</p>
 * @author         Dr.Dusan N.Sormaz+Prashanth Borse+Deepak Pisipati
 * @version 1.0
 * @see	           AnimApplet
 * @see	           Drawable3D
 * @see	           ImpObject
 */

//Importing classes for gui components
import javax.media.j3d.Bounds;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Color;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.BranchGroup;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;
import javax.vecmath.Vector3d;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//Importing packages required for Java 3D methods
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;

//Importing classes from IMPLAN
import edu.ohiou.mfgresearch.labimp.basis.Viewable;
import edu.ohiou.mfgresearch.labimp.basis.GUIApplet;
import edu.ohiou.mfgresearch.labimp.gtk3d.*;

public class AnimPanel extends JPanel implements Drawable3D {

	//Static variables
	private static final Vector3d DEFAULT_VIEW_POINT = new Vector3d(0, 5, 50); // instead of 0 4 23
	private static final BoundingSphere DEFAULT_BOUNDING_SPHERE = new BoundingSphere(
		new Point3d(0.0, 0.0, 0.0),
		1000);
	public static final boolean SOLID = false;
	public static final boolean WIREFRAME = true;

	//Variables to interact with applet and target
	protected Drawable3D target;
	protected AnimApplet applet;

	//Java3D variables to help display the target
	private Canvas3D canvasFor3D;
	private SimpleUniverse animUniverse;
	private GraphicsConfiguration config = SimpleUniverse
		.getPreferredConfiguration();
	private BranchGroup contentBranchGroup;
	private BranchGroup rootBranchGroup;
	private BranchGroup axisBranchGroup;
	private TransformGroup axisMouseBehaviorTransformGroup;
	private Background background;
	private BranchGroup backgroundBranchGroup;
	private boolean animFlag = true;

	//  private boolean showWireFrame;

	//Gui variables
	private JPanel viewPanel = new JPanel();
	private BorderLayout mainPanelBorderLayout = new BorderLayout();
	private GridLayout viewPanelGridLayout = new GridLayout();
	private GridLayout viewPointButtonsPanelGridLayout = new GridLayout();
	private JPanel labelsPanel = new JPanel();
	private JPanel viewPointPanel = new JPanel();
	private JPanel radioButtonsPanel = new JPanel();
	private JPanel jButtonsPanel = new JPanel();

	private JLabel viewLabel = new JLabel();
	private JLabel xViewLabel = new JLabel();
	private JLabel yViewLabel = new JLabel();
	private JLabel zViewLabel = new JLabel();
	private JTextField xViewTextField = new JTextField();
	private JTextField yViewTextField = new JTextField();
	private JTextField zViewTextField = new JTextField();
	private JButton setViewButton = new JButton();
	private JButton resetViewButton = new JButton();
	private JRadioButton solidModelRadioButton = new JRadioButton();
	private JRadioButton wireModelRadioButton = new JRadioButton();
	private JCheckBox axisCheckBox = new JCheckBox("Co-ordinate Axis", false);
	private JColorChooser backgroundColorChooser = new JColorChooser();
	private JDialog backgroundDialog;
	private JButton backgroundButton = new JButton();
	private ButtonGroup modelType = new ButtonGroup();

	private JButton startAnimButton = new JButton();
	private JButton clearCanvasButton = new JButton();
	private JButton openLightEditorButton = new JButton();
	private LightEditorDialog openLightEditorDialog;

	//CONSTRUCTORS

	/**
	 * Constructs a panel in AnimApplet with no target
	 */
	public AnimPanel() {
		this(null, new AnimApplet(), DEFAULT_VIEW_POINT, SOLID);
	}

	/**
	 * Constructs a panel in AnimApplet to display supplied target
	 *
	 * @param target 3D object to be viewed
	 */
	public AnimPanel(Drawable3D target) {
		this(target, (AnimApplet) ((Viewable)target).gettApplet(), DEFAULT_VIEW_POINT, SOLID);
	}

	/**
	 * Constructs a panel within supplied AnimApplet to display supplied target
	 *
	 * @param target 3D object to be viewed
	 * @param applet AnimApplet in which the object is to be viewed
	 */
	public AnimPanel(Drawable3D target, AnimApplet applet) {
		this(target, applet, DEFAULT_VIEW_POINT, SOLID);
	}

	/**
	 * Constructs a panel within supplied AnimApplet to display supplied target
	 * viewed from the specified view point
	 *
	 * @param target 3D object to be viewed
	 * @param applet AnimApplet in which the object is to be viewed
	 * @param viewPoint Point from which the object is to be viewed
	 * @param showWireFrame boolean to determine wire mode or solid mode
	 *                      true - wire mode
	 *                      false - solid mode
	 */
	public AnimPanel(
		Drawable3D target,
		AnimApplet applet,
		Vector3d viewPoint,
		boolean showWireFrame) {
		this.target = target;
		this.applet = applet;
		if (this.applet != null) {
			this.applet.setCanvas(this);
		}
		init();
		setAppletView(viewPoint);
		setAppearance(showWireFrame);
	}

	/**
	 *
	 *  Initialize the panel with the gui components
	 *
	 */

	public void init() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 *  JBuilder initialization of gui components
	 *
	 *  @throws Exception
	 *
	 */

	private void jbInit() throws Exception {
		this.setLayout(mainPanelBorderLayout);
		createNewCanvas3D();
		createNewUniverse();
		this.add(viewPanel, BorderLayout.SOUTH);

		viewPanelGridLayout.setRows(3);
		viewPanelGridLayout.setColumns(1);

		viewPanel.setLayout(viewPanelGridLayout);
		viewPanel.setToolTipText("View Panel with special features");
		viewPanel.add(viewPointPanel);
		viewPanel.add(radioButtonsPanel);
		viewPanel.add(jButtonsPanel);
		viewPanel.setBorder(BorderFactory.createTitledBorder("View Panel"));

		viewLabel.setText("Enter View Point -");
		viewLabel.setForeground(Color.black);
		viewLabel
			.setToolTipText("Enter Valid view point in the text fields provided");
		labelsPanel.add(viewLabel, BorderLayout.CENTER);

		viewPointButtonsPanelGridLayout.setRows(1);
		viewPointButtonsPanelGridLayout.setColumns(2);
		xViewLabel.setText("x value: ");
		xViewLabel.setHorizontalAlignment(JLabel.CENTER);
		xViewTextField
			.setToolTipText("Enter valid x co-ordinate of view point");
		xViewTextField.addFocusListener(new JTextFieldFocusListener(
			xViewTextField));
		xViewTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		yViewLabel.setText("y value: ");
		yViewLabel.setHorizontalAlignment(JLabel.CENTER);
		yViewTextField
			.setToolTipText("Enter valid y co-ordinate of view point");
		yViewTextField.addFocusListener(new JTextFieldFocusListener(
			yViewTextField));
		yViewTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		zViewLabel.setText("z value: ");
		zViewLabel.setHorizontalAlignment(JLabel.CENTER);
		zViewTextField
			.setToolTipText("Enter valid z co-ordinate of view point");
		zViewTextField.addFocusListener(new JTextFieldFocusListener(
			zViewTextField));
		zViewTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		setViewButton.setText("Set View");
		setViewButton.setToolTipText("Click to set view point");
		setViewButton.setMnemonic(KeyEvent.VK_S);
		resetViewButton.setText("Reset View");
		resetViewButton
			.setToolTipText("Click to reset view point to default view point");
		resetViewButton.setMnemonic(KeyEvent.VK_R);
		viewPointPanel.add(viewLabel);
		viewPointPanel.add(xViewLabel);
		viewPointPanel.add(xViewTextField);
		viewPointPanel.add(yViewLabel);
		viewPointPanel.add(yViewTextField);
		viewPointPanel.add(zViewLabel);
		viewPointPanel.add(zViewTextField);
		viewPointPanel.add(setViewButton);
		viewPointPanel.add(resetViewButton);

		solidModelRadioButton.setText("Solid Model");
		solidModelRadioButton
			.setToolTipText("Select to view object in solid mode");
		solidModelRadioButton.setMnemonic(KeyEvent.VK_D);
		wireModelRadioButton.setText("Wire Frame Model");
		wireModelRadioButton
			.setToolTipText("Select to view object in wire frame mode");
		wireModelRadioButton.setMnemonic(KeyEvent.VK_W);
		modelType.add(solidModelRadioButton);
		modelType.add(wireModelRadioButton);
		backgroundButton.setText("Background Color");
		backgroundButton.setToolTipText("Click to change background color");
		backgroundButton.setMnemonic(KeyEvent.VK_B);
		radioButtonsPanel.add(solidModelRadioButton);
		radioButtonsPanel.add(wireModelRadioButton);
		radioButtonsPanel.add(axisCheckBox);
		radioButtonsPanel.add(backgroundButton);

		startAnimButton.setText("Start Animation");
		disableAnimation();
		startAnimButton.setToolTipText("Click to start animation");
		startAnimButton.setMnemonic(KeyEvent.VK_A);
		clearCanvasButton.setText("Clear Canvas");
		clearCanvasButton.setToolTipText("Click to clear canvas");
		clearCanvasButton.setMnemonic(KeyEvent.VK_C);
		openLightEditorButton.setText("Open Light Editor");
		openLightEditorButton.setToolTipText("Click to Add Lights to scene");
		openLightEditorButton.setMnemonic(KeyEvent.VK_O);
		jButtonsPanel.add(startAnimButton);
		jButtonsPanel.add(clearCanvasButton);
		jButtonsPanel.add(openLightEditorButton);

		setViewButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setViewButton_actionPerformed(e);
			}
		});

		resetViewButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetViewButton_actionPerformed(e);
			}
		});

		solidModelRadioButton
			.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					solidModelRadioButton_actionPerformed(e);
				}
			});

		wireModelRadioButton
			.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					wireModelRadioButton_actionPerformed(e);
				}
			});

		axisCheckBox.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				axisCheckBox_stateChanged(e);
			}
		});

		backgroundButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backgroundButton_actionPerformed(e);
			}
		});

		ActionListener backgroundColorChooserOkListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				background.setColor(new Color3f(backgroundColorChooser
					.getColor()));
			}
		};
		backgroundDialog = JColorChooser.createDialog(
			this,
			"Choose a Color",
			true,
			backgroundColorChooser,
			backgroundColorChooserOkListener,
			null);

		startAnimButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startAnimButton_actionPerformed(e);
			}
		});

		clearCanvasButton
			.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clearCanvasButton_actionPerformed(e);
				}
			});

		openLightEditorButton
			.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openLightEditorButton_actionPerformed(e);
				}
			});
		if (target != null) {
			contentBranchGroup = ((Drawable3D) target).createSceneGraph();
			if (contentBranchGroup.isCompiled()) {
				openLightEditorButton.setEnabled(false);
			} else {
				contentBranchGroup
					.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
				contentBranchGroup.compile();
			}
			addContentBranchGroupToSameUniverse(contentBranchGroup);
		}
	}

	/**
	 * Sets the view point of the simple universe with supplied point
	 *
	 * @param e ActionEvent on which the view should be set
	 */
	void setViewButton_actionPerformed(ActionEvent e) {
		if (xViewTextField.getText().trim().equals("")) {
			xViewTextField.setText("0.0");
		}
		if (yViewTextField.getText().trim().equals("")) {
			yViewTextField.setText("0.0");
		}
		if (zViewTextField.getText().trim().equals("")) {
			zViewTextField.setText("0.0");
		}
		try {
			Vector3d newViewVec = new Vector3d(Double
				.parseDouble(xViewTextField.getText()), Double
				.parseDouble(yViewTextField.getText()), Double
				.parseDouble(zViewTextField.getText()));
			animUniverse.getViewingPlatform().setNominalViewingTransform();
			this.setUniverseView(newViewVec);
			viewLabel.setText("Enter View Point");
			viewLabel.setForeground(Color.black);
		} catch (NumberFormatException ex) {
			viewLabel.setText("Enter VALID VALUES for View Point!");
			viewLabel.setForeground(Color.red);
		}
	}

	/**
	 * Resets the initial view point for the applet.
	 *
	 * @param e ActionEvent on which the view should be reset to default view
	 */
	void resetViewButton_actionPerformed(ActionEvent e) {
		setDefaultViewPoint();
	}

	/**
	 * Sets the object's display mode to Solid
	 *
	 * @param e ActionEvent on which the mode should be set to Solid
	 * @todo implement solidRadioButton
	 */
	void solidModelRadioButton_actionPerformed(ActionEvent e) {
		((Drawable3D) target).setAppearance(SOLID);
	}

	/**
	 * Sets the object's display mode to Wire
	 *
	 * @param e ActionEvent on which the mode should be set to Wire
	 * @todo implement wireRadioButton
	 */
	void wireModelRadioButton_actionPerformed(ActionEvent e) {
		((Drawable3D) target).setAppearance(WIREFRAME);
	}

	/**
	 * Sets the 3 dimensional Co-ordinate axes
	 *
	 * @param e ItemEvent on which the axis should be displayed or not displayed
	 */
	void axisCheckBox_stateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			axisMouseBehaviorTransformGroup.addChild(axisBranchGroup);
		} else {
			axisBranchGroup.detach();
		}
	}

	/**
	 * Changes the background color
	 *
	 * @param e ActionEvent on which the background color should be changed
	 */
	void backgroundButton_actionPerformed(ActionEvent e) {
		Color3f previousColor = new Color3f();
		background.getColor(previousColor);
		backgroundColorChooser.setColor(previousColor.get());
		backgroundDialog.setLocationRelativeTo(this);
		backgroundDialog.show();
	}

	/**
	 * Starts animation of the object
	 *
	 * @param e ActionEvent on which the animation is started
	 * @todo implement startAnimationButton
	 */
	void startAnimButton_actionPerformed(ActionEvent e) {
		try {
			if (animFlag) {
				animFlag = false;
				((Drawable3D) target).startAnimation();
				startAnimButton.setText("Stop Animation");
			} else {
				animFlag = true;
				((Drawable3D) target).stopAnimation();
				startAnimButton.setText("Start Animation");
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	/**
	 * Clears the canvas by creating a new universe
	 *
	 * @param e ActionEvent on which the canvas is cleared
	 */
	void clearCanvasButton_actionPerformed(ActionEvent e) {
		clearCanvas(false);
	}

	/**
	 * Adds lights to the target's scene graph
	 *
	 * @param e ActionEvent on which lights are added to the object
	 */
	void openLightEditorButton_actionPerformed(ActionEvent e) {
		if (openLightEditorDialog == null) {
			initializeLightEditorDialog();
		}
		openLightEditorDialog.initializeDialog(contentBranchGroup);
		openLightEditorDialog.show();
		System.out.println("Content branch children number: "
			+ contentBranchGroup.numChildren());
	}

	/**
	 * Changes applet view based on supplied viewing vector
	 *
	 * @param   viewPoint   Point from which the object is to be viewed
	 */
	public void setAppletView(Vector3d viewPoint) {
		animUniverse.getViewingPlatform().setNominalViewingTransform();
		this.setUniverseView(viewPoint);
	}

	/**
	 *Changes applet view based on supplied viewing coordinates.
	 *
	 * @param   p1   x value of the Point from which the object is to be viewed
	 * @param   p2   y value of the Point from which the object is to be viewed
	 * @param   p3   z value of the Point from which the object is to be viewed
	 */
	public void setAppletView(double p1, double p2, double p3) {
		animUniverse.getViewingPlatform().setNominalViewingTransform();
		this.setUniverseView(new Vector3d(p1, p2, p3));
	}

	/**
	 *Adds a simple universe for supplied BranchGroup and Canvas3D.
	 *
	 * @param   suppliedScene  ContentBranch to be added
	 * @param   canvasAnimApplet  ContentBranch to be added
	 */
	public void addBranchGraphInAnimApplet(
		BranchGroup suppliedScene,
		Canvas3D canvasAnimApplet) {
		animUniverse = new SimpleUniverse(canvasFor3D);
		animUniverse.getViewingPlatform().setNominalViewingTransform();
		this.setUniverseView(DEFAULT_VIEW_POINT);
		animUniverse.addBranchGraph(suppliedScene);
		System.out.println("Total Children number : "
			+ suppliedScene.numChildren());
	}

	/**
	 * Adds BranchGraph to the existing locale
	 *
	 * @param   contentBranch  ContentBranch to be added
	 */
	public void addContentBranchGroupToSameUniverse(BranchGroup contentBranch) {
		contentBranchGroup = contentBranch;
		rootBranchGroup.detach();
		setMouseBehavior();
		animUniverse.addBranchGraph(rootBranchGroup);
		System.out.println("Content Branch Children number: "
			+ contentBranch.numChildren());
	}

	/**
	 * Adds BranchGraph to a new locale
	 *
	 * @param   contentBranch  ContentBranch to be added
	 */
	public void addContentBranchGroupToNewUniverse(BranchGroup contentBranch) {
		createNewCanvas3D();
		createNewUniverse();
		contentBranchGroup = contentBranch;
		setMouseBehavior();
		initializeLightEditorDialog();
		animUniverse.addBranchGraph(rootBranchGroup);
		System.out.println("Content Branch Children number : "
			+ contentBranch.numChildren());
	}




	/**
	 *
	 *  Makes the target's panel
	 *
	 *  @return JPanel target's panel
	 *
	 */

//	public JPanel makePanel() {
//		if (target != null) {
//			return target.makePanel();
//		} else {
//			return new JPanel();
//		}
//	}

	/**
	 *
	 *  Gets the target's applet
	 *
	 *  @return GUIApplet target's applet
	 *
	 */

//	public GUIApplet getApplet() {
//		return ((Viewable)target.getApplet();
//	}

	/**
	 * Sets the target's applet with the supplied GUIApplet
	 *
	 * @param inApplet Target's applet
	 */
//	public void setApplet(GUIApplet inApplet) {
//		target.setApplet(inApplet);
//	}

	/**
	 * Sets the target with the supplied Viewable object
	 *
	 * @param inTarget target to be viewed
	 */
	public void setTarget(Drawable3D inTarget) {
		target = inTarget;
    target.settCanvas(this);
	}

	/**
	 *
	 *  Gets the target's panel orientation
	 *
	 *  @return int an integer denoting the orientation
	 *
	 */

//	public int getPanelOrientation() {
//		return target.getPanelOrientation();
//	}

	/**
	 *
	 *  Gets the target's panel location
	 *
	 *  @return int an integer denoting the location
	 *
	 */

//	public int getPanelLocation() {
//		return target.getPanelLocation();
//	}

	/**
	 *
	 *  Gets the instance of AnimPanel
	 *
	 *  @return JPanel
	 *
	 */

//	public JPanel getPanel() {
//		return this;
//	}

	/**
	 *
	 *  Gets the instance of AnimPanel
	 *
	 *  @return JPanel
	 *
	 */

	public JPanel gettCanvas() {
		return this;
	}

	/**
	 * Sets the canvas
	 *
	 * @param inCanvas instance of AnimPanel
	 */
	public void settCanvas(AnimPanel inCanvas) {
	}

	/**
	 * Sets the target's panel
	 *
	 * @param panel instance of JPanel
	 */
//	public void setPanel(JPanel panel) {
//		if (target != null) {
//			target.setPanel(panel);
//		}
//	}

	/**
	 *
	 *  Displays the applet with the default parameters
	 *
	 */

	public void display() {
			applet.display();
		
	}

	/**
	 * Displays the applet with the supplied title
	 *
	 * @param inTitle title of the applet
	 */
	public void display(String inTitle) {
		display(inTitle, getAppletSize(), JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Displays the applet with the supplied title, width and height
	 *
	 * @param inTitle title of the applet
	 * @param inWidth width of the applet
	 * @param inHeight height of the applet
	 */
	public void display(String inTitle, int inWidth, int inHeight) {
		display(
			inTitle,
			new Dimension(inWidth, inHeight),
			JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Displays the applet with the supplied title and Dimension
	 *
	 * @param inTitle title of the applet
	 * @param inSize dimension of the applet
	 */
	public void display(String inTitle, Dimension inSize) {
		display(inTitle, inSize, JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Displays the applet with the supplied title, width and height
	 *
	 * @param inTitle title of the applet
	 * @param inSize dimension of the applet
	 * @param appletClosing integer denoting the disposal of the applet
	 */
	public void display(String inTitle, Dimension inSize, int appletClosing) {
		if (applet != null) {
			applet.display(inTitle, inSize, appletClosing);
    }
	}

	/**
	 * Sets the target's GuiObject with the supplied Viewable object
	 *
	 * @param guiObject instance of Viewable
	 */
//	public void setGuiObject(Viewable guiObject) {
//		target.setGuiObject(guiObject);
//	}

	/**
	 *
	 *  Gets the target's GuiObject
	 *
	 *  @return Viewable target's guiObject
	 *
	 */

//	public Viewable getGuiObject() {
//		return target.getGuiObject();
//	}

	/**
	 *
	 *  Gets the size of applet
	 *
	 *  @return Dimension dimension of the applet
	 *
	 */

	public Dimension getAppletSize() {
		return GUIApplet.defaultGUIAppletSize;
	}

	/**
	 *
	 *  Toggles the target's display
	 *
	 */


//	public void toggleVisible() {
//		target.toggleVisible();
//	}
	/*
	 * method to generate tool tip string fro display in swing
	 * (non-Javadoc)
	 * @see edu.ohiou.mfgresearch.labimp.basis.Viewable#toToolTipString()
	 */

//	 public String toToolTipString() {
//		  return "Class " + target.getClass().toString() +
//	      " needs to implement toToolTipString() method";
//
//	  }

	/**
	 *
	 *  Starts the target's animation
	 *
	 *  @todo implement start animation
	 *
	 */

	public void startAnimation() {
		((Drawable3D) target).startAnimation();
	}

	/**
	 *
	 *  Stops the target's animation
	 *
	 *  @todo implement stop animation
	 *
	 */

	public void stopAnimation() {
		((Drawable3D) target).stopAnimation();
	}

	/**
	 *
	 *  Sets the mouse behaviour on the target
	 *
	 */

	public void setMouseBehavior() {
		TransformGroup mouseBehaviorTG = getMouseBehaviorTransformGroup(DEFAULT_BOUNDING_SPHERE);
		mouseBehaviorTG.addChild(contentBranchGroup);
		rootBranchGroup.addChild(mouseBehaviorTG);
	}

	public TransformGroup setMouseBehavior(Bounds bounds) {
		return getMouseBehaviorTransformGroup(bounds);
	}

	/**
	 *
	 *  @todo implement animation graph
	 *  @return BranchGroup
	 *
	 */

	public BranchGroup createAnimationGraph() {
		return new BranchGroup();
	}

	/**
	 * @todo implement setAppearance
	 * @param showWireFrame boolean to determine wire mode or solid mode
	 *                      true - wire mode
	 *                      false - solid mode
	 */
	public void setAppearance(boolean showWireFrame) {
		if (showWireFrame) {
			wireModelRadioButton.setSelected(showWireFrame);
		} else {
			solidModelRadioButton.setSelected(true);
		}
		if (target != null) {
			target.setAppearance(showWireFrame);
		}
	}

	/**
	 * @todo implement set view point
	 * @param inVector Vector3d view point
	 */
	public void setUniverseViewPoint(Vector3d inVector) {
	}

	/**
	 *
	 *  Calls the target's createSceneGraph method to create the content branch
	 *
	 *  @return BranchGroup target's scene graph
	 *
	 */

	public BranchGroup createSceneGraph() {
		BranchGroup basicGroup;
		if (target != null) {
			basicGroup = ((Drawable3D) target).createSceneGraph();
		} else {
			basicGroup = new BranchGroup();
		}
//		basicGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
//		basicGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
//		basicGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		basicGroup.compile();
		return basicGroup;
	}

	//PRIVATE METHODS

	/**
	 * Creates a new universe and initializes axisBranchGroup,backgroundBranchGroup
	 * and rootBranchGroup
	 */
	private void createNewUniverse() {
		animUniverse = new SimpleUniverse(canvasFor3D);
		setDefaultViewPoint();
		rootBranchGroup = new BranchGroup();
		rootBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		rootBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		rootBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		rootBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
		axisBranchGroup = new BranchGroup();
		axisBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		axisBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		axisBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
		axisBranchGroup.addChild(new Axis(5));
		axisMouseBehaviorTransformGroup = getMouseBehaviorTransformGroup(DEFAULT_BOUNDING_SPHERE);
		background = new Background();
		background.setCapability(Background.ALLOW_COLOR_READ);
		background.setCapability(Background.ALLOW_COLOR_WRITE);
		background.setApplicationBounds(DEFAULT_BOUNDING_SPHERE);
		background.setColor(new Color3f(0f, 0f, 0f));
		backgroundBranchGroup = new BranchGroup();
		backgroundBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		backgroundBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
		backgroundBranchGroup.addChild(background);
		rootBranchGroup.addChild(backgroundBranchGroup);
		rootBranchGroup.addChild(axisMouseBehaviorTransformGroup);
	}

	/**
	 *
	 *  Creates a new canvas3D
	 *
	 */

	private void createNewCanvas3D() {
		if (canvasFor3D != null) {
			remove(canvasFor3D);
		}
		canvasFor3D = new Canvas3D(config);
		add(canvasFor3D, BorderLayout.CENTER);
		revalidate();
		canvasFor3D.repaint();
	}

	/**
	 *
	 *  Creates a new light editor dialog
	 *
	 */

	private void initializeLightEditorDialog() {
		try {
			// dsormaz removed this.getapplet fro embedded panel
			openLightEditorDialog = LightEditorDialog.createDialog(this
				, "Add Lights", false);
			openLightEditorDialog.setLocationRelativeTo(applet);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 *
	 *  Method to set view of the universe to the default view point.
	 *
	 */

	private void setDefaultViewPoint() {
		animUniverse.getViewingPlatform().setNominalViewingTransform();
		this.setUniverseView(DEFAULT_VIEW_POINT);
		xViewTextField.setText(new Double(DEFAULT_VIEW_POINT.x).toString());
		yViewTextField.setText(new Double(DEFAULT_VIEW_POINT.y).toString());
		zViewTextField.setText(new Double(DEFAULT_VIEW_POINT.z).toString());
		viewLabel.setText("Enter View Point");
		viewLabel.setForeground(Color.black);
	}

	/**
	 * Method to set view of the universe to the supplied view point.
	 * @param viewPoint view point vector
	 */
	private void setUniverseView(Vector3d viewPoint) {
		xViewTextField.setText(new Double(viewPoint.x).toString());
		yViewTextField.setText(new Double(viewPoint.y).toString());
		zViewTextField.setText(new Double(viewPoint.z).toString());
		Transform3D universeT3D = new Transform3D();
		animUniverse.getViewingPlatform().getViewPlatformTransform()
			.getTransform(universeT3D);
		Vector3d universeT3DVec = new Vector3d(), tempVec = new Vector3d();
		universeT3D.get(universeT3DVec);
		universeT3D.setTranslation(viewPoint);
		tempVec.cross(universeT3DVec, viewPoint);
		universeT3D.setRotation(new AxisAngle4d(tempVec, universeT3DVec
			.angle(viewPoint)));
		animUniverse.getViewingPlatform().getViewPlatformTransform()
			.setTransform(universeT3D);
	}

	/**
	 * Enables scaling, rotation and zooming behavior using the mouse
	 * @param bounds schedulingbounds for mouse behavior
	 * @return TransformGroup Mouse Behavior TransformGroup
	 */
	private TransformGroup getMouseBehaviorTransformGroup(Bounds bounds) {
		TransformGroup mouseBehaviorTG = new TransformGroup();
		mouseBehaviorTG.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		mouseBehaviorTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		mouseBehaviorTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		mouseBehaviorTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		mouseBehaviorTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		MouseRotate mRotate = new MouseRotate();
		MouseTranslate mTranslate = new MouseTranslate();
		MouseZoom mZoom = new MouseZoom();
		mRotate.setSchedulingBounds(bounds);
		mTranslate.setSchedulingBounds(bounds);
		mZoom.setSchedulingBounds(bounds);
		mRotate.setTransformGroup(mouseBehaviorTG);
		mTranslate.setTransformGroup(mouseBehaviorTG);
		mZoom.setTransformGroup(mouseBehaviorTG);
		mouseBehaviorTG.addChild(mRotate);
		mouseBehaviorTG.addChild(mTranslate);
		mouseBehaviorTG.addChild(mZoom);
		return mouseBehaviorTG;
	}

	public void enableAnimation() {
		this.startAnimButton.setEnabled(true);
		this.animFlag = true;
	}

	public void disableAnimation() {
		this.startAnimButton.setEnabled(false);
	}

	public void clearCanvas(boolean restoreSettings) {
		axisCheckBox.setSelected(false);
		BranchGroup emptyBranchGroup = new BranchGroup();
		emptyBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		emptyBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		emptyBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		addContentBranchGroupToNewUniverse(emptyBranchGroup);
	}

	public void setAnimationButtonTextStart() {
		this.startAnimButton.setText("Start Animation");
	}

	public void setAnimationButtonTextStop() {
		this.startAnimButton.setText("Stop Animation");
	}

	public Vector3d getViewPoint() {
		Transform3D universeT3D = new Transform3D();
		animUniverse.getViewingPlatform().getViewPlatformTransform()
			.getTransform(universeT3D);
		Vector3d vec = new Vector3d();
		universeT3D.get(vec);
		return vec;
	}

	/**
	 *
	 *  <p>Title:       JTextFieldFocusListener </p>
	 *  <p>Description: Class that provides functionality so that the text in a
	 *                  given text field is selected when focus is gained</p>
	 *  <p>Copyright:   Dr.D.N.Sormaz, Ohio University Copyright (c) 2003</p>
	 *  <p>Company:     IMSE Dept, Ohio University</p>
	 *  @author         Deepak Pisipati
	 *  @version 1.0
	 *
	 */

	class JTextFieldFocusListener implements FocusListener {
		private JTextField field;

		//Default Constructor
		public JTextFieldFocusListener(JTextField field) {
			this.field = field;
		}

		//Selects complete text in the text field when focus is gained
		public void focusGained(FocusEvent e) {
			field.selectAll();
		}

		public void focusLost(FocusEvent e) {
		}
	}

	public static void main(String[] args) {
	}

}
