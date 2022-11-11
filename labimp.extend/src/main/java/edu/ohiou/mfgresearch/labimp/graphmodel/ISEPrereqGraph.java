package edu.ohiou.mfgresearch.labimp.graphmodel;

import java.io.*;
import java.util.*;

import edu.ohiou.mfgresearch.labimp.basis.ViewObject;
import edu.ohiou.mfgresearch.labimp.graphmodel.gui.GraphRenderer;
import edu.ohiou.mfgresearch.labimp.table.TableCellGenerator;

public class ISEPrereqGraph extends ViewObject {
	
	static Map<String,Collection<String>> prereqMap = new HashMap<String,Collection<String>>();
	
	static {
//		prereqMap.put("ise1100", new ArrayList<String> ());
//		Collection c = prereqMap.get("ise1100");
//		c.add("ise4140");
//		c.add("ise4120");
//		prereqMap.put("ise4140", new ArrayList<String> ());
//		prereqMap.put("ise4120", new ArrayList<String> ());
		
		String filename = "";
		  filename = getProperties().getProperty("ISE_FILE", "");
		  List<String> records = new ArrayList<String>();
		  try
		  {
			  
		    BufferedReader reader = new BufferedReader(new FileReader(filename));
		    String line;
		    while ((line = reader.readLine()) != null)
		    {
		      String [] lineSplit = line.split("=");
		      String course = lineSplit[0];
		      String [] depCourses = lineSplit[1].split(",");
		      if (prereqMap.get(course) == null) {
		    	  prereqMap.put(course, new ArrayList<String> ());
		      }
		      Collection coll = prereqMap.get(course);
		      coll.addAll(Arrays.asList(depCourses));
		      for (String c : depCourses)
		      {
			      if (prereqMap.get(c) == null) {
			    	  prereqMap.put(c, new ArrayList<String> ());
			      }
		      }
		    }
		    reader.close();
		  
		  }
		  catch (Exception e)
		  {
		    System.err.format("Exception occurred trying to read '%s'.", filename);
		    e.printStackTrace();
		   
		  }
		}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Object[] courses = prereqMap.keySet().toArray();
		
		GraphModel model1 = new DefaultGraphModel(courses, new PrereqGenerator(prereqMap),
				false, false);
		
		System.out.println(model1.toString());
		GraphRenderer test = new GraphRenderer(model1, true);		
		test.display();
	}

}

class PrereqGenerator implements TableCellGenerator {
	
	 Map<String,Collection> prereqMap;
	 
	 PrereqGenerator (Map m) {
		 prereqMap = m;
	 }
	 

	/* (non-Javadoc)
	 * @see edu.ohiou.mfgresearch.labimp.table.TableCellGenerator#makeTableCell(java.lang.Object, java.lang.Object)
	 */
	public Object makeTableCell(Object o1, Object o2) {
		if (o1 instanceof String && o2 instanceof String) {
			String s1 = (String) o1;
			String s2 = (String) o2;
			return prereqMap.get(s1).contains(s2);
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
