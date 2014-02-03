package de.ocker.olfitracker.commons;

import java.sql.SQLException;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 * a various amount of methods, which open and react on dialog-required events
 * 
 * @author Florian J. Ocker
 */
public class Dialog {

  /**
   * opens an exception dialog that just contains information for the user how
   * to react
   * 
   * @param userInfoKey
   *          key that leads to a message that should help reacting on the
   *          exception
   * @param values
   *          specify the found user message
   */
  public static void showExceptionDialog(String userInfoKey, Object... values) {
    String userInfo = String.format(userInfoKey, values);
    Print.err(userInfo);
    JXErrorPane.showDialog(null, new ErrorInfo("Fehler ", userInfo, null, null, null, Level.ALL, null));
  }

  /**
   * opens an exception dialog that gives information about the exception and
   * how the user should react. <br>
   * the exception is more over saved in the database and the log
   * 
   * @param e
   *          the exception
   * @param userInfoPropertiesKey
   *          key that leads to a message that should help reacting on the
   *          exception
   * @param values
   *          specify the found user message
   */
  public static void showExceptionDialog(Exception e, String userInfoPropertiesKey, Object... values) {
    String userInfo = String.format(userInfoPropertiesKey, values);
    Print.err(e);
    JXErrorPane.showDialog(null, new ErrorInfo("program_name", userInfo, null, "category", e, Level.ALL, null));
  }

  /**
   * opens the standard database exception that provides information about
   * SQLExceptions
   * 
   * @param e
   *          the exception
   */
  public static void showExceptionDialog(SQLException e) {
    showExceptionDialog(e, "error.db");
  }

  /**
   * opens a standard warning dialog with the info object in its body
   * 
   * @param info
   *          can be anyhow formatted object
   */
  public static void warnExtended(Object info) {
    JOptionPane.showMessageDialog(null, info, "program_name", JOptionPane.WARNING_MESSAGE);
  }

  /**
   * opens a standard warning dialog with message found to given key and
   * refining values
   * 
   * @param messageKey
   *          is the key in properties
   * @param values
   *          may be inserted in the final String
   */
  public static void warn(String messageKey, Object... values) {
    String message = String.format(messageKey, values);
    JOptionPane.showMessageDialog(null, message, "program_name", JOptionPane.WARNING_MESSAGE);
  }

  /**
   * opens a standard warning dialog with message found to given key and
   * refining values
   * 
   * @param messageKey
   *          is the key in properties
   * @param values
   *          may be inserted in the final String
   */
  public static void warn(String titleKey, String messageKey, Object... values) {
    String message = String.format(messageKey, values);
    JOptionPane.showMessageDialog(null, message, titleKey, JOptionPane.WARNING_MESSAGE);
  }

}
