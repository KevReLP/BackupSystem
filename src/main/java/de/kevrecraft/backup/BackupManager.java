package de.kevrecraft.backup;

import de.kevrecraft.backup.utilitys.Utility;
import de.kevrecraft.backup.utilitys.WorldHandler;
import de.kevrecraft.backup.utilitys.ZipHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class BackupManager {
    private static final File folder = new File("backups");

    public static File getFolder() {
        return folder;
    }

    public static ArrayList<String> getBackpsFolder() {
        ArrayList<String> list = new ArrayList<>();

        if(folder.exists() && folder.isDirectory()) {
            for(File file : folder.listFiles()) {
                if(file.isDirectory()) {
                    list.add(file.getName());
                }
            }
        }

        return list;
    }

    public static ArrayList<String> getBackups(String worldFolder) {
        ArrayList<String> list = new ArrayList<>();

        if(folder.exists() && folder.isDirectory()) {
            File worldFolderFile = new File(folder.getAbsolutePath(), worldFolder);
            if(worldFolderFile.exists() && worldFolderFile.isDirectory()) {
                for(File file : worldFolderFile.listFiles()) {
                    if(file.getName().endsWith(".zip")) {
                        list.add(file.getName().replace(".zip", ""));
                    }
                }
            }

        }

        return list;
    }

    private static File getFile(World world) {
        File dir = new File(folder + "/" + world.getName());
        dir.mkdirs();
        return new File(dir.getAbsolutePath(), world.getName());
    }

    private static File getZipFile(World world) {
        return new File(getFile(world).getParent(), Utility.getTime() + ".zip");
    }

    public static boolean backup(World world) {
        if(world == null) {
            return false;
        }

        try {
            copy(world.getWorldFolder(), getFile(world));
            ZipHandler zh = new ZipHandler(getZipFile(world));
            zh.zip(getFile(world), true);
            Utility.deleteFile(getFile(world));
            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    private static ArrayList<String> isLoading = new ArrayList<>();

    public static boolean isLoading(String worldName) {
        return isLoading.contains(worldName);
    }
    private static HashMap<UUID, Location> playerLocs = new HashMap<>();

    public static void load(String worldName, String zipName) {
        isLoading.add(worldName);
        for (Player player : Bukkit.getOnlinePlayers()){
            if(player.getWorld().getName().equals(worldName)) {
                playerLocs.put(player.getUniqueId(), player.getLocation());
                player.teleport(new Location(Bukkit.getWorld("load_world"), 0.5, 1 ,0.5));
            }
        }

        File zipFile = new File(folder + "/" + worldName, zipName + ".zip");
        WorldHandler wh = new WorldHandler(worldName);
        if(wh.exist()) {
            System.out.println("Welt " + worldName + " wird entladen...");
            wh.unload(false);
            wh.delete();
        }
        ZipHandler zh = new ZipHandler(zipFile);
        try {
            zh.unzip(Bukkit.getWorldContainer(), true);
            wh.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        isLoading.remove(worldName);
        for (UUID uuid : playerLocs.keySet()){
            if(playerLocs.get(uuid).getWorld().getName().equals(worldName)) {
                Bukkit.getPlayer(uuid).teleport(playerLocs.get(uuid));
            }
        }
    }

    private static void copy(File source, File target) throws IOException {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.dat", "session.lock"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                        target.mkdirs();
                    String files[] = source.list();
                    for (String file : files) {
                        copy(new File(source, file), new File(target, file));
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.close();
                }
            }
    }
}
