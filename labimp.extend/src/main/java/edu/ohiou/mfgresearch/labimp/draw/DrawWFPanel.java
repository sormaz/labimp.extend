package edu.ohiou.mfgresearch.labimp.draw;
/**
 * Title:        DrawWFCanvas Class <p>
 * Description:  Class defining canvas used for drawing wireframes. <p>
 * Copyright:    Copyright (c) 2001 <p>
 * Company:      Ohio University <p>
 * @author  Jaikumar Arumugam + Dusan N. Sormaz
 * @version 1.0
 */



import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Color;

import java.awt.geom.*;
import java.text.DecimalFormat;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Matrix4d;
import java.awt.*;
import edu.ohiou.mfgresearch.labimp.gtk3d.*;
//import edu.ohiou.implanner.parts.MfgPartModel;
import java.util.*;
import edu.ohiou.mfgresearch.labimp.basis.*;

public class DrawWFPanel extends JPanel implements DrawableWF, Scalable {
	public static final Point3d DEFAULT_VIEW_POINT = new Point3d(0, 0, 20);// default view point;// default view point.
	public static final double DEFAULT_SCALE = 50; // default scale value; // default scale value.
	//  private static final boolean MODIFY_VIEW = false;
	//  private static final boolean MODIFY_TARGET = true;
	private static final double POINT_PROXIMITY = 10.0;

	protected DrawableWF target;
	protected DrawWFApplet applet;
	protected DrawWFCanvas drawPanel = new DrawWFCanvas();
	//  protected JPanel parent;
	protected Point3d viewPoint = DEFAULT_VIEW_POINT;
	protected JPanel viewPanel = new JPanel();
	private Matrix4d viewTransform;
	protected int oldX;
	protected int oldY;
	private double scale;
	public final static Dimension canvasSize = new Dimension(600, 400);
	private final static Dimension viewPanelSize = new Dimension(600, 55);
	private double mouseSensitivity = 20;
	public final static Dimension scrollPaneSize = new Dimension(600, 400);
	private Hashtable targetTable;
	private HashMap shapeMap = new HashMap();
	private HashMap fillMap = new HashMap();

	public boolean mouseMode;
	private boolean showWorldCS = false;
	private Point3d activePoint;
	WorldCS wcs = new WorldCS(5, 6, 7);
	Collection shapeList;
	boolean needsUpdate = true;

	// components of viewPanel.
	FlowLayout flowLayout1 = new FlowLayout();
	JLabel xLabel = new JLabel();
	JTextField xField = new JTextField();
	JLabel yLabel = new JLabel();
	JTextField yField = new JTextField();
	JLabel zLabel = new JLabel();
	JTextField zField = new JTextField();
	JButton viewButton = new JButton();
	FlowLayout flowLayout2 = new FlowLayout();
	FlowLayout flowLayout3 = new FlowLayout();
	JLabel errorLabel = new JLabel();
	JLabel viewPointLabel = new JLabel();
	JLabel scaleLabel = new JLabel();
	JTextField scaleField = new JTextField();
	JLabel showPointLabel = new JLabel();
	JCheckBox wcsCheckBox = new JCheckBox();
	private static final Dimension LABEL_SIZE = new Dimension(35, 17);
	private static final Color BACKGROUND_COLOR = Color.pink;

	// CONSTRUCTORS

	/** Default constructor.
	 *
	 */
	public DrawWFPanel() {
		this(null, new DrawWFApplet(), DEFAULT_VIEW_POINT, DEFAULT_SCALE);
	}

	/**
	 * Constructor taking DrawWFApplet.
	 */
	public DrawWFPanel(DrawableWF inTarget) {
		this(
			inTarget,
			(DrawWFApplet) ((Viewable)inTarget).geettApplet(),
			DEFAULT_VIEW_POINT,
			DEFAULT_SCALE);
	}

	public DrawWFPanel(DrawableWF inTarget, DrawWFApplet inApplet) {
		this(inTarget, inApplet, DEFAULT_VIEW_POINT, DEFAULT_SCALE);

	}

	public DrawWFPanel(
		DrawableWF inTarget,
		DrawWFApplet inApplet,
		Point3d inPoint,
		double inScale) {
		target = inTarget;
		applet = inApplet;
		if (applet != null)
			applet.settCanvas(this);
		this.viewPoint = inPoint;
		scale = inScale;
		this.setViewTransform();
		init();
	}

	// SELECTORS

