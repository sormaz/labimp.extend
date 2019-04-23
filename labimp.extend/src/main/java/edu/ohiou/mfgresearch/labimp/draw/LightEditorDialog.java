package edu.ohiou.mfgresearch.labimp.draw;

/**
 * <p>Title:       LightEditorDialog class</p>
 * <p>Description: Dialog box to add lights to the content branch</p>
 * <p>Copyright:   Dr.D.N.Sormaz, Ohio University Copyright (c) 2003</p>
 * <p>Company:     IMSE Dept, Ohio University</p>
 * @author         Deepak Pisipati + Dr.Dusan N. Sormaz
 * @version 1.0
 */

/**
 * Note - Some of the code has been adopted from Sun tutorials and EE590 course
 *        projects
 */

//Importing awt and util classes
import javax.media.j3d.Light;
import javax.swing.table.TableColumn;
import javax.media.j3d.BranchGroup;
import java.awt.Frame;
import java.awt.Dialog;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JComboBox;
import java.awt.Component;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;

//Importing swing classes
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

//Importing packages required for Java3D methods
import javax.vecmath.*;
import javax.media.j3d.*;

//Importing classes from IMPLAN
import edu.ohiou.mfgresearch.labimp.table.*;

import edu.ohiou.mfgresearch.labimp.gtk3d.Tuple3fEditor;
import edu.ohiou.mfgresearch.labimp.gtk3d.Tuple3fRenderer;

/**
 *
 *  The class has method implementations in order to do the following actions.
 *  <br><p>
 *  When a light is added, it is added to a new BranchGroup (lightBranchGroup)
 *  and this BranchGroup is in turn attached to the supplied 'contentBranchGroup'
 *  The new light added and its parent BranchGroup are stored in a Hashmap
 *  (lightHashMap).</p>
 *  <p>
 *  When a light is to be deleted/removed, the parent BranchGroup pertaining to
 *  the 'lightToBeDetached' is retained from the 'lightHashMap' and detached from
 *  the 'contentBranchGroup'.</p>
 *
 */

public class LightEditorDialog extends JDialog {
	private BorderLayout mainPanelBorderLayout = new BorderLayout();
	private JScrollPane scrollPane = new JScrollPane();
	private JPanel mainPanel = new JPanel();
	private JPanel namePanel = new JPanel();
	private JPanel tablePanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private Component parent;

	private JLabel nameLabel = new JLabel();
	private JTable lightEditorTable = new JTable();
	private LightEditorTableModel lightEditorTableModel;
	private JColorChooser colorChooser;
	private JDialog dialog;
	private JButton colorButton;
	private JButton deleteRowButton = new JButton();
	private ColorColumnEditor colorColumnEditor;
	private JButton closeButton = new JButton();
	private JComboBox comboBox = new JComboBox();

	private BranchGroup contentBranchGroup;
	private HashMap lightHashMap = new HashMap();

	public static final int CLOSE_OPTION = -1;
	public static final int AMBIENT_LIGHT = 0;
	public static final int DIRECTIONAL_LIGHT = 1;
	public static final int POINT_LIGHT = 2;
	public static final int SPOT_LIGHT = 3;
	public static int COMBOBOX_OPTION_CHOSEN;

	/**
	 * Creates a new dialog after determining its parent class
	 *
	 * @param parent parent of this
	 * @param title title of this
	 * @param modal boolean to determine whether the thread should continue
	 *              execution or not when this is active
	 * @throws Exception
	 * @return this
	 */
	public static LightEditorDialog createDialog(
		Component parent,
		String title,
		boolean modal) throws Exception {
		Window w;
		if (parent instanceof Window)
			w = (Window) parent;
		else
			w = SwingUtilities.getWindowAncestor(parent);
		LightEditorDialog d;
		if (w instanceof Frame)
			d = new LightEditorDialog(parent, (Frame) w, title, modal);
		else
			d = new LightEditorDialog(parent, (Dialog) w, title, modal);
		return d;
	}

