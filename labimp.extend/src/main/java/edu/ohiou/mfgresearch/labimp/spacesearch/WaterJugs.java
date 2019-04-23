package edu.ohiou.mfgresearch.labimp.spacesearch;


import java.util.*;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.tree.*;

/**
 *
 *  Title:
 *  Description:
 *  Copyright:    Copyright (c) 2001
 *  Company:
 *  @author
 *  @version 1.0
 *
 */

public class WaterJugs extends DefaultSpaceState {

	static int bigCapacity = 8;
	static int smallCapacity = 6;
	int bigState;
	int smallState;

	static public void setCapacity(int bigC, int smallC) {
		bigCapacity = bigC;
		smallCapacity = smallC;
	}

	static final int FILL_BIG = 1;
	static final int FILL_SMALL = 2;
	static final int EMPTY_BIG = 3;
	static final int EMPTY_SMALL = 4;
	static final int FROM_BIG_TO_SMALL = 5;
	static final int FROM_SMALL_TO_BIG = 6;

	public WaterJugs(int bigS, int smallS) {
		bigState = bigS;
		smallState = smallS;
		node = new DefaultMutableTreeNode(this);
	}

	public WaterJugs(WaterJugs old, int action) {
		int total = old.bigState + old.smallState;
		parent = old;
		switch (action) {
			case FILL_BIG :
				bigState = bigCapacity;
				smallState = old.smallState;
				break;
			case FILL_SMALL :
				smallState = smallCapacity;
				bigState = old.bigState;
				break;
			case EMPTY_BIG :
				bigState = 0;
				smallState = old.smallState;
				break;
			case EMPTY_SMALL :
				smallState = 0;
				bigState = old.bigState;
				break;
			case FROM_BIG_TO_SMALL :
				if (total > smallCapacity) {
					smallState = smallCapacity;
					bigState = total - smallCapacity;
				} else {
					smallState = total;
					bigState = 0;
				}
				break;
			case FROM_SMALL_TO_BIG :
				if (total > bigCapacity) {
					bigState = bigCapacity;
					smallState = total - bigCapacity;
				} else {
					bigState = total;
					smallState = 0;
				}
				break;
			default :
				bigState = old.bigState;
				smallState = old.smallState;
		}
    node = new DefaultMutableTreeNode(this);
	}

	public static void main(String[] args) {
		WaterJugs wj = new WaterJugs(0, 0);
    BlindSearcher searcher = new BlindSearcher(wj, new WaterJugs(4, 4));
//  Searchable g = f.runSpaceSearch(nf);
// System.out.println(g.printPath());
  searcher.setApplet();
  searcher.display("Farmer searcher");
//		Searchable g = wj.runSpaceSearch(new WaterJugs(5, 0));
//		System.out.println(g.printPath());
//		wj.display("title", new Dimension(200, 300), JFrame.EXIT_ON_CLOSE);
	}

	public void init() {
		panel = new JPanel();
	}

	public Dimension geetAppletSize() {
		return new Dimension(400, 400);
	}

	public Set<Searchable> makeNewStates() {
		Set<Searchable> states = new HashSet<Searchable>();

    if (smallState < smallCapacity)
      states.add(new WaterJugs(this, FILL_SMALL));
    if (bigState < bigCapacity)
      states.add(new WaterJugs(this, FILL_BIG));
    if (bigState >0)
      states.add(new WaterJugs(this, EMPTY_BIG));
    if (smallState >0)
      states.add(new WaterJugs(this, EMPTY_SMALL));
    if (bigState >0)
      states.add(new WaterJugs(this, FROM_BIG_TO_SMALL));
    if (smallState >0)
		states.add(new WaterJugs(this, FROM_SMALL_TO_BIG));
		return states;
	}

	public int[] setSearchTypes() {
		System.out.println("setting search types");
		int[] types = { SpaceSearcher.BREADTH_FIRST, SpaceSearcher.DEPTH_FIRST };
		return types;
	}

	public DefaultMutableTreeNode createNodes(Searchable s) {
		return null;//SpaceSearcher.createNodes (this);
	}
  
  public boolean equals(Object o) {
    return equals ((Searchable) o);
  }

	public boolean equals(Searchable s) {
		return (bigState == ((WaterJugs) s).bigState)
			&& (smallState == ((WaterJugs) s).smallState);
	}
  
  public int hashCode () {
    return smallState + 100* bigState;
  }

	public String toString() {
		return "[" + index +"],Big Jug: " + 
      bigState + ", Small Jug: " + smallState +"[" + hashCode() + "]";
	}
}
