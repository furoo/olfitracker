package de.ocker.olfitracker.model;

import java.sql.SQLException;

import de.ocker.olfitracker.commons.Print;
import de.ocker.olfitracker.persistency.SQLConnector;

/**
 * 
 * @author Florian J. Ocker
 * 
 */
public abstract class PersistencyEntity  {
  private final String relation;
  private final Integer id;
  private final String key;
  protected String value;

  public PersistencyEntity(String relation, Integer id, String key, String value) {
    this.relation = relation;
    this.id = id;
    this.key = key;
    this.value = value;
  }

  private void save() {
    try {
      SQLConnector.getInstance().execute(String.format("UPDATE %s SET %s = ? WHERE ID = ?", relation, key), value, id);
    } catch (SQLException e) {
      Print.err(e);
    }
  }

  public void setValue(String value) {
    this.value = value;
    save();
  }

  public String getValue() {
    return value;
  }
  
  public Integer getId() {
    return id;
  }
}
