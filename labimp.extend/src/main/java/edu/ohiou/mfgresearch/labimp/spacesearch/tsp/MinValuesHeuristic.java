package edu.ohiou.mfgresearch.labimp.spacesearch.tsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import edu.ohiou.mfgresearch.labimp.spacesearch.ComparableSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.HeuristicFunction;

public class MinValuesHeuristic implements HeuristicFunction {
	  TravelingSalesman state;
	  public MinValuesHeuristic (ComparableSpaceState state) {
	    this.state = (TravelingSalesman)state; 
	  }
	  public double evaluate () {
	    if (state.unvisitedCities.isEmpty()) {
	      return 0;
	    }
	    ArrayList rows = new ArrayList (state.unvisitedCities);
	    ArrayList cols = new ArrayList (state.unvisitedCities);
	    if (state.visitedPath.size() > 0) {
	      rows.add(state.visitedPath.getFirst());
	      cols.add(state.visitedPath.getLast());
	      
	    }
	    ArrayList<Double> values = new ArrayList<Double>();
	    for (Iterator rowItr = rows.iterator(); rowItr.hasNext();) {
	      int i = state.tableModel.findRow(rowItr.next());
	      for (Iterator colItr = cols.iterator(); colItr.hasNext();) {
	         int j = state.tableModel.findColumn(colItr.next());
//	        String val = (String)state.tableModel.getValueAt(i,j);
	         if (i != j) {
	        Double val = (Double)state.tableModel.getValueAt(i,j+1);
	        values.add(val);
	         }
	      }
	    }
	     Double [] arr = new Double [values.size()];
	    values.toArray(arr);
	   Arrays.sort(arr);
	   double result = 0.0;
	   for (int i = 0; i < rows.size(); i++) 
	     result += arr[i];
	    return result;
	    
	  }
	}
