package pl.hazam.hjoin.INITPLUGIN;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.hazam.hjoin.CMD.CMD;
import pl.hazam.hjoin.Database.INITIAL.INITIAL;
import pl.hazam.hjoin.Listener.Events;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.util.Objects;


public class Init {
    /** ================= CHECK AND CREATE FILE AND CONFIG ========================= **/
    public static void checkAndCreateConfigFiles(Plugin plugin) {
        File pluginFolder = plugin.getDataFolder().getParentFile();

        // Sprawdź i utwórz folder HPlugin wewnątrz folderu pluginu.
        File hPluginFolder = new File(pluginFolder, "HPlugin");
        if (!hPluginFolder.exists()) {
            if (!hPluginFolder.mkdirs()){
                plugin.getLogger().warning("ERROR on create HPlugin folder");
                plugin.onDisable();
            }
        }

        // Teraz utwórz folder HJoin wewnątrz folderu HPlugin.
        File hJoinFolder = new File(hPluginFolder, "HJoin");
        if (!hJoinFolder.exists()) {
            if (!hJoinFolder.mkdirs()){
                plugin.getLogger().warning("ERROR on create HPlugin folder");
                plugin.onDisable();
            }
        }

        // Sprawdź i utwórz plik globalconfig.yml w folderze HPlugin.
        File globalConfigFile = new File(hPluginFolder, "globalconfig.yml");
        if (!globalConfigFile.exists()) {
            try {
                // Skopiuj domyślny config do nowo utworzonego pliku.
                copyDefaultConfigFile("globalconfig.yml", globalConfigFile, plugin);
            } catch (IOException e) {
                plugin.getLogger().warning("Nie można utworzyć pliku globalconfig.yml.");
            }
        }

        // Sprawdź i utwórz plik config.yml w folderze HJoin.
        File configFile = new File(hJoinFolder, "config.yml");
        if (!configFile.exists()) {
            try {
                // Skopiuj domyślny config do nowo utworzonego pliku.
                copyDefaultConfigFile("config.yml", configFile, plugin);
            } catch (IOException e) {
                plugin.getLogger().warning("Nie można utworzyć pliku config.yml.");
            }
        }
    }

    /** ================ COPING FILE AND CONFIG ================================== **/

    public static void copyDefaultConfigFile(String defaultResourcePath, File targetFile, Plugin plugin) throws IOException {
        try (InputStream inputStream = plugin.getResource(defaultResourcePath)) {
            if (inputStream != null) {
                Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                plugin.getLogger().warning("Nie znaleziono domyślnego pliku konfiguracyjnego. " + defaultResourcePath);
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Kopiowanie nie powiodło się!");
        }
    }

    /** ======================== LOADING CONFIGURATION ===========================**/
    public static void loadConfigurations(Plugin plugin, YamlConfiguration globalConfig, YamlConfiguration config) {
        File globalConfigFile = new File("plugins/HPlugin/globalconfig.yml");
        globalConfig = YamlConfiguration.loadConfiguration(globalConfigFile);
        plugin.getLogger().info(globalConfig.getString("Version"));

        File configFile = new File("HJoin/config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}
