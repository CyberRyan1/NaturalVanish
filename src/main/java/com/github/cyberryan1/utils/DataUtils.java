package com.github.cyberryan1.utils;

import com.github.cyberryan1.managers.DataManager;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @deprecated
 */
public class DataUtils {

    private static DataManager data;

    public DataUtils( DataManager dat ) { data = dat; }

    // returns the config of the DataManager
    public static FileConfiguration getConfig() { return data.getConfig(); }


    // returns the DataManager
    public static DataManager getDataManager() { return data; }



    // save the current config of the DataManager
    public static void save() { data.saveConfig(); }



    // returns a boolean value from the path
    public static boolean getBool( String path ) { return data.getConfig().getBoolean( path ); }


    // returns an int from the path
    public static int getInt( String path ) { return data.getConfig().getInt( path ); }


    // returns a float from the path
    public static float getFloat( String path ) { return ( float ) data.getConfig().getDouble( path ); }


    // returns a string from the path
    public static String getStr( String path ) { return data.getConfig().getString( path ); }


    // returns an object from the path
    public static Object get( String path ) { return data.getConfig().get( path ); }



    // saves an object to the path
    public static void set( String path, Object value ) { data.getConfig().set( path, value ); }
}
