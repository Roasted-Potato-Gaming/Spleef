package com.roastedpotatogaming.spleef;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class Spleef extends JavaPlugin {

    public ConfigurationSection arenas;

    @Override
    public void onDisable() {}

    @Override
    public void onEnable() {
        saveDefaultConfig();
        arenas = getConfig().getConfigurationSection("arenas");
        saveConfig();
        getCommand("spleef").setExecutor(new SpleefCommand(this));
        getServer().getPluginManager().registerEvents(new SpleefListener(this), this);
    }

    public void saveLocation(Location location, String arena) {
        arenas.set(arena + ".x", location.getBlockX());
        arenas.set(arena + ".y", location.getBlockY());
        arenas.set(arena + ".z", location.getBlockZ());
        arenas.set(arena + ".yaw", (int) location.getYaw());
        arenas.set(arena + ".pitch", (int) location.getPitch());
        arenas.set(arena + ".world", location.getWorld().getName());
        saveConfig();
    }

    public Location getLocation(String arena) {
        Location location = new Location(Bukkit.getWorld(arenas.getString(arena + ".world")),
                arenas.getInt(arena + ".x"),
                arenas.getInt(arena + ".y"),
                arenas.getInt(arena + ".z"),
                arenas.getInt(arena + ".yaw"),
                arenas.getInt(arena + ".pitch"));
        return location;
    }
}
