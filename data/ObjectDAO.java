package data;

import java.io.Serializable;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public abstract class ObjectDAO<T> implements DAO<T>, Serializable {


    public static Map<String ,String> getDatabase(String databaseName) {
        Map<String, String> map = new HashMap<>();
        try (Connection connection = getConnection(databaseName);
            PreparedStatement ps = connection.prepareStatement("show databases")) {
            ResultSetMetaData rs = ps.executeQuery().getMetaData();
            for (int i = 1; i <= rs.getColumnCount(); i++) {
                map.put(rs.getColumnName(i), rs.getColumnTypeName(i));
            }
            return map;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
      }
        return null;
    }

    @Override
    public boolean delete(String databaseName, String tableName, int id) {
        String sql = "delete from " + tableName + " where " + getPrimaryKeyName(databaseName, tableName) + " = ?";
        try (Connection con = getConnection(databaseName);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * this method is used to get the primary key name of a table in a database or null if the table does not exist
     * @param databaseName
     * @param tableName
     * @return primary key name of table
     */
    public String getPrimaryKeyName(String databaseName, String tableName) {
        String sql = "show keys from " + tableName + " where key_name = 'PRIMARY'";
        try (Connection con = getConnection(databaseName);
             Statement statement = Objects.requireNonNull(con).createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("Column_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * get a connection to the database
     * @param databaseName
     * @return a connection to the database or null if the connection fails
     */
    public static Connection getConnection(String databaseName) {
        String url = "jdbc:mysql://localhost:3306/" + databaseName;
        String user = "root";
        String password = "root";
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }


    /**
     * a method to print all the objects in the table
     * @param databaseName
     * @param tableName
     */
    public static void printAll(String databaseName, String tableName) {
        String sql = "select * from " + tableName;
        try (Connection con = getConnection(databaseName);
             Statement statement = Objects.requireNonNull(con).createStatement();) {
            statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * create a table in the database
     * @param databaseName
     * @param tableName
     * @param "the sql statement to create the table"
     * @return true if the table was created, false otherwise
     */
    public static boolean createTable(String databaseName, String tableName, String... ts) {
        StringBuilder sql = new StringBuilder("create table " + tableName + "(");
        for (String t : ts) {
            sql.append(t).append(",");
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1));
        sql.append(")");
        try (Connection connection = getConnection(databaseName)) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql.toString());
                System.out.println(sql);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(sql);
        }
        return false;
    }

}
