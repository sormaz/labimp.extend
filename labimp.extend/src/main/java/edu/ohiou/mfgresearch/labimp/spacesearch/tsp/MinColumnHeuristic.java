package edu.ohiou.mfgresearch.labimp.spacesearch.tsp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import edu.ohiou.mfgresearch.labimp.spacesearch.ComparableSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.HeuristicFunction;

public class MinColumnHeuristic implements HeuristicFunction {
	  TravelingSalesman state;
	  public MinColumnHeuristic (ComparableSpaceState state) {
	    this.state = (TravelingSalesman)state; 
	  }
	  public double evaluate () {
	    double result = 0.0;
	    if (state.unvisitedCities.isEmpty()) {
	      return result;
	    }
	    ArrayList rows = new ArrayList (state.unvisitedCities);
	    ArrayList cols = new ArrayList (state.unvisitedCities);
	    if (state.visitedPath.size() > 0) {
	      rows.add(state.visitedPath.getFirst());
	      cols.add(state.visitedPath.getLast());
	      
	    }
//	    ArrayList<Double> values = new ArrayList<Double>();
	    for (Iterator colsItr =cols.iterator(); colsItr.hasNext();) {
	      int j = state.tableModel.findColumn(colsItr.next());
	      TreeSet<Double> valueSet = new TreeSet<Double>();
	      for (Iterator rowsItr = rows.iterator(); rowsItr.hasNext();) {
	         int i = state.tableModel.findRow(rowsItr.next());
//	        String val = (String)state.tableModel.getValueAt(i,j);
	         if (i != j) {
	        Double val = (Double)state.tableModel.getValueAt(i,j+1);
	        valueSet.add(val);
	         }
	      }
	      result += valueSet.first();
	   }
	    return result;
	    
	  }
	}
