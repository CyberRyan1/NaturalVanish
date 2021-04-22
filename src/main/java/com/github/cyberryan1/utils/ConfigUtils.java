package com.github.cyberryan1.utils;

import com.github.cyberryan1.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ConfigUtils {

    private static ConfigManager config;

    public ConfigUtils( ConfigManager con ) {
        config = con;
    }



    // returns the config of the ConfigManager
    public static FileConfiguration getConfig() {
        return config.getConfig();
    }


    // returns the ConfigManager
    public static ConfigManager getConfigManager() {
        return config;
    }



    // returns a boolean value from the path
    public static boolean getBool( String path ) {
        checkWarn( path );
        return config.getConfig().getBoolean( path );
    }


    // returns an int from the path
    public static int getInt( String path ) {
        checkWarn( path );
        return config.getConfig().getInt( path );
    }


    // returns a float from the path
    public static float getFloat( String path ) {
        checkWarn( path );
        return ( float ) config.getConfig().getDouble( path );
    }


    // returns a string from the path
    public static String getStr( String path ) {
        checkWarn( path );
        return config.getConfig().getString( path );
    }



    // returns a colored string from the path
    public static String getColoredStr( String path ) {
        if ( config.getConfig().getString( path ) == null ) { return null; }
        checkWarn( path );
        return ChatColor.translateAlternateColorCodes( '&', config.getConfig().getString( path ) );
    }


    // returns a colored string from the path, replacing all [PLAYER] variables as needed
    public static String getColoredStr( String path, CommandSender sender ) {
        if ( getColoredStr( path ) == null ) { return null; }
        return getColoredStr( path ).replace( "[PLAYER]", sender.getName() );
    }


    // returns a colored string from the path, replacing all [PLAYER] and [ARG] variables as needed
    public static String getColoredStr( String path, CommandSender sender, Player target ) {
        if ( getColoredStr( path ) == null ) { return null; }
        return getColoredStr( path, sender ).replace( "[ARG]", target.getName() );
    }


    // returns a colored string from the path, replacing all [PLAYER] and [ARG] variables as needed
    public static String getColoredStr( String path, CommandSender sender, OfflinePlayer target ) {
        if ( getColoredStr( path ) == null || target.getName() == null ) { return null; }
        return getColoredStr( path, sender ).replace( "[ARG]", target.getName() );
    }


    // returns a colored string from the path, replacing all [PLAYER] and [ARG] variables as needed
    public static String getColoredStr( String path, CommandSender sender, String arg ) {
        if ( getColoredStr( path ) == null ) { return null; }
        return getColoredStr( path, sender ).replace( "[ARG]", arg );
    }



    // sends a console warn if a path returns null
    private static void checkWarn( String path ) {
        if ( config.getConfig().get( path ) == null ) {
            Utilities.logWarn( "Config path " + path + " was not found!" );
        }
    }
}
