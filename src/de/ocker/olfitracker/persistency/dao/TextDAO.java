package de.ocker.olfitracker.persistency.dao;

import de.ocker.olfitracker.model.Text;

/**
 * 
 * @author Florian J. Ocker
 * 
 */
public class TextDAO extends AbstractDAO<Text>{
  private static TextDAO instance;

  public static TextDAO instance() {
    if (instance == null) {
      instance = new TextDAO();
    }
    return instance;
  }
  public TextDAO() {
    super("text");
  }
  
  @Override
  protected Text construct(Integer id, String value) {
    return new Text(id, value);
  }


}
