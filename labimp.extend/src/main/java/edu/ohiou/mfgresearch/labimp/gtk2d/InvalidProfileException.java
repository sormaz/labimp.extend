package edu.ohiou.mfgresearch.labimp.gtk2d;

/**
 * <p>Title: Generic classes for manufacturing planning</p>
 * <p>Description: Thsi project implements general classes for intelligent manufacturing planning. These are:
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: Ohio University</p>
 * @author Dusan Sormaz
 * @version 1.0
 */

public class InvalidProfileException extends GeometryException {

  public InvalidProfileException(String s) {
    super(s);
  }

  /**
   *  Displays error message to standard error.
   */
  public void showException() {
    System.out.println("InvalidProfileException: " + getMessage());
  }

}