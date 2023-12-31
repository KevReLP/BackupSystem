package de.kevrecraft.backup.commands;

import de.kevrecraft.backup.BackupManager;
import de.kevrecraft.backup.utilitys.Utility;
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

        if (help(sender, args))
            return true;
        if (save(sender, args))
            return true;
        if (load(sender, args))
            return true;
        if (autobackup(sender, args))
            return true;

        sender.sendMessage(ChatColor.RED + "Fehler: Benutze " + ChatColor.GRAY + "/backup help" + ChatColor.RED + " für eine Hilfe!");
        return true;
    }

    private boolean save(CommandSender sender, String[] args) {
        if (args.length != 2)
            return false;
        if(args[0].equalsIgnoreCase("save"))
            return false;

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

    private boolean load(CommandSender sender, String[] args) {
        if(args.length != 3)
            return false;
        if(!args[0].equalsIgnoreCase("load"))
            return false;

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

    private boolean autobackup(CommandSender sender, String[] args) {
        if(args.length==0)
            return false;
        if (!args[0].equalsIgnoreCase("autobackup"))
            return false;
        if(args.length == 1) {
            if(pluigin.getConfig().getBoolean("autobackup.enabled")) {
                sender.sendMessage(ChatColor.GREEN + "Es werden alle " +
                        pluigin.getConfig().getInt("autobackup.h") + " Stunden " +
                        pluigin.getConfig().getInt("autobackup.m") + " Minuten und " +
                        pluigin.getConfig().getInt("autobackup.s") + " Sekunden durchgeführt!");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Autobackups sind nicht Aktiviert!");
                return true;
            }
        }
        if (args.length < 4)
            return false;
        if (args[1].equalsIgnoreCase("set"))
            if (args[2].equalsIgnoreCase("timer")) {
                if (Utility.isInt(args[3])) {
                    if (args.length == 4) {
                        long time = Long.parseLong(args[3]) * 60 * 60 * 20;
                        pluigin.setAutobackupTimer(time);
                        pluigin.getConfig().set("autobackup.h", Integer.parseInt(args[3]));
                        sender.sendMessage("Der Autobackuptimer wurde auf " + args[3] + " geändert!");
                        return true;
                    }

                    if (Utility.isInt(args[4])) {
                        if (args.length == 5) {
                            long time = Long.parseLong(args[3]) * 60 * 60 * 20;
                            time += Long.parseLong(args[4]) * 60 * 20;
                            pluigin.setAutobackupTimer(time);
                            pluigin.getConfig().set("autobackup.h", Integer.parseInt(args[3]));
                            pluigin.getConfig().set("autobackup.m", Integer.parseInt(args[4]));
                            sender.sendMessage("Der Autobackuptimer wurde auf " + args[3] + "h und " + args[4] + "m geändert!");
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Die eingegebene Minutenzahl " + args[4] + " ist keine Gültige Zahl!");
                        return true;
                    }

                    if (Utility.isInt(args[5])) {
                        if (args.length == 6) {
                            long time = Long.parseLong(args[3]) * 60 * 60 * 20;
                            time += Long.parseLong(args[4]) * 60 * 20;
                            time += Long.parseLong(args[5]) * 20;
                            pluigin.setAutobackupTimer(time);
                            pluigin.getConfig().set("autobackup.h", Integer.parseInt(args[3]));
                            pluigin.getConfig().set("autobackup.m", Integer.parseInt(args[4]));
                            pluigin.getConfig().set("autobackup.s", Integer.parseInt(args[5]));
                            sender.sendMessage("Der Autobackuptimer wurde auf " + args[3] + "h, " + args[4] + "m und " + args[5] + "s geändert!");
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Die eingegebene Sekundenzahl " + args[5] + " ist keine Gültige Zahl!");
                        return true;
                    }
                }
            } else if(args[2].equalsIgnoreCase("enabled")) {
                if (args.length == 4) {
                    if(args[3].equalsIgnoreCase("true")) {
                        pluigin.getConfig().set("autobackup.enabled", true);
                        sender.sendMessage(ChatColor.GREEN + "Du hast das Automatische backup " + ChatColor.DARK_GREEN + "Aktiviert" + ChatColor.GREEN + "!");
                        return true;
                    } else if(args[3].equalsIgnoreCase("false")){
                        pluigin.getConfig().set("autobackup.enabled", false);
                        sender.sendMessage(ChatColor.GREEN + "Du hast das Automatische backup " + ChatColor.RED + "Deaktiviert" + ChatColor.GREEN + "!");
                        return true;
                    }
                }
            }
        return false;
    }

    private boolean help(CommandSender sender, String[] args) {
        if (args.length!= 1)
            return false;
        if(!args[0].equalsIgnoreCase("help"))
            return false;

        String color = ChatColor.BLUE + "";
        String commandColor = ChatColor.GRAY + "";

        sender.sendMessage(color + "" + ChatColor.BOLD + "---- Backup Hilfe: ----");
        if(sender instanceof Player) {
            sender.sendMessage( commandColor + "/backup" + color + " -> Startet ein Backup für die Welt, in der du dich gerade befindest.");
        }
        sender.sendMessage( commandColor + "/backup save <world>" + color + " -> Startet ein Backup für die Welt, die du angegeben hast.");
        sender.sendMessage( commandColor + "/backup load <world> <date>" + color + " -> Ladet ein entsprechendes backup...");
        sender.sendMessage( commandColor + "/backup autobackup" + color + " -> Erhalte eine Infos zu Autobackup.");
        sender.sendMessage( commandColor + "/backup autobackup set timer <h> <m> <s>" + color + " -> Setzt die entsprechende intervalle der Zeit...");
        sender.sendMessage( commandColor + "/backup autobackup set enabled <true/false>" + color + " -> Aktiviert/Deaktiviert das automatische backup");
        return true;
    }
}