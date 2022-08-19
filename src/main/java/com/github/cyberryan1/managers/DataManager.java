package com.github.cyberryan1.managers;

import com.github.cyberryan1.NaturalVanish;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 * @deprecated
 */
public class DataManager {

    private NaturalVanish plugin;
    private FileConfiguration dataConfig = null;
    private File dataFile = null;



    public DataManager( NaturalVanish pl ) {
        this.plugin = pl;
        saveDefaultConfig();
    }



    public void reloadConfig() {
        if ( dataFile == null ) {
            dataFile = new File( plugin.getDataFolder(), "data.yml" );
        }

        dataConfig = YamlConfiguration.loadConfiguration( dataFile );

        InputStream defaultStream = plugin.getResource( "data.yml" );
        if ( defaultStream != null ) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration( new InputStreamReader( defaultStream ) );
            dataConfig.setDefaults( defaultConfig );
        }
    }



    public FileConfiguration getConfig() {
        if ( dataConfig == null ) {
            reloadConfig();
        }

        return dataConfig;
    }



    public void saveConfig() {
        if ( dataConfig == null || dataFile == null ) {
            return;
        }

        try {
            getConfig().save( dataFile );
        } catch ( IOException e ) {
            plugin.getLogger().log( Level.SEVERE, "Could not save data config to " + dataFile, e );
        }
    }



    public void saveDefaultConfig() {
        if ( dataFile == null ) {
            dataFile = new File( plugin.getDataFolder(), "data.yml" );
        }

        if ( ! dataFile.exists() ) {
            plugin.saveResource( "data.yml", false );
        }
    }
}
