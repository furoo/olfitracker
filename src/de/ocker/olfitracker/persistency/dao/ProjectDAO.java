package de.ocker.olfitracker.persistency.dao;

import de.ocker.olfitracker.model.Project;

/**
 * 
 * @author Florian J. Ocker
 * 
 */
public class ProjectDAO extends AbstractDAO<Project> {
  private static ProjectDAO instance;

  public static ProjectDAO instance() {
    if (instance == null) {
      instance = new ProjectDAO();
    }
    return instance;
  }

  public ProjectDAO() {
    super("project");
  }

  @Override
  protected Project construct(Integer id, String value) {
    return new Project(id, value);
  }

}
