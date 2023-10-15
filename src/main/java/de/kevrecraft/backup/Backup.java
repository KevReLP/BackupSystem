package de.kevrecraft.backup;

import de.kevrecraft.backup.commands.BackupCommand;
import de.kevrecraft.backup.commands.TeleportCommand;
import de.kevrecraft.backup.tabcompleter.BackupTabCompleter;
import de.kevrecraft.backup.tabcompleter.TeleportTabCompleter;
import de.kevrecraft.backup.utilitys.WorldHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Backup extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getCommand("backup").setExecutor(new BackupCommand(this));
        getCommand("backup").setTabCompleter(new BackupTabCompleter());
        getCommand("teleport").setExecutor(new TeleportCommand());
        getCommand("teleport").setTabCompleter(new TeleportTabCompleter());

        WorldHandler wh = new WorldHandler("load_world");
        wh.loadEmpty();
    }

    @Override
    public void onDisable() {

    }
}
