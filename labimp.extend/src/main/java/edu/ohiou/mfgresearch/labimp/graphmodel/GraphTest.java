/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.graphmodel;

import edu.ohiou.mfgresearch.labimp.basis.Draw2DApplet;
import edu.ohiou.mfgresearch.labimp.table.TableCellGenerator;
import edu.ohiou.mfgresearch.labimp.table.ModelTable;
import edu.ohiou.mfgresearch.labimp.graphmodel.gui.*;

/**
 * @author Ganduri
 *
 */
public class GraphTest {
	public static void main(String[] args) {
		String s1 = "jovan";
		String s2 = "j";
		String s3 = "dusan";
		String s4 = "dus";
		String s5 = "olga";
		String s6 = "olga sormaz";
		String s7 = "jova";
		String s8 = "jo";
		String s9 = "dusa";
		String s10 = "du";
		String s11 = "olg";
		String s12 = "olga dusan sormaz";
		String[] strings = { s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12};
		String [] st2 = {s3, s9, s12};
		
		GraphModel model1 = new DefaultGraphModel(strings, new SubstringCellGenerator(),
				false, false);
		
		System.out.println(model1.toString());
		GraphRenderer test = new GraphRenderer(model1, true);		
		test.display();
				
	}
}

class SubstringCellGenerator implements TableCellGenerator {

	/* (non-Javadoc)
	 * @see edu.ohiou.mfgresearch.labimp.table.TableCellGenerator#makeTableCell(java.lang.Object, java.lang.Object)
	 */
	public Object makeTableCell(Object o1, Object o2) {
		if (o1 instanceof String && o2 instanceof String) {
			String s1 = (String) o1;
			String s2 = (String) o2;
			if (s1.equalsIgnoreCase(s2))
				return new Boolean(false);
			return new Boolean(s1.startsWith(s2));
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see edu.ohiou.mfgresearch.labimp.table.TableCellGenerator#updateRelation(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public void updateRelation(Object arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}
	
}

