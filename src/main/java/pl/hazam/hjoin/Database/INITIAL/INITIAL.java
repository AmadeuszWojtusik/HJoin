package pl.hazam.hjoin.Database.INITIAL;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import pl.hazam.hjoin.Database.DbUtil.DbUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class INITIAL {

    private YamlConfiguration globalConfig;
    private Plugin plugin;
    public boolean initialStart (YamlConfiguration conf, Plugin plugin) {

        this.globalConfig = conf;
        this.plugin = plugin;

        boolean useMySQL = globalConfig.getBoolean("MySQL");
        plugin.getLogger().warning(String.valueOf(useMySQL));
        plugin.getLogger().warning(globalConfig.getString("tittest"));
        boolean initialized = false;

        if (useMySQL) {
            initialized = initialDatabase(true);
        }

        if (!initialized) {
            plugin.getLogger().warning("USING SQL LITE");
            initialized = initialDatabase(false);
        }

        if (initialized) {
            plugin.getLogger().info("INITIAL " + (useMySQL ? "MYSQL" : "SQL LITE") + " SUCCESS");
            return true;
        } else {
            plugin.getLogger().warning("SQL LITE ERROR --- DISABLING PLUGIN");
            return false;
        }
    }
    private boolean initialSQLExecute(Connection connection, boolean databaseType) throws IOException, SQLException {
        // Otwieranie pliku ze skryptem SQL
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream((databaseType ? "First.sql" : "FirstLite.sql"));
            plugin.getLogger().info("1");
            assert inputStream != null;
            plugin.getLogger().info("2");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            plugin.getLogger().info("3");

            StringBuilder query = new StringBuilder();
            plugin.getLogger().info("4");
            String line;
            plugin.getLogger().info("5");
            while ((line = reader.readLine()) != null) {
                plugin.getLogger().info("A");
                // Sprawdzamy, czy linia jest pusta lub jest komentarzem.
                if (!line.trim().isEmpty() && !line.trim().startsWith("--")) {
                    plugin.getLogger().info("B");
                    query.append(line).append(" ");
                    plugin.getLogger().info("C");
                    // Jeśli linia kończy się średnikiem, wykonujemy zapytanie SQL
                    if (line.trim().endsWith(";")) {
                        plugin.getLogger().info("D");
                        String singleQuery = query.toString().trim();
                        plugin.getLogger().info("E");
                        // Wykonanie pojedynczego zapytania SQL
                        Statement statement = connection.createStatement();
                        plugin.getLogger().info("F");
                        statement.execute(singleQuery);
                        plugin.getLogger().info("G");
                        statement.close();
                        plugin.getLogger().info("H");
                        query.setLength(0);
                        plugin.getLogger().info("I");
                    }
                }
            }

            // Zamknięcie połączenia i zasobów
            reader.close();
        } catch (IOException e) {
            plugin.getLogger().warning("[FJ] INITIAL SQL EXECUTE IO ERROR" + e);
            return false;
        } catch (SQLException e) {
            plugin.getLogger().warning("[FJ] INITIAL SQL EXECUTE SQL ERROR" + e);
            return false;
        } catch (AssertionError e) {
            plugin.getLogger().warning("[FJ] INITIAL SQL EXECUTE Assertion ERROR" + e);
            return false;
        } catch (NullPointerException e) {
            plugin.getLogger().warning("[FJ] INITIAL SQL EXECUTE NULL POINTER ERROR" + e);
            return false;
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    plugin.getLogger().warning("Error while closing " + (databaseType ? "MYSQL" : "SQL LITE") + " connection: " + e);
                }
            } else {
                plugin.getLogger().warning("[FJ] Connection object is null before closing!");
            }
        }
        return true;
    }


    private boolean initialDatabase(boolean databaseType) {
        Connection connection = null;
        try {
            if (databaseType) {
                connection = DbUtil.getConnection(globalConfig);
                assert connection != null;
            } else{
                connection = DbUtil.getSQLiteConnection();
                assert connection != null;
            }

            if (initialSQLExecute(connection,databaseType)) {
                plugin.getLogger().info("INITIAL DATABASE " + (databaseType ? "MYSQL" : "SQL LITE") + " SUCCESS");
                return true;
            } else {
                plugin.getLogger().warning("INITIAL DATABASE " + (databaseType ? "MYSQL" : "SQL LITE") + " ERROR");
                return false;
            }
        } catch (IOException | SQLException e) {
            plugin.getLogger().warning("HJOIN DATABASE INITIAL " + (databaseType ? "MYSQL" : "SQL LITE") + " ERROR: " + e);
            return false;
        } catch (AssertionError e){
            plugin.getLogger().warning("HJOIN DATABASE INITIAL ASSERTION " + (databaseType ? "MYSQL" : "SQL LITE") + " ERROR: " + e);
            return false;
        }finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    plugin.getLogger().warning("Error while closing " + (databaseType ? "MYSQL" : "SQL LITE") + " connection: " + e);
                }
            }
        }
    }
}
