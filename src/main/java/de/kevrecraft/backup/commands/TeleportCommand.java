package de.kevrecraft.backup.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length == 1) {
                World world = Bukkit.getWorld(args[0]);
                if(world != null) {
                    player.teleport(new Location(world, 0.5, 0, 0.5));
                    sender.sendMessage(ChatColor.GREEN + "Du wurdest in die Welt " + args[0] + " teleportiert!");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Die eingegebene Welt " + args[0] + " existiert nicht!");
                    return true;
                }
            }
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("help")) {
                sendHelp(sender);
                return true;
            }
        } else if (args.length == 2) {
            World world = Bukkit.getWorld(args[0]);
            if(world != null) {
                Player player = Bukkit.getPlayer(args[1]);
                if(player != null) {
                    player.teleport(new Location(world, 0.5, 0, 0.5));
                    sender.sendMessage(ChatColor.GREEN + "Du hast " + args[1] + " in die Welt " + args[0] + " teleportiert!");
                    player.sendMessage(ChatColor.GREEN + "Du wurdest in die Welt " + args[0] + " teleportiert!");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Der eingegebene Spieler " + args[1] + "konnte nicht gefunden werden!");
                    return true;
                }

            } else {
                sender.sendMessage(ChatColor.RED + "Die eingegebene Welt " + args[0] + " existiert nicht!");
                return true;
            }
        }


        sender.sendMessage(ChatColor.RED + "Fehler: Benutze " +ChatColor.GRAY + "/telpeort help"  + ChatColor.RED + " für eine Hilfestellung");
        return true;
    }

    private void sendHelp(CommandSender sender) {
        String color = ChatColor.BLUE + "";
        String commandColor = ChatColor.GRAY + "";

        sender.sendMessage(color + "" + ChatColor.BOLD + "---- Teleport Hilfe: ----");
        if(sender instanceof Player) {
            sender.sendMessage( commandColor + "/teleport <world>" + color + " -> Teleportiert dich in die Welt.");
        }



    }
}
