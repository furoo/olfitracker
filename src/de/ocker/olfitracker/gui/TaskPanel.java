package de.ocker.olfitracker.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.ocker.olfitracker.model.Task;
import de.ocker.olfitracker.model.Text;
import de.ocker.olfitracker.model.Title;

/**
 * 
 * @author Florian J. Ocker
 * 
 */
public class TaskPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public TaskPanel(Task task) {
    super(new BorderLayout());
    setPreferredSize(new Dimension(300, 100));
    setBorder(BorderFactory.createLineBorder(Color.BLACK));

    final Title title = task.getTitle();
    final JTextField titleField = new JTextField(title.getValue());

    final Text text = task.getText();
    final JTextArea textArea = new JTextArea(text.getValue());

    titleField.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
      }

      public void removeUpdate(DocumentEvent e) {
        title.setValue(titleField.getText());
      }

      public void insertUpdate(DocumentEvent e) {
        title.setValue(titleField.getText());
      }
    });

    textArea.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
      }

      public void removeUpdate(DocumentEvent e) {
        text.setValue(textArea.getText());
      }

      public void insertUpdate(DocumentEvent e) {
        text.setValue(textArea.getText());
      }
    });

    add(BorderLayout.NORTH, titleField);
    add(BorderLayout.CENTER, textArea);
  }
}
