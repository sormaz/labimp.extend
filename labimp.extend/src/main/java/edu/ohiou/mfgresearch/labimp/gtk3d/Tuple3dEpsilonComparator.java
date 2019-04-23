package edu.ohiou.mfgresearch.labimp.gtk3d;

import java.util.Comparator;
import javax.vecmath.Tuple3d;

public class Tuple3dEpsilonComparator implements Comparator {
	double precision = GeometryConstants.EPSILON;
	public Tuple3dEpsilonComparator() {
	}

	public Tuple3dEpsilonComparator(double precision) {
		this.precision = precision;
	}

	public int compare(Object o1, Object o2) {
		if (((Tuple3d) o1).epsilonEquals((Tuple3d) o2, precision))
			return 0;
		return 1;
	}
	//
	//public boolean equals(Object obj) {
	//return ((Tuple3d) this).epsilonEquals( (Tuple3d) obj, precision);
	//
	//    }
}