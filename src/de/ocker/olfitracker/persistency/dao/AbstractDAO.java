package de.ocker.olfitracker.persistency.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.ocker.olfitracker.commons.Dialog;
import de.ocker.olfitracker.model.PersistencyEntity;
import de.ocker.olfitracker.persistency.SQLConnector;

/**
 * 
 * @author Florian J. Ocker
 * 
 */
public abstract class AbstractDAO<T extends PersistencyEntity> {
  protected HashMap<Integer, T> byId;
  protected final String relation;

  public AbstractDAO(String relation) {
    this.relation = relation;
  }

  public List<T> getAll() {
    if (byId == null) {
      load();
    }
    return Lists.newArrayList(byId.values());
  }

  public T get(Integer id) {
    if (byId == null) {
      load();
    }
    return byId.get(id);
  }

  protected void load() {
    byId = Maps.newHashMap();
    try {
      for (Map<String, Object> row : SQLConnector.getInstance().select("SELECT * FROM " + relation)) {
        Integer id = (Integer) row.get("id");
        String value = (String) row.get("value");
        byId.put(id, construct(id, value));
      }
    } catch (SQLException e) {
      Dialog.showExceptionDialog(e);
    }
  }

  public T insert(String value) {
    if (byId == null) {
      load();
    }
    try {
      Integer id = SQLConnector.getInstance().execute(
          String.format("INSERT INTO %s (`id`, `value`) VALUES (null, ?);", relation), value);
      T t = construct(id, value);
      byId.put(id, t);
      return t;
    } catch (SQLException e) {
      Dialog.showExceptionDialog(e);
      return null;
    }
  }

  public void delete(Integer id) {
    try {
      SQLConnector.getInstance().execute(String.format("DELETE FROM %s WHERE id = ?;", relation, id));
    } catch (SQLException e) {
      Dialog.showExceptionDialog(e);
    }
  }

  protected abstract T construct(Integer id, String value);
}
