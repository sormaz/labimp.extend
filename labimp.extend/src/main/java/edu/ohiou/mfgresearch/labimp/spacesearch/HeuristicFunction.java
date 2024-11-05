/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.spacesearch;

/**
 * @author Dusan Sormaz
 *
 */
public interface HeuristicFunction {
  
  public double evaluate();

}

class BestFirstHeuristic implements HeuristicFunction {
	  ComparableSpaceState state;
	  public BestFirstHeuristic (ComparableSpaceState state) {
	    this.state = state; 
	  }
	  public double evaluate () {
	    return 0;
	}
}
