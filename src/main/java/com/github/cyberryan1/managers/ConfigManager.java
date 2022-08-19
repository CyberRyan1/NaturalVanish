package com.github.cyberryan1.managers;

import com.github.cyberryan1.NaturalVanish;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 * @deprecated
 */
public class ConfigManager {

    public NaturalVanish plugin;

    private FileConfiguration dataConfig;
    private File configFile;


    public ConfigManager( NaturalVanish pl ) {
        this.plugin = pl;
        saveDefaultConfig();
    }



    public void reloadConfig() {
        if ( this.configFile == null ) {
            this.configFile = new File( this.plugin.getDataFolder(), "config.yml" );
        }

        this.dataConfig = YamlConfiguration.loadConfiguration( this.configFile );

        InputStream defaultStream = this.plugin.getResource( "config.yml" );
        if ( defaultStream != null ) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration( new InputStreamReader( defaultStream ) );
            this.dataConfig.setDefaults( defaultConfig );
        }
    }



    public FileConfiguration getConfig() {
        if ( this.dataConfig == null ) {
            reloadConfig();
        }

        return this.dataConfig;
    }



    public File getConfigFile() {
        return configFile;
    }



    public void saveConfig() {
        if ( this.dataConfig == null || this.configFile == null ) {
            plugin.getLogger().log( Level.WARNING, "dataConfig == null || configFile == null, returning " );
            return;
        }

        plugin.saveConfig();
    }



    public void saveDefaultConfig() {
        if ( this.configFile == null ) {
            this.configFile = new File( this.plugin.getDataFolder(), "config.yml" );
        }

        if ( ! this.configFile.exists() ) {
            this.plugin.saveResource( "config.yml", false );
        }
    }
}
