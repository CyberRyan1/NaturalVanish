package com.github.cyberryan1.utils;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class BossbarUtils {


    // Add a bossbar to the player
    public static void addBossbar( Player player ) {
        NamespacedKey key = new NamespacedKey( Utilities.getPlugin(), player.getUniqueId().toString() );
        String title = ConfigUtils.getColoredStr( "vanish.use.bossbar.title", player ).replace( "[LEVEL]", VanishUtils.getVanishLevel( player ) + "" );
        BarColor color = getBarColor();
        double progress = ConfigUtils.getDouble( "vanish.use.bossbar.percent" ) / 100.0;

        KeyedBossBar bossbar = Bukkit.createBossBar( key, title, color, BarStyle.SOLID );
        bossbar.addPlayer( player );
        bossbar.setProgress( progress );
    }



    // Remove a bossbar from the player
    public static void removeBossbar( Player player ) {
        NamespacedKey key = new NamespacedKey( Utilities.getPlugin(), player.getUniqueId().toString() );
        KeyedBossBar bossbar = Bukkit.getBossBar( key );
        if ( bossbar != null ) {
            bossbar.removePlayer( player );
            Bukkit.removeBossBar( key );
        }
    }



    // Remove all bossbars
    public static void removeAllBossbars() {
        Iterator<KeyedBossBar> bossbarList = Bukkit.getBossBars();
        while ( bossbarList != null && bossbarList.hasNext() ) {
            KeyedBossBar bossbar = bossbarList.next();
            if ( bossbar != null ) {
                Bukkit.removeBossBar( bossbar.getKey() );
            }
        }
    }



    // Gets the bar color, depending on what was stated in the config
    private static BarColor getBarColor() {
        switch ( ConfigUtils.getStr( "vanish.use.bossbar.color" ).toUpperCase() ) {
            case "BLUE": return BarColor.BLUE;
            case "GREEN": return BarColor.GREEN;
            case "PINK": return BarColor.PINK;
            case "PURPLE": return BarColor.PURPLE;
            case "RED": return BarColor.RED;
            case "WHITE": return BarColor.WHITE;
            case "YELLOW": return BarColor.YELLOW;
            default: return null;
        }
    }
}
