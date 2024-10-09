package pl.hazam.hjoin.CMD;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.plugin.Plugin;

import static org.bukkit.ChatColor.*;

public class CMD implements CommandExecutor {

    private final Plugin plugin;
    public CMD(Plugin plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("hj")) {
            sender.sendMessage(BOLD + " " + DARK_GREEN + "*=========================================*");
            sender.sendMessage("§l§a[HJoin] VERSJA:§r§l§5" + plugin.getDescription().getVersion());
            sender.sendMessage(BOLD + " " + GOLD + "[HJoin] AUTOR:" + RESET + " " + AQUA + plugin.getDescription().getAuthors());
            sender.sendMessage(BOLD + " " + GOLD + "[HJoin] STRONA:" + RESET + " " + AQUA + plugin.getDescription().getWebsite());
            sender.sendMessage(BOLD + " " + GOLD + "[HJoin] VERSJA API:" + RESET + " " + AQUA + plugin.getDescription().getAPIVersion());
            sender.sendMessage(BOLD + " " + DARK_GREEN + "*========================================*");
            return true;
        }
        if (command.getName().equalsIgnoreCase("hj raport")) {

            return true;
        }
        return false;
    }
}
