package de.kevrecraft.backup.commands;

import de.kevrecraft.backup.BackupManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class BackupCommand implements CommandExecutor {
    private final de.kevrecraft.backup.Backup pluigin;

    public BackupCommand(de.kevrecraft.backup.Backup plugin) {
        this.pluigin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length == 0) {
                String worldName = player.getWorld().getName();
                sender.sendMessage(ChatColor.YELLOW + "Backup von " + worldName + " wird erstellt ...");
                Bukkit.getScheduler().runTaskAsynchronously(pluigin, new Runnable() {
                    @Override
                    public void run() {
                        if(BackupManager.backup(player.getWorld())) {
                            sender.sendMessage(ChatColor.GREEN + "Das Backup " + worldName + " wurde erfolgreich erstellt!");
                        } else {
                            sender.sendMessage(ChatColor.RED + "Es ist ein Fehler aufgetreten! Die Welt " + worldName + " konnte nicht gespeichert werden!");
                        }
                    }
                });
                return true;
            }
        }

        if (args.length == 1) {
            if(args[0].equalsIgnoreCase("help")) {
                sendHelp(sender);
                return true;
            }

        } else if (args.length == 2) {
            if(args[0].equalsIgnoreCase("save")) {
                World world = Bukkit.getWorld(args[1]);
                if(world != null) {
                    sender.sendMessage(ChatColor.YELLOW + "Backup von " + args[1] + " wird erstellt ...");
                    Bukkit.getScheduler().runTaskAsynchronously(pluigin, new Runnable() {
                        @Override
                        public void run() {
                            if(BackupManager.backup(world)) {
                                sender.sendMessage(ChatColor.GREEN + "Das Backup " + args[1] + " wurde erfolgreich erstellt!");
                            } else {
                                sender.sendMessage(ChatColor.RED + "Es ist ein Fehler aufgetreten! Die Welt " + args[1] + " konnte nicht gespeichert werden!");
                            }
                        }
                    });
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Die eingegebene Welt " + args[1] + " ist keine geladene Welt!");
                    return true;
                }
            }
        } else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("load")) {
                if(BackupManager.getFolder().exists() && BackupManager.getFolder().isDirectory()) {
                    File backupFile = new File(BackupManager.getFolder().getAbsolutePath(), args[1]);
                    if(backupFile.exists() && backupFile.isDirectory()) {
                        File zipFile = new File(backupFile.getAbsolutePath(), args[2] + ".zip");
                        if(zipFile.exists() && !zipFile.isDirectory()) {
                            BackupManager.load(args[1], args[2]);
                            sender.sendMessage(ChatColor.GREEN + "Die Welt " + args[2] + " wurde erfolgreich geladen!");
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.RED + "Die eingegebene Backupdatei " + args[2] + " konnte nicht gefunden werden!");
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Konnte den WeltenOrdner " +args[1] + " nicht finden!");
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Es exestieren keine Backups!");
                    return true;
                }
            }
        }

        sender.sendMessage(ChatColor.RED + "Fehler: Benutze " + ChatColor.GRAY + "/backup help" + ChatColor.RED + " für eine Hilfe!");
        return true;
    }

    private void sendHelp(CommandSender sender) {
        String color = ChatColor.BLUE + "";
        String commandColor = ChatColor.GRAY + "";

        sender.sendMessage(color + "" + ChatColor.BOLD + "---- Backup Hilfe: ----");
        if(sender instanceof Player) {
            sender.sendMessage( commandColor + "/backup" + color + " -> Startet ein Backup für die Welt, in der du dich gerade befindest.");
        }
        sender.sendMessage( commandColor + "/backup save <world>" + color + " -> Startet ein Backup für die Welt, die du angegeben hast.");
        sender.sendMessage( commandColor + "/backup load <world> <date>" + color + " -> Ladet ein entsprechendes backup...");
    }
}