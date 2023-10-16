package de.kevrecraft.backup.utilitys;

import org.bukkit.*;
import org.bukkit.generator.ChunkGenerator;

import javax.annotation.Nonnull;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Random;

public class Utility {
    public static String getTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static void deleteFile(File dir) {
        for(File f : dir.listFiles()) {
            if(f.isDirectory()) {
                deleteFile(f);
            }
            f.delete();
        }
        dir.delete();
    }

    public static String getServerProperties(String property) {
        String value = Bukkit.getWorldContainer().toPath().resolve("server.properties").toString();
        try (InputStream input = new FileInputStream(value)) {
            Properties properties = new Properties();
            properties.load(input);
            return properties.getProperty(property);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isInt(String string) {
        for (char c : string.toCharArray()) {
            if (!isInt(c))
                return false;
        }
        return true;
    }

    public static boolean isInt(char c) {
        switch (c) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return true;
        }
        return false;
    }


}
