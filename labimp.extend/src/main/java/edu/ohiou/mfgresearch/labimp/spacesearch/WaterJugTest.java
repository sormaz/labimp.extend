package edu.ohiou.mfgresearch.labimp.spacesearch;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
import edu.ohiou.mfgresearch.labimp.spacesearch.SpaceSearcher;

public class WaterJugTest {

	public WaterJugTest() {
	}
	public static void main(String[] args) {
		SpaceSearcher ss = new BlindSearcher(new TestJugs(5, 3), new TestJugs(
			0,
			4));
		ss.display();
	}
}