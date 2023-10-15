package de.kevrecraft.backup.utilitys;

import org.bukkit.*;
import org.bukkit.generator.ChunkGenerator;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Random;

public class WorldHandler {

    private String name;
    private File file;

    public WorldHandler(String worldName) {
        this.name = worldName;
        this.file = new File(Bukkit.getWorldContainer(), name);
    }

    public boolean unload(boolean save) {
        return Bukkit.unloadWorld(this.name, save);
    }

    public void delete() {
        unload(false);
        Utility.deleteFile(file);
    }

    public void load() {
        World existingWorld = Bukkit.getWorld(this.file.getName());

        if(existingWorld != null) {
            return;
        }

        if(this.file.getName().equalsIgnoreCase(Utility.getServerProperties("level-name") + "_nether")) {
            WorldCreator creator = new WorldCreator(this.file.getName());
            creator.environment(World.Environment.NETHER);

            World world = Bukkit.createWorld(creator);
            Bukkit.getServer().getWorlds().add(world);
        } else if(this.file.getName().equalsIgnoreCase(Utility.getServerProperties("level-name") + "_the_end")) {
            WorldCreator creator = new WorldCreator(this.file.getName());
            creator.environment(World.Environment.THE_END);

            World world = Bukkit.createWorld(creator);
            Bukkit.getServer().getWorlds().add(world);
        } else{
            World world = Bukkit.createWorld(WorldCreator.name(this.name));
            Bukkit.getServer().getWorlds().add(world);
        }
    }

    private class EmptyChunkGenerator extends ChunkGenerator {
        @Override
        @Nonnull
        public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
            return createChunkData(world);
        }
    }

    public void loadEmpty() {
        if(exist()) {
            WorldCreator wc = new WorldCreator(this.name);
            wc.generator(new EmptyChunkGenerator());
            wc.createWorld();
        } else {
            WorldCreator wc = new WorldCreator(this.name);
            wc.generator(new EmptyChunkGenerator());
            wc.createWorld();

            Location loc = new Location(Bukkit.getWorld(this.name), 0, 0, 0);
            loc.getWorld().setType(loc, Material.BEDROCK);
            loc.getWorld().setSpawnLocation(loc.add(0,1,0));
        }
    }

    public boolean exist() {
        return this.file.exists();
    }
}
