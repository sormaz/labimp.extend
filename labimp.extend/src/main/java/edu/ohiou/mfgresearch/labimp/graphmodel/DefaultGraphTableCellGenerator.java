/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.graphmodel;

import edu.ohiou.mfgresearch.labimp.table.TableCellGenerator;

/**
 * @author Ganduri
 *
 */
public class DefaultGraphTableCellGenerator implements TableCellGenerator {

	/* (non-Javadoc)
	 * @see edu.ohiou.mfgresearch.labimp.table.TableCellGenerator#makeTableCell(java.lang.Object, java.lang.Object)
	 */
	public static final Integer NO_RELATION = new Integer(0);
	public static final Integer DIRECTED_RELATION = new Integer(1);
	public static final Integer BIDIRECTED_RELATION = new Integer(2);
	
	public Object makeTableCell(Object o1, Object o2) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ohiou.mfgresearch.labimp.table.TableCellGenerator#updateRelation(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public void updateRelation(Object o1, Object o2, Object dataValue) {
		// TODO Auto-generated method stub

	}

}