	/** Method to set view transformation matrix.
	 *
	 */
	public void setViewTransform() {
		Vector3d vp = new Vector3d(this.viewPoint);
		double distFromOrigin = viewPoint.distance(GeometryConstants.ORIGIN);
		Matrix4d vtMatrix = new Matrix4d();
		boolean exceptionFlag = false;
		//    System.out.println("Viewpoint vector: " + vp);

		// Check for valid inversion of generated matrix.
		try {
			vtMatrix = Gtk.computeTransformMatrix(vp);
			vtMatrix.invert();
		} catch (Exception ex) {
			//      System.out.println("Cannot invert view transform matrix.");
			exceptionFlag = true;
		}

		// set value for viewTransform matrix
		if (exceptionFlag == false) {
			this.viewTransform = new Matrix4d(vtMatrix);
//			System.out.println(viewTransform.toString());
		}
	}

	/**
	 * Method to get view transformation matrix.
	 *
	 *
	 */

	public Matrix4d getViewTransform() {
		return new Matrix4d(this.viewTransform);
	}

	/** Method to perform perspective transformation on a Point3d.
	 *
	 */
	public Point3d perspTransform(Point3d p) {
		double newX, newY;
		Point3d newP;
		double distFromOrigin = viewPoint.distance(GeometryConstants.ORIGIN);

		if (p.z == distFromOrigin) {
			newX = Double.MAX_VALUE;
			newY = Double.MAX_VALUE;
		} else {
			newX = p.x / (1 - p.z / distFromOrigin);
			newY = p.y / (1 - p.z / distFromOrigin);
		}

		newP = new Point3d(newX, newY, 0);
		
		return newP;
	}

	/** Method to perform scaling on a Point3d to fit display on canvas.
	 *
	 */
	public Point2D.Double calcDisplayTransform(Point3d p) {
		double newX, newY;
		Point2D.Double newP;
		//    ImpObject.doNothing(this, "panel" + drawPanel);
		//    ImpObject.doNothing(this, "point" + p);
		newX = drawPanel.getWidth() / 2.0 + p.x * scale;
		newY = drawPanel.getHeight() / 2.0 - p.y * scale;
		newP = new Point2D.Double(newX, newY);
		return newP;
	}

	public Point3d calcInverseDisplayTransform(Point2D.Double p) {
		double newX, newY;
		newX = (p.x - drawPanel.getWidth() / 2.0) / scale;
		newY = (drawPanel.getHeight() / 2.0 - p.y) / scale;
		return new Point3d(newX, newY, 0);
	}

	public Point2D.Double createDisplayPoint(Point3d inPoint) {
		Point3d newPoint = new Point3d();
		viewTransform.transform(inPoint, newPoint);
		return calcDisplayTransform(perspTransform(newPoint));
	}

	/** set scale.
	 *
	 */
	public void setScale(double newScale) {
		this.scale = newScale;
		// update visual component
		DecimalFormat df = new DecimalFormat("#.00");
		this.scaleField.setText(df.format(newScale));
		this.setViewTransform();
	}

	/**
	 * view button to set value for viewpoint and redraw canvas.
	 *   (get viewpoint vector values from user)
	 *
	 */

	public void setView(double scale, double x, double y, double z) {	
		this.scale = scale;	
		setViewPoint(new Point3d(x, y, z));
		this.needsUpdate = true;
		createTargetTable();
	}

	/** set view point.
	 *
	 */
	public void setViewPoint(Point3d newViewPoint) {
		this.viewPoint = new Point3d(newViewPoint);
		// update visual component
		this.xField.setText(Double.toString(newViewPoint.x));
		this.yField.setText(Double.toString(newViewPoint.y));
		this.zField.setText(Double.toString(newViewPoint.z));
		this.setViewTransform();
	}

	/**
	 * init() method required for displaying applet.
	 *
	 *
	 */

	public void init() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.createTargetTable();

	}

	/**
	 *
	 *  Returns the size of applet for display
	 *
	 */

	public Dimension getAppletSize() {
		Dimension vpSize = this.viewPanel.getPreferredSize();
		Dimension finalCanvasSize = new Dimension(Math.max(
			vpSize.width,
			canvasSize.width), vpSize.height + canvasSize.height + 30);
		//    System.out.println("DrawWFCanvas panel size = "+finalCanvasSize);
		return finalCanvasSize;
	}

	/**
	 * Method to return panel.
	 *
	 *
	 */

	public JPanel getPanel() {
		return applet.geettPanel();
	}

	public JPanel makePanel() {
		if (target != null && target instanceof Viewable)
			return ((Viewable)target).makePanel();
		else
			return new JPanel();
	}

	public DrawWFCanvas getDrawPanel() {
		return drawPanel;
	}

