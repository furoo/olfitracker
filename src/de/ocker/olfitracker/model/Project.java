package de.ocker.olfitracker.model;

import de.ocker.olfitracker.persistency.dao.ProjectDAO;

/**
 * 
 * @author Florian J. Ocker
 * 
 */
public class Project extends PersistencyEntity {

  public Project(Integer id, String value) {
    super("project", id, "value", value);
  }

  public static Project generate() {
    return ProjectDAO.instance().insert("");
  }

  @Override
  public String toString() {
    return value;
  }
}