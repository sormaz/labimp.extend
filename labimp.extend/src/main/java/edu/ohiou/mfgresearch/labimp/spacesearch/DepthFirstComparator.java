/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.spacesearch;

import java.util.Comparator;

/**
 * @author Dusan Sormaz
 *
 */
public class DepthFirstComparator implements Comparator<Searchable> {

  public int compare(Searchable s1, Searchable s2) {
//    if (!(o1 instanceof Searchable)) {
//      throw new ClassCastException ("Class " + o1.getClass() + " of " 
//          + o1.toString() + "is not Searchable");
//    }
//    if (!(o2 instanceof Searchable)) {
//      throw new ClassCastException ("Class " + o2.getClass() + " of " 
//          + o2.toString() + "is not Searchable");
//    }
//    Searchable s1 = (Searchable) o1;
//    Searchable s2 = (Searchable) o2;
    if (s1.equals(s2)) {
      return 0;
    }
    else if (s1.getIndex() < s2.getIndex()) {
      return 1;
    }
    else return -1;
  }

  public String toString () {
    return "Depth-first search";
  }
}