//	public int getPanelLocation() {
//		return target.getPanelLocation();
//	}
//
//	public int getPanelOrientation() {
//		return target.getPanelOrientation();
//	}
//
//	public void addPanel() {
//		target.addPanel();
//	}

	public void setTarget(DrawableWF inTarget) {
		target = inTarget;
	}
  
//	public void setApplet(GUIApplet inApplet) {
//		target.setApplet(inApplet);
//	}
//
	public DrawableWF getTarget() {
		return target;
	}
//
//	public void setGuiObject(Viewable guiObject) {
//		target.setGuiObject(guiObject);
//	}
//
//	public Viewable getGuiObject() {
//		return target.getGuiObject();
//	}


	public void setNeedUpdate(boolean needUpdate) {
		this.needsUpdate = needUpdate;
	}

	public Color geettColor() {
		return target.geettColor();
	}

	public void settColor(Color color) {
		target.settColor(color);
	}

	//	public void ssetNeedsUpdate (boolean needsUpdate) {
	//		this.needsUpdate= needsUpdate;
	//	}

	public void settCanvas(DrawWFPanel inCanvas) {
	}

	public JPanel geettCanvas() {
		return this;
	}
	public GUIApplet geetApplet() {
		return applet;
	}

//	public void setPanel(JPanel panel) {
//		if (target != null)
//			target.setPanel(panel);
//	}
//
//	 public String toToolTipString() {
//		  return "Class " + target.getClass().toString() +
//	      " needs to implement toToolTipString() method";
//
//	  }
//
//	public void toggleVisible() {
//		if (target != null)
//			target.toggleVisible();
//	}

	public JPanel getContentPane() {
		JPanel contentPanel = new JPanel();
		return contentPanel;
	}

  public void addDrawShape (Color color, Shape shape) {

// if key in map retrive set
// add new shape to set
  if (shapeMap.containsKey(color) ) {
    HashSet shapes = (HashSet) shapeMap.get(color);
    shapes.add(shape);
  }
  else {
    HashSet shapes = new HashSet ();
    shapes.add(shape);
    shapeMap.put(color, shapes);
  }
}

public void addDrawShapes (Color color, Collection newShapes) {

// if key in map retrive set
// add new shape to set
  if (shapeMap.containsKey(color) ) {
    HashSet shapes = (HashSet) shapeMap.get(color);
    shapes.addAll(newShapes);
  }
  else {
    HashSet shapes = new HashSet (newShapes);
    shapeMap.put(color, shapes);
  }
}


