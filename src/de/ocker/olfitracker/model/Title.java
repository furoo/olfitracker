package de.ocker.olfitracker.model;

import de.ocker.olfitracker.persistency.dao.TitleDAO;

/**
 * 
 * @author Florian J. Ocker
 * 
 */
public class Title extends PersistencyEntity {

  public Title(Integer id, String value) {
    super("title", id, "value", value);
  }

  public static Title generate() {
    return TitleDAO.instance().insert(null);
  }

}
