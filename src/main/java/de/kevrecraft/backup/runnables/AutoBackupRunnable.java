package de.kevrecraft.backup.runnables;

import de.kevrecraft.backup.Backup;
import de.kevrecraft.backup.BackupManager;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class AutoBackupRunnable implements Runnable {

    private final Backup plugin;

    public AutoBackupRunnable(Backup plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for(World world : Bukkit.getWorlds()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    BackupManager.backup(world);
                }
            });
        }
    }
}
