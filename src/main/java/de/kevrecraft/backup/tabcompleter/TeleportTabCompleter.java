package de.kevrecraft.backup.tabcompleter;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 1) {
            ArrayList<String> welten = new ArrayList();
            welten.add("help");
            for (World w : Bukkit.getWorlds()) {
                welten.add(w.getName());
            }
            return welten;
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("help"))
                return new ArrayList<>();

            ArrayList<String> players = new ArrayList();
            for (Player p : Bukkit.getOnlinePlayers()) {
                players.add(p.getName());
            }
            return players;
        }

        return null;
    }
}
