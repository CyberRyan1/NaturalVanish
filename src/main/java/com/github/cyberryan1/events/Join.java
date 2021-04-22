package com.github.cyberryan1.events;

import com.github.cyberryan1.utils.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    @EventHandler
    public void onPlayerJoin( PlayerJoinEvent event ) {
        String playerUUID = event.getPlayer().getUniqueId().toString();

        // Add the player to any players who wouldn't be able to see the player
        if ( VanishUtils.checkVanished( event.getPlayer() ) ) {
            for ( Player p : Bukkit.getOnlinePlayers() ) {
                if ( VaultUtils.hasPerms( p, ConfigUtils.getStr( "vanish.use.permission" ) ) == false ) {
                    p.hidePlayer( Utilities.getPlugin(), event.getPlayer() );
                }
                else if ( VanishUtils.getVanishLevel( event.getPlayer() ) <= VanishUtils.getMaxVanishLevel( p ) == false ) {
                    p.hidePlayer( Utilities.getPlugin(), event.getPlayer() );
                }
            }
        }

        // Hiding any vanished players who the player wouldn't be able to see
        for ( Player p : Bukkit.getOnlinePlayers() ) {
            if ( VanishUtils.checkVanished( p ) ) {
                if ( VanishUtils.getVanishLevel( p ) > VanishUtils.getMaxVanishLevel( event.getPlayer() ) ) {
                    event.getPlayer().hidePlayer( Utilities.getPlugin(), p );
                }
            }
        }



        // checking if the player's vanish was enabled or disabled while they were offline
        // was disabled while offline
        if ( DataUtils.getBool( "data.offline.disabled." + playerUUID ) ) {
            DataUtils.set( "data.offline.disabled." + playerUUID, null );
            DataUtils.save();

            VanishUtils.disableVanish( event.getPlayer() );
            event.getPlayer().sendMessage( ConfigUtils.getColoredStr( "vanish.toggle-others.offline.disabled-target" ) );
        }



        // was enabled while offline
        else if ( DataUtils.getBool( "data.offline.enabled." + playerUUID ) ) {
            DataUtils.set( "data.offline.enabled." + playerUUID, null );
            DataUtils.save();

            VanishUtils.enableVanish( event.getPlayer() );
            event.getPlayer().sendMessage( ConfigUtils.getColoredStr( "vanish.toggle-others.offline.enabled-target" ) );
        }



        // level was changed while offline
        if ( DataUtils.getBool( "data.offline.level-changed." + playerUUID ) ) {
            DataUtils.set( "data.offline.level-changed." + playerUUID, null );
            DataUtils.save();

            event.getPlayer().sendMessage( Utilities.getColored( "&7Your vanish level has been set to &a" + DataUtils.getStr( "data." + playerUUID + ".level" ) + " &7while you were offline" ) );
        }



        // checking if the player who joined is vanished and adding that vanish
        if ( ConfigUtils.getBool( "vanish." + event.getPlayer().getUniqueId().toString() + ".enabled" ) ) {
            int vanishLevel = VanishUtils.getVanishLevel( event.getPlayer() );
            for ( Player p : Bukkit.getServer().getOnlinePlayers() ) {
                if ( p.hasPermission( ConfigUtils.getStr( "vanish.use.permission" ) ) == false ) {
                    p.hidePlayer( Utilities.getPlugin(), event.getPlayer() );
                }
                else if ( vanishLevel <= VanishUtils.getMaxVanishLevel( p ) ) {
                    p.sendMessage( ConfigUtils.getColoredStr( "vanish.level." + vanishLevel + ".enable-message", event.getPlayer() ) );
                }
                else {
                    p.hidePlayer( Utilities.getPlugin(), event.getPlayer() );
                }
            }

            // if to cancel the join message or not (config)
            if ( ConfigUtils.getBool( "vanish.join.cancel-message" ) ) {
                event.setJoinMessage( null );
            }
        }
    }
}
