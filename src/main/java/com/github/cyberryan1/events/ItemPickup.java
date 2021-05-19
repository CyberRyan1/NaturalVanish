package com.github.cyberryan1.events;

import com.github.cyberryan1.utils.ConfigUtils;
import com.github.cyberryan1.utils.Utilities;
import com.github.cyberryan1.utils.VanishUtils;
import com.github.cyberryan1.utils.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.ArrayList;

public class ItemPickup implements Listener {
    private ArrayList<String> cooldown = new ArrayList<>();

    @EventHandler
    public void onPlayerPickup( PlayerPickupItemEvent event ) {
        if ( VanishUtils.checkVanished( event.getPlayer() ) && ConfigUtils.getBool( "vanish.other-events.item-pickup.cancel" ) ) {
            if ( ConfigUtils.getBool( "vanish.other-events.item-pickup.bypass" ) == false ||
                    VaultUtils.hasPerms( event.getPlayer(), ConfigUtils.getStr( "vanish.other-events.item-pickup.bypass-perm" ) ) == false ) {
                event.setCancelled( true );

                if ( ConfigUtils.getStr( "vanish.other-events.item-pickup.cancel-msg" ).equals( "" ) == false &&
                        cooldown.contains( event.getPlayer().getName() ) == false ) {
                    event.getPlayer().sendMessage( ConfigUtils.getColoredStr( "vanish.other-events.item-pickup.cancel-msg" ) );

                    cooldown.add( event.getPlayer().getName() );
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask( Utilities.getPlugin(), new Runnable() {
                        public void run() {
                            cooldown.remove( event.getPlayer().getName() );
                        }
                    }, 40L );
                }
            }
        }
    }
}
