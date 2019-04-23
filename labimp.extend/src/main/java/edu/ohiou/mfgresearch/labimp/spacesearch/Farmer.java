package edu.ohiou.mfgresearch.labimp.spacesearch;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
//import com.borland.jbcl.layout.*;

public class Farmer extends DefaultSpaceState {
	boolean farmerLocation;
	boolean wolfLocation;
	boolean goatLocation;
	boolean cabbageLocation;
	JPanel jPanel1 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();

	public Farmer(boolean f, boolean w, boolean g, boolean c, Farmer p) {
		farmerLocation = f;
		wolfLocation = w;
		goatLocation = g;
		cabbageLocation = c;
		parent = p;
		node = new DefaultMutableTreeNode(this);
	}

	public void init() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// creates the main panel and initializes it if necessary :SM
	public void jbInit() throws Exception {
		this.panel = new JPanel();
		//this.panel.add(new JLabel(this.toString()));
	}
	

	public int[] setSearchTypes() {
		int[] types = { SpaceSearcher.BREADTH_FIRST, SpaceSearcher.DEPTH_FIRST };
		return types;
	}

	public Dimension getAppletSize() {
		return new Dimension(400, 400);
	}

	public DefaultMutableTreeNode createNodes(Searchable s) {
		return null;//SpaceSearcher.createNodes (this);
	}

	public Comparator getComparator() {
		return null;
	}
	/*

	 public Searchable runSpaceSearch (Searchable goal)
	 {
	 SpaceSearcher searcher = new SpaceSearcher(this,goal);
	 searcher.ssPanel.problemSelected = true;
	 searcher.ssPanel.initialiseTree();
	 searcher.ssPanel.menuItemMC.setEnabled(false);
	 searcher.ssPanel.menuItemTS.setEnabled(false);
	 searcher.ssPanel.menuItemGT.setEnabled(false);
	 searcher.setApplet();
	 searcher.display("searcher");
	 return null;
	 }

	 */
	
	// the main problem-dependent method, creates children of each state :SM
	public Set<Searchable> makeNewStates() {
		HashSet<Searchable> states = new HashSet<Searchable>();
		Farmer moveFarmer = new Farmer(
			!farmerLocation,
			wolfLocation,
			goatLocation,
			cabbageLocation,
			this);

		Farmer moveFarmerWolf = new Farmer(
			!farmerLocation,
			!wolfLocation,
			goatLocation,
			cabbageLocation,
			this);

		Farmer moveFarmerGoat = new Farmer(
			!farmerLocation,
			wolfLocation,
			!goatLocation,
			cabbageLocation,
			this);

		Farmer moveFarmerCabbage = new Farmer(
			!farmerLocation,
			wolfLocation,
			goatLocation,
			!cabbageLocation,
			this);

		if (moveFarmer.safe())
			states.add(moveFarmer);
		if (moveFarmerWolf.safe())
			states.add(moveFarmerWolf);
		if (moveFarmerGoat.safe())
			states.add(moveFarmerGoat);
		if (moveFarmerCabbage.safe())
			states.add(moveFarmerCabbage);
		return states;
	}

	public boolean equals(Searchable s) {
		Farmer f = (Farmer) s;
		return farmerLocation == f.farmerLocation
			&& wolfLocation == f.wolfLocation
			&& goatLocation == f.goatLocation
			&& cabbageLocation == f.cabbageLocation;
	}
  
	public int hashCode() {
		return (new Boolean(farmerLocation).hashCode()-1230) * 1 +
				(new Boolean(wolfLocation).hashCode()-1230)* 10 +
				(new Boolean(goatLocation).hashCode()-1230) * 100 +
				(new Boolean(cabbageLocation).hashCode()-1230) * 1000;
		}

	private boolean safe() {
		boolean wolfEatGoat = (wolfLocation == goatLocation)
			&& (farmerLocation != wolfLocation);
		boolean goatEatCabbage = (goatLocation == cabbageLocation)
			&& (farmerLocation != goatLocation);

		return !wolfEatGoat && !goatEatCabbage && true;
	}

	public String toString() {
		StringBuffer sbuffer = new StringBuffer(10);
		sbuffer.append(super.toString());
		sbuffer.append("[Farmer ").append((farmerLocation ? "left" : "right"))
			.append(", Wolf ").append((wolfLocation ? "left" : "right"))
			.append(", Goat ").append((goatLocation ? "left" : "right"))
			.append(", Cabbage ")
			.append((cabbageLocation ? "left]" : "right]"))
			.append("hc" + hashCode());
		return sbuffer.toString();
	}

	public static void main(String[] args) {
		Farmer f = new Farmer(true, true, true, true, null);
		Farmer nf = new Farmer(false, false, false, false, null);
		BlindSearcher searcher = new BlindSearcher(f, nf);
//     Searchable g = f.runSpaceSearch(nf);
//    System.out.println(g.printPath());
		searcher.setApplet();
		searcher.display("Farmer searcher");
	}
}
