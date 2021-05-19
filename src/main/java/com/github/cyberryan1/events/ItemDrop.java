package com.github.cyberryan1.events;

import com.github.cyberryan1.utils.ConfigUtils;
import com.github.cyberryan1.utils.VanishUtils;

import com.github.cyberryan1.utils.VaultUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDrop implements Listener {

    @EventHandler
    public void onPlayerDrop( PlayerDropItemEvent event ) {
        if ( VanishUtils.checkVanished( event.getPlayer() ) && ConfigUtils.getBool( "vanish.other-events.item-drop.cancel" ) ) {
            if ( ConfigUtils.getBool( "vanish.other-events.item-drop.bypass" ) == false ||
                    VaultUtils.hasPerms( event.getPlayer(), ConfigUtils.getStr( "vanish.other-events.item-drop.bypass-perm" ) ) == false ) {
                event.setCancelled( true );

                if ( ConfigUtils.getStr( "vanish.other-events.item-drop.cancel-msg" ).equals( "" ) == false ) {
                    event.getPlayer().sendMessage( ConfigUtils.getColoredStr( "vanish.other-events.item-drop.cancel-msg", event.getPlayer() ) );
                }
            }
        }
    }
}
