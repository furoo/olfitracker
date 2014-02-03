package de.ocker.olfitracker.commons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

public abstract class ExceptionCatchingActionListener extends AbstractAction implements ActionListener {
  private static final long serialVersionUID = 1L;

  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      actionPerform(e);
    } catch (RuntimeException exc) {
      Dialog.showExceptionDialog(exc, "error.program");
    }
  }

  public abstract void actionPerform(ActionEvent e);

}
