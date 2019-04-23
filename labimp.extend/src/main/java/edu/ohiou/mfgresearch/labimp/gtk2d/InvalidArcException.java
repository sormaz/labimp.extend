package edu.ohiou.mfgresearch.labimp.gtk2d;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Dr.Dusan Sormaz + Deepak Pisipati
 * @version 1.0
 */

public class InvalidArcException extends InvalidCurveException
{
  /**
   * Constructor for initializing the Exception using a message string.
   * @param s - string specifying the error message.
   */
  public InvalidArcException(String s)
  {
    super(s);
  }

  /**
   *  Displays error message to standard error.
   */
  public void showException()
  {
    System.out.println("InvalidArcException: " + getMessage());
  }
}
