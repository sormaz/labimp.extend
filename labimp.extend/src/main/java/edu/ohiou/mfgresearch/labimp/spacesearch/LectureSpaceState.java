/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.spacesearch;

import java.util.*;
import java.awt.*;
import javax.swing.*;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Ganduri
 *
 */
public class LectureSpaceState extends DefaultSpaceState {
	String name;
	
	public static HashMap stateMap = new HashMap();
	
	static {
		String bcd [] = {"B","C","D"};
		stateMap.put("A", bcd);
		String ef [] = {"E","F","D"};
		stateMap.put("B", ef);
		
	}
	

	/**
	 * 
	 */
	public LectureSpaceState(String name) {
		super();
		this.name = name;
		node = new DefaultMutableTreeNode(this);
	}
	
	public String toString () {
		return "State [" + name + "]";
	}

	/* (non-Javadoc)
	 * @see edu.ohiou.mfgresearch.labimp.spacesearch.DefaultSpaceState#setSearchTypes()
	 */
	public int[] setSearchTypes() {
		int [] array = { SpaceSearcher.DEPTH_FIRST,
						SpaceSearcher.BREADTH_FIRST};
		return array;
	}

	/* (non-Javadoc)
	 * @see edu.ohiou.mfgresearch.labimp.spacesearch.Searchable#makeNewStates()
	 */
	public Set<Searchable> makeNewStates() {
		Set<Searchable> states = new HashSet<Searchable> ();
		states.add(new LectureSpaceState("B"));
		// TODO Auto-generated method stub
		return states;
	}

	/* (non-Javadoc)
	 * @see edu.ohiou.mfgresearch.labimp.spacesearch.Searchable#equals(edu.ohiou.mfgresearch.labimp.spacesearch.Searchable)
	 */
	public boolean equals(Searchable s) {
		return name.equalsIgnoreCase(((LectureSpaceState) s).name);
	}
	
	// viewable interface
	
	public void init () {
		panel = new JPanel();
		JLabel nameLabel = new JLabel(name);
		Font bigFont = new Font ("arial", Font.BOLD, 44);
		
		nameLabel.setFont(bigFont);
		panel.add(nameLabel);
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LectureSpaceState inState = new LectureSpaceState ("A");
		LectureSpaceState goal = new LectureSpaceState ("B");
		SpaceSearcher searcher = new BlindSearcher (inState, goal);
//		Searchable g = searcher.runSpaceSearch();
		searcher.display();
//System.out.println ("Result:" + g.toString());
	}

}
