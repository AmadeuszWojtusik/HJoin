package pl.hazam.hjoin.Database.INITIAL;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import pl.hazam.hjoin.Database.DbUtil.DbUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
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
    private boolean initialSQLExecute(Connection connection) throws IOException, SQLException {
        // Otwieranie pliku ze skryptem SQL
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("First.sql");
            assert inputStream != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


            StringBuilder query = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                // Sprawdzamy, czy linia jest pusta lub jest komentarzem.
                if (!line.trim().isEmpty() && !line.trim().startsWith("--")) {
                    query.append(line).append(" ");
                    // Jeśli linia kończy się średnikiem, wykonujemy zapytanie SQL
                    if (line.trim().endsWith(";")) {
                        String singleQuery = query.toString().trim();
                        // Wykonanie pojedynczego zapytania SQL
                        Statement statement = connection.createStatement();
                        statement.execute(singleQuery);
                        statement.close();
                        query.setLength(0);
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
            try {
                connection.close();
            } catch (SQLException e) {
                plugin.getLogger().warning("[FJ] INITIAL SQL EXECUTE CONNECTION CLOSE ERROR" + e);
            }
        }
        return true;
    }

    private boolean initialDatabase(boolean databaseType) {
        Connection connection = null;
        try {
            if (databaseType) {
                connection = DbUtil.getConnection(globalConfig);
            } else{
                connection = DbUtil.getSQLiteConnection();
            }

            if (connection != null && initialSQLExecute(connection)) {
                plugin.getLogger().info("INITIAL DATABASE " + (databaseType ? "MYSQL" : "SQL LITE") + " SUCCESS");
                return true;
            } else {
                plugin.getLogger().warning("INITIAL DATABASE " + (databaseType ? "MYSQL" : "SQL LITE") + " ERROR");
                return false;
            }
        } catch (IOException | SQLException e) {
            plugin.getLogger().warning("HJOIN DATABASE INITIAL " + (databaseType ? "MYSQL" : "SQL LITE") + " ERROR: " + e);
            return false;
        } finally {
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
