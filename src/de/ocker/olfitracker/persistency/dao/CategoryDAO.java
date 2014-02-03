package de.ocker.olfitracker.persistency.dao;

import de.ocker.olfitracker.model.Category;

/**
 * 
 * @author Florian J. Ocker
 * 
 */
public class CategoryDAO extends AbstractDAO<Category> {
  private static CategoryDAO instance;

  public static CategoryDAO instance() {
    if (instance == null) {
      instance = new CategoryDAO();
    }
    return instance;
  }

  public CategoryDAO() {
    super("category");
  }

  @Override
  protected Category construct(Integer id, String value) {
    return new Category(id, value);
  }

}