	/**
	 * Creates a new dialog with a 'Dialog' as its owner
	 *
	 * @param parent parent of this
	 * @param owner ancestor of the parent
	 * @param title title of this
	 * @param modal boolean to determine whether the thread should continue
	 *              execution or not when this is active
	 * @throws Exception
	 * @return this
	 */
	private LightEditorDialog(
		Component parent,
		Dialog owner,
		String title,
		boolean modal) throws Exception {
		super(owner, title, modal);
		this.parent = parent;
		jbInit();
	}

	/**
	 * Creates a new dialog with a 'Frame' as its owner
	 *
	 * @param parent parent of this
	 * @param owner ancestor of the parent
	 * @param title title of this
	 * @param modal boolean to determine whether the thread should continue
	 *              execution or not when this is active
	 * @throws Exception
	 * @return this
	 */
	private LightEditorDialog(
		Component parent,
		Frame owner,
		String title,
		boolean modal) throws Exception {
		super(owner, title, modal);
		this.parent = parent;
		jbInit();
	}

	/**
	 * Initializes the dialog box with the supplied contentbranch
	 *
	 * @param contentBranch root branch of the scenegraph to which the lights
	 *                      have to be added
	 */
	public void initializeDialog(BranchGroup contentBranch) {
		contentBranchGroup = contentBranch;
	}

	/**
	 *
	 *    JBuilder initialization of gui components
	 *
	 */

