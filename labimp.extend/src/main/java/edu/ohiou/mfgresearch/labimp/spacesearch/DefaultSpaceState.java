package edu.ohiou.mfgresearch.labimp.spacesearch;

/**
 * Title:        Generic classes for manufacturing planning
 * Description:  Thsi project implements general classes for intelligent manufacturing planning. These are:
 * ImpObject - umbrella class fro all objects
 * MrgPartModel - general part object data
 * Viewable - interface to display objects in applet
 * GUIApplet - applet that utilizes viewable interface
 * Copyright:    Copyright (c) 2001
 * Company:      Ohio University
 * @author Dusan Sormaz
 * @version 1.0
 */

//import edu.ohiou.implanner.impClasses.*;
import java.util.Collection;
import java.util.Comparator;

import edu.ohiou.mfgresearch.labimp.basis.Viewable;
import edu.ohiou.mfgresearch.labimp.draw.ImpObject;
import edu.ohiou.mfgresearch.labimp.spacesearch.Searchable;
import edu.ohiou.mfgresearch.labimp.spacesearch.TravelingSalesman.TSComparator;
import edu.ohiou.mfgresearch.labimp.draw.*;

import java.util.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 *  Class DefaultSpaceState provides implementation of common behavior for Space
 *  states. It can not be instantiated, so application developers will need to
 *  extend it and implement extension specific method, here declared abstract
 * 
 */

abstract public class DefaultSpaceState extends ImpObject implements Searchable {
  
  protected static int current =0;
  protected static boolean printIndex = false;
  public static String problemName = new String();

  static {
	  printIndex = properties.
	  getProperty("edu.ohiou.mfgresearch.labimp.spacesearch.DefaultSpaceState.printIndex", "No").
	  equalsIgnoreCase("Yes");
  }
  
  public int index;
  
  public DefaultSpaceState () {
    current++;
    index = current;
    settColor(SpaceSearcher.OPEN_COLOR);
  }

	public Searchable parent;
	public DefaultMutableTreeNode node;

	/**
	 *
	 *    This method returns member variable parent
	 *   
	 *    @return - parent member variable
	 *   
	 */

	public Searchable getParent() {
		return parent;
	}
	/**
	 *
	 *    This method returns the default tree node for the current state
	 *   
	 */

	public DefaultMutableTreeNode getNode() {
		return node;// = new DefaultMutableTreeNode(this);
	}

	public DefaultMutableTreeNode getTree() {
		return null;
	}
  
  public int getIndex () {
    return index;
  }

	/**
	 *
	 *    this method runs Space search to this state as goal
	 *   
	 *    @return - returns the final state in space search
	 *   
	 */

	public Searchable runSpaceSearch() {
		return runSpaceSearch(this);
	}

	/**
	 * this method runs space search
	 * It constructs new spacesearcher and calls its spacesearch method with
	 * this as initial state and goal as goal state
	 *
	 * @param - goal Space state that is the goal of space search
	 * @param - order parameter that specifies which search order to apply, breadth, depth or best first
	 * @return - return goal state with chain of paths from start to goal
	 */
	public Searchable runSpaceSearch(Searchable goal, int order) {
		SpaceSearcher searcher = new BlindSearcher(this, goal, order);
		return searcher.runSpaceSearch();
	}
	/**
	 * this method runs space search
	 * It constructs new spacesearcher and calls its spacesearch method with
	 * this as initial state and goal as goal state
	 *
	 * @param - goal Space state that is the goal of space search
	 * @return - return goal state with chain of paths from start to goal
	 */
	public Searchable runSpaceSearch(Searchable goal) {
		SpaceSearcher searcher = new BlindSearcher(
			this,
			goal,
			SpaceSearcher.DEPTH_FIRST);
		return searcher.runSpaceSearch();
	}

	/**
	 * This method verifies if this state is a member of supplied list
	 * Method uses Searchable implementation of equals that performs
	 * comparison based on equality o fstates as impelmented in any searchable
	 * class, not on defual Object equals() method
	 * The method verifies that each memeber of linked list impelements searhcable
	 * interface
	 *
	 * @param - l LInked list of states fro comparison
	 * @return - boolean value that reflects the fact that this is memberr of list
	 */
	public boolean memberInList(Collection l) {
		for (Iterator itr = l.iterator(); itr.hasNext();)
			if (this.equals((Searchable) itr.next()))
				return true;
		return false;
	}

	abstract public int[] setSearchTypes();

	public String printPath() {
		if (parent == null)
			return toString();
		return parent.printPath() + "\n" + toString();
	}
  
  public String toString () {
    return printIndex ? "[" + index + "]:" : "";
  }
  
  public String getProblemName () {
	  return problemName;
  }

	public double evaluate() {
		return 0.0;
	}

	public boolean canBeGoal() {
		return true;
	}
	public boolean isBetterThan(Searchable inState) {
		return false;
	}

	public Searchable getClone() {
		return null;
	}
  
  public Comparator getComparator() {
    return null;
  }
  
  public boolean isSearchComplete(SpaceSearcher s) {
//  if ( s.getOpen().isEmpty()) {
//    s.setHasReachedGoal(false);
//    return true;
////    return null;
//  } 
//  s.setCurrentState(s.getOpen().first());
//  s.getOpen().remove(s.getCurrentState());
//  ((Viewable)s.getCurrentState()).settColor (SpaceSearcher.CURRENT_COLOR);

  // decide goal for farmer
  if (s.getCurrentState().equals(s.goalState) ) {
    s.setHasReachedGoal(true);
    s.goalState = s.getCurrentState();
    ((Viewable)s.goalState).settColor (SpaceSearcher.GOAL_COLOR);
    return true;
//    return goalState;
  }
  return false;
  }
  
  public double distFromStart() {
    throw new UnsupportedOperationException("Method 'distFromStart' is " +
        "not supported by DefaultSpaceState");
  }
  public double distToGoal() {
    throw new UnsupportedOperationException("Method 'distToGoal' is " +
        "not supported by DefaultSpaceState");
  }



} //EndOfClass DefaultSpaceState