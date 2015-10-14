package edu.ohiou.mfgresearch.labimp.graphmodel;

import edu.ohiou.mfgresearch.labimp.graphmodel.gui.GraphRenderer;
import edu.ohiou.mfgresearch.labimp.table.*;

public class DirectedGraphTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Integer i1 = 10;
		Integer i2 = 20;
		Integer i3 = 30;
		Integer i4 = 40;
		Integer i6 = 60;
		
		Integer[] ints = { i1, i2, i3, i4, i6 };
		
		
		GraphModel model1 = new DefaultGraphModel(ints, new IntegerDivisionCellGenerator(),
				false, false);
//		((SquareTableModel)model1.getTableModel()).display();
		System.out.println(model1.toString());
		GraphRenderer test = new GraphRenderer(model1, true);		
		test.display();

	}
}
	
	
	class IntegerDivisionCellGenerator extends DefaultGraphTableCellGenerator {

		public Object makeTableCell(Object o1, Object o2) {
			if (o1 instanceof Integer && o2 instanceof Integer) {
				Integer i1 = (Integer) o1;
				Integer i2 = (Integer) o2;
//				System.out.print(" i1: "+i1);
//				System.out.println(" i2: "+i2);
				if (i2.intValue()%i1.intValue() == 0)
				{
					if(i1.intValue()%i2.intValue() == 0)
					{
//						System.out.println("BiDirected: i2: "+i1.toString()+" i1: "+i2.toString());
						return new Integer(2);
					}
//					System.out.println("Directed: i1: "+i1.toString()+" i2: "+i2.toString());
					return new Integer(1);
				}
				else
				{
//					System.out.println("No Relation: i1: "+i1.toString()+" i2: "+i2.toString());
					return new Integer(0);
				}									
			}
			return null;
		}
	}
		