	private void jbInit() {
		this.getContentPane().add(mainPanel);
		this.setLocationRelativeTo(parent);
		this.setSize(new Dimension(500, 200));
		mainPanel.setLayout(mainPanelBorderLayout);
		mainPanel.add(namePanel, BorderLayout.NORTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		nameLabel.setText("Light Editor");
		namePanel.add(nameLabel);
		JLabel addLightLabel = new JLabel("Add Light");
		addLightLabel.setToolTipText("Click here to add new light");
		String[] columnNames = new String[] { "Light On/Off", "Light Color",
				"Light Position/Direction" };
		lightEditorTableModel = new LightEditorTableModel(
			new Object[] { "Add light ..." },
			columnNames,
			new LightEditorCellGenerator());
		lightEditorTable.setModel(lightEditorTableModel);
		TableColumnModel columnModel = lightEditorTable.getColumnModel();
		TableColumn lightColumn = columnModel.getColumn(0);
		columnModel.getColumn(1).setMaxWidth(90);
		lightColumn.setMaxWidth(100);
		lightColumn.setWidth(100);
		lightColumn.setCellRenderer(new ClassNameRenderer());
		setLightEditor(lightColumn);
		columnModel.getColumn(1).setCellRenderer(new BooleanColumnRenderer());
		TableColumn colorColumn = columnModel.getColumn(2);
		colorColumn.setCellRenderer(new ColorColumnRenderer());
		colorColumn.setMaxWidth(90);
		setColorEditor(colorColumn);
		TableColumn tuple3dColumn = columnModel.getColumn(3);
		tuple3dColumn.setCellRenderer(new Tuple3fColumnRenderer());
		setTuple3fEditor(tuple3dColumn);
		scrollPane.getViewport().add(lightEditorTable, null);
		lightEditorTable.setGridColor(Color.cyan);

		ListSelectionModel rowSM = lightEditorTable.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				int selectedRow = lsm.getMinSelectionIndex();
				if (selectedRow != lightEditorTableModel.getRowCount() - 1)
					deleteRowButton.setEnabled(true);
				else
					deleteRowButton.setEnabled(false);
			}
		});

		closeButton.setMnemonic('C');
		closeButton.setText("CLOSE");
		deleteRowButton.setMnemonic('D');
		deleteRowButton.setText("Delete Light(s)");
		deleteRowButton.setEnabled(false);
		buttonPanel.add(deleteRowButton);
		buttonPanel.add(closeButton);

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hide();
			}
		});

		deleteRowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowsToBeDeleted = lightEditorTable.getSelectedRowCount();
				for (int i = 0; i < rowsToBeDeleted; i++) {
					int selectedRow = lightEditorTable.getSelectedRow();
					if (selectedRow != lightEditorTableModel.getRowCount() - 1)
						lightEditorTableModel.deleteRow(selectedRow);
				}
			}
		});
	}

	/**
	 * Creates a combobox to add new sources of light
	 *
	 * @param lightColumn Table Column for which a combobox is created as an editor
	 *                    to enable addition of lights
	 */
	private void setLightEditor(TableColumn lightColumn) {
		AmbientLight ambientLight = new AmbientLight();
		DirectionalLight directionalLight = new DirectionalLight();
		PointLight pointLight = new PointLight();
		SpotLight spotLight = new SpotLight();
		spotLight.setPosition(new Point3f(0.0f, 1.0f, 0.0f));
		pointLight.setPosition(new Point3f(0.0f, 1.0f, 0.0f));
		comboBox.setRenderer(new ClassNameRenderer());
		comboBox.addItem(ambientLight);
		comboBox.addItem(directionalLight);
		comboBox.addItem(pointLight);
		comboBox.addItem(spotLight);
		LightColumnEditor editor = new LightColumnEditor(comboBox);
		lightColumn.setCellEditor(editor);
	}

	/**
	 * Sets the editor for the Color Column cells
	 *
	 * @param colorColumn Table Column for which a JColorchooser is created as an
	 *                    editor to change color of light
	 */
	private void setColorEditor(TableColumn colorColumn) {
		colorButton = new JButton("");
		colorChooser = new JColorChooser();
		colorColumnEditor = new ColorColumnEditor(colorButton);
		colorColumn.setCellEditor(colorColumnEditor);
		ActionListener okListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorColumnEditor.currentColor = colorChooser.getColor();
			}
		};
		dialog = JColorChooser.createDialog(
			colorButton,
			"Choose a Color",
			true,
			colorChooser,
			okListener,
			null);
		colorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorButton.setBackground(colorColumnEditor.currentColor);
				colorChooser.setColor(colorColumnEditor.currentColor);
				dialog.show();
			}
		});
	}

	Tuple3f currentTuple3f;
	/**
	 * Creates a Point3dEditor for the Light Position/Direction column
	 *
	 * @param tuple3dColumn Table Column for which a Point3fEditor is created as
	 *                      an editor to change position/direction of light
	 */
	private void setTuple3fEditor(TableColumn tuple3dColumn) {
		final Tuple3fEditor tuple3fEditor = new Tuple3fEditor(new Vector3f());
		Action outAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("in action");
				currentTuple3f = (Tuple3f) tuple3fEditor.getCellEditorValue();
			}
		};
