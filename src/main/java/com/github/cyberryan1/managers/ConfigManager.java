package com.github.cyberryan1.managers;

import com.github.cyberryan1.NaturalVanish;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private NaturalVanish plugin;
    private FileConfiguration config = null;

    public ConfigManager( NaturalVanish pl ) {
        plugin = pl;
    }



    public void reloadConfig() {
        if ( config == null ) {
            config = plugin.getConfig();
        }

        plugin.saveDefaultConfig();
        plugin.reloadConfig();
    }



    public FileConfiguration getConfig() {
        if ( config == null ) {
            reloadConfig();
        }

        return config;
    }
}