public void addFillShape (Color color, Shape shape) {

// if key in map retrive set
// add new shape to set
  if (fillMap.containsKey(color) ) {
    HashSet shapes = (HashSet) fillMap.get(color);
    shapes.add(shape);
  }
  else {
    HashSet shapes = new HashSet ();
    shapes.add(shape);
    fillMap.put(color, shapes);
  }
}
public void addFillShapes (Color color, Collection newShapes) {

// if key in map retrive set
// add new shape to set
  if (fillMap.containsKey(color) ) {
    HashSet shapes = (HashSet) fillMap.get(color);
    shapes.addAll(newShapes);
  }
  else {
    HashSet shapes = new HashSet (newShapes);
    fillMap.put(color, shapes);
  }
}

  public void makeShapeSets (DrawWFPanel canvas) {
    shapeMap = new HashMap();
    fillMap = new HashMap();
    if (target != null)
      target.makeShapeSets(canvas);
    if (showWorldCS) {
      wcs.makeShapeSets(canvas);
    }

}
	/** get shapes list.
	 *  (from target)
	 */
	public LinkedList geetShapeList(DrawWFPanel canvas) {
		LinkedList shapes = new LinkedList();
		if (target != null)
			shapes.addAll(target.geetShapeList(this));
		if (showWorldCS) {
			shapes.addAll(wcs.geetShapeList(this));
		}
		return shapes;
	}

	public LinkedList getPointSet() {
		return target.getPointSet();
	}

	public void createTargetTable() {
		if (target != null) {
			targetTable = new Hashtable();
			LinkedList targets = target.getPointSet();
			for (ListIterator itr = targets.listIterator(); itr.hasNext();) {
				Point3d point = (Point3d) itr.next();
//				System.out.println(point.toString() + "-->" + 
//						this.createDisplayPoint(point).toString());
//				System.out.println("View point: " + viewPoint.toString());
				this.targetTable.put(this.createDisplayPoint(point), point);
			}
		}
	}

	/** paintComponent method for drawing wireframes.
	 *  (taking graphics context)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	/** paintComponent method for drawing wireframes.
	 *  (taking graphics2D context)
	 */
	public void paintComponent(Graphics2D g) {
		paintComponent((Graphics) g);
	}

	public void paintComponent(Graphics2D g, DrawWFPanel canvas) {
		if (target != null)
			target.paintComponent(g, canvas);
		if (showWorldCS)
			wcs.paintComponent(g, canvas);
	}

	public void repaint() {
		createTargetTable();
		super.repaint();
	}

	/*  public void revalidate () {
	 createTargetTable();
	 super.revalidate();
	 }
	 */
  
  
	/** display method taking title, width and height details of applet.
	 *
	 */
	public void display(String inTitle, int inWidth, int inHeight) {
		display(
			inTitle,
			new Dimension(inWidth, inHeight),
			JFrame.DISPOSE_ON_CLOSE);
	}

	/** display method taking title and dimension(w,h) details of applet.
	 *
	 */
	public void display(String inTitle, Dimension inSize) {
		display(inTitle, inSize, JFrame.DISPOSE_ON_CLOSE);
	}

	public void display(String inTitle, Dimension inSize, int appletClosing) {
		if (applet != null)
			applet.display(inTitle, inSize, appletClosing);
		else
			applet.display(inTitle, inSize, appletClosing);
	}

	/** display method taking title of applet.
	 *
	 */
	public void display(String inTitle) {
		display(inTitle, getAppletSize(), JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * display method taking no arguments.
	 *
	 *
	 */

	public void display() {
	  applet.display();
	}
	
	void viewButton_actionPerformed() {
		// Local variables.
		double x1 = 0, y1 = 0, z1 = 0, s = 0;
		// variable to check for validity for values.
		boolean exceptionFlag = false;

		// View Point Vector
		try {
			x1 = Double.parseDouble(xField.getText());
			y1 = Double.parseDouble(yField.getText());
			z1 = Double.parseDouble(zField.getText());
		} catch (Exception ex1) {
			errorLabel
				.setText("< Invalid string for coordinate of view point >");
			return;
		}

		// Scale
		try {
			s = Double.parseDouble(scaleField.getText());
		} catch (Exception ex2) {
			errorLabel.setText("< Invalid string for scale >");
			return;
		}

		// Check validity of view point vector.
		if ((x1 == 0) && ((y1 == 0) && (z1 == 0))) {
			errorLabel.setText("< View point can not be at origin >"); //does NOT change/create new view point.
			return;
		}

		// Check for validity of scale.
		if (s <= 0) {
			errorLabel.setText("< Scale can not be negative number or zero >");
			return;
		}

		if (exceptionFlag == false) {
			errorLabel.setText("");
			// create a 3d vector with the viewpoint values.
			Vector3d vp = new Vector3d(x1, y1, z1);
			this.viewPoint = new Point3d(vp);
			// update value for scale.
			this.setScale(s);
			// show the view point values in view panel.
			this.showPointLabel.setText(this.viewPoint.toString());
			// set new value for view transformation matrix.
			this.setViewTransform();
			// repaint based on new viewpoint and scale.
			this.needsUpdate = true;
			this.repaint();
			createTargetTable();
		}
	}

	private void jbInit() throws Exception {

		ActionListener action = new DrawWFPanel_viewButton_actionAdapter(this);
		//    drawPanel.setPreferredSize(new Dimension(1600, 1400));
		drawPanel.setBackground(Color.white);
		drawPanel.setPreferredSize(new Dimension(2000, 2000));
		//    JScrollPane scrollPanel = new JScrollPane (drawPanel);
		//    scrollPanel.setPreferredSize(new Dimension (600, 400));
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		viewPointLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		xLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		this.add(drawPanel, BorderLayout.CENTER);
		this.add(viewPanel, BorderLayout.SOUTH);
		GraphicsToolBar myToolbar = new GraphicsToolBar(this);
		myToolbar.addButtonOptions(GraphicsToolBar.ALL);
		myToolbar.setFloatable(true);
		this.add(myToolbar, BorderLayout.NORTH);
		viewPanel.setBackground(BACKGROUND_COLOR);
		viewPanel.setLayout(new BorderLayout());
		viewPanel.setToolTipText("Enter view point and scale here.");

		xLabel.setForeground(Color.blue);
		xLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		xLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		xLabel.setText("X: ");
		xLabel.setPreferredSize(LABEL_SIZE);
		xField.setText("" + viewPoint.x);
		xField.setHorizontalAlignment(SwingConstants.RIGHT);
		xField.addActionListener(action);
		xField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
				field_focusGained(xField);
			}
			public void focusLost(FocusEvent e) {
				viewButton_actionPerformed();
			}
		});

		yLabel.setForeground(Color.blue);
		yLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		yLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		yLabel.setText("Y: ");
		yLabel.setPreferredSize(LABEL_SIZE);
		yLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		yField.setHorizontalAlignment(SwingConstants.RIGHT);
		yField.setText("" + viewPoint.y);
		yField.addActionListener(action);
		yField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
				field_focusGained(yField);
			}
			public void focusLost(FocusEvent e) {
				viewButton_actionPerformed();
			}
		});

		zLabel.setForeground(Color.blue);
		zLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		zLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		zLabel.setText("Z: ");
		zLabel.setPreferredSize(LABEL_SIZE);
		zLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		zField.setText("" + viewPoint.z);
		zField.setHorizontalAlignment(SwingConstants.RIGHT);
		zField.addActionListener(action);
		zField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
				field_focusGained(zField);
			}
			public void focusLost(FocusEvent e) {
				viewButton_actionPerformed();
			}
		});

		viewButton.setText("Redisplay");
		viewButton.addActionListener(action);
		errorLabel.setFont(new java.awt.Font("SansSerif", 0, 12));
		errorLabel.setForeground(Color.red);

		viewPointLabel.setText("View Point:");
		viewPointLabel.setBackground(BACKGROUND_COLOR);
		scaleLabel.setText("Scale:");
		scaleLabel.setPreferredSize(LABEL_SIZE);
		scaleField.setHorizontalAlignment(SwingConstants.RIGHT);
		scaleField.setText("" + scale);
		scaleField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
				field_focusGained(scaleField);
			}
			public void focusLost(FocusEvent e) {
				viewButton_actionPerformed();
			}
		});

		showPointLabel.setFont(new java.awt.Font("SansSerif", 0, 10));
		showPointLabel.setForeground(SystemColor.desktop);
		showPointLabel.setHorizontalAlignment(SwingConstants.CENTER);
		showPointLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		wcsCheckBox.setText("Show WorldCS");
		wcsCheckBox.setBackground(BACKGROUND_COLOR);
		wcsCheckBox.addItemListener(new DrawWFPanel_wcsCheckBox_itemAdapter(
			this));

		JPanel xPanel = new JPanel();
		xPanel.setLayout(new BorderLayout());
		xPanel.add(xLabel, BorderLayout.WEST);
		xPanel.add(xField, BorderLayout.CENTER);
		xPanel.setBackground(BACKGROUND_COLOR);

		JPanel yPanel = new JPanel();
		yPanel.setLayout(new BorderLayout());
		yPanel.add(yLabel, BorderLayout.WEST);
		yPanel.add(yField, BorderLayout.CENTER);
		yPanel.setBackground(BACKGROUND_COLOR);

		JPanel zPanel = new JPanel();
		zPanel.setLayout(new BorderLayout());
		zPanel.add(zLabel, BorderLayout.WEST);
		zPanel.add(zField, BorderLayout.CENTER);
		zPanel.setBackground(BACKGROUND_COLOR);

		JPanel scalePanel = new JPanel();
		scalePanel.setLayout(new BorderLayout());
		scalePanel.add(scaleLabel, BorderLayout.WEST);
		scalePanel.add(scaleField, BorderLayout.CENTER);
		scalePanel.setBackground(BACKGROUND_COLOR);

		JPanel pointPanel = new JPanel();
		pointPanel.setLayout(new GridLayout(1, 5, 5, 0));
		pointPanel.add(viewPointLabel);
		pointPanel.add(xPanel);
		pointPanel.add(yPanel);
		pointPanel.add(zPanel);
		pointPanel.add(scalePanel);
		pointPanel.setBackground(BACKGROUND_COLOR);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(showPointLabel, BorderLayout.CENTER);
		bottomPanel.add(wcsCheckBox, BorderLayout.EAST);
		bottomPanel.setBackground(BACKGROUND_COLOR);
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BorderLayout());
		middlePanel.setBackground(BACKGROUND_COLOR);
		middlePanel.add(viewPointLabel, BorderLayout.WEST);
		middlePanel.add(pointPanel, BorderLayout.CENTER);
		middlePanel.add(viewButton, BorderLayout.EAST);

		viewPanel.add(errorLabel, BorderLayout.NORTH);
		viewPanel.add(middlePanel, BorderLayout.CENTER);
		viewPanel.add(bottomPanel, BorderLayout.SOUTH);

		// set initial view transform matrix based on default viewpoint
		this.setViewTransform();
	}

	/**
	 * Callback. When field has the focus, ensures that all the text within
	 * it is selected.
	 */
	private void field_focusGained(JTextField field) {
		field.selectAll();
	}
	
	public void mousePressed(int x, int y) {
		drawPanel.mouseAdapter.this_mousePressed(x, y);
	}

	public void mouseClicked(int x, int y) {
		drawPanel.mouseAdapter.this_mouseClicked(x, y);
	}

	public void mouseMoved(int x, int y) {
		drawPanel.mouseAdapter.mouseMoved(x, y);
	}

	public void mouseDragged(int x, int y) {
		drawPanel.mouseAdapter.mouseDragged(x, y);
	}
	
	public void modifyViewPoint(int x, int y) {
		drawPanel.mouseAdapter.modifyViewPoint(x, y);
	}
	
	public void modifyTargetPoint(int x, int y) {
		drawPanel.mouseAdapter.modifyTargetPoint(x, y);
	}

	public static void main(String[] args) {
		//    DrawWFPanel dp =
		//          new DrawWFPanel (null, new DrawWFApplet(), new Point3d (0,0,2), 20);
		//    dp.setSize(400,400);
		//    MfgPartModel part = new MfgPartModel();
		//    MfgPartModel.staticPart = part;
		//    part.display();
		//    Point3d origP = new Point3d (1,1,1);
		//    System.out.println ("orig point" + origP);
		//    Point3d viewP = new Point3d();
		//    dp.getViewTransform().transform(origP, viewP);
		//    System.out.println ("view point" + viewP);
		//    Point3d perpP = dp.perspTransform(viewP);
		//    System.out.println ("persp point" + perpP);
		//    Point2D.Double scaledP = dp.calcDisplayTransform(perpP);
		//    System.out.println ("scaled point" + scaledP);
		//    Point2D.Double totP = dp.createDisplayPoint(origP);
		//    System.out.println ("total point" + totP);
		//    dp.display();

	}

	public class DrawWFCanvas extends JPanel implements Scrollable {

		private DrawWFCanvasMouseAdapter mouseAdapter;
		
		public DrawWFCanvas() {
			mouseAdapter = new DrawWFCanvasMouseAdapter(
				this);
			this.addMouseMotionListener(mouseAdapter);
			this.addMouseListener(mouseAdapter);
			this.addMouseWheelListener(mouseAdapter);
		}

//		public void paint(Graphics g) {
//			g.clearRect(0, 0, this.getPreferredSize().width, this
//				.getPreferredSize().height);
//			Graphics2D gd = (Graphics2D) g;
//			paint(gd);
//		}
		
    public void paintComponent (Graphics g) {
      Graphics2D g2d = (Graphics2D) g;
      paintComponent(g2d);
    }

    public void paintComponent (Graphics2D g) {
      makeShapeSets(DrawWFPanel.this);
      Collection colors = shapeMap.keySet();
for (Iterator colorItr = colors.iterator(); colorItr.hasNext();) {
  Color currentColor = (Color) colorItr.next();
  HashSet shapeSet = (HashSet) shapeMap.get(currentColor);
  for (Iterator shapeItr = shapeSet.iterator(); shapeItr.hasNext();) {
    Shape currentShape = (Shape) shapeItr.next();
    if (currentShape.intersects(0, 0, getSize().width, getSize().height)) {
      g.setColor(currentColor );
      g.draw(currentShape);
    }
  }
}

    }

		/** paintComponent method for drawing wireframes.
		 *  (taking graphics2D context)
		 */
		public void paintt(Graphics2D g) {
			if (target != null) {
				if (target.geettColor() == null) {
					if (needsUpdate) {
						shapeList = geetShapeList(DrawWFPanel.this);
						needsUpdate = false;
					}
					for (Iterator itr = shapeList.iterator(); itr.hasNext();) {
						Shape shape = (Shape) itr.next();
						// verify that shape is inside canvas
						if (shape.intersects(
							0,
							0,
							getSize().width,
							getSize().height)) {
							g.draw(shape);
						}
					}
				} else {
					DrawWFPanel.this.paintComponent(g, DrawWFPanel.this);
				}
			}
		}

		/** Method to get scrollable unit increment.
		 *
		 */
		public int getScrollableUnitIncrement(
			Rectangle visibleRect,
			int orientation,
			int direction) {

			// get current position
			int currentPosition = 0;
			if (orientation == SwingConstants.HORIZONTAL)
				currentPosition = visibleRect.x;
			else
				currentPosition = visibleRect.y;

			return 10;
		}

		/** Method to get scrollable block increment.
		 *
		 */
		public int getScrollableBlockIncrement(
			Rectangle visibleRect,
			int orientation,
			int direction) {

			return 50;
		}

		/**
		 *
		 *
		 *
		 */

		public boolean getScrollableTracksViewportHeight() {
			return false;
		}

		/**
		 *
		 *
		 *
		 */

		public boolean getScrollableTracksViewportWidth() {
			return false;
		}

		/**
		 * Method to get preferred viewport size.
		 *
		 *
		 */

		public Dimension getPreferredScrollableViewportSize() {
			return this.getPreferredSize();
		}
		/** Mouse clicked event.
		 *
		 */
	} // end of DrawWFCanvas definition
	
	/**
	 * MouseAdapter class.
	 *
	 *
	 */

	class DrawWFCanvasMouseAdapter extends MouseInputAdapter {
		DrawWFCanvas adaptee;

		DrawWFCanvasMouseAdapter(DrawWFCanvas adaptee) {
			//      ImpObject.doNothing(this, "In adapter constructor");
			this.adaptee = adaptee;
		}
		public void mousePressed(MouseEvent e) {
			this_mousePressed(e.getX(), e.getY());
		}

		public void mouseClicked(MouseEvent e) {
			this_mouseClicked(e.getX(), e.getY());
		}

		public void mouseMoved(MouseEvent e) {
			mouseMoved(e.getX(), e.getY());
		}
		
		private void mouseMoved(int x, int y) {
			
//			System.out.println("Mouse moved at " + x + ", " + y );
			
			if (targetTable != null) {
				Point2D.Double mouseLocation = new Point2D.Double(x, y);
				TreeSet ts = new TreeSet(new Point2DComparator(mouseLocation));
				ts.addAll(targetTable.keySet());
				
//				if (targetTable.size() > 0) {
//				System.out.println("Minimum required proximity: " + POINT_PROXIMITY);
//				System.out.println("Target location: " + ts.first().toString());
//				System.out.println("Distance: " + mouseLocation.distance((Point2D.Double) ts.first()));
//				}
//				
//				System.out.println(targetTable.toString());
				
				try {
					activePoint = (Point3d) targetTable.get(ts.first());
					if (mouseLocation.distance((Point2D.Double) ts.first()) < POINT_PROXIMITY) {
						mouseMode = MODIFY_TARGET;
//						System.out.println("Mouse mode: Modify target");
						setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
					} else {
						mouseMode = MODIFY_VIEW;
//						System.out.println("Mouse mode: Modify view");
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				} catch (Exception ex) {
				}
			}
		}

		public void mouseDragged(MouseEvent e) {
			
//			System.out.println("Mouse dragged at " + e.getX() + ", " + e.getY() );
			
			if (mouseMode == MODIFY_VIEW)
				modifyViewPoint(e.getX(), e.getY());
			else
				modifyTargetPoint(e.getX(), e.getY());
		}
		
		public void mouseDragged(int x, int y) {
			
//			System.out.println("Mouse dragged at " + x + ", " + y );
			
			if (mouseMode == MODIFY_VIEW)
				modifyViewPoint(x, y);
			else
				modifyTargetPoint(x, y);
		}
		
		void this_mouseClicked(int x, int y) {
			
//			System.out.println("Mouse clicked at " + x + ", " + y );
			
			//viewPoint = new Point3d ( (double) (e.getX() - oldX), (double) (e. getY() - oldY), 0.0);
			repaint();
		}

		public void modifyTargetPoint(int x, int y) {
			
//			System.out.println("Target table modified at " + x + ", " + y );
			
			Point2D.Double mouseLocation = new Point2D.Double(x, y);
			Point3d newP = calcInverseDisplayTransform(mouseLocation);
			Matrix4d inverse = new Matrix4d();
			inverse.invert(viewTransform);
			inverse.transform(newP);
			try {
				Plane activePlane = new Plane(activePoint, new Vector3d(
					viewPoint));
				Line3d activeLine = new Line3d(viewPoint, newP);
				Point3d newPoint = activePlane.intersectLinePlane(activeLine);
				activePoint.set(newPoint);
				needsUpdate = true;
				repaint();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		/** Mouse dragged event.
		 *
		 */
		void modifyViewPoint(int x, int y) {
			
//			System.out.println("View point modified at " + x + ", " + y );
			
			// Point3d to hold the change in view point in xv,yv,zv coordinates.
			Point3d deltaViewPoint;
			// Point3d to hold the change in view point in x,y,z coordinates.
			Point3d deltaPoint = new Point3d();
			// Matrix4d to hold the inverted view transformation matrix.
			Matrix4d invertTransform = new Matrix4d();
			boolean exceptionFlag = false;

			// Value of change in coordinates updated.
			deltaViewPoint = new Point3d(
				(x - oldX) / mouseSensitivity,
				(y - oldY) / mouseSensitivity,
				0.0);
			try {
				invertTransform.invert(viewTransform);
			} catch (Exception ex) {
				System.out.println("Cannot invert view transform matrix.");
				exceptionFlag = true;
			}

			if (exceptionFlag == false) {
				invertTransform.transform(deltaViewPoint, deltaPoint);
				deltaPoint.y = -1 * deltaPoint.y;
				//    System.out.println("...test...");
				// view point is updated.
				viewPoint.sub(deltaPoint);
				// show the view point values in view panel.
				showPointLabel.setText(viewPoint.toString());
				// new view transform matrix is created.
				setViewTransform();
			}

			// to update canvas.
			needsUpdate = true;
			repaint();
			// to update values for oldX and oldY.
			oldX = x;
			oldY = y;
			createTargetTable(); // update target popints for changed view point
		}

		/** Mouse pressed event.
		 *
		 */
		void this_mousePressed(int x, int y) {
			
//			System.out.println("Mouse pressed at " + x + ", " + y );
			
			// show the view point values in view panel.
			showPointLabel.setText(viewPoint.toString());
			oldX = x;
			oldY = y;
		}
		
		public void mouseWheelMoved(MouseWheelEvent e) {

			if(e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {

				double zoomRatio = 1;
				if (e.getWheelRotation() < 0) {
					zoomRatio = 1.05;
				} else {
					zoomRatio = 0.95;
				}

				setScale(scale * zoomRatio);
				createTargetTable();
				repaint();

			}


		}

	}// end of DrawWFCanvasMouseAdapter definition
	
	

	void wcsCheckBox_itemStateChanged(ItemEvent e) {
		showWorldCS = !showWorldCS;
		needsUpdate = true;
		repaint();

	}

	
	public AffineTransform getTransform() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Rectangle2D geetBoundigBox() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void scale(double sx, double sy) {
		// TODO Auto-generated method stub
		
	}

	
	public void translate(double tx, double ty) {
		// TODO Auto-generated method stub
		
	}

	
	public void rotate(double angle) {
		// TODO Auto-generated method stub
		
	}

	
	public void togglePaintStrings() {
		// TODO Auto-generated method stub
		
	}

	
	public void togglePaintImages() {
		// TODO Auto-generated method stub
		
	}


	public void computeBoundingBox() {
		// TODO Auto-generated method stub
		
	}

	
	public void scaleFitTarget() {
		// TODO Auto-generated method stub
		
	}
} // END OF CLASS DEFINITION

// INNER CLASSES.

/** viewButton adapter class.
 *
 */
class DrawWFPanel_viewButton_actionAdapter
	implements
		java.awt.event.ActionListener {
	DrawWFPanel adaptee;

	DrawWFPanel_viewButton_actionAdapter(DrawWFPanel adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.viewButton_actionPerformed();
	}
}

class DrawWFPanel_wcsCheckBox_itemAdapter
	implements
		java.awt.event.ItemListener {
	DrawWFPanel adaptee;

	DrawWFPanel_wcsCheckBox_itemAdapter(DrawWFPanel adaptee) {
		this.adaptee = adaptee;
	}
	public void itemStateChanged(ItemEvent e) {
		adaptee.wcsCheckBox_itemStateChanged(e);
	}
}
