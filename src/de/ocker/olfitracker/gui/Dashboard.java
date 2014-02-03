package de.ocker.olfitracker.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import de.ocker.olfitracker.commons.ComponentHolder;
import de.ocker.olfitracker.commons.ExceptionCatchingActionListener;
import de.ocker.olfitracker.model.Category;
import de.ocker.olfitracker.model.Project;
import de.ocker.olfitracker.model.Task;
import de.ocker.olfitracker.persistency.dao.CategoryDAO;
import de.ocker.olfitracker.persistency.dao.ProjectDAO;
import de.ocker.olfitracker.persistency.dao.TaskDAO;

/**
 * @author Florian J. Ocker
 */
public class Dashboard extends JPanel {
  private static final long serialVersionUID = 1L;

  public Dashboard() {
    super(new VerticalLayout(1));
    JXStatusBar toolbar = new JXStatusBar();
    final JScrollPane scrollPane = new JScrollPane();
    final JPanel overalPanel = new JPanel(new VerticalLayout(1));
    scrollPane.setViewportView(overalPanel);

    final List<Task> tasksList = TaskDAO.instance().getAll();

    JButton addButton = new JButton("+");
    toolbar.add(addButton);

    final JComboBox<Project> project = new JComboBox<Project>(new Vector<Project>(ProjectDAO.instance().getAll()));
    AutoCompleteDecorator.decorate(project);
    toolbar.add(project);

    final JComboBox<Category> category = new JComboBox<Category>(new Vector<Category>(CategoryDAO.instance().getAll()));
    AutoCompleteDecorator.decorate(category);
    toolbar.add(category);

    addButton.addActionListener(new ExceptionCatchingActionListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionPerform(ActionEvent arg0) {
        // Filter filter = Filter.generate();
        addTask(scrollPane, overalPanel, tasksList,
            Task.generate((Project) project.getSelectedItem(), (Category) category.getSelectedItem()));
        validate();
      }
    });

    for (final Task task : tasksList) {
      addTask(scrollPane, overalPanel, tasksList, task);
    }
    add(toolbar);
    add(scrollPane);
  }

  private void addTask(final JScrollPane scrollPane, final JPanel overalPanel, final List<Task> tasksList,
      final Task task) {
    final TaskPanel taskPanel = new TaskPanel(task);
    JButton subButton = new JButton("-");
    final JPanel wrapperPanel = new JPanel();
    wrapperPanel.add(BorderLayout.WEST, new ComponentHolder(subButton));
    wrapperPanel.add(BorderLayout.CENTER, taskPanel);
    overalPanel.add(wrapperPanel);

    subButton.addActionListener(new ExceptionCatchingActionListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionPerform(ActionEvent e) {
        removeTask(scrollPane, overalPanel, tasksList, task, wrapperPanel);
      }
    });
  }

  protected void removeTask(JScrollPane scrollPane, JPanel overalPanel, List<Task> tasksList, Task task,
      JPanel wrapperPanel) {
    tasksList.remove(task);
    new TaskDAO().delete(task.getId());
    overalPanel.remove(wrapperPanel);
    overalPanel.validate();
    overalPanel.repaint();
  }
}