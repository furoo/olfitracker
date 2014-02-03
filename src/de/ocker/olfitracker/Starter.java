package de.ocker.olfitracker;

import de.ocker.olfitracker.gui.Window;

/**
 * 
 * @author Florian J. Ocker
 * 
 */
public class Starter {

  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        try {
          new Window();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
}