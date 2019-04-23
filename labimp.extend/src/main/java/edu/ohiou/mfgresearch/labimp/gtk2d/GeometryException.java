package edu.ohiou.mfgresearch.labimp.gtk2d;

/**
 * Title:        Generic classes for manufacturing planning
 * Description:  Thsi project implements general classes for intelligent manufacturing planning. These are:
 * ImpObject - umbrella class fro all objects
 * MrgPartModel - general part object data
 * Viewable - interface to display objects in applet
 * GUIApplet - applet that utilizes viewable interface
 * Copyright:    Copyright (c) 2001
 * Company:      Ohio University
 * @author Dusan Sormaz
 * @version 1.0
 */

public class GeometryException extends Exception {

  /**
    * Constructor that takes string descriptino of the cause
    */
  public GeometryException(String s) {
    super (s);
  }

  /**
	 *  Displays error message to standard error.
	 */
	public void showException() {
	  System.out.println("GeometryException: " + getMessage());
	}

}