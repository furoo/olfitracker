package de.ocker.olfitracker.persistency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.management.relation.Relation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.ocker.olfitracker.commons.Print;

/**
 * encapsulates access to the MySQL database
 * 
 * @author Florian J. Ocker
 */
public class SQLConnector {
  private final String classname = getClass().getSimpleName();
  private final String sheme = "olfitracker";
  private final String username = "root";
  private final String password = "";
  private final String server = "localhost";

  private final Vector<PoolConnection> connectionPool = new Vector<PoolConnection>();

  private static SQLConnector connector;

  /**
   * 
   * @return the singelton sql connector
   */
  public static SQLConnector getInstance() {
    if (connector == null) {
      connector = new SQLConnector();
    }
    return connector;
  }

  /**
   * provides access to a connection<br>
   * connections are generated if necessary or hold in a pool for fast access
   * 
   * @return a valid connection of the pool
   * @throws SQLException
   */
  private Connection connect() throws SQLException {
    PoolConnection poolConnection;
    boolean invalidConnections = false;
    for (int c = 0; c < connectionPool.size(); c++) {
      if (!connectionPool.elementAt(c).active) {
        poolConnection = connectionPool.elementAt(c);
        if (poolConnection.isValid()) {
          poolConnection.active = true;
          return poolConnection.connection;
        } else {
          invalidConnections = true;
        }
      }
    }
    // remove invalid connections
    if (invalidConnections) {
      for (PoolConnection con : Lists.newArrayList(connectionPool)) {
        if (!con.active) {
          connectionPool.remove(con);
        }
      }
    }

    Connection connection;
    String url = String.format("jdbc:mysql://%s/%s", server, sheme);
    connection = DriverManager.getConnection(url, username, password);
    poolConnection = new PoolConnection(connection, true);
    connectionPool.addElement(poolConnection);
    return connection;
  }

  /**
   * releases the connection, so that it can be reused
   * 
   * @param connection
   *          that will be released
   */
  private void release(Connection connection) {
    int c = 0;
    while (!connectionPool.elementAt(c).connection.equals(connection)) {
      c++;
    }
    connectionPool.elementAt(c).active = false;
  }

  /**
   * closes all open connections
   * 
   * @throws SQLException
   */
  public void disconnect() throws SQLException {
    Print.line("%s.disconnect()", classname);
    for (PoolConnection poolConnection : connectionPool) {
      poolConnection.connection.close();
    }
  }

  /**
   * Executes a give sql string and its values
   * 
   * @param sql
   *          formatted for a prepared statement
   * @param values
   *          that are inserted in the prepared statement
   * @return the generated id
   * @throws SQLException
   */
  public Integer execute(String sql, Object... values) throws SQLException {
    Integer id = -1;
    Print.line(classname + "(%s values)\n%s", values.length, sql);
    Connection connection = connect();
    PreparedStatement pstatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    setValuesInStatement(pstatement, values);

    try {
      pstatement.executeUpdate();
      ResultSet generatedKeys = pstatement.getGeneratedKeys();
      if (generatedKeys != null && generatedKeys.next()) {
        id = generatedKeys.getInt(1);
      }
      pstatement.close();
      release(connection);
    } catch (SQLException e) {
      // always release the connection for the next operation
      pstatement.close();
      release(connection);
      throw e;
    }
    return id;
  }

  /**
   * @param sql
   *          statement, which will be executed
   * @param values
   *          that will be inserted into the sql string
   * @return a list of maps that contain attributes (keys) and their values
   * @throws SQLException
   */
  public List<Map<String, Object>> select(String sql, Object... values) throws SQLException {
    Connection connection = connect();
    PreparedStatement pstatement = connection.prepareStatement(sql);
    setValuesInStatement(pstatement, values);
    ResultSet resultSet = pstatement.executeQuery();
    List<Map<String, Object>> table = new ArrayList<Map<String, Object>>();
    Map<String, Object> row;
    while (resultSet.next()) {
      row = new LinkedHashMap<String, Object>();
      for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
        row.put(resultSet.getMetaData().getColumnLabel(i).toLowerCase(), resultSet.getObject(i));
      }
      table.add(row);
    }
    pstatement.close();
    release(connection);
    return table;
  }

  // UTIL

  /**
   * @param table
   * @return all column names of the given table
   * @throws SQLException
   */
  public List<String> getColumns(Relation table) throws SQLException {
    List<String> columns = new ArrayList<String>();
    String sql = "SHOW FIELDS FROM " + table;
    Connection connection = connect();
    Statement createStatement = connection.createStatement();
    ResultSet ergebnis = createStatement.executeQuery(sql);
    while (ergebnis.next()) {
      columns.add(ergebnis.getString("Field"));
    }
    createStatement.close();
    release(connection);
    return columns;
  }

  /**
   * meta access to database table information
   * 
   * @param table
   * @return a map of all columns in this table, related to their type
   * @throws SQLException
   */
  public Map<String, String> getFields(String table) throws SQLException {
    Map<String, String> columns = Maps.newHashMap();
    String sql = "SHOW FIELDS FROM " + table;
    Connection connection = connect();
    Statement createStatement = connection.createStatement();
    ResultSet ergebnis = createStatement.executeQuery(sql);
    while (ergebnis.next()) {
      columns.put(ergebnis.getString("Field"), ergebnis.getString("Type"));
    }
    createStatement.close();
    release(connection);
    return columns;
  }

  /**
   * @param table
   * @return the foreign key of the parent table if available otherwise null
   * @throws SQLException
   */
  public String getForeignKey(Relation table) throws SQLException {
    for (String columnName : getColumns(table)) {
      if (columnName.toLowerCase().contains("_id")) {
        return columnName;
      }
    }
    return null;
  }

  /**
   * replaces the ? in the pstatement with the correct value
   * 
   * @param pstatement
   *          the prepared statement
   * @param values
   *          any values that should be inserted in pstatement
   * @return the filled and so prepared pstatement
   * @throws SQLException
   */
  private PreparedStatement setValuesInStatement(PreparedStatement pstatement, Object... values) throws SQLException {
    return setRecursive(pstatement, new Index(1), Lists.newArrayList(values));
  }

  private PreparedStatement setRecursive(PreparedStatement pstatement, Index index, List<?> values) throws SQLException {
    if (values.isEmpty()) {
      return pstatement;
    }
    Object value = values.remove(0);
    if (value instanceof Collection<?>) {
      setRecursive(pstatement, index, Lists.newArrayList((Collection<?>) value));
    } else if (value != null && value.getClass().isArray()) {
      setRecursive(pstatement, index, Lists.newArrayList((Object[]) value));
    } else {
      pstatement.setObject(index.getValue(), value);
      index.increment();
    }
    return setRecursive(pstatement, index, values);
  }

  /**
   * needed for a call by reference highering of the index
   * 
   * @author Florian J. Ocker
   */
  private class Index {
    private int value;

    public Index(int value) {
      this.value = value;
    }

    public void increment() {
      value++;
    }

    public int getValue() {
      return value;
    }
  }

  /**
   * encapsulates a single connection to the database
   * 
   */
  private class PoolConnection {
    Connection connection;
    boolean active;

    public PoolConnection(Connection con, boolean active) {
      this.connection = con;
      this.active = active;
    }

    public boolean isValid() {
      try {
        return connection.isValid(200);
      } catch (SQLException e) {
        return false;
      }
    }
  }

  @Override
  public String toString() {
    return classname + String.format("[%s]", server);
  }
}
