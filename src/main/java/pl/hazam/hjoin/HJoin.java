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
            //================== REJESTRACJA COMMANDE MENAGER ==================================
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
            getLogger().warning("---------DATABASE ERROR---------");
            getLogger().warning("---------DISABLING PLUGIN HJ---------");
            onDisable();
        }
    }


    @Override
    public void onDisable() {

        getLogger().info("Plugin HJoin został wyłączony!");


    }
}
