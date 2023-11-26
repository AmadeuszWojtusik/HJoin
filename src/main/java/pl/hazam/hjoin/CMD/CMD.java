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
            sender.sendMessage(ChatColor.BOLD + " " + ChatColor.DARK_PURPLE + "*=======================================================*");
            sender.sendMessage(ChatColor.BOLD + " " + ChatColor.DARK_PURPLE + "==== [HJoin] VERSJA:" + ChatColor.RESET + " " + ChatColor.AQUA + plugin.getConfig().getString("version"));
            sender.sendMessage(ChatColor.BOLD + " " + ChatColor.DARK_PURPLE + "==== [HJoin] AUTOR:" + ChatColor.RESET + " " + ChatColor.AQUA + plugin.getConfig().getString("author"));
            sender.sendMessage(ChatColor.BOLD + " " + ChatColor.DARK_PURPLE + "==== [HJoin] STRONA:" + ChatColor.RESET + " " + ChatColor.AQUA + plugin.getConfig().getString("website"));
            sender.sendMessage(ChatColor.BOLD + " " + ChatColor.DARK_PURPLE + "==== [HJoin] VERSJA API:" + ChatColor.RESET + " " + ChatColor.AQUA + plugin.getConfig().getString("api-version"));
            sender.sendMessage(ChatColor.BOLD + " " + ChatColor.DARK_PURPLE + "*=======================================================*");
            return true;
        }
        return false;
    }
}
