package de.ocker.olfitracker.persistency.dao;

import de.ocker.olfitracker.model.Title;

/**
 * 
 * @author Florian J. Ocker
 * 
 */
public class TitleDAO extends AbstractDAO<Title>{
  
  private static TitleDAO instance;

  public static TitleDAO instance() {
    if(instance == null){
      instance = new TitleDAO();
    }   
      return instance;
  }

  public TitleDAO() {
    super("title");
  }
  
  @Override
  protected Title construct(Integer id, String value) {
    return new Title(id, value);
  }


}
