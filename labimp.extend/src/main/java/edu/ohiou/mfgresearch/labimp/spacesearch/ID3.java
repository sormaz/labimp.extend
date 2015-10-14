package edu.ohiou.mfgresearch.labimp.spacesearch;
/**
 * Title: 		The ID3 algorithm
 * Description: Implementation using the space search structure.
 * 				Each ID3 object is a state. All states share 4 static
 * 				variables for storing the data. allExamples, allFeatures
 * 				featureLabels and N. Each state has a fixed array of indices
 * 				of examples that are relevant, and a fixed array of 
 * 				indices of features that are relevant. To create children
 * 				of a state, minimum label entropy is calculated considering
 * 				all the examples and features relevant to current state.
 * Note: 		- Hardcoded example is from Tom Mitchell's ML book.
 * 				- The algorithm expands the whole tree regardless of whether
 * 				  a feature can describe the label fully or not. This way a 
 * 				  better sense of the tree can be achieved.
 * Copyright: 	Summer 2015
 * Company:		
 * @author		Sadegh Mirshekarian
 * @version 	1.0
 */

import java.awt.Dimension;
import java.util.*;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;


public class ID3 extends DefaultSpaceState {
	private static final int[][] allExamples;		// list of all training examples, label+feature values
	private static final String[][] allFeatures;	// list of all feature values, first one is always the label
	private static final String[] featureLabels;	// list of all feature labels/names
	private static final int N;						// number of training examples = allExamples.length
	private int[] examples;							// list of example indices relevant to this state
	private int[] features;							// list of feature indices relevant to this state
	private String headLabel;						// head of the ID3 tree node, usually a feature name
	
	public ID3(int[] ex, int[] ft, String hl, ID3 p) {
		examples 	= ex;
		features 	= ft;
		headLabel 	= hl;
		parent 		= p;
		node 		= new DefaultMutableTreeNode(this);
	}
	
	//// calculates entropy of the labels indicated in 'subLabels'
	//// assumes at least one true in 'subLabels'
	private double labelEntropy(boolean[] subLabels) {
		double entropy = 0.0;
		int subN = 0;
		int[] count = new int[allFeatures[0].length];
		Arrays.fill(count, 0);
		for (int i = 0; i < N; i++) {
			if (subLabels[i]) {
				subN += 1;
				count[allExamples[i][0]] += 1;
			}
		}
		for (int c : count) {
			if (c != 0) 
				entropy -= (((double) c)/subN) * Math.log(((double) c)/subN);
		}		
		
		return entropy;
	}
	
	//// initialize training data, hardcoded here but can be read from a file
	static {
		allExamples = new int[][] {		// don't forget allExamples[...][0] is the label
			{1, 0, 0, 0, 1}, 
			{1, 0, 0, 0, 0},
			{0, 1, 0, 0, 1},
			{0, 2, 1, 0, 1},
			{0, 2, 2, 1, 1},
			{1, 2, 2, 1, 0},
			{0, 1, 2, 1, 0},
			{1, 0, 1, 0, 1},
			{0, 0, 2, 1, 1},
			{0, 2, 1, 1, 1},
			{0, 0, 1, 1, 0},
			{0, 1, 1, 0, 0},
			{0, 1, 0, 1, 1},
			{1, 2, 1, 0, 0},
			};
		N = allExamples.length;
		allFeatures = new String[][] {
				{"Yes", "No"},
				{"Sunny", "Overcast", "Rainy"},
				{"Hot", "Mild", "Cold"},
				{"High", "Normal"},
				{"Strong", "Weak"}
		};
		featureLabels = new String[] {"Label", "Outlook", "Temperature", "Humidity", "Wind"};
	}

	
	public void init() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	//// creates the main panel and initializes it if necessary :SM
	public void jbInit() throws Exception {
		this.panel = new JPanel();
		this.panel.add(new JLabel(this.toString()));
	}

	
	public int[] setSearchTypes() {
		// only braedth-first is relevant to ID3. We need the whole tree.
		int[] types = {SpaceSearcher.BREADTH_FIRST};
		return types;
	}

	
	public Dimension getAppletSize() {
		return new Dimension(400, 400);
	}
	
	//// the main problem-dependent method, creates children of each state :SM
	public Set<Searchable> makeNewStates() {
		HashSet<Searchable> states = new HashSet<Searchable>();
		

		if (features.length != 0 && examples.length != 0){
			// calculate expected label entropy for each feature in 'this.features'
			int 		count;
			double 		EEnt;
			double 		minEEnt 	= 2.0;
			boolean[] 	subLabels 	= new boolean[N];
			int minEEntI = 0; // index of feature giving the minimum EEnt
			for (int f=0; f<features.length; f++){
				EEnt = 0;
				for (int v=0; v<allFeatures[features[f]].length; v++){
					count = 0;
					Arrays.fill(subLabels, false);
					for (int e : examples) {
						if (allExamples[e][features[f]] == v){
							count += 1;
							subLabels[e] = true;
						}
					}
					EEnt += (((double) count) / examples.length) * labelEntropy(subLabels);
				}
				if (EEnt < minEEnt){
					minEEnt = EEnt;
					minEEntI = f;
				}
			}
	
			// use the feature resulting in 'minEEntI' to expand the tree
			ArrayList<Integer> tmp = new ArrayList<Integer>();
			for (int i=0; i<features.length; i++)
				tmp.add(features[i]);	
			tmp.remove(minEEntI);
			int[] childFeatures = new int[tmp.size()];
			for (int i=0; i<childFeatures.length; i++)
				childFeatures[i] = tmp.get(i);
			
			for (int v=0; v<allFeatures[features[minEEntI]].length; v++){
				tmp.clear();
				for (int e : examples) {
					if (allExamples[e][features[minEEntI]] == v)
						tmp.add(e);
				}
				int[] childExamples = new int[tmp.size()];
				for (int i=0; i<childExamples.length; i++){
					childExamples[i] = tmp.get(i);
				}
				states.add(new ID3(childExamples, childFeatures, featureLabels[features[minEEntI]] + ": " +
						allFeatures[features[minEEntI]][v], this));
			}
		} else if (features.length == 0) { // either 'features' or 'examples' are empty
			states.add(new ID3(new int[0], new int[0], "NULL", null));
		}
		
		return states;
	}

	public boolean equals(Searchable s) {
		ID3 target = (ID3) s;
		return Arrays.equals(examples, target.examples)
			&& Arrays.equals(features, target.features)
			&& headLabel.equals(target.headLabel);
	}
  
	public int hashCode() {
		return (examples.hashCode() + headLabel.hashCode());
	}

	public String toString() {
		StringBuffer sbuffer = new StringBuffer(10);
		sbuffer.append(headLabel).append(" hc:" + hashCode());
		return sbuffer.toString();
	}
	
	//// MAIN ////
	public static void main(String[] args) {
		int[] ex = new int[N];
		for (int i=0; i<ex.length; i++)
			ex[i] = i;
		int[] ft = new int[allFeatures.length - 1];
		for (int i=1; i<=ft.length; i++)
			ft[i-1] = i;
		ID3 ID3i = new ID3(ex, ft, "ROOT", null);
		ID3 ID3f = new ID3(new int[0], new int[0], "NULL", null);
		BlindSearcher searcher = new BlindSearcher(ID3i, ID3f);
		searcher.setApplet();
		searcher.display("ID3 as space search");
	}
	//////////////
}
