package de.ocker.olfitracker.persistency.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.ocker.olfitracker.commons.Dialog;
import de.ocker.olfitracker.model.Category;
import de.ocker.olfitracker.model.Project;
import de.ocker.olfitracker.model.Task;
import de.ocker.olfitracker.model.Text;
import de.ocker.olfitracker.model.Title;
import de.ocker.olfitracker.persistency.SQLConnector;

/**
 * 
 * @author Florian J. Ocker
 * 
 */
public class TaskDAO {
  private final String relation = "task";

  protected HashMap<Integer, Task> byId;
  private static TaskDAO instance;

  public static TaskDAO instance() {
    if (instance == null) {
      instance = new TaskDAO();
    }
    return instance;
  }

  public List<Task> getAll() {
    if (byId == null) {
      load();
    }
    return Lists.newArrayList(byId.values());
  }

  public Task get(Integer id) {
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
        Project project = ProjectDAO.instance().get((Integer) row.get("project_id"));
        Category category = CategoryDAO.instance().get((Integer) row.get("category_id"));
        Title title = TitleDAO.instance().get((Integer) row.get("title_id"));
        Text text = TextDAO.instance().get((Integer) row.get("text_id"));
        byId.put(id, construct(id, project, category, title, text));
      }
    } catch (SQLException e) {
      Dialog.showExceptionDialog(e);
    }
  }

  private Task construct(Integer id, Project project, Category category, Title title, Text text) {
    return new Task(id, project, category, title, text);
  }

  public Task insert(Project project, Category category, Title title, Text text) {
    if (byId == null) {
      load();
    }
    try {
      Integer id = SQLConnector.getInstance().execute(
          String.format(
              "INSERT INTO %s (`id`, `project_id`, `category_id`, `title_id`, `text_id`) VALUES (null, ?, ?, ?, ?);",
              relation), project.getId(), category.getId(), title.getId(), text.getId());
      Task task = construct(id, project, category, title, text);
      byId.put(id, task);
      return task;
    } catch (SQLException e) {
      Dialog.showExceptionDialog(e);
      return null;
    }
  }

  public void delete(Integer id) {
    try {
      SQLConnector.getInstance().execute(String.format("DELETE FROM %s WHERE id = ?;", relation), id);
    } catch (SQLException e) {
      Dialog.showExceptionDialog(e);
    }
  }
}
