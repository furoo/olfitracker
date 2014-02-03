package de.ocker.olfitracker.model;

import de.ocker.olfitracker.persistency.dao.TaskDAO;

/**
 * 
 * @author Florian J. Ocker
 * 
 */
public class Task {
  private final Project project;
  private final Category category;
  private final Title title;
  private final Text text;
  private final Integer id;

  public Task(Integer id, Project project, Category category, Title title, Text text) {
    this.id = id;
    this.project = project;
    this.category = category;
    this.title = title;
    this.text = text;
  }
//TODO datum, Status, bei Enter automatisch '-'
  public Project getProject() {
    return project;
  }

  public Category getCategory() {
    return category;
  }

  public Title getTitle() {
    return title;
  }

  public Text getText() {
    return text;
  }

  public static Task generate(Project project, Category category) {
    return TaskDAO.instance().insert(project, category, Title.generate(), Text.generate());
  }

  public Integer getId() {
    return id;
  }
}
