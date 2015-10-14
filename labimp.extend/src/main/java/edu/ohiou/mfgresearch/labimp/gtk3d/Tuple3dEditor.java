package edu.ohiou.mfgresearch.labimp.gtk3d;

import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.JTree;
import javax.swing.JTable;
import javax.swing.Action;
import javax.vecmath.Tuple3d;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.EventObject;
import javax.swing.event.*;
import javax.vecmath.*;

public class Tuple3dEditor extends JPanel
	implements
		TableCellEditor,
		TreeCellEditor {
	Tuple3d tuple;
	Action action = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		}
	};

	private static final Dimension LABEL_SIZE = new Dimension(35, 17);
	private static final Color BACKGROUND_COLOR = Color.pink;

	public Tuple3dEditor(Tuple3d tuple, Action action) {
		this(tuple);
		this.action = action;
	}
	public Tuple3dEditor(Tuple3d tuple) {
		this.tuple = tuple;
		init();
	}

	public void init() {
		JLabel viewPointLabel = new JLabel();
		viewPointLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		xLabel.setHorizontalTextPosition(SwingConstants.RIGHT);

		this.setBackground(BACKGROUND_COLOR);
		this.setLayout(new BorderLayout());
		this.setToolTipText("Enter view point and scale here.");

		xLabel.setForeground(Color.blue);
		xLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		xLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		xLabel.setText("X: ");
		xLabel.setPreferredSize(LABEL_SIZE);
		xField.setText("" + tuple.x);
		xField.setHorizontalAlignment(SwingConstants.RIGHT);
		xField.addActionListener(action);
		xField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
				field_focusGained(xField);
			}
			public void focusLost(FocusEvent e) {
				try {
					fieldFocusLost();
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
		yField.setText("" + tuple.y);
		yField.addActionListener(action);
		yField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
				field_focusGained(yField);
			}
			public void focusLost(FocusEvent e) {
				try {
					fieldFocusLost();
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
		zField.setText("" + tuple.z);
		zField.setHorizontalAlignment(SwingConstants.RIGHT);
		zField.addActionListener(action);
		zField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
				field_focusGained(zField);
			}
			public void focusLost(FocusEvent e) {
				try {
					fieldFocusLost();
					action.actionPerformed(null);
				} catch (Exception ex) {
				}
			}
		});

		errorLabel.setFont(new java.awt.Font("SansSerif", 0, 12));
		errorLabel.setForeground(Color.red);
		String name = tuple.getClass().getName();
		String shortName = name.substring(name.lastIndexOf(".") + 1, name
			.length());
		viewPointLabel.setText(shortName);
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

		this.add(errorLabel, BorderLayout.NORTH);
		this.add(middlePanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);

	}

	public static void main(String[] args) {
		Action outAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("action done");
			}
		};
		Tuple3dEditor tuple3dEditor1 = new Tuple3dEditor(
			new Vector3d(),
			outAction);

		JFrame frame = new JFrame("p3ed editor");
		frame.setSize(600, 600);
		frame.getContentPane().add(tuple3dEditor1);
		frame.validate();
		frame.show();
	}
	public Component getTableCellEditorComponent(
		JTable table,
		Object value,
		boolean isSelected,
		int row,
		int column) {
		Tuple3d tuple = (Tuple3d) value;
		xField.setText("" + tuple.x);
		yField.setText("" + tuple.y);
		zField.setText("" + tuple.z);
		return this;
	}

	public Component getTreeCellEditorComponent(
		JTree tree,
		Object value,
		boolean isSelected,
		boolean expanded,
		boolean leaf,
		int row) {
		Tuple3d tuple = (Tuple3d) value;
		xField.setText("" + tuple.x);
		yField.setText("" + tuple.y);
		zField.setText("" + tuple.z);
		return this;
	}
	public Object getCellEditorValue() {
		/**@todo Implement this javax.swing.CellEditor method*/
		throw new java.lang.UnsupportedOperationException(
			"Method getCellEditorValue() not yet implemented.");
	}
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}
	public boolean shouldSelectCell(EventObject anEvent) {
		/**@todo Implement this javax.swing.CellEditor method*/
		throw new java.lang.UnsupportedOperationException(
			"Method shouldSelectCell() not yet implemented.");
	}
	public boolean stopCellEditing() {
		/**@todo Implement this javax.swing.CellEditor method*/
		throw new java.lang.UnsupportedOperationException(
			"Method stopCellEditing() not yet implemented.");
	}
	public void cancelCellEditing() {
		/**@todo Implement this javax.swing.CellEditor method*/
		throw new java.lang.UnsupportedOperationException(
			"Method cancelCellEditing() not yet implemented.");
	}
	public void addCellEditorListener(CellEditorListener l) {
		/**@todo Implement this javax.swing.CellEditor method*/
		throw new java.lang.UnsupportedOperationException(
			"Method addCellEditorListener() not yet implemented.");
	}
	public void removeCellEditorListener(CellEditorListener l) {
		/**@todo Implement this javax.swing.CellEditor method*/
		throw new java.lang.UnsupportedOperationException(
			"Method removeCellEditorListener() not yet implemented.");
	}

	JLabel xLabel = new JLabel();
	JTextField xField = new JTextField();
	JLabel yLabel = new JLabel();
	JTextField yField = new JTextField();
	JLabel zLabel = new JLabel();
	JTextField zField = new JTextField();
	FlowLayout flowLayout2 = new FlowLayout();
	FlowLayout flowLayout3 = new FlowLayout();
	JLabel errorLabel = new JLabel();
	JLabel tupleLabel = new JLabel();
	//  JLabel scaleLabel = new JLabel();
	JTextField scaleField = new JTextField();
	JLabel showPointLabel = new JLabel();

	/**
	 * Callback. When field has the focus, ensures that all the text within
	 * it is selected.
	 */
	private void field_focusGained(JTextField field) {
		field.selectAll();
	}

	/**
	 * view button to set value for viewpoint and redraw canvas.
	 *     (get viewpoint vector values from user)
	 *   
	 */

	void fieldFocusLost() {
		// Local variables.
		double x1 = 0, y1 = 0, z1 = 0, s = 0;
		// variable to check for validity for values.
		boolean exceptionFlag = false;

		// View Point Vector
		try {
			x1 = Double.parseDouble(xField.getText());
			y1 = Double.parseDouble(yField.getText());
			z1 = Double.parseDouble(zField.getText());
		} catch (NumberFormatException ex1) {
			errorLabel
				.setText("< Invalid string for coordinate of view point >");
			throw ex1;
		}

		if (exceptionFlag == false) {
			errorLabel.setText("");
			tuple.set(x1, y1, z1);
		}
	}
}