package com.github.cyberryan1.utils;

import com.github.cyberryan1.cybercore.spigot.CyberCore;
import com.github.cyberryan1.utils.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BossbarUtils {


    // Add a bossbar to the player
    public static void addBossbar( Player player ) {
        NamespacedKey key = new NamespacedKey( CyberCore.getPlugin(), player.getUniqueId().toString() );
        String title = Settings.VANISH_BOSSBAR_TITLE.coloredString()
                .replace( "[PLAYER]", player.getName() )
                .replace( "[LEVEL]", VanishUtils.getVanishLevel( player ) + "" );
        BarColor color = getBarColor();
        double progress = Settings.VANISH_BOSSBAR_PERCENT.integer() / 100.0;

        KeyedBossBar bossbar = Bukkit.createBossBar( key, title, color, BarStyle.SOLID );
        bossbar.addPlayer( player );
        bossbar.setProgress( progress );
    }



    // Remove a bossbar from the player
    public static void removeBossbar( Player player ) {
        NamespacedKey key = new NamespacedKey( CyberCore.getPlugin(), player.getUniqueId().toString() );
        KeyedBossBar bossbar = Bukkit.getBossBar( key );
        if ( bossbar != null ) {
            bossbar.removePlayer( player );
            Bukkit.removeBossBar( key );
        }
    }



    // Remove all bossbars
    public static void removeAllBossbars() {
        Iterator<KeyedBossBar> bossbarIterator = Bukkit.getBossBars();
        List<KeyedBossBar> bossbarList = new ArrayList<>();
        bossbarIterator.forEachRemaining( bossbarList::add );
        for ( int index = bossbarList.size() - 1; index >= 0; index-- ) {
            Bukkit.removeBossBar( bossbarList.get( index ).getKey() );
        }
    }



    // Gets the bar color, depending on what was stated in the config
    private static BarColor getBarColor() {
        return switch ( Settings.VANISH_BOSSBAR_COLOR.string().toUpperCase() ) {
            case "BLUE" -> BarColor.BLUE;
            case "GREEN" -> BarColor.GREEN;
            case "PINK" -> BarColor.PINK;
            case "PURPLE" -> BarColor.PURPLE;
            case "RED" -> BarColor.RED;
            case "WHITE" -> BarColor.WHITE;
            case "YELLOW" -> BarColor.YELLOW;
            default -> null;
        };
    }
}
