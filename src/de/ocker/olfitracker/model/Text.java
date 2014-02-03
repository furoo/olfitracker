package de.ocker.olfitracker.model;

import de.ocker.olfitracker.persistency.dao.TextDAO;

/**
 * 
 * @author Florian J. Ocker
 * 
 */
public class Text extends PersistencyEntity {

  public Text(Integer id, String value) {
    super("text", id, "value", value);
  }

  public static Text generate() {
    return TextDAO.instance().insert(null);
  }

}
