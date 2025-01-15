package edu.ohiou.mfgresearch.labimp.spacesearch.tsp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import edu.ohiou.mfgresearch.labimp.spacesearch.ComparableSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.HeuristicFunction;

public class MinRowHeuristic implements HeuristicFunction {
	  TravelingSalesman state;
	  public MinRowHeuristic (ComparableSpaceState state) {
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
	    for (Iterator rowItr = rows.iterator(); rowItr.hasNext();) {
	      int i = state.tableModel.findRow(rowItr.next());
	      TreeSet<Double> valueSet = new TreeSet<Double>();
	      for (Iterator colItr = cols.iterator(); colItr.hasNext();) {
	         int j = state.tableModel.findColumn(colItr.next());
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
