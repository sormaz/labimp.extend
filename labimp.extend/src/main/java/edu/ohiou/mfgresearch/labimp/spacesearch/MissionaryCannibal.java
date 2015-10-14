package edu.ohiou.mfgresearch.labimp.spacesearch;

import java.util.*;
import java.awt.*;
import javax.swing.tree.*;
import javax.swing.*;
//import com.borland.jbcl.layout.*;

public class MissionaryCannibal extends DefaultSpaceState {
	static final int NUMBER_M = 3;
	static final int NUMBER_C = 3;
	int numberOfM = 0;
	int numberOfC = 0;
	boolean safe = false;
	String bank = new String("");
	JPanel jPanel1 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	String[] operators = new String[5];

	public MissionaryCannibal(
		int numberOfMissionaries,
		int numberOfCannibals,
		String bank,
		MissionaryCannibal parent) {
		this.numberOfM = numberOfMissionaries;
		this.numberOfC = numberOfCannibals;
		this.bank = bank;
		this.parent = parent;
		node = new DefaultMutableTreeNode(this);
		defineOperators();
	}

	public void init() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void jbInit() throws Exception {
		this.panel = new JPanel();
		this.panel.add(new JLabel(this.toString()));
	}

	public void defineOperators() {
		operators[0] = "2M";
		operators[1] = "1M";
		operators[2] = "1M1C";
		operators[3] = "2C";
		operators[4] = "1C";
	}

	public boolean canOperatorBeUsed(String op) {
		if (this.bank == "Left") {
			if (op == "2M") {
				return (this.numberOfM >= 2);
			}

			if (op == "1M") {
				return (this.numberOfM >= 1);
			}

			if (op == "1M1C") {
				return ((this.numberOfM >= 1) && (this.numberOfC >= 1));
			}
			if (op == "2C") {
				return (this.numberOfC >= 2);
			}
			if (op == "1C") {
				return (this.numberOfC >= 1);
			}
		} else {
			if (op == "2M") {
				return ((MissionaryCannibal.NUMBER_M - this.numberOfM) >= 2);
			}

			if (op == "1M") {
				return ((MissionaryCannibal.NUMBER_M - this.numberOfM) >= 1);
			}

			if (op == "1M1C") {
				return (((MissionaryCannibal.NUMBER_M - this.numberOfM) >= 1) && ((MissionaryCannibal.NUMBER_C - this.numberOfC) >= 1));
			}
			if (op == "2C") {
				return ((MissionaryCannibal.NUMBER_C - this.numberOfC) >= 2);
			}
			if (op == "1C") {
				return ((MissionaryCannibal.NUMBER_C - this.numberOfC) >= 1);
			}
		}
		return false;
	}

	public String changeBanks() {
		if (this.bank == "Left")
			return "Right";
		else
			return "Left";
	}

	public MissionaryCannibal applyOperator(String op) {
		if (this.bank == "Left") {
			if (op == "2M") {
				return new MissionaryCannibal(
					this.numberOfM - 2,
					this.numberOfC,
					this.changeBanks(),
					this);
			}
			if (op == "1M") {
				return new MissionaryCannibal(
					this.numberOfM - 1,
					this.numberOfC,
					this.changeBanks(),
					this);
			}
			if (op == "1M1C") {
				return new MissionaryCannibal(
					this.numberOfM - 1,
					this.numberOfC - 1,
					this.changeBanks(),
					this);
			}
			if (op == "2C") {
				return new MissionaryCannibal(
					this.numberOfM,
					this.numberOfC - 2,
					this.changeBanks(),
					this);
			}
			if (op == "1C") {
				return new MissionaryCannibal(
					this.numberOfM,
					this.numberOfC - 1,
					this.changeBanks(),
					this);
			}
		} else {
			if (op == "2M") {
				return new MissionaryCannibal(
					this.numberOfM + 2,
					this.numberOfC,
					this.changeBanks(),
					this);
			}
			if (op == "1M") {
				return new MissionaryCannibal(
					this.numberOfM + 1,
					this.numberOfC,
					this.changeBanks(),
					this);
			}
			if (op == "1M1C") {
				return new MissionaryCannibal(
					this.numberOfM + 1,
					this.numberOfC + 1,
					this.changeBanks(),
					this);
			}
			if (op == "2C") {
				return new MissionaryCannibal(
					this.numberOfM,
					this.numberOfC + 2,
					this.changeBanks(),
					this);
			}
			if (op == "1C") {
				return new MissionaryCannibal(
					this.numberOfM,
					this.numberOfC + 1,
					this.changeBanks(),
					this);
			}
		}
		return null;
	}

	public int[] setSearchTypes() {
		int[] types = new int[2];
		types[0] = 1;
		types[1] = 2;
		return types;
	}

	public Dimension getAppletSize() {
		return new Dimension(400, 400);
	}

	public Set<Searchable> makeNewStates() {
		Set<Searchable>  list = new HashSet<Searchable>();
		for (int i = 0; i < this.operators.length; i++) {
			String op = this.operators[i];
			if (this.canOperatorBeUsed(op)) {

				MissionaryCannibal mc = this.applyOperator(op);
				if (mc.safe()) {
					list.add(mc);
				}
			}
		}
		return list;
	}

	public Searchable rrunSpaceSearch(Searchable goal) {
		SpaceSearcher searcher = new BlindSearcher(this, goal);
		searcher.setApplet();
		searcher.display("searcher");
		return null;
		//        return searcher.runSpaceSearch();

	}

	public boolean equals(Searchable s) {
		MissionaryCannibal mc = (MissionaryCannibal) s;
		return ((this.numberOfM == mc.numberOfM)
			&& (this.numberOfC == mc.numberOfC) && (this.bank == mc.bank));
	}
	/*

	 public boolean safe()
	 {
	 if(this.bank=="Left")
	 {
	 return (this.numberOfM>=this.numberOfC);
	 }
	 else
	 {
	 return ((3-this.numberOfM)>=(3-this.numberOfC));
	 }
	 }

	 */

	public boolean safe() {
		if (this.numberOfM == this.numberOfC)
			return true;
		if (this.numberOfM == MissionaryCannibal.NUMBER_M)
			return true;

		if (this.numberOfM == 0)
			return true;

		if (Math.abs(this.numberOfM - this.numberOfC) == 1)
			return false;

		if (this.numberOfM < this.numberOfC)
			return false;
		return false;
	}

	public String toString() {
		return Integer.toString(this.numberOfM)
			+ "M "
			+ Integer.toString(this.numberOfC)
			+ "C "
			+ " "
			+ this.bank;
	}

	public static void main(String[] args) {
		MissionaryCannibal mc1 = new MissionaryCannibal(3, 3, "Left", null);
		MissionaryCannibal goal = new MissionaryCannibal(0, 0, "Right", null);
		Searchable g = mc1.runSpaceSearch(goal);
    System.out.println(g.printPath());
	}
}
