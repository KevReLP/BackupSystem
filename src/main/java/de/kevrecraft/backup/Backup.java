package de.kevrecraft.backup;

import de.kevrecraft.backup.commands.BackupCommand;
import de.kevrecraft.backup.commands.TeleportCommand;
import de.kevrecraft.backup.runnables.AutoBackupRunnable;
import de.kevrecraft.backup.tabcompleter.BackupTabCompleter;
import de.kevrecraft.backup.tabcompleter.TeleportTabCompleter;
import de.kevrecraft.backup.utilitys.WorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Backup extends JavaPlugin implements Listener {

    private int autobackupID = -1;
    private long lastBackupTime;


    private void setAutoBackupTimer(long start, long timer) {
        if(autobackupID != -1) {
            Bukkit.getScheduler().cancelTask(autobackupID);
        }
        autobackupID = Bukkit.getScheduler().runTaskTimerAsynchronously(this, new AutoBackupRunnable(this), start, timer).getTaskId();
        lastBackupTime = System.currentTimeMillis();
    }

    public void setAutobackupTimer(long timer) {
        if(autobackupID != -1) {
            Bukkit.getScheduler().cancelTask(autobackupID);
        }
        long start = System.currentTimeMillis() - lastBackupTime;
        start = start / 1000 * 20;

        if(start >= timer) {
            autobackupID = Bukkit.getScheduler().runTaskTimerAsynchronously(this, new AutoBackupRunnable(this), 0, timer).getTaskId();
        } else {
            autobackupID = Bukkit.getScheduler().runTaskTimerAsynchronously(this, new AutoBackupRunnable(this), timer-start, timer).getTaskId();
        }
        lastBackupTime = System.currentTimeMillis();
    }


    @Override
    public void onEnable() {
        saveDefaultConfig();

        getCommand("backup").setExecutor(new BackupCommand(this));
        getCommand("backup").setTabCompleter(new BackupTabCompleter());
        getCommand("teleport").setExecutor(new TeleportCommand());
        getCommand("teleport").setTabCompleter(new TeleportTabCompleter());

        WorldHandler wh = new WorldHandler("load_world");
        wh.loadEmpty();

        Long time = getConfig().getLong("autobackup.s") * 20l;
        time += getConfig().getLong("autobackup.m") * 20l * 60;
        time += getConfig().getLong("autobackup.h") * 20l * 60 * 60;
        setAutoBackupTimer(0l, time);
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }
}
