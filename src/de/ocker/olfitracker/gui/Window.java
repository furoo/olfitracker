package de.ocker.olfitracker.gui;

import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXStatusBar;

import com.google.common.collect.Sets;

import de.ocker.olfitracker.commons.Dialog;
import de.ocker.olfitracker.persistency.SQLConnector;

public class Window extends JXFrame {
  private static final long serialVersionUID = 1L;

  public Window() {
    super("Olfitracker");
    JXStatusBar toolbar = new JXStatusBar();
    toolbar.add(new JButton("+"));

    setJMenuBar(new MenuBar());
    getContentPane().add(new Dashboard());

    setSize(500, 800);
    setLocationRelativeTo(null);
    setKeyboardTraversalPolicy();
    addClosingListener();

    // URL systemResource = this.getClass().getResource("/data/logo/32x32.png");
    // Image img = Toolkit.getDefaultToolkit().getImage(systemResource);
    // setIconImage(img);

    setVisible(true);
    repaint();
    validate();
    setLookAndFeel();
  }

  private void setKeyboardTraversalPolicy() {
    // add right-arrow
    Set<AWTKeyStroke> focusTraversalKeys = Sets.newHashSet(KeyboardFocusManager.getCurrentKeyboardFocusManager()
        .getDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));

    focusTraversalKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
    KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalKeys(
        KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, focusTraversalKeys);

    // add left-arrow
    focusTraversalKeys = Sets.newHashSet(KeyboardFocusManager.getCurrentKeyboardFocusManager()
        .getDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));

    focusTraversalKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
    KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalKeys(
        KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, focusTraversalKeys);
  }

  private void addClosingListener() {
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        exit();
      }
    });
  }

  public void exit() {
    try {
      SQLConnector.getInstance().disconnect();
    } catch (SQLException e) {
      Dialog.showExceptionDialog(e);
    }
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * Create the GUI and show it. For thread safety, this method should be
   * invoked from the event dispatch thread.
   */
  private static void setLookAndFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      Dialog.showExceptionDialog(e, "exception.look_and_feel");
    }
  }
}