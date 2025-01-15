package edu.ohiou.mfgresearch.labimp.spacesearch;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.StringTokenizer;

import edu.ohiou.mfgresearch.labimp.basis.ViewObject;
import edu.ohiou.mfgresearch.labimp.basis.Viewable;

public abstract class ComparableSpaceState extends DefaultSpaceState implements
Comparable {

	public int compareTo (Object other) {
		return getComparator().compare(this, other);
	}

	public boolean isSearchComplete (SpaceSearcher s) {
		//  if (s.getOpen().isEmpty()) {
		//    s.setHasReachedGoal((s.goalState != null));
		//    return true;
		////    return s.goalState;
		//  }
		//
		//   s.setCurrentState(s.getOpen().first());
		//   s.getOpen().remove(s.getCurrentState());
		//   ((Viewable)s.getCurrentState()).settColor (SpaceSearcher.CURRENT_COLOR);
		if (s.getCurrentState().canBeGoal()) { 
			if (s.goalState == null || s.getCurrentState().isBetterThan(s.goalState)) {
				if (s.goalState != null) {
					((Viewable)s.goalState).settColor (SpaceSearcher.CLOSED_COLOR);
				}
				s.goalState = s.getCurrentState();      
				((Viewable)s.getCurrentState()).settColor (SpaceSearcher.GOAL_COLOR);
				
			}
			boolean goalBetter = s.goalState.isBetterThan(s.getOpen().first());
			System.out.println("DNS 2025: Goal better then first in Open? " + goalBetter);
			if (s.getOpen().isEmpty() || (s instanceof InformedSearcher)) {
//				if (s instanceof InformedSearcher) { // this condition gives  hill climbing
				s.setHasReachedGoal(true);
				return true;
			}
		}
		return false;
	}

	public HeuristicFunction getHeuristic () 
			throws HeuristicException {
		String hName = ViewObject.properties.getProperty(
				this.getClass().getName() +".heuristic", "");
		HeuristicFunction heuristic = null;
		Class[] argTypes = {ComparableSpaceState.class };
		Object[] args = { this };
		if (hName != null ) {
			try {
				Class hClass = Class.forName(hName);
				Constructor c = hClass.getConstructor(argTypes);
				heuristic = (HeuristicFunction) c.newInstance(args);
			} catch (Exception e) {
				throw new HeuristicException("Heuristic unavailable", e);
			}
		}
		return heuristic;

	}

	public HeuristicFunction getHeuristic (String name) 
			throws HeuristicException {
		//String hName = this.getClass().getName() +"." + name;
		String hName ="";
		StringTokenizer tokenizer = new StringTokenizer (this.getClass().getName(),".");
		while (tokenizer.countTokens()!=1) {
			hName=hName+tokenizer.nextToken()+".";
		}
		hName=hName+name;
		HeuristicFunction heuristic = null;
		Class[] argTypes = {ComparableSpaceState.class };
		Object[] args = { this };
		if (hName != null && !hName.equalsIgnoreCase("")) {
			try {
				Class hClass = Class.forName(hName);
				Constructor c = hClass.getConstructor(argTypes);
				heuristic = (HeuristicFunction) c.newInstance(args);
			} catch (Exception e) {
				throw new HeuristicException("Heuristic unavailable", e);
			}
		}
		return heuristic;
	}

}
