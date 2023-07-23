package pl.hazam.hjoin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.hazam.hjoin.CMD.CMD;
import pl.hazam.hjoin.Database.DbUtil.DbUtil;
import pl.hazam.hjoin.Database.INITIAL.INITIAL;
import pl.hazam.hjoin.Listener.Events;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class HJoin extends JavaPlugin {

    public YamlConfiguration globalConfig;
    public YamlConfiguration config;

    @Override
    public void onEnable() {

        getLogger().info("HJoin Enabled! Check Configuration.");
        checkAndCreateConfigFiles();
        loadConfigurations();

        globalConfig = getGlobalConfig();
        config = getConfig();

        getServer().getPluginManager().registerEvents(new Events(),this);
        try {
            Objects.requireNonNull(getCommand("hj")).setExecutor(new CMD());
        } catch (NullPointerException e){
            getLogger().warning("GET COMMAND ERROR" + e);
        }


        //Sprawdzam czy MYSQL
        boolean useMySQL = globalConfig.getBoolean("MySQL");
        boolean initialized = false;

        if (useMySQL) {
            initialized = initialDatabase(true);
        }

        if (!initialized) {
            getLogger().warning("USING SQL LITE");
            initialized = initialDatabase(false);
        }

        if (initialized) {
            getLogger().info("INITIAL " + (useMySQL ? "MYSQL" : "SQL LITE") + " SUCCESS");
            getLogger().info("#####################");
            getLogger().info("----FJ ENABLING------");
            getLogger().info("------SUCCESS--------");
            getLogger().info("#####################");
        } else {
            getLogger().warning("SQL LITE ERROR --- DISABLING PLUGIN");
            onDisable();
        }

//        if(globalConfig.getBoolean("MySQL")) {
//            if(initialMYSQL()){
//                getLogger().info("INITIAL MYSQL SUCCESS");
//            } else {
//                getLogger().warning("USING LIGHTSQL");
//                if (initialLIGHTSQL()){
//                    getLogger().info("INITIAL LIGHT SQL SUCCESS");
//                } else {
//                    getLogger().warning("LIGHT SQL ERROR --- DISABLING PLUGIN");
//                    onDisable();
//                }
//            }
//        } else {
//            if (initialLIGHTSQL()){
//                getLogger().info("INITIAL LIGHT SQL SUCCESS");
//            } else {
//                getLogger().warning("LIGHT SQL ERROR --- DISABLING PLUGIN");
//                onDisable();
//            }
//        }
    }


    @Override
    public void onDisable() {

        getLogger().info("Plugin HJoin został wyłączony!");


    }

/** ==================== INITIAL DATABASE ==================== **/
    //TRUE = MYSQL FALSE = SQL LITE
    private boolean initialDatabase(boolean databaseType) {
        INITIAL initial = new INITIAL();
        Connection connection = null;
        try {
            if (databaseType) {
                connection = DbUtil.getConnection(globalConfig);
            } else{
                connection = DbUtil.getSQLiteConnection();
            }

            if (connection != null && initial.INITIALSQL(connection)) {
                getLogger().info("INITIAL BODY " + (databaseType ? "MYSQL" : "SQL LITE") + " SUCCESS");
                return true;
            } else {
                getLogger().warning("INITIAL BODY " + (databaseType ? "MYSQL" : "SQL LITE") + " ERROR");
                return false;
            }
        } catch (IOException | SQLException e) {
            getLogger().warning("HJOIN BODY INITIAL " + (databaseType ? "MYSQL" : "SQL LITE") + " ERROR: " + e);
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    getLogger().warning("Error while closing " + (databaseType ? "MYSQL" : "SQL LITE") + " connection: " + e);
                }
            }
        }
    }

//    private boolean  initialSQLLITE() {
//        INITIAL initial = new INITIAL();
//        try {
//            if (!initial.INITIALSQL(DbUtil.getSQLiteConnection())){
//                getLogger().warning("INITIAL SQL LITE ERROR");
//                return false;
//            } else {
//                return true;
//            }
//        } catch (IOException | SQLException e) {
//            getLogger().warning("HJOIN BODY INITIAL SQL LITE ERROR" + e);
//            return false;
//        }
//    }
//
//    /** INICJAL MYSQL **/
//    private boolean initialMYSQL() {
//        INITIAL initial = new INITIAL();
//        try {
//            if (!initial.INITIALSQL(DbUtil.getConnection(globalConfig))){
//                getLogger().warning("INITIAL MYSQL ERROR");
//                return false;
//            } else {
//                return true;
//            }
//        } catch (IOException | SQLException e) {
//            getLogger().warning("HJOIN BODY INITIAL MYSQL ERROR" + e);
//            return false;
//        }
//    }

    /** CHECK AND CREATE FILE AND CONFIG **/
    private void checkAndCreateConfigFiles() {
        File pluginFolder = getDataFolder().getParentFile();

        // Sprawdź i utwórz folder HPlugin wewnątrz folderu pluginu.
        File hPluginFolder = new File(pluginFolder, "HPlugin");
        if (!hPluginFolder.exists()) {
            hPluginFolder.mkdirs();
        }

        // Teraz utwórz folder HJoin wewnątrz folderu HPlugin.
        File hJoinFolder = new File(hPluginFolder, "HJoin");
        if (!hJoinFolder.exists()) {
            hJoinFolder.mkdirs();
        }

        // Sprawdź i utwórz plik globalconfig.yml w folderze HPlugin.
        File globalConfigFile = new File(hPluginFolder, "globalconfig.yml");
        if (!globalConfigFile.exists()) {
            try {
                // Skopiuj domyślny config do nowo utworzonego pliku.
                copyDefaultConfigFile("globalconfig.yml", globalConfigFile);
            } catch (IOException e) {
                getLogger().warning("Nie można utworzyć pliku globalconfig.yml.");
            }
        }

        // Sprawdź i utwórz plik config.yml w folderze HJoin.
        File configFile = new File(hJoinFolder, "config.yml");
        if (!configFile.exists()) {
            try {
                // Skopiuj domyślny config do nowo utworzonego pliku.
                copyDefaultConfigFile("config.yml", configFile);
            } catch (IOException e) {
                getLogger().warning("Nie można utworzyć pliku config.yml.");
            }
        }
    }

   /** COPING FILE AND CONFIG **/
    private void copyDefaultConfigFile(String defaultResourcePath, File targetFile) throws IOException {
        try (InputStream inputStream = getResource(defaultResourcePath)) {
            if (inputStream != null) {
                Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                getLogger().warning("Nie znaleziono domyślnego pliku konfiguracyjnego. " + defaultResourcePath);
            }
        } catch (IOException e) {
            getLogger().warning("Kopiowanie nie powiodło się!");
        }
    }

    /** =========== LOADING CONFIGURATION ===========**/
    private void loadConfigurations() {
        File globalConfigFile = new File(new File(getDataFolder(), "HPlugin"), "globalconfig.yml");
        globalConfig = YamlConfiguration.loadConfiguration(globalConfigFile);

        File configFile = new File(new File(new File(getDataFolder(), "HPlugin"), "HJoin"), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

    }

    public YamlConfiguration getGlobalConfig() {
        return globalConfig;
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}
/** ================================================================= **/
