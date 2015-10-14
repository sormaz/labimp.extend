/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.gtk3d;

import java.text.NumberFormat;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.vecmath.Tuple2d;
import javax.vecmath.Tuple3d;

/**
 * @author Dusan Sormaz
 *
 */
public class Tuple2dRenderer extends JLabel {
	
	private Tuple2d tuple;
	static public final int NUM_FRACTION_DIGITS = 3;
	int numFracDigits;


	/**
	 * 
	 */
	public Tuple2dRenderer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public Tuple2dRenderer(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public Tuple2dRenderer(Icon arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public Tuple2dRenderer(String arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public Tuple2dRenderer(Icon arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public Tuple2dRenderer(String arg0, Icon arg1, int arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	static public String format(Tuple2d tuple) {
		return format(tuple, NUM_FRACTION_DIGITS);
	}

	static public String format(Tuple2d tuple, int numFracDigits) {

		NumberFormat formatter = NumberFormat.getInstance();
		String className = tuple.getClass().getName();
		String name = className.substring(
			className.lastIndexOf(".") + 1,
			className.length());
		formatter.setMaximumFractionDigits(numFracDigits);
		return name
			+ ": ["
			+ formatter.format(tuple.x)
			+ ", "
			+ formatter.format(tuple.y)
			
			+ "]";
	}
	static public String formatXML(Tuple2d tuple) {
		return formatXML(tuple, NUM_FRACTION_DIGITS);
	}

	static public String formatXML(Tuple2d tuple, int numFracDigits) {

		NumberFormat formatter = NumberFormat.getInstance();
		String className = tuple.getClass().getName();
		String name = className.substring(
			className.lastIndexOf(".") + 1,
			className.length());
		formatter.setMaximumFractionDigits(numFracDigits);
		return formatter.format(tuple.x)
			+ " "
			+ formatter.format(tuple.y);
	}

}
