package pl.hazam.hjoin;

import org.bukkit.plugin.Plugin;
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

import pl.hazam.hjoin.INITPLUGIN.Init;

public class HJoin extends JavaPlugin {

//================== CONFIGI ==================
    public YamlConfiguration globalConfig;
    public YamlConfiguration config;
//============================================

    @Override
    public void onEnable() {

        getLogger().info("HJoin Enabled! Check Configuration.");

//============================= ŁADOWANIE KONFIGURACJI ==========================================
        Init.checkAndCreateConfigFiles(this); //SPRAWDZAM I TWORZĘ CONFIGI
        globalConfig = Init.loadConfigurations(this, 1); //ŁADUJE KONFIGURACJE POCZĄTKOWĄ
        config = Init.loadConfigurations(this, 2);

//=================== REJESTRACJA PLUGINU  I EVENTU ============================================
        getServer().getPluginManager().registerEvents(new Events(),this);

        try {
            //================== REJESTRACJA COMMANDE MENAGER ===============================
            Objects.requireNonNull(getCommand("hj")).setExecutor(new CMD(this));
        } catch (NullPointerException e){
            getLogger().warning("GET COMMAND ERROR" + e);
            onDisable();
        }

//======================== INICJALIZACJA BAZY DANYCH ================================
        INITIAL initial = new INITIAL();
        //boolean initialSuccess = initial.initialStart(globalConfig,this);

        if(initial.initialStart(globalConfig,this)){ //JEŻELI SIĘ POWIEDZIE
            getLogger().info("#####################");
            getLogger().info("----FJ ENABLING------");
            getLogger().info("------SUCCESS--------");
            getLogger().info("#####################");
            getLogger().info("PLUGIN VER: " + this.getConfig().getString("version"));
            getLogger().info("CONFIG VER: " + globalConfig.getString("Version"));
        }else{ //=========================================== JEŻELI SIĘ JEDNAK NIE POWIEDZIE
            getLogger().warning("DATABASE ERROR");
            getLogger().warning("DISABLING PLUGIN");
            onDisable();
        }
    }


    @Override
    public void onDisable() {

        getLogger().info("Plugin HJoin został wyłączony!");


    }

//    /** ================= CHECK AND CREATE FILE AND CONFIG ========================= **/
//    private void checkAndCreateConfigFiles() {
//        File pluginFolder = getDataFolder().getParentFile();
//
//        // Sprawdź i utwórz folder HPlugin wewnątrz folderu pluginu.
//        File hPluginFolder = new File(pluginFolder, "HPlugin");
//        if (!hPluginFolder.exists()) {
//            if (!hPluginFolder.mkdirs()){
//                getLogger().warning("ERROR on create HPlugin folder");
//                onDisable();
//            }
//        }
//
//        // Teraz utwórz folder HJoin wewnątrz folderu HPlugin.
//        File hJoinFolder = new File(hPluginFolder, "HJoin");
//        if (!hJoinFolder.exists()) {
//            if (!hJoinFolder.mkdirs()){
//                getLogger().warning("ERROR on create HPlugin folder");
//                onDisable();
//            }
//        }
//
//        // Sprawdź i utwórz plik globalconfig.yml w folderze HPlugin.
//        File globalConfigFile = new File(hPluginFolder, "globalconfig.yml");
//        if (!globalConfigFile.exists()) {
//            try {
//                // Skopiuj domyślny config do nowo utworzonego pliku.
//                copyDefaultConfigFile("globalconfig.yml", globalConfigFile);
//            } catch (IOException e) {
//                getLogger().warning("Nie można utworzyć pliku globalconfig.yml.");
//            }
//        }
//
//        // Sprawdź i utwórz plik config.yml w folderze HJoin.
//        File configFile = new File(hJoinFolder, "config.yml");
//        if (!configFile.exists()) {
//            try {
//                // Skopiuj domyślny config do nowo utworzonego pliku.
//                copyDefaultConfigFile("config.yml", configFile);
//            } catch (IOException e) {
//                getLogger().warning("Nie można utworzyć pliku config.yml.");
//            }
//        }
//    }
//
//   /** ================ COPING FILE AND CONFIG ================================== **/
//
//    private void copyDefaultConfigFile(String defaultResourcePath, File targetFile) throws IOException {
//        try (InputStream inputStream = getResource(defaultResourcePath)) {
//            if (inputStream != null) {
//                    Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//            } else {
//                getLogger().warning("Nie znaleziono domyślnego pliku konfiguracyjnego. " + defaultResourcePath);
//            }
//        } catch (IOException e) {
//            getLogger().warning("Kopiowanie nie powiodło się!");
//        }
//    }
//
//    /** ======================== LOADING CONFIGURATION ===========================**/
//    private void loadConfigurations() {
//        File globalConfigFile = new File("plugins/HPlugin/globalconfig.yml");
//        globalConfig = YamlConfiguration.loadConfiguration(globalConfigFile);
//        getLogger().info(globalConfig.getString("Version"));
//
//        File configFile = new File("HJoin/config.yml");
//        config = YamlConfiguration.loadConfiguration(configFile);
//    }
}
