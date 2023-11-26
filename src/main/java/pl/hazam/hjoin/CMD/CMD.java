package pl.hazam.hjoin.CMD;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.plugin.Plugin;
public class CMD implements CommandExecutor {

    private final Plugin plugin;
    public CMD(Plugin plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("hj")) {
            sender.sendMessage(ChatColor.BOLD + " " + ChatColor.DARK_GREEN + "*=========================================*");
            sender.sendMessage(ChatColor.BOLD + " " + ChatColor.GOLD + "[HJoin] VERSJA:" + ChatColor.RESET + " " + ChatColor.AQUA + plugin.getDescription().getVersion());
            sender.sendMessage(ChatColor.BOLD + " " + ChatColor.GOLD + "[HJoin] AUTOR:" + ChatColor.RESET + " " + ChatColor.AQUA + plugin.getDescription().getAuthors());
            sender.sendMessage(ChatColor.BOLD + " " + ChatColor.GOLD + "[HJoin] STRONA:" + ChatColor.RESET + " " + ChatColor.AQUA + plugin.getDescription().getWebsite());
            sender.sendMessage(ChatColor.BOLD + " " + ChatColor.GOLD + "[HJoin] VERSJA API:" + ChatColor.RESET + " " + ChatColor.AQUA + plugin.getDescription().getAPIVersion());
            sender.sendMessage(ChatColor.BOLD + " " + ChatColor.DARK_GREEN + "*========================================*");
            return true;
        }
        return false;
    }
}
