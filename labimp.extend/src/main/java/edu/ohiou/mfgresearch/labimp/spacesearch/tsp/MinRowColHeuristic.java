package edu.ohiou.mfgresearch.labimp.spacesearch.tsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

import edu.ohiou.mfgresearch.labimp.spacesearch.ComparableSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.HeuristicFunction;

public class MinRowColHeuristic implements HeuristicFunction {
	  TravelingSalesman state;
	  public MinRowColHeuristic  (ComparableSpaceState state) {
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
	  ArrayList resultArray= new ArrayList();  
	    for (Iterator rowItr = rows.iterator(); rowItr.hasNext();) {
	        int i = state.tableModel.findRow(rowItr.next());
	        TreeSet<Double> valueSet = new TreeSet<Double>();
	        for (Iterator colItr = cols.iterator(); colItr.hasNext();) {
	           int j = state.tableModel.findColumn(colItr.next());
//	          String val = (String)state.tableModel.getValueAt(i,j);
	           if (i != j) {
	          Double val = (Double)state.tableModel.getValueAt(i,j+1);
	          valueSet.add(val);
	           }
	        }
	        resultArray.add(valueSet.first());
	     }
	    for (Iterator colsItr =cols.iterator(); colsItr.hasNext();) {
		      int j = state.tableModel.findColumn(colsItr.next());
		      TreeSet<Double> valueSet = new TreeSet<Double>();
		      for (Iterator rowsItr = rows.iterator(); rowsItr.hasNext();) {
		         int i = state.tableModel.findRow(rowsItr.next());
//		        String val = (String)state.tableModel.getValueAt(i,j);
		         if (i != j) {
		        Double val = (Double)state.tableModel.getValueAt(i,j+1);
		        valueSet.add(val);
		         }
		      }
		      resultArray.add(valueSet.first());
		   } 
	     Double [] arr = new Double [resultArray.size()];
	     resultArray.toArray(arr);
	     Arrays.sort(arr);
	     double result = 0.0;
	     for (int i = resultArray.size() -1; i > rows.size()-1; i--) 
	         result += arr[i];
	      return result;
	    
	  }
	}

