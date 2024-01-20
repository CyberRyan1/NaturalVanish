package com.github.cyberryan1.events;

import com.github.cyberryan1.cybercore.spigot.CyberCore;
import com.github.cyberryan1.cybercore.spigot.utils.CyberColorUtils;
import com.github.cyberryan1.cybercore.spigot.utils.CyberMsgUtils;
import com.github.cyberryan1.cybercore.spigot.utils.CyberVaultUtils;
import com.github.cyberryan1.utils.BossbarUtils;
import com.github.cyberryan1.utils.VanishUtils;
import com.github.cyberryan1.utils.settings.Settings;
import com.github.cyberryan1.utils.yml.YMLUtils;
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
                if ( CyberVaultUtils.hasPerms( p, Settings.VANISH_PERMISSION.string() ) == false ) {
                    p.hidePlayer( CyberCore.getPlugin(), event.getPlayer() );
                }
                else if ( VanishUtils.getVanishLevel( event.getPlayer() ) <= VanishUtils.getMaxVanishLevel( p ) == false ) {
                    p.hidePlayer( CyberCore.getPlugin(), event.getPlayer() );
                }
            }
        }

        // Hiding any vanished players who the player wouldn't be able to see
        for ( Player p : Bukkit.getOnlinePlayers() ) {
            if ( VanishUtils.checkVanished( p ) ) {
                if ( VanishUtils.getVanishLevel( p ) > VanishUtils.getMaxVanishLevel( event.getPlayer() ) ) {
                    event.getPlayer().hidePlayer( CyberCore.getPlugin(), p );
                }
            }
        }



        // checking if the player's vanish was enabled or disabled while they were offline
        // was disabled while offline
        if ( YMLUtils.getData().getBool( "data.offline.disabled." + playerUUID ) ) {
            YMLUtils.getData().set( "data.offline.disabled." + playerUUID, null );
            YMLUtils.getData().save();

            VanishUtils.disableVanish( event.getPlayer() );
            CyberMsgUtils.sendMsg( event.getPlayer(), Settings.VANISH_TOGGLE_TARGET_OFFLINE_DISABLED_MSG.coloredString() );
        }



        // was enabled while offline
        else if ( YMLUtils.getData().getBool( "data.offline.enabled." + playerUUID ) ) {
            YMLUtils.getData().set( "data.offline.enabled." + playerUUID, null );
            YMLUtils.getData().save();

            VanishUtils.enableVanish( event.getPlayer() );
            CyberMsgUtils.sendMsg( event.getPlayer(), Settings.VANISH_TOGGLE_TARGET_OFFLINE_ENABLED_MSG.coloredString() );
        }



        // level was changed while offline
        if ( YMLUtils.getData().getBool( "data.offline.level-changed." + playerUUID ) ) {
            YMLUtils.getData().set( "data.offline.level-changed." + playerUUID, null );
            YMLUtils.getData().save();

            event.getPlayer().sendMessage( CyberColorUtils.getColored( "&7Your vanish level has been set to &a" +
                    YMLUtils.getData().getStr( "data." + playerUUID + ".level" ) + " &7while you were offline" ) );
        }



        // checking if the player who joined is vanished and adding that vanish
        if ( YMLUtils.getData().getBool( "vanish." + event.getPlayer().getUniqueId().toString() + ".enabled" ) ) {
            int vanishLevel = VanishUtils.getVanishLevel( event.getPlayer() );
            for ( Player p : Bukkit.getServer().getOnlinePlayers() ) {
                if ( CyberVaultUtils.hasPerms( p, Settings.VANISH_PERMISSION.string() ) == false ) {
                    p.hidePlayer( CyberCore.getPlugin(), event.getPlayer() );
                }
                else if ( vanishLevel <= VanishUtils.getMaxVanishLevel( p ) ) {
                    String enableMsg;
                    switch ( vanishLevel ) {
                        case 2: {
                            enableMsg = Settings.VANISH_LEVEL_TWO_ENABLE_MSG.coloredString();
                            break;
                        }
                        case 3: {
                            enableMsg = Settings.VANISH_LEVEL_THREE_ENABLE_MSG.coloredString();
                            break;
                        }
                        case 4: {
                            enableMsg = Settings.VANISH_LEVEL_FOUR_ENABLE_MSG.coloredString();
                            break;
                        }
                        case 5: {
                            enableMsg = Settings.VANISH_LEVEL_FIVE_ENABLE_MSG.coloredString();
                            break;
                        }
                        default: {
                            enableMsg = Settings.VANISH_LEVEL_ONE_ENABLE_MSG.coloredString();
                            break;
                        }
                    }

                    CyberMsgUtils.sendMsg( p, enableMsg.replace( "[PLAYER]", event.getPlayer().getName() ) );
                }
                else {
                    p.hidePlayer( CyberCore.getPlugin(), event.getPlayer() );
                }
            }

            // if to cancel the join message or not (config)
            if ( Settings.VANISH_JOIN_CANCEL_MSG.bool() ) {
                event.setJoinMessage( null );
            }

            // if to set the player's bossbar or not (config)
            if ( Settings.VANISH_BOSSBAR_ENABLE.bool() ) {
                BossbarUtils.addBossbar( event.getPlayer() );
            }
        }
    }
}
