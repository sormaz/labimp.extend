package edu.ohiou.mfgresearch.labimp.gtk3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

public class Tuple3fEditor extends DefaultCellEditor {
	Tuple3f tuple;
	Action action = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		}
	};

	private static final Dimension LABEL_SIZE = new Dimension(35, 17);
	private static final Color BACKGROUND_COLOR = Color.pink;

	// GUI components needed for event handling
	JTextField xField = new JTextField();
	JTextField yField = new JTextField();
	JTextField zField = new JTextField();
	JLabel viewPointLabel = new JLabel();

	public Tuple3fEditor(Tuple3f tuple, Action action) {
		this(tuple);
		this.action = action;
		
	}
	public Tuple3fEditor(Tuple3f inTuple) {
		super(new JTextField());
		tuple = inTuple;
		editorComponent = new JPanel();
		delegate = new EditorDelegate() {
			public void setValue(Object value) {
				tuple = (Tuple3f) value;
				xField.setText("" + tuple.x);
				yField.setText("" + tuple.y);

				zField.setText("" + tuple.z);

			}

			public Object getCellEditorValue() {
				double x1 = 0, y1 = 0, z1 = 0, s = 0;
				try {
					//       tuple = (Tuple3f) tupleClass.newInstance();
					x1 = Double.parseDouble(xField.getText());
					y1 = Double.parseDouble(yField.getText());
					z1 = Double.parseDouble(zField.getText());
				} catch (NumberFormatException ex1) {
					//      errorLabel.setText("< Invalid string for coordinate of view point >");
					xField.grabFocus();
					//      throw ex1;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				tuple.set((float) x1, (float) y1, (float) z1);
				return tuple;
			}

		};
		init();
	}

	public void init() {

		JLabel xLabel = new JLabel();
		JLabel yLabel = new JLabel();
		JLabel zLabel = new JLabel();
		FlowLayout flowLayout2 = new FlowLayout();
		FlowLayout flowLayout3 = new FlowLayout();
		JLabel errorLabel = new JLabel();
		JLabel tupleLabel = new JLabel();
		JTextField scaleField = new JTextField();
		JLabel showPointLabel = new JLabel();

		viewPointLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		xLabel.setHorizontalTextPosition(SwingConstants.RIGHT);

		editorComponent.setBackground(BACKGROUND_COLOR);
		editorComponent.setLayout(new BorderLayout());
		editorComponent.setToolTipText("Enter view point and scale here.");

		xLabel.setForeground(Color.blue);
		xLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		xLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		xLabel.setText("X: ");
		xLabel.setPreferredSize(LABEL_SIZE);
		//    xField.setText("" +tuple.x);
		xField.setHorizontalAlignment(SwingConstants.RIGHT);
		xField.addActionListener(action);
		xField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
				field_focusGained(xField);
			}
			public void focusLost(FocusEvent e) {
				try {
					fieldFocusLost(xField);
					fireEditingStopped();
					action.actionPerformed(null);
				} catch (Exception ex) {
				}
			}
		});

		yLabel.setForeground(Color.blue);
		yLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		yLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		yLabel.setText("Y: ");
		yLabel.setPreferredSize(LABEL_SIZE);
		yLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		yField.setHorizontalAlignment(SwingConstants.RIGHT);
		//    yField.setText("" +tuple.y);
		yField.addActionListener(action);
		yField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
				field_focusGained(yField);
			}
			public void focusLost(FocusEvent e) {
				try {
					fieldFocusLost(yField);
					action.actionPerformed(null);
				} catch (Exception ex) {
				}
			}
		});

		zLabel.setForeground(Color.blue);
		zLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		zLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		zLabel.setText("Z: ");
		zLabel.setPreferredSize(LABEL_SIZE);
		zLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		//    zField.setText("" +tuple.z);
		zField.setHorizontalAlignment(SwingConstants.RIGHT);
		zField.addActionListener(action);
		zField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
				field_focusGained(zField);
			}
			public void focusLost(FocusEvent e) {
				try {
					fieldFocusLost(zField);
					action.actionPerformed(null);
				} catch (Exception ex) {
				}
			}
		});

		errorLabel.setFont(new java.awt.Font("SansSerif", 0, 12));
		errorLabel.setForeground(Color.red);
		//    String name = tuple.getClass().getName();
		//    String shortName = name.substring(name.lastIndexOf(".")+1, name.length());
		//    viewPointLabel.setText(shortName);
		viewPointLabel.setBackground(BACKGROUND_COLOR);

		showPointLabel.setFont(new java.awt.Font("SansSerif", 0, 10));
		showPointLabel.setForeground(SystemColor.desktop);
		showPointLabel.setHorizontalAlignment(SwingConstants.CENTER);
		showPointLabel.setHorizontalTextPosition(SwingConstants.CENTER);

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

		//JPanel scalePanel = new JPanel();
		//scalePanel.setLayout (new BorderLayout());
		//scalePanel.add(scaleLabel, BorderLayout.WEST);
		//scalePanel.add(scaleField, BorderLayout.CENTER);
		//scalePanel.setBackground(BACKGROUND_COLOR);

		JPanel pointPanel = new JPanel();
		pointPanel.setLayout(new GridLayout(1, 5, 5, 0));
		pointPanel.add(viewPointLabel);
		pointPanel.add(xPanel);
		pointPanel.add(yPanel);
		pointPanel.add(zPanel);
		//pointPanel.add(scalePanel);
		pointPanel.setBackground(BACKGROUND_COLOR);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(showPointLabel, BorderLayout.CENTER);
		bottomPanel.setBackground(BACKGROUND_COLOR);
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BorderLayout());
		middlePanel.setBackground(BACKGROUND_COLOR);
		middlePanel.add(viewPointLabel, BorderLayout.WEST);
		middlePanel.add(pointPanel, BorderLayout.CENTER);

		editorComponent.add(errorLabel, BorderLayout.NORTH);
		editorComponent.add(middlePanel, BorderLayout.CENTER);
		editorComponent.add(bottomPanel, BorderLayout.SOUTH);

	}
	private void configure(Object value) {

		tuple = (Tuple3f) value;
		String name = tuple.getClass().getName();
		String shortName = name.substring(name.lastIndexOf(".") + 1, name
			.length());
		viewPointLabel.setText(shortName);

		xField.setText("" + tuple.x);
		yField.setText("" + tuple.y);
		zField.setText("" + tuple.z);
	}

	public static void main(String[] args) {
		Action outAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("action done");
			}
		};
		Tuple3fEditor editor = new Tuple3fEditor(new Vector3f(), outAction);

		JFrame frame = new JFrame("p3ed editor");
		frame.setSize(600, 600);
		frame.getContentPane().add(editor.editorComponent);
		frame.validate();
		frame.show();
	}
	public Component getTableCellEditorComponent(
		JTable table,
		Object value,
		boolean isSelected,
		int row,
		int column) {
		configure(value);
		return editorComponent;
	}

	public Component getTreeCellEditorComponent(
		JTree tree,
		Object value,
		boolean isSelected,
		boolean expanded,
		boolean leaf,
		int row) {
		configure(value);
		return editorComponent;
	}
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	/**
	 * Callback. When field has the focus, ensures that all the text within
	 * it is selected.
	 */
	private void field_focusGained(JTextField field) {
		field.selectAll();
	}
	
	public Object getCellEditorValue() {
		return tuple;
	}

	/** view button to set value for viewpoint and redraw canvas.
	 *  (get viewpoint vector values from user)
	 */
	void fieldFocusLost(JTextField field) {
		// Local variables.
		double x1 = 0, y1 = 0, z1 = 0, s = 0;
		// variable to check for validity for values.
		boolean exceptionFlag = false;

		// View Point Vector
		try {
			x1 = Double.parseDouble(xField.getText());
			y1 = Double.parseDouble(yField.getText());
			z1 = Double.parseDouble(zField.getText());
			tuple.set((float) x1, (float) y1, (float) z1);
		} catch (NumberFormatException ex1) {
			field.grabFocus();
			//      throw ex1;
		}
	}
}