package com.github.cyberryan1.events;

import com.github.cyberryan1.utils.ConfigUtils;
import com.github.cyberryan1.utils.DataUtils;
import com.github.cyberryan1.utils.Utilities;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick( InventoryClickEvent event ) {
        if ( ConfigUtils.getBool( "vanish.use.skull-helmet" ) ) {
            if ( DataUtils.getConfig().contains( "vanish." + event.getWhoClicked().getUniqueId().toString() + ".enabled" ) ) {
                if ( event.getCurrentItem() == null ) { return; }
                if ( event.getWhoClicked().getInventory().getHelmet() == null ) { return; }

                if ( event.getCurrentItem().equals( event.getWhoClicked().getInventory().getHelmet() ) ) {
                    if ( event.getCurrentItem().equals( Utilities.getPlayerHead( ( Player ) event.getWhoClicked() ) ) ) {
                        event.setCancelled( true );
                        Player player = ( Player ) event.getWhoClicked();

                        player.sendMessage( ConfigUtils.getColoredStr( "vanish.cancel.skull" ) );
                        player.closeInventory();
                    }
                }
            }
        }
    }
}
