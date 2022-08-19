package com.github.cyberryan1.utils;

import com.github.cyberryan1.cybercore.utils.CoreUtils;
import com.github.cyberryan1.managers.ConfigManager;
import com.github.cyberryan1.managers.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * @deprecated
 */
public class Utilities {

    private static Plugin plugin;
    private ConfigManager config;
    private DataManager data;

    public Utilities( Plugin pl, ConfigManager con, DataManager dat ) {
        plugin = pl;
        config = con;
        data = dat;
    }



    public static Plugin getPlugin() { return plugin; }


    public static PluginManager getPluginManager() { return plugin.getServer().getPluginManager(); }



    // gets the colored value of the string
    public static String getColored( String in ) {
        return CoreUtils.getColored( in );
    }



    // logs something with severity INFO to the console
    public static void logInfo( String info ) {
        plugin.getLogger().log( Level.INFO, info );
    }


    // logs something with severity WARN to the console
    public static void logWarn( String warn ) { plugin.getLogger().log( Level.WARNING, warn ); }


    // logs something with severity ERROR to the console
    public static void logError( String severe ) { plugin.getLogger().log( Level.SEVERE, severe ); }



    // gets a player's skull
    // only works in 1.13+ servers
    public static ItemStack getPlayerHead( Player player ) {
        ItemStack skull = new ItemStack( Material.PLAYER_HEAD, 1 );
        SkullMeta meta = ( SkullMeta ) skull.getItemMeta();
        meta.setOwningPlayer( Bukkit.getOfflinePlayer( player.getUniqueId() ) );

        skull.setItemMeta( meta );
        return skull;
    }



    // checks if an index of an array is out of bounds
    // true = out of bounds, false = within bounds
    public static boolean isOutOfBounds( Object obj[], int index ) {
        try {
            Object o = obj[ index ];
        }
        catch ( IndexOutOfBoundsException e ) {
            return true;
        }

        return false;
    }



    // formats a string list into a nice string
    public static String getStringFromList( String list[] ) {
        if ( list.length == 0 ) {
            return "N/A";
        }
        else if ( list.length == 1 ) {
            return list[0];
        }
        else if ( list.length == 2 ) {
            return list[0] + " and " + list[1];
        }

        String toReturn = "";
        String listWithoutLast[] = new String[ list.length - 1 ];

        for ( int index = 0; index < listWithoutLast.length; index++ ) {
            listWithoutLast[index] = list[index];
        }

        for ( String s : listWithoutLast ) {
            toReturn += s + ", ";
        }

        toReturn += "and " + list[list.length - 1];
        return toReturn;
    }



    // formats a string ArrayList into a nice string
    public static String getStringFromList( ArrayList<String> list ) {
        if ( list.size() == 0 ) {
            return "N/A";
        }

        return getStringFromList( list.toArray( new String[ list.size() - 1 ] ) );
    }
}
