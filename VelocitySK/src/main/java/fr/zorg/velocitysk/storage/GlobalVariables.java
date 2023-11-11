package fr.zorg.velocitysk.storage;

import fr.zorg.bungeesk.common.utils.Pair;
import fr.zorg.velocitysk.BungeeSK;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Base64;

public class GlobalVariables {

    private static final File VARIABLES_FILE = new File(BungeeSK.getDataDirectory().toFile(), "variables.db");
    private static Connection connection;

    public static void init() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        if (!VARIABLES_FILE.exists()) {
            try {
                VARIABLES_FILE.getParentFile().mkdirs();
                VARIABLES_FILE.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        checkConnection();
        initTables();
    }

    private static void initTables() {
        checkConnection();
        try {
            final Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS global_variables (name TEXT PRIMARY KEY, value TEXT, type TEXT);");
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static Pair<byte[], String> getGlobalVariable(String variableName) {
        checkConnection();
        try {
            final Statement statement = connection.createStatement();
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM global_variables WHERE name = '" + variableName + "';");
            if (resultSet.next()) {
                final String base64value = resultSet.getString("value");
                final byte[] value = Base64.getDecoder().decode(base64value);
                final String type = resultSet.getString("type");
                resultSet.close();
                statement.close();
                return Pair.from(value, type);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void setGlobalVariable(String name, byte[] value, String type) {
        checkConnection();
        final String base64Value = Base64.getEncoder().encodeToString(value);
        try {
            final Statement statement = connection.createStatement();
            statement.execute("INSERT OR REPLACE INTO global_variables VALUES ('" + name + "', '" + base64Value + "', '" + type + "');");
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteGlobalVariable(String name) {
        checkConnection();
        try {
            final Statement statement = connection.createStatement();
            statement.execute("DELETE FROM global_variables WHERE name = '" + name + "';");
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void checkConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:" + VARIABLES_FILE.getAbsolutePath());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}