/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.graphmodel.gui;

import java.awt.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;


import edu.ohiou.mfgresearch.labimp.graphmodel.DefaultGraphTableCellGenerator;

/**
 * @author Ganduri
 *
 */
public class DefaultGraphTableCellRenderer extends DefaultTableCellRenderer {
	
	int precision = 0;
	
	
	public DefaultGraphTableCellRenderer()
	{
		super();
	}
	
	
	
	public Component getTableCellRendererComponent(JTable table ,
	        Object value ,
	        boolean isSelected ,
	        boolean hasFocus ,
	        int row ,
	        int column)
	    {

	        Component toReturn = super.getTableCellRendererComponent(table , value ,
	            isSelected , hasFocus , row , column);
	        
	       // Setting row headers to background
	        if(column == 0)
	        {
	        	((JLabel) toReturn).setBackground(table.getTableHeader().getBackground());
	        	((JLabel) toReturn).setHorizontalAlignment(JLabel.CENTER);
	        	super.setValue(value);	            
	            return toReturn;
	        }
	        
	        // Selection action
	        if (!isSelected) {
	            toReturn.setBackground(Color.white);
	        }
	        
	        // Boolean
	        if(value instanceof Boolean)
	        {
	        	if(((Boolean)value).booleanValue() == true)
	        	{
	        		((JLabel) toReturn).setHorizontalAlignment(JLabel.CENTER);	        		
	        		return toReturn;
	        	}
	        	else
	        	{
	        		 super.setValue(null);
	        		 return toReturn;
	        	}
	        }    
	        
	        // Integer
	        if(value instanceof Integer)
	        {
	        	((JLabel) toReturn).setHorizontalAlignment(JLabel.RIGHT);
	        	((JLabel) toReturn).setText(value.toString());
	            if(((Integer)value).intValue() == 1)
	        	{
	        		super.setValue(((Integer)value).toString()); 		
	        		return toReturn;
	        	}
	        	else if(((Integer)value).intValue() == 2)
	        	{
	        		super.setValue(((Integer)value).toString()); 		
	        		return toReturn;
	        	}
	        	else if(((Integer)value).intValue() == 0)
	        	{
	        		super.setValue(null);
	        		return toReturn;
	        	}
	        }
	        else
	        {
	        	
	        	return toReturn;	        	
	        }
	        
	        return toReturn;
	        
	    }
	
}
