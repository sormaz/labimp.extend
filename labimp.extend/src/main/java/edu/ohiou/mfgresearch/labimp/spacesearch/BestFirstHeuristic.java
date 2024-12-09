package edu.ohiou.mfgresearch.labimp.spacesearch;

class BestFirstHeuristic implements HeuristicFunction {
	  ComparableSpaceState state;
	  public BestFirstHeuristic (ComparableSpaceState state) {
	    this.state = state; 
	  }
	  public double evaluate () {
	    return 0;
	}
}