//		tuple3fEditor.
		tuple3dColumn.setCellEditor(tuple3fEditor);
	}

	/**
	 *
	 *    LightCellEditor to determine option chosen and add corresponding new light
	 *    into the content branch
	 *
	 */

	class LightColumnEditor extends DefaultCellEditor {
		public LightColumnEditor(JComboBox jcb) {
			super(jcb);
		}

		/**
		 *
		 *      Adds the light chosen from the combobox to the scenegraph.<br>
		 *      Sets all required capabilites for the selectedLight depending on the type
		 *      of Light. Adds this selectedLight to the parent lightBranchGroup
		 *      which subsequently is then added to the contentBranchGroup
		 *
		 *      @return Object returns a String for the TableCell(0,0)
		 *
		 */

		public Object getCellEditorValue() {
			JComboBox comboBox = (JComboBox) editorComponent;
			COMBOBOX_OPTION_CHOSEN = comboBox.getSelectedIndex();
			BranchGroup lightBranchGroup;
			BoundingSphere bounds = new BoundingSphere(new Point3d(
				0.0,
				0.0,
				0.0), 1000);
			Light selectedLight = (Light) ((Light) comboBox.getSelectedItem())
				.cloneNode(true);
			if (COMBOBOX_OPTION_CHOSEN == DIRECTIONAL_LIGHT) {
				selectedLight = (DirectionalLight) ((DirectionalLight) comboBox
					.getSelectedItem()).cloneNode(true);
				selectedLight
					.setCapability(DirectionalLight.ALLOW_DIRECTION_READ);
				selectedLight
					.setCapability(DirectionalLight.ALLOW_DIRECTION_WRITE);
			}
			if (COMBOBOX_OPTION_CHOSEN == POINT_LIGHT
				|| COMBOBOX_OPTION_CHOSEN == SPOT_LIGHT) {
				selectedLight = (PointLight) ((PointLight) comboBox
					.getSelectedItem()).cloneNode(true);
				selectedLight.setCapability(PointLight.ALLOW_POSITION_READ);
				selectedLight.setCapability(PointLight.ALLOW_POSITION_WRITE);
			}
			selectedLight.setCapability(Light.ALLOW_STATE_READ);
			selectedLight.setCapability(Light.ALLOW_STATE_WRITE);
			selectedLight.setCapability(Light.ALLOW_COLOR_READ);
			selectedLight.setCapability(Light.ALLOW_COLOR_WRITE);
			selectedLight.setCapability(Light.ALLOW_COLOR_WRITE);
			selectedLight.setInfluencingBounds(bounds);
			lightBranchGroup = new BranchGroup();
			lightBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
			lightBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
			lightBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
			lightBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
			lightBranchGroup.addChild(selectedLight);
			lightHashMap.put(selectedLight, lightBranchGroup);
			contentBranchGroup.addChild(lightBranchGroup);
			lightEditorTableModel.addRow(selectedLight, lightEditorTableModel
				.getRowCount() - 1);
			return selectedLight;
		}
	}

	/**
	 *
	 *    BooleanCellRenderer to display checkbox for all the rows except the first row
	 *
	 */

	class BooleanColumnRenderer implements TableCellRenderer {
		public Component getTableCellRendererComponent(
			JTable table,
			Object color,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
			if ((row + 1) == table.getModel().getRowCount()) {
				JLabel label = new JLabel("");
				return label;
			} else {
				TableCellRenderer tr = table.getDefaultRenderer(Boolean.class);
				return tr.getTableCellRendererComponent(
					table,
					color,
					isSelected,
					hasFocus,
					row,
					column);
			}
		}
	}

	/**
	 *
	 *    ColorCellRenderer to display selected color of light from color chooser
	 *
	 */

	class ColorColumnRenderer implements TableCellRenderer {
		public Component getTableCellRendererComponent(
			JTable table,
			Object color,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
			JLabel label = new JLabel();
			if ((row + 1) == table.getModel().getRowCount()) {
				label.setText("");
			} else {
				label.setBackground(((Color) color));
				label.setOpaque(true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setToolTipText("Click here to change light color");
				label.setText("Change color ...");
			}
			return label;
		}
	}

	/**
	 *
	 *    TupleRenderer to display Tuple3fRenderer for all the rows except the first
	 *    row
	 *
	 */

	class Tuple3fColumnRenderer implements TableCellRenderer {
		public Component getTableCellRendererComponent(
			JTable table,
			Object color,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
			if ((row + 1) == table.getModel().getRowCount()) {
				JLabel label = new JLabel("");
				return label;
			} else {
				TableCellRenderer tr = table
					.getDefaultRenderer(Tuple3fRenderer.class);
				return tr.getTableCellRendererComponent(
					table,
					color,
					isSelected,
					hasFocus,
					row,
					column);
			}
		}
	}

	/**
	 *
	 *    ColorCellEditor to edit colors using a button
	 *
	 */

	class ColorColumnEditor extends DefaultCellEditor {
		Color currentColor = null;

		public ColorColumnEditor(JButton jButton) {
			super(new JCheckBox());
			editorComponent = jButton;
			setClickCountToStart(1);
			jButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		public Object getCellEditorValue() {
			return currentColor;
		}

		public Component getTableCellEditorComponent(
			JTable table,
			Object value,
			boolean isSelected,
			int row,
			int column) {
			currentColor = (Color) value;
			return editorComponent;
		}
	}

	/**
	 *
	 *    TableModel for the lightEditorTable setting the first column as not editable
	 *
	 */

	public class LightEditorTableModel extends RectangularTableModel {
		public LightEditorTableModel() {
			super();
		}

		public LightEditorTableModel(Object[] inRows, Object[] inColumns) {
			super(inRows, inColumns);
		}

		public LightEditorTableModel(
			Object[] inRows,
			Object[] inColumns,
			TableCellGenerator generator) {
			super(inRows, inColumns, generator);
		}

		public boolean isCellEditable(int row, int col) {
			Object o = getValueAt(row, 0);
			if (o instanceof Light)
				return col > 0;
			else
				return col == 0;
		}

		public void deleteRow(int rowNumber) {
			Light lightToBeDetached = (Light) lightEditorTableModel.getValueAt(
				rowNumber,
				0);
			((BranchGroup) lightHashMap.get(lightToBeDetached)).detach();
			lightHashMap.remove(lightToBeDetached);
			super.deleteRow(rowNumber);
		}
	}

	/**
	 *
	 *    Generator to generate and modify cells for the light editor
	 *
	 */

	public class LightEditorCellGenerator implements TableCellGenerator {
		public Object makeTableCell(Object o1, Object o2) {
			if (((String) o2).equalsIgnoreCase("light on/off")) {
				if (o1 instanceof Light)
					return new Boolean(((Light) o1).getEnable());
				else
					//For String "Add new Light"
					return new Boolean(false);
			} else if (((String) o2).equalsIgnoreCase("light color")) {
				if (o1 instanceof Light) {
					Color3f lightColor = new Color3f();
					((Light) o1).getColor(lightColor);
					return lightColor.get();
				} else
					//For String "Add new Light"
					return new Color(1f, 1f, 1f);
			} else//Light Position/Direction
			{
				Vector3f direction3f = new Vector3f();
				Point3f point3f = new Point3f();
				if (o1 instanceof Light) {
					if (o1 instanceof AmbientLight)
						return new Vector3f();
					else if (o1 instanceof DirectionalLight) {
						((DirectionalLight) o1).getDirection(direction3f);
						return direction3f;
					} else {
						((PointLight) o1).getPosition(point3f);
						return point3f;
					}
				} else
					//For String "Add new Light"
					return new Vector3f();
			}
		}

		public void updateRelation(Object o1, Object o2, Object value) {
			if (((String) o2).compareToIgnoreCase("light on/off") == 0)
				((Light) o1).setEnable(((Boolean) value).booleanValue());
			if (((String) o2).compareToIgnoreCase("light color") == 0)
				((Light) o1).setColor(new Color3f((Color) value));
			if (((String) o2).compareToIgnoreCase("Light Position/Direction") == 0)
				updateTuple((Light) o1, value);
		}
	}

	private void updateTuple(Light light, Object tuple) {
		System.out.println ("update tuple");
		if (light instanceof DirectionalLight)
			((DirectionalLight) light).setDirection((Vector3f) tuple);
		else {
			if (light instanceof PointLight) {
				((PointLight) light).setPosition((Point3f) tuple);				
			}
		}
	}

	public static void main(String[] args) {
		try {
			JFrame frame = new JFrame("frame");
			frame.setVisible(true);
			LightEditorDialog dia = LightEditorDialog.createDialog(
				frame,
				"Light editor",
				true);
			dia.initializeDialog(new BranchGroup());

			dia.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
