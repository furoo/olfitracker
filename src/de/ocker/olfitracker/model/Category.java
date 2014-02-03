package de.ocker.olfitracker.model;

import de.ocker.olfitracker.persistency.dao.CategoryDAO;

/**
 * 
 * @author Florian J. Ocker
 * 
 */
public class Category extends PersistencyEntity {

  public Category(Integer id, String value) {
    super("category", id, "value", value);
  }

  public static Category generate() {
    return CategoryDAO.instance().insert("");
  }

  
  @Override
  public String toString() {
    return value;
  }
}
