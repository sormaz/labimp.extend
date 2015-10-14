/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.graphmodel.gui;



import javax.swing.*;
import java.awt.*;
import edu.ohiou.mfgresearch.labimp.basis.*;
import edu.ohiou.mfgresearch.labimp.table.ModelTable;
import edu.ohiou.mfgresearch.labimp.graphmodel.GraphModelListener;


/**
 * The component which displays the graph model
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Ohio University</p>
 * @author Chandrasekhar Ganduri
 * @version 1.0
 */
public class GraphCanvas extends ViewObject.ViewPanel implements GraphModelListener {

	protected GraphRenderer renderer;
	protected Draw2DPanel graphPanel;
	protected JTabbedPane tabbedPane;	
	protected JToolBar toolBar;
	
	private DefaultGraphTableCellRenderer cellRenderer;
	
	public GraphCanvas()
	{
	}
	
	public GraphCanvas(GraphRenderer renderer)
	{
		this.renderer = renderer;		
		tabbedPane = new JTabbedPane();
		makeTablePanel();
		makeGraphPanel();
		makeToolBar();
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);
		this.add(toolBar, BorderLayout.SOUTH);		
		
	}
	
	
	private void makeTablePanel()
	{
		cellRenderer = new DefaultGraphTableCellRenderer();
		ModelTable modelTable = new ModelTable(renderer.getGraphModel().getTableModel());
		modelTable.setDefaultRenderer(Boolean.class, cellRenderer);
		modelTable.setDefaultRenderer(Integer.class, cellRenderer);
		modelTable.setDefaultRenderer(String.class, cellRenderer);
		// Cell Editor ?
		tabbedPane.addTab("Table",modelTable.getPanel());		
	}
	
	private void makeGraphPanel()
	{
		graphPanel = new Draw2DPanel(renderer, null);
		tabbedPane.addTab("Graph", graphPanel);
		System.out.println("Graph Panel" + ((Object)graphPanel).toString());
	}
	
	public Draw2DPanel GraphPanel()
	{
		return graphPanel;
	}
	
	private void makeToolBar()
	{
		toolBar = new JToolBar("Graph Controls");
		
		// LayoutChooser
		JPanel layoutPanel = new JPanel();		
		String[] layouts = {"Random", "Orthogonal", "Disjunctive"};
		JComboBox layoutChooser = new JComboBox(layouts);
		layoutChooser.setEditable(true);
        layoutChooser.setSelectedIndex(0);
        JLabel layoutChooserLabel = new JLabel("Layout");
        layoutPanel.add(layoutChooserLabel);
        layoutPanel.add(layoutChooser);
        
        // Rendering Control
        JPanel renderingOpPanel = new JPanel();
        String[] renderings = {"Default", "user defined"};
        JComboBox renderingOptions = new JComboBox(renderings);
        renderingOptions.setEditable(true);
        renderingOptions.setSelectedIndex(0);
        JLabel renderingOpLabel = new JLabel("Rendering Options");
        renderingOpPanel.add(renderingOpLabel);
        renderingOpPanel.add(renderingOptions);
        
        //
        toolBar.add(renderingOpPanel);
        toolBar.addSeparator();
        toolBar.add(layoutPanel);        
	}
	
	// Graph Listener
	public void nodeAdded() {
		// TODO Auto-generated method stub
	}

	
	public void nodeDeleted() {
		// TODO Auto-generated method stub
	}

	
	public void arcAdded() {
		// TODO Auto-generated method stub
	}

	
	public void arcDeleted() {
		// TODO Auto-generated method stub
	}

	
	public void graphChanged() {
		// TODO Auto-generated method stub
	}
}
