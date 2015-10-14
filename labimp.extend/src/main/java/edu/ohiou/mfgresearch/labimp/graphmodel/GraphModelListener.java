/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.graphmodel;

import java.util.EventListener;

/**
 * This interface specifies the APIs for a listener to the graph model
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Ohio University</p>
 * @author Chandrasekhar Ganduri
 * @version 1.0
 */
public interface GraphModelListener extends EventListener {
	
	public void nodeAdded();
	
	public void nodeDeleted();
	
	public void arcAdded();
	
	public void arcDeleted();
	
	public void graphChanged();

}
