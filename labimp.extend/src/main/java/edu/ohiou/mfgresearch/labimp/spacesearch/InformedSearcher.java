package edu.ohiou.mfgresearch.labimp.spacesearch;

import java.io.IOException;
import java.util.*;

import edu.ohiou.mfgresearch.labimp.basis.Viewable;

public class InformedSearcher extends SpaceSearcher {

  
	
  public InformedSearcher() {
    // TODO Auto-generated constructor stub
  }

  public InformedSearcher(Searchable initial, Searchable goal) {
    super(initial, goal);
    // TODO Auto-generated constructor stub
  }

  public InformedSearcher(Searchable initial, Searchable goal, int order) {
    super(initial, goal, order);
    // TODO Auto-generated constructor stub
  }

  public InformedSearcher(Searchable initial, Searchable currentState,
      Searchable goal, Comparator comparator) {
    super(initial, currentState, goal, comparator);
    // TODO Auto-generated constructor stub
  }

  public InformedSearcher(Searchable initial, Searchable goal,
      Comparator comparator) {
    super(initial, goal, comparator);
    // TODO Auto-generated constructor stub
  }
  
  public void initializeSearch() {
   initializeSearch(initialState.getComparator());
  }

  
  public void initializeSearch(Comparator<Searchable> comparator) {
    setOpen(new SearchSet(comparator));
    closed = new HashSet<Searchable>();
    getOpen().add(initialState);
  }
  
  /* (non-Javadoc)
   * @see edu.ohiou.mfgresearch.labimp.spacesearch.SpaceSearcher#runOptSpaceSearch(int)
   */
  @Override
  public Searchable runOptSpaceSearch(int numberOfSteps) {
    // TODO Auto-generated method stub
    return super.runOptSpaceSearch(numberOfSteps);
  }

  /* (non-Javadoc)
   * @see edu.ohiou.mfgresearch.labimp.spacesearch.SpaceSearcher#runSpaceSearch(int)
   */
  @Override
  public Searchable runSpaceSearch(int numberOfSteps) {
 
//    System.out.println(getOpen().comparator());
     try {
//    	 BY JING HUANG''''''''''''''''''''''''''''''''''''''''''''
       if (getCurrentState()!= null && getCurrentState()!= goalState)
         ((Viewable)getCurrentState()).settColor (CLOSED_COLOR);
//       BY JING HUANG''''''''''''''''''''''''''''''''''''''''''''       
    } catch (NullPointerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    if (isSearchComplete(this)) {
      if (hasReachedGoal()) 
         return goalState;
      else
         return null;
    }
    closed.add(getCurrentState());

    Set newStates = (Set) getCurrentState().makeNewStates();

//    System.out.println("current states: " + getCurrentState());
//    System.out.println("new states: " + newStates);
//    System.out.println("new states size: " + newStates.size());

    
    removeOldStates(newStates, getOpen()); // needed for jtree
    removeOldStates(newStates, closed);
    children = new LinkedList(newStates);
    getOpen().addAll(children);
     
//    System.out.println("open size:" + getOpen().size());
//    System.out.println("closed size:" + closed.size());
//     BY JING HUANG'''''''''''''''''''''''''''''''''''''''''''''
//    try {
//		(new TSPSampleGenerator()).appendFile(sampleInfo+ "1.csv",closed.size()+","+open.size());
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
// BY JING HUANG''''''''''''''''''''''''''''''''''''''''''''' 
    
    if (numberOfSteps <= 1) {
      return getCurrentState();
    } 
    else {
      numberOfSteps--;
      try {
      return runSpaceSearch(numberOfSteps);
      }
      catch (RuntimeException rte) {
    	  try { 
    		TSPSampleGenerator.genFile("exception.txt");
			TSPSampleGenerator.appendFile(TSPSampleGenerator.getfileName(2, "exception.txt"), rte.toString()  );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rte.printStackTrace();
		return null;
      }
    }
  }


  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

}
