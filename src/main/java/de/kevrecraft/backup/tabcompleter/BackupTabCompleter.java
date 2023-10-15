package de.kevrecraft.backup.tabcompleter;

import de.kevrecraft.backup.BackupManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class BackupTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 1) {
            ArrayList<String> list = new ArrayList<>();
            list.add("help");
            list.add("save");
            list.add("load");
            return list;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("save")) {
                ArrayList<String> list = new ArrayList<>();
                for(World world : Bukkit.getWorlds()) {
                    list.add(world.getName());
                }
                return list;
            } else if (args[0].equalsIgnoreCase("load")) {
                return BackupManager.getBackpsFolder();
            }

        } else if (args.length == 3) {
            if(args[0].equalsIgnoreCase("load"))
                return BackupManager.getBackups(args[1]);
        }

        return new ArrayList<>();
    }
}
